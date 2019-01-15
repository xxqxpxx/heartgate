package dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel;

import com.google.gson.annotations.SerializedName;

public class ResultModelResources {

    @SerializedName("data")
    public data data;
    @SerializedName("state")
    public String state;

    public ResultModelResources.data getData() {
        return data;
    }

    public void setData(ResultModelResources.data data) {
        this.data = data;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public class data {
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
    }

}
