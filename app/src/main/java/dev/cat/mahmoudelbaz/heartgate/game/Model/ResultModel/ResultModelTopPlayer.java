package dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultModelTopPlayer {

    @SerializedName("data")
    public List<player> data;
    @SerializedName("state")
    public String state;

    public List<player> getData() {
        return data;
    }

    public void setData(List<player> data) {
        this.data = data;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public class player {

        @SerializedName("id")
        public String id;
        @SerializedName("user_id")
        public String user_id;
        @SerializedName("food")
        public String food;
        @SerializedName("drug")
        public String drug;
        @SerializedName("animals")
        public String animals;
        @SerializedName("gold")
        public String gold;
        @SerializedName("username")
        public String username;

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

        public String getFood() {
            return food;
        }

        public void setFood(String food) {
            this.food = food;
        }

        public String getDrug() {
            return drug;
        }

        public void setDrug(String drug) {
            this.drug = drug;
        }

        public String getAnimals() {
            return animals;
        }

        public void setAnimals(String animals) {
            this.animals = animals;
        }

        public String getGold() {
            return gold;
        }

        public void setGold(String gold) {
            this.gold = gold;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

}
