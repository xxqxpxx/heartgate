package dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel;

import com.google.gson.annotations.SerializedName;

public class ResultModelInventory {

    @SerializedName("data")
    public data data;
    @SerializedName("state")
    public String state;

    public ResultModelInventory.data getData() {
        return data;
    }

    public void setData(ResultModelInventory.data data) {
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
        @SerializedName("drug")
        public String drug;
        @SerializedName("food")
        public String food;
        @SerializedName("animals")
        public String animals;
        @SerializedName("gold")
        public String gold;

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

        public String getDrug() {
            return drug;
        }

        public void setDrug(String drug) {
            this.drug = drug;
        }

        public String getFood() {
            return food;
        }

        public void setFood(String food) {
            this.food = food;
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


    }
}
