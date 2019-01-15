package dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel;

import com.google.gson.annotations.SerializedName;

public class ResultModelUpgradeInfo {

    @SerializedName("data")
    public Data data;
    @SerializedName("state")
    public String state;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public class Data {


        @SerializedName("current_level")
        public String current_level;

        @SerializedName("next_level")
        public String next_level;

        @SerializedName("req_gold")
        public String req_gold;

        @SerializedName("production")
        public String production;

        @SerializedName("req_resources")
        public String req_resources;

        @SerializedName("details")
        public String details;

        public String getCurrent_level() {
            return current_level;
        }

        public void setCurrent_level(String current_level) {
            this.current_level = current_level;
        }

        public String getNext_level() {
            return next_level;
        }

        public void setNext_level(String next_level) {
            this.next_level = next_level;
        }

        public String getReq_gold() {
            return req_gold;
        }

        public void setReq_gold(String req_gold) {
            this.req_gold = req_gold;
        }

        public String getProduction() {
            return production;
        }

        public void setProduction(String production) {
            this.production = production;
        }

        public String getReq_resources() {
            return req_resources;
        }

        public void setReq_resources(String req_resources) {
            this.req_resources = req_resources;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }
    }
}
