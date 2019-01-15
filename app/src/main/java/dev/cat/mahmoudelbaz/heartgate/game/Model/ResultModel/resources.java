package dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel;

import com.google.gson.annotations.SerializedName;

public class resources {

    @SerializedName("id")
    public String id;
    @SerializedName("user_id")
    public String user_id;
    @SerializedName("water")
    public String water;
    @SerializedName("electricity")
    public String electricity;
    @SerializedName("workers")
    public String workers;

    @SerializedName("farmers")
    public String farmers;


    @SerializedName("doctors")
    public String doctors;


    @SerializedName("updated_at")
    public String updated_at;

    @SerializedName("created_at")
    public String created_at;


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

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getElectricity() {
        return electricity;
    }

    public void setElectricity(String electricity) {
        this.electricity = electricity;
    }

    public String getWorkers() {
        return workers;
    }

    public void setWorkers(String workers) {
        this.workers = workers;
    }

    public String getFarmers() {
        return farmers;
    }

    public void setFarmers(String farmers) {
        this.farmers = farmers;
    }

    public String getDoctors() {
        return doctors;
    }

    public void setDoctors(String doctors) {
        this.doctors = doctors;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

}
