package dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel;

import com.google.gson.annotations.SerializedName;

public class ResultModelLogin {

    @SerializedName("data")
    public data data;
    @SerializedName("state")
    public String state;


    public data getData() {
        return data;
    }

    public void setData(data data) {
        this.data = data;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


}
