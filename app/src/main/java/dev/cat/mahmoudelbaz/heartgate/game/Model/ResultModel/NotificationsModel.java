package dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel;

import com.google.gson.annotations.SerializedName;

public class NotificationsModel {

    @SerializedName("id")
    public String id;

    @SerializedName("user_id")
    public String user_id;

    @SerializedName("type")
    public String type;

    @SerializedName("notifiable_id")
    public String notifiable_id;

    @SerializedName("notifiable_type")
    public String notifiable_type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNotifiable_id() {
        return notifiable_id;
    }

    public void setNotifiable_id(String notifiable_id) {
        this.notifiable_id = notifiable_id;
    }

    public String getNotifiable_type() {
        return notifiable_type;
    }

    public void setNotifiable_type(String notifiable_type) {
        this.notifiable_type = notifiable_type;
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
