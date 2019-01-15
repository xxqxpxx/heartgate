package dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class data {

    @SerializedName("id")
    public String id;
    @SerializedName("btn")
    public String btn;
    @SerializedName("village_id")
    public String village_id;
    @SerializedName("name")
    public String name;
    @SerializedName("username")
    public String username;
    @SerializedName("email")
    public String email;
    @SerializedName("admin")
    public String admin;
    @SerializedName("created_at")
    public String created_at;
    @SerializedName("updated_at")
    public String updated_at;
    @SerializedName("player_id")
    public String player_id;
    @SerializedName("village")
    public Village village;
    @SerializedName("resources")
    public resources resources;
    @SerializedName("inventory")
    public inventory inventory;
    @SerializedName("buildings")
    public buildings buildings;
    @SerializedName("notifications")
    public List<NotificationsModel> notifications;

    public String getBtn() {
        return btn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVillage_id() {
        return village_id;
    }

    public void setVillage_id(String village_id) {
        this.village_id = village_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
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

    public String getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(String player_id) {
        this.player_id = player_id;
    }

    public Village getVillage() {
        return village;
    }

    public void setVillage(Village village) {
        this.village = village;
    }

    public resources getResources() {
        return resources;
    }

    public void setResources(resources resources) {
        this.resources = resources;
    }

    public inventory getInventory() {
        return inventory;
    }

    public void setInventory(inventory inventory) {
        this.inventory = inventory;
    }

    public buildings getBuildings() {
        return buildings;
    }

    public void setBuildings(buildings buildings) {
        this.buildings = buildings;
    }

    public List<NotificationsModel> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationsModel> notifications) {
        this.notifications = notifications;
    }


}
