
package mycam.com.dominykas.documentreader.myapplication.JsonClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rate {

    @SerializedName("isWeekend")
    @Expose
    private Boolean isWeekend;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("currencySymbol")
    @Expose
    private String currencySymbol;
    @SerializedName("lease")
    @Expose
    private Lease lease;
    @SerializedName("reservation")
    @Expose
    private Reservation reservation;

    public Boolean getIsWeekend() {
        return isWeekend;
    }

    public void setIsWeekend(Boolean isWeekend) {
        this.isWeekend = isWeekend;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public Lease getLease() {
        return lease;
    }

    public void setLease(Lease lease) {
        this.lease = lease;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

}
