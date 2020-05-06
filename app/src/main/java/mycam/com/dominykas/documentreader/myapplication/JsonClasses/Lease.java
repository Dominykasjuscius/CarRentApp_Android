
package mycam.com.dominykas.documentreader.myapplication.JsonClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lease {

    @SerializedName("workdays")
    @Expose
    private Workdays workdays;
    @SerializedName("weekends")
    @Expose
    private Weekends weekends;
    @SerializedName("kilometerPrice")
    @Expose
    private Double kilometerPrice;
    @SerializedName("freeKilometersPerDay")
    @Expose
    private Integer freeKilometersPerDay;
    @SerializedName("servicePlusBatteryMaxKm")
    @Expose
    private Integer servicePlusBatteryMaxKm;
    @SerializedName("servicePlusBatteryMinKm")
    @Expose
    private Integer servicePlusBatteryMinKm;
    @SerializedName("servicePlusEGoPoints")
    @Expose
    private Integer servicePlusEGoPoints;

    public Workdays getWorkdays() {
        return workdays;
    }

    public void setWorkdays(Workdays workdays) {
        this.workdays = workdays;
    }

    public Weekends getWeekends() {
        return weekends;
    }

    public void setWeekends(Weekends weekends) {
        this.weekends = weekends;
    }

    public Double getKilometerPrice() {
        return kilometerPrice;
    }

    public void setKilometerPrice(Double kilometerPrice) {
        this.kilometerPrice = kilometerPrice;
    }

    public Integer getFreeKilometersPerDay() {
        return freeKilometersPerDay;
    }

    public void setFreeKilometersPerDay(Integer freeKilometersPerDay) {
        this.freeKilometersPerDay = freeKilometersPerDay;
    }

    public Integer getServicePlusBatteryMaxKm() {
        return servicePlusBatteryMaxKm;
    }

    public void setServicePlusBatteryMaxKm(Integer servicePlusBatteryMaxKm) {
        this.servicePlusBatteryMaxKm = servicePlusBatteryMaxKm;
    }

    public Integer getServicePlusBatteryMinKm() {
        return servicePlusBatteryMinKm;
    }

    public void setServicePlusBatteryMinKm(Integer servicePlusBatteryMinKm) {
        this.servicePlusBatteryMinKm = servicePlusBatteryMinKm;
    }

    public Integer getServicePlusEGoPoints() {
        return servicePlusEGoPoints;
    }

    public void setServicePlusEGoPoints(Integer servicePlusEGoPoints) {
        this.servicePlusEGoPoints = servicePlusEGoPoints;
    }

}
