package dev.cat.mahmoudelbaz.heartgate.concor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CornrPriceModel implements Serializable {

    @Expose
    @SerializedName("price")
    public String price;
    @Expose
    @SerializedName("concor")
    public String concor;
    @Expose
    @SerializedName("id")
    public String id;


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getConcor() {
        return concor;
    }

    public void setConcor(String concor) {
        this.concor = concor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
