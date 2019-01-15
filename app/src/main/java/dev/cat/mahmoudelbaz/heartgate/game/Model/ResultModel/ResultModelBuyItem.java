package dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel;

import com.google.gson.annotations.SerializedName;

public class ResultModelBuyItem {

    @SerializedName("data")
    public inventory data;

    @SerializedName("state")
    public String state;

    public inventory getData() {
        return data;
    }

    public void setData(inventory data) {
        this.data = data;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
