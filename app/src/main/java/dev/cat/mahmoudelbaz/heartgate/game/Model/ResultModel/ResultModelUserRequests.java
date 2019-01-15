package dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultModelUserRequests {


    @SerializedName("data")
    public List<Data> data;
    @SerializedName("state")
    public String state;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public class Data {
        @SerializedName("id")
        public String id;
        @SerializedName("user_id")
        public String user_id;
        @SerializedName("resource_id")
        public String resource_id;
        @SerializedName("supplier_id")
        public String supplier_id;
        @SerializedName("user_name")
        public String user_name;
        @SerializedName("supplier_name")
        public String supplier_name;
        @SerializedName("reject")
        public String reject;

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

        public String getResource_id() {
            return resource_id;
        }

        public void setResource_id(String resource_id) {
            this.resource_id = resource_id;
        }

        public String getSupplier_id() {
            return supplier_id;
        }

        public void setSupplier_id(String supplier_id) {
            this.supplier_id = supplier_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getSupplier_name() {
            return supplier_name;
        }

        public void setSupplier_name(String supplier_name) {
            this.supplier_name = supplier_name;
        }

        public String getReject() {
            return reject;
        }

        public void setReject(String reject) {
            this.reject = reject;
        }
    }
}
