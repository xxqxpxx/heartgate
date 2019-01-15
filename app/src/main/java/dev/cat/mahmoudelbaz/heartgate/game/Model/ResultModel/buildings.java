package dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel;

import com.google.gson.annotations.SerializedName;

public class buildings {

    @SerializedName("id")
    public String id;

    @SerializedName("user_id")
    public String user_id;

    @SerializedName("farm")
    public String farm;

    @SerializedName("factory")
    public String factory;

    @SerializedName("hospital")
    public String hospital;

    @SerializedName("stockyard")
    public String stockyard;

    @SerializedName("created_at")
    public String created_at;


    @SerializedName("updated_at")
    public String updated_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFarm() {
        return farm;
    }

    public void setFarm(String farm) {
        this.farm = farm;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getStockyard() {
        return stockyard;
    }

    public void setStockyard(String stockyard) {
        this.stockyard = stockyard;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }


}
