
package mycam.com.dominykas.documentreader.myapplication.JsonClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reservation {

    @SerializedName("initialPrice")
    @Expose
    private Integer initialPrice;
    @SerializedName("initialMinutes")
    @Expose
    private Integer initialMinutes;
    @SerializedName("extensionPrice")
    @Expose
    private Integer extensionPrice;
    @SerializedName("extensionMinutes")
    @Expose
    private Integer extensionMinutes;
    @SerializedName("longerExtensionPrice")
    @Expose
    private Integer longerExtensionPrice;
    @SerializedName("longerExtensionMinutes")
    @Expose
    private Integer longerExtensionMinutes;

    public Integer getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(Integer initialPrice) {
        this.initialPrice = initialPrice;
    }

    public Integer getInitialMinutes() {
        return initialMinutes;
    }

    public void setInitialMinutes(Integer initialMinutes) {
        this.initialMinutes = initialMinutes;
    }

    public Integer getExtensionPrice() {
        return extensionPrice;
    }

    public void setExtensionPrice(Integer extensionPrice) {
        this.extensionPrice = extensionPrice;
    }

    public Integer getExtensionMinutes() {
        return extensionMinutes;
    }

    public void setExtensionMinutes(Integer extensionMinutes) {
        this.extensionMinutes = extensionMinutes;
    }

    public Integer getLongerExtensionPrice() {
        return longerExtensionPrice;
    }

    public void setLongerExtensionPrice(Integer longerExtensionPrice) {
        this.longerExtensionPrice = longerExtensionPrice;
    }

    public Integer getLongerExtensionMinutes() {
        return longerExtensionMinutes;
    }

    public void setLongerExtensionMinutes(Integer longerExtensionMinutes) {
        this.longerExtensionMinutes = longerExtensionMinutes;
    }

}
