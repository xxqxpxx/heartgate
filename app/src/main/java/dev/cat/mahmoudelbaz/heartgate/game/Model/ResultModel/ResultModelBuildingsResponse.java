package dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel;

import com.google.gson.annotations.SerializedName;

public class ResultModelBuildingsResponse {

    @SerializedName("data")
    public buildings data;
    @SerializedName("state")
    public String state;

    public buildings getData() {
        return data;
    }

    public String getState() {
        return state;
    }
}
