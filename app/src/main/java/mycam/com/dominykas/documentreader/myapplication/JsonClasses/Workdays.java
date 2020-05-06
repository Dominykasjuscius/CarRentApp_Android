
package mycam.com.dominykas.documentreader.myapplication.JsonClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Workdays {

    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("minutes")
    @Expose
    private Integer minutes;
    @SerializedName("dailyAmount")
    @Expose
    private Integer dailyAmount;
    @SerializedName("minimumPrice")
    @Expose
    private Double minimumPrice;
    @SerializedName("minimumMinutes")
    @Expose
    private Integer minimumMinutes;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public Integer getDailyAmount() {
        return dailyAmount;
    }

    public void setDailyAmount(Integer dailyAmount) {
        this.dailyAmount = dailyAmount;
    }

    public Double getMinimumPrice() {
        return minimumPrice;
    }

    public void setMinimumPrice(Double minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    public Integer getMinimumMinutes() {
        return minimumMinutes;
    }

    public void setMinimumMinutes(Integer minimumMinutes) {
        this.minimumMinutes = minimumMinutes;
    }

}
