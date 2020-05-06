package mycam.com.dominykas.documentreader.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import mycam.com.dominykas.documentreader.myapplication.JsonClasses.Car;
import mycam.com.dominykas.documentreader.myapplication.JsonClasses.Lease;
import mycam.com.dominykas.documentreader.myapplication.JsonClasses.Rate;
import mycam.com.dominykas.documentreader.myapplication.JsonClasses.Reservation;
import mycam.com.dominykas.documentreader.myapplication.JsonClasses.Weekends;
import mycam.com.dominykas.documentreader.myapplication.JsonClasses.Workdays;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static GoogleMap mMap;
    LocationManager locationManager;
    static double longitude, latitude;
    static JsonReader reader;
    static Marker CurLocMarker;
    static DrawerLayout drawer;
    Toolbar toolbar;
    LocationListener listener;
    ImageButton locationbtn;
    static boolean isCameraMoved = false;
    ActionBarDrawerToggle toggle;
    Button filterplate;
    static EditText filterText;
    static String filtertype = "none";
    static String filternum;
    static NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        reader = new JsonReader(this);

        toggle = new ActionBarDrawerToggle(this, drawer, R.string.drawer_open, R.string.drawer_closed);

        toolbar = findViewById(R.id.toolbar3);
        drawer = findViewById(R.id.activity_main);
        navigationView = findViewById(R.id.nv);
        filterplate = findViewById(R.id.filterbyplat);
        locationbtn = findViewById(R.id.locationbtn);
        drawer.addDrawerListener(toggle);
        filterText = findViewById(R.id.editSearch);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_closed);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        GetClickListeners();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }else {
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }else {
            if(isNetworkConnected()) {
                reader.execute(mMap);
            }
            else Toast.makeText(this, "No network connection", Toast.LENGTH_LONG).show();

            find_Location();
            SetMarkerListener();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a CurLocMarker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        RequestLocation();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }else {
            find_Location();
        }

        if(isNetworkConnected()) {
            reader.execute(mMap);

        }
        else Toast.makeText(this, "No network connection", Toast.LENGTH_LONG).show();
        SetMarkerListener();



    }

    /**
     * Makes it so when you click on a car marker on the map, the list opens and displays the
     * specific car you have chosen
     */
    public void SetMarkerListener() {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                CarList<Car> cars = reader.GetCars();
                String tag = marker.getTag().toString();
                if(tag != "USER") {

                    Car car = new Car();
                    if (cars != null) {
                        for (Car c : cars) {
                            if (c.getId().toString().equals(tag) && !tag.equals("USER")) {
                                car = c;
                            }
                        }
                        marker.setTitle(car.getPlateNumber());
                        AddCarToDrawerLowInfo(getApplicationContext(), car);

                    }
                }
                return false;
            }
        });
    }

    /**
     * Shows the information about the car, adds filter button, sort button
     * @param cars the array of cars gotten from the JSON file in the url
     * @param ctx
     * @param FilterType shows if the list is sorted by battery or by plate number
     * @param filter the user input of filter which is used to filter the car list
     * @param info shows how much information "HIGH" or "LOW" will be shows on each car
     */
    public static void ShowCarList(CarList<Car> cars, Context ctx, String FilterType, String filter, String info) {
        navigationView.getMenu().clear();

        navigationView.getMenu().add("Sort cars by closest\n").setChecked(true);
        MenuItem sort = navigationView.getMenu().getItem(0);
        SpannableString b = new SpannableString(sort.getTitle());
        int num = b.length();
        b.setSpan(new RelativeSizeSpan(1.3f), 0, num, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sort.setTitle(b);

        navigationView.getMenu().add("Filter by battery\n").setChecked(true);
        MenuItem filteritem = navigationView.getMenu().getItem(1);
        SpannableString string = new SpannableString(filteritem.getTitle());
        int end = string.length();
        string.setSpan(new RelativeSizeSpan(1.3f), 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        filteritem.setTitle(string);

        int[] batteryValues = {0, 20,60,80,100};
        filteritem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int i = 1;
                navigationView.getMenu().clear();
                navigationView.getMenu().add("Choose battery percentage:\n").setChecked(true);
                MenuItem bat = navigationView.getMenu().getItem(0);
                SpannableString g = new SpannableString(bat.getTitle());
                int ss = g.length();
                g.setSpan(new RelativeSizeSpan(1.2f), 0, ss, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                bat.setTitle(g);
                for(int num : batteryValues) {
                    if(num != 0)navigationView.getMenu().add("Battery >= " + num).setChecked(true);
                    else navigationView.getMenu().add("Show all cars").setChecked(true);
                    MenuItem item = navigationView.getMenu().getItem(i);
                    item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            mMap.clear();
                            CurLocMarker = null;
                            CreateLocMarker();
                            JsonReader.PutCarsOnMap(reader.GetCars(), "Battery", num+"");
                            ShowCarList(reader.GetCars(), ctx, "Battery", num+"", "LOW");
                            filtertype = "Battery";
                            filternum = num+"";
                            Toast.makeText(ctx, "Cars were filtered", Toast.LENGTH_LONG).show();
                            return false;
                        }
                    });
                    i++;
                }
                return false;
            }
        });

        sort.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(ctx, "Car list has been sorted by closest", Toast.LENGTH_LONG).show();
                SortCarList(reader.GetCars(), ctx, filtertype, filternum);
                return false;
            }
        });


        for(Car c : cars) {
            double battery = c.getBatteryPercentage();
            try {
                if (FilterType.equals("Battery") && Double.parseDouble(filter) <= battery) {
                    CreateCarMenu(c, ctx, info);
                }
            }
            catch (NumberFormatException e) {}
            if(FilterType.equals("Plate") && filter.equals(c.getPlateNumber().toLowerCase()))
            {
                CreateCarMenu(c, ctx, info);
            }
            if(FilterType.equals("none"))
            {
                CreateCarMenu(c, ctx, info);
            }


        }
    }

    /**
     * Creates A menu and puts the list of cars gotten from the url into the menu along with the image next it
     * @param car
     * @param ctx
     * @param info Shows the amount of information the car will show once its clicked
     */
    public static void CreateCarMenu(Car car, Context ctx, String info) {
        Menu menu = navigationView.getMenu();

            String link = car.getModel().getPhotoUrl();

            navigationView.getHeaderView(0).setVisibility(View.GONE);
            menu.add(car.getPlateNumber()+ "\n" + car.getPlateNumber());
            MenuItem item = menu.getItem(menu.size()-1);
            ImageView img = new ImageView(ctx);
            Picasso.get().load(link).into(img);
            item.setActionView(img);

            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if(info.equals("HIGH")) {
                        AddCarToDrawerHighInfo(ctx, car);
                    }
                    else AddCarToDrawerLowInfo(ctx, car);
                    return false;
                }
            });
    }

    public static void SortCarList(CarList<Car> cars, Context ctx, String filtertype, String filter) {
        cars.sortJava(Car.distanceComp);

        ShowCarList(cars, ctx, filtertype, filter, "LOW");
    }

    /**
     * Car menu with all the information about the car
     * @param ctx
     * @param car
     */
    public static void AddCarToDrawerHighInfo(Context ctx, Car car) {

        Menu menu = addButtonsToDrawer(ctx, car);

        menu.add("Plate Number: " + car.getPlateNumber());
        car.SetDistance(car.getDistance(latitude, longitude));
        menu.add("Distance to you: " + new DecimalFormat("#.##").format(car.getDistance(latitude, longitude)/1000) + " km");
        SubMenu location = menu.addSubMenu("Location");
            location.add("Latitude: " + car.getLocation().getLatitude().toString());
            location.add("Longitude: " + car.getLocation().getLongitude().toString());
            location.add("Address: " + car.getLocation().getAddress());
            location.add("Battery Percentage: " + car.getBatteryPercentage().toString());
            location.add("Battery Estimated Distance: " + car.getBatteryEstimatedDistance().toString());
            location.add("Is Charging: " + car.getIsCharging().toString());

        SubMenu model = menu.addSubMenu("Model");
            model.add("Model: " + car.getModel().getTitle());

        SubMenu rate = menu.addSubMenu("Rate");
            Rate rateItem = car.getModel().getRate();
            rate.add("Currency: " + rateItem.getCurrency());
            rate.add("Currency Symbol: " + rateItem.getCurrencySymbol());
            rate.add("Is Weekend: " + rateItem.getIsWeekend().toString());

        SubMenu lease = menu.addSubMenu("Lease");
            Lease leaseitem = rateItem.getLease();
            lease.add("Free Kilometers Per Day: " + leaseitem.getFreeKilometersPerDay().toString());
            lease.add("Service Plus Battery Max Km: " + leaseitem.getServicePlusBatteryMaxKm().toString());
            lease.add("Service Plus Batter Min Km: " + leaseitem.getServicePlusBatteryMinKm().toString());
            lease.add("Service Plus Ego Points: " + leaseitem.getServicePlusEGoPoints().toString());
            lease.add("Kilometer Price: " + leaseitem.getKilometerPrice().toString());

        SubMenu weekend = menu.addSubMenu("Weekends");
            Weekends weekendsItem = leaseitem.getWeekends();
            weekend.add("Daily Amount: " + weekendsItem.getDailyAmount().toString());
            weekend.add("Minimum Minutes: " + weekendsItem.getMinimumMinutes().toString());
            weekend.add("Minutes: " + weekendsItem.getMinutes().toString());
            weekend.add("Amount: " + weekendsItem.getAmount().toString());
            weekend.add("Minimum Price: " + weekendsItem.getMinimumPrice().toString());

        SubMenu workdays = menu.addSubMenu("Workdays");
            Workdays workdaysItem = leaseitem.getWorkdays();
            workdays.add("Daily Amount: " + workdaysItem.getDailyAmount().toString());
            workdays.add("Minimum Minutes: " + workdaysItem.getMinimumMinutes().toString());
            workdays.add("Minutes: " + workdaysItem.getMinutes().toString());
            workdays.add("Amount: " + workdaysItem.getAmount().toString());
            workdays.add("Minimum Price: " + workdaysItem.getMinimumPrice().toString());
        SubMenu reservation = menu.addSubMenu("Reservation");
            Reservation reservationItem = rateItem.getReservation();
            reservation.add("Initial Price: " + reservationItem.getInitialPrice().toString());
            reservation.add("Initial Minutes: " + reservationItem.getInitialMinutes().toString());
            reservation.add("Extension Price: " + reservationItem.getExtensionPrice().toString());
            reservation.add("Extension Minutes: " + reservationItem.getExtensionMinutes().toString());
            reservation.add("Longer Extension Minutes: " + reservationItem.getLongerExtensionMinutes().toString());
            reservation.add("Longer Extension Price: " + reservationItem.getLongerExtensionPrice().toString());

    }

    /**
     * Adds buttons like "Back" and "Show on map" also downloads the image of car and displays it on menu header
     * @param ctx
     * @param car
     * @return returns the menu which the car has been added to
     */
    public static Menu addButtonsToDrawer(Context ctx, Car car) {
        navigationView.getMenu().clear();
        Menu menu = navigationView.getMenu();

        String link = car.getModel().getPhotoUrl();

        menu.add("BACK").setChecked(true);
        MenuItem BackBtn = menu.getItem(0);
        SpannableString s = new SpannableString(BackBtn.getTitle());
        int end = s.length();
        s.setSpan(new RelativeSizeSpan(1.5f), 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        BackBtn.setTitle(s);


        menu.add("SHOW ON MAP").setChecked(true);
        MenuItem showmap = menu.getItem(1);
        SpannableString g = new SpannableString(showmap.getTitle());
        int end0 = g.length();
        g.setSpan(new RelativeSizeSpan(1.5f), 0, end0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        showmap.setTitle(g);

        View view = navigationView.getHeaderView(0);
        navigationView.removeHeaderView(view);
        ImageView imgView = new ImageView(ctx);
        Picasso.get().load(link).into(imgView);
        navigationView.addHeaderView(imgView);
        drawer.openDrawer(Gravity.LEFT);
        BackBtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                ShowCarList(reader.GetCars(), ctx, "none", "none", "LOW");
                return false;
            }
        });
        showmap.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                drawer.closeDrawer(Gravity.LEFT);
                car.getMarker().setTitle(car.getPlateNumber());
                car.getMarker().showInfoWindow();
                LatLng loc = new LatLng(car.getLocation().getLatitude(), car.getLocation().getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 14));
                isCameraMoved = true;
                return false;
            }
        });
        return menu;
    }

    /**
     * Adds a car to drawer with low information below it
     * @param ctx
     * @param car the car which is added
     */
    public static void AddCarToDrawerLowInfo(Context ctx, Car car) {

        Menu menu = addButtonsToDrawer(ctx, car);

        menu.add("Plate Number: " + car.getPlateNumber());
        car.SetDistance(car.getDistance(latitude, longitude));

        menu.add("Distance to you: " + new DecimalFormat("#.##").format(car.getDistance(latitude, longitude)/1000) + " km");
        SubMenu location = menu.addSubMenu("Location");
        location.add("Address: " + car.getLocation().getAddress());
        location.add("Battery Percentage: " + car.getBatteryPercentage().toString());
        location.add("Battery Estimated Distance: " + car.getBatteryEstimatedDistance().toString());
        location.add("Is Charging: " + car.getIsCharging().toString());

        SubMenu model = menu.addSubMenu("Model");
        model.add("Model: " + car.getModel().getTitle());


        SubMenu lease = menu.addSubMenu("Lease");
        Lease leaseitem = car.getModel().getRate().getLease();
        lease.add("Free Kilometers Per Day: " + leaseitem.getFreeKilometersPerDay().toString());
        lease.add("Kilometer Price: " + leaseitem.getKilometerPrice().toString());
        menu.add("More info").setChecked(true);
        MenuItem info = menu.getItem(menu.size()-1);

        info.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                AddCarToDrawerHighInfo(ctx, car);
                return false;
            }
        });
    }

    /**
     * finds last know location of user and updates the location marker
     */
   public void find_Location() {
        Location location;
           List<String> providers = locationManager.getProviders(true);
           for (String provider : providers) {
               try {
                    listener = new LocationListener() {

                        public void onLocationChanged(Location location) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            LatLng coord = new LatLng(latitude, longitude);

                            if (CurLocMarker == null) {
                                if(!isCameraMoved) mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 11));
                                CreateLocMarker();
                            } else {
                                CurLocMarker.setPosition(coord);
                            }

                        }

                        public void onProviderDisabled(String provider) {
                        }

                        public void onProviderEnabled(String provider) {
                        }

                        public void onStatusChanged(String provider, int status,
                                                    Bundle extras) {
                        }
                    };
                    locationManager.requestLocationUpdates(provider, 1000, 0, listener);

                   location = locationManager.getLastKnownLocation(provider);

                   if (location != null) {

                       latitude = location.getLatitude();
                       longitude = location.getLongitude();
                       if (latitude != 0 && longitude != 0) {
                           LatLng coord = new LatLng(latitude, longitude);
                           mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 11));
                           CreateLocMarker();
                       }
                   }
               } catch (SecurityException e) {

           }
       }
       // }
    }


    /**
     * Checks if gps is enabled, if not, request user to turn it on
     */
    public void RequestLocation() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to enable location for this app to work?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    /**
     * Checks if network is connected
     * @return
     */
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    /**
     * Adds all the button listeners
     */
    public void GetClickListeners() {

        filterplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!filterText.getText().toString().equals("") && filterText.getText() != null) {
                    mMap.clear();
                    CreateLocMarker();
                    JsonReader.PutCarsOnMap(reader.GetCars(), "Plate", filterText.getText().toString().toLowerCase());
                    ShowCarList(reader.GetCars(), getApplicationContext(), "Plate", filterText.getText().toString().toLowerCase(), "LOW");

                }
                else {
                    Toast.makeText(getApplicationContext(), "Input filter text first, then click button ", Toast.LENGTH_LONG).show();
                    if (filterText.requestFocus()) {
                        filterText.setInputType(InputType.TYPE_CLASS_TEXT);
                        InputMethodManager keyboard=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        keyboard.showSoftInput(filterText,0);
                    }
                }
            }
        });
        filterText.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        filterText.setInputType(InputType.TYPE_CLASS_TEXT);
                    }
                }
        );
        locationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                find_Location();
                LatLng coord = new LatLng(latitude, longitude);
                if(latitude != 0 && longitude != 0) mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 11));
                else Toast.makeText(getApplicationContext(), "Location not found, check if gps is enabled", Toast.LENGTH_LONG).show();
            }

        });

    }

    /**
     * Used when user location has changed. Readds the marker to the map with new location
     */
    public static void CreateLocMarker() {
        LatLng coord = new LatLng(latitude, longitude);
        if(CurLocMarker != null) CurLocMarker.remove();
        if(latitude != 0 && longitude != 0) {
            CurLocMarker = mMap.addMarker(new MarkerOptions().position(coord).title("YOUR LOCATION")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            CurLocMarker.setTag("USER");
            CurLocMarker.showInfoWindow();
        }
    }

}
