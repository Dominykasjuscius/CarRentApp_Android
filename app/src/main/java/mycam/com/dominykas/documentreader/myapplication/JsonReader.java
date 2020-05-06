package mycam.com.dominykas.documentreader.myapplication;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import mycam.com.dominykas.documentreader.myapplication.JsonClasses.Car;

class JsonReader extends AsyncTask<GoogleMap,Integer,Void>  {
    String text = "";
    static Context ctx;
    static GoogleMap map;
    CarList<Car> cars;
    public JsonReader(Context ctx){
        JsonReader.ctx = ctx;
    }

    protected Void doInBackground(GoogleMap...params) {
        URL url;
        map = params[0];
        try {
            //create url object to point to the file location on internet
            url = new URL("https://development.espark.lt/api/mobile/public/availablecars");
            //make a request to server
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla");

            //get InputStream instance
            InputStream is = con.getInputStream();
            //create BufferedReader object
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            //read content of the file line by line

            while ((line = br.readLine()) != null) {
                text += line;
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
            //close dialog if error occurs
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        ConvertToCars(text);
        PutCarsOnMap(cars, "none", "none");
        MapsActivity.ShowCarList(cars, ctx, "none", "none", "LOW");
    }

    /**
     * Converts String gotten from the URL to Car array
     * @param json
     */
    public void ConvertToCars(String json) {
        Gson gson = new Gson();
        Car[] carsarray;
        this.cars = new CarList<Car>();
        carsarray = gson.fromJson(json, Car[].class);

        for(Car c : carsarray) {
            c.SetDistance(c.getDistance(MapsActivity.latitude, MapsActivity.longitude));
            this.cars.add(c);
        }
    }

    public CarList<Car> GetCars() {
        return cars;
    }

    /**
     * Puts the car markers on the map based on the filter
     * @param cars array of cars gotten from the JSON file url
     * @param FilterType shows if cars are filtered by battery or plate number
     * @param filter string gotten from user input
     */
    public static void PutCarsOnMap(CarList<Car> cars, String FilterType, String filter) {


        LatLng pos;
        if(FilterType.equals("Battery")) {
            for (Car car : cars) {
                try {
                    double battery = car.getBatteryPercentage();
                    double dbl = Double.parseDouble(filter);
                    if (dbl <= battery) {

                        pos = new LatLng(car.getLocation().getLatitude(), car.getLocation().getLongitude());
                        car.setMarker(map.addMarker(new MarkerOptions().position(pos)));
                        car.getMarker().setTag(car.getId());

                    }
                }
                catch(NumberFormatException e){}
            }
        }
        if(FilterType.equals("Plate")) {
            int i = 0;
            for (Car car : cars) {
                String plate = car.getPlateNumber().toLowerCase();
                if(filter.equals(plate)) {
                    pos = new LatLng(car.getLocation().getLatitude(), car.getLocation().getLongitude());
                    car.setMarker(map.addMarker(new MarkerOptions().position(pos)));
                    car.getMarker().setTag(car.getId());
                    i++;
                }
                if (i == 0) Toast.makeText(ctx, "No cars were found with this plate number", Toast.LENGTH_LONG).show();
                else Toast.makeText(ctx, "cars were filtered", Toast.LENGTH_LONG).show();
            }
        }
        else if(FilterType.equals("none")){
            for (Car car : cars) {

                pos = new LatLng(car.getLocation().getLatitude(), car.getLocation().getLongitude());
                car.setMarker(map.addMarker(new MarkerOptions().position(pos)));
                car.getMarker().setTag(car.getId());



            }
        }
    }
}