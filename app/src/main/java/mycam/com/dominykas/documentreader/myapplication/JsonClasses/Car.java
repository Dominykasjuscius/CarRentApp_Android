
package mycam.com.dominykas.documentreader.myapplication.JsonClasses;

import com.google.android.gms.maps.model.Marker;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

import mycam.com.dominykas.documentreader.myapplication.MapsActivity;

public class Car implements Comparable<Car>{

    float distance;

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    Marker marker;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("plateNumber")
    @Expose
    private String plateNumber;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("model")
    @Expose
    private Model model;
    @SerializedName("batteryPercentage")
    @Expose
    private Integer batteryPercentage;
    @SerializedName("batteryEstimatedDistance")
    @Expose
    private Integer batteryEstimatedDistance;
    @SerializedName("isCharging")
    @Expose
    private Boolean isCharging;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Integer getBatteryPercentage() {
        return batteryPercentage;
    }

    public void setBatteryPercentage(Integer batteryPercentage) {
        this.batteryPercentage = batteryPercentage;
    }

    public Integer getBatteryEstimatedDistance() {
        return batteryEstimatedDistance;
    }

    public void setBatteryEstimatedDistance(Integer batteryEstimatedDistance) {
        this.batteryEstimatedDistance = batteryEstimatedDistance;
    }

    public Boolean getIsCharging() {
        return isCharging;
    }

    public void setIsCharging(Boolean isCharging) {
        this.isCharging = isCharging;
    }

    public float getDistance(double latitude, double longitude) {
        float[] results2 = new float[1];

        android.location.Location.distanceBetween(latitude, longitude,
                getLocation().getLatitude(), getLocation().getLongitude(), results2);
        return results2[0];
    }

    public void SetDistance(float distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(Car car) {
       if(this.distance > car.distance) {
           return 1;
       }
       else if(this.distance < car.distance)
       {
           return -1;
       }
       else return 0;
    }
    public final static Comparator<Car> distanceComp = new Comparator<Car>() {
        @Override
        public int compare(Car a1, Car a2) {

            return Float.compare(a1.distance, a2.distance);
        }
    };

}
