package dev.cat.mahmoudelbaz.heartgate.game.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;

import dev.cat.mahmoudelbaz.heartgate.R;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ApiInterface.UserApi;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelBuildingsResponse;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelBuyItem;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelFiterbyResource;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelLogin;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelUpgradeRequest;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.buildings;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.inventory;
import dev.cat.mahmoudelbaz.heartgate.game.Presenter.CustomMarketDialog;
import dev.cat.mahmoudelbaz.heartgate.game.Presenter.CustomPlayerDialog;
import dev.cat.mahmoudelbaz.heartgate.game.Presenter.customBuildingDialog;
import dev.cat.mahmoudelbaz.heartgate.game.Presenter.customResourcesDialog;
import dev.cat.mahmoudelbaz.heartgate.game.Presenter.wrapper;
import dev.cat.mahmoudelbaz.heartgate.game.Retrofit.ApiConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BuildingActivity extends AppCompatActivity implements wrapper {

    public static String item, count;
    public static ResultModelBuyItem resultModelBuyItem;
    ImageView img_resources, img_upgrade, btn_water, btn_electricity, btn_doctors, btn_farmers, btn_workers, img_requests, img_market, img_profile, building_img;
    TextView stock_level_count, building_level_count, factorylevelcount, farm_count_level, building_level, hospital_level_count, txt_money_count, txt_food_count, building_title, txt_medicine_count, txt_animals_count;
    CustomMarketDialog marketDialog;
    customBuildingDialog buildingDialog;
    customResourcesDialog resourceDialog;
    CustomPlayerDialog customPlayerDialog;
    Handler handler;
    ProgressDialog progress;
    List<ResultModelFiterbyResource> resultModelFiterbyResources;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);

        Intent intent = getIntent();
        item = intent.getExtras().getString("type");

        setupUI();

        // Get User Inventory

        setdata(HomeActivty.resultModelLogin);

        initView();

    }

    private void setupUI() {

        img_resources = findViewById(R.id.img_resources);
        img_market = findViewById(R.id.img_market);
        img_upgrade = findViewById(R.id.img_upgrade);
        building_img = findViewById(R.id.building_img);
        img_profile = findViewById(R.id.img_profile);

        txt_food_count = findViewById(R.id.txt_food_count);
        txt_animals_count = findViewById(R.id.txt_animals_count);
        txt_medicine_count = findViewById(R.id.txt_medicine_count);
        txt_money_count = findViewById(R.id.txt_money_count);

        building_title = findViewById(R.id.building_title);
        building_level = findViewById(R.id.building_level);
        building_level_count = findViewById(R.id.building_level_count);

        btn_water = findViewById(R.id.btn_water);
        btn_electricity = findViewById(R.id.btn_electricity);
        btn_doctors = findViewById(R.id.btn_doctors);
        btn_farmers = findViewById(R.id.btn_farmers);
        btn_workers = findViewById(R.id.btn_workers);
    }

    private void initView() {


        marketDialog = new CustomMarketDialog(this, userId);


        resourceDialog = new customResourcesDialog(this, userId, resultModelFiterbyResources);

        customPlayerDialog = new CustomPlayerDialog(this, userId);

        img_resources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resourceDialog.show();
            }
        });

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customPlayerDialog.show();
            }
        });


        building_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpgradeDialog();
            }
        });

        img_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marketDialog.show();
            }
        });

        img_upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpgradeDialog();
            }
        });


    } // intialization of farmDialogs

    private void showUpgradeDialog() {
        buildingDialog = new customBuildingDialog(this, userId, item);
        buildingDialog.show();
    }

    private void setdata(ResultModelLogin resultModelLogin) {

        if (resultModelLogin.getData() != null) {
            // users counts
            txt_money_count.setText(resultModelLogin.getData().getInventory().getGold());
            txt_medicine_count.setText(resultModelLogin.getData().getInventory().getDrug());
            txt_animals_count.setText(resultModelLogin.getData().getInventory().getAnimals());
            txt_food_count.setText(resultModelLogin.getData().getInventory().getFood());


            // buildings Levels
            if (item.equals("farm")) {
                building_level_count.setText(resultModelLogin.getData().getBuildings().getFarm());
                building_title.setText("farm");

                if (building_level_count.getText().toString().trim().equals("1"))
                    building_img.setImageResource(R.drawable.farm_level1);

                else if (building_level_count.getText().toString().trim().equals("2"))
                    building_img.setImageResource(R.drawable.farm_level2);

                else if (building_level_count.getText().toString().trim().equals("3"))
                    building_img.setImageResource(R.drawable.farm_level3);

                else if (building_level_count.getText().toString().trim().equals("4"))
                    building_img.setImageResource(R.drawable.farm_level4);

                else if (building_level_count.getText().toString().trim().equals("5"))
                    building_img.setImageResource(R.drawable.farm_level5);

                else if (building_level_count.getText().toString().trim().equals("6"))
                    building_img.setImageResource(R.drawable.farm_level6);

                else if (building_level_count.getText().toString().trim().equals("7"))
                    building_img.setImageResource(R.drawable.farm_level7);

                else
                    building_img.setImageResource(R.drawable.farm_level8);


            } else if (item.equals("hospital")) {
                building_level_count.setText(resultModelLogin.getData().getBuildings().getHospital());
                building_title.setText("hospital");


                if (building_level_count.getText().toString().trim().equals("1"))
                    building_img.setImageResource(R.drawable.hospital_level1);

                else if (building_level_count.getText().toString().trim().equals("2"))
                    building_img.setImageResource(R.drawable.hospital_level2);

                else if (building_level_count.getText().toString().trim().equals("3"))
                    building_img.setImageResource(R.drawable.hospital_level4);

                else if (building_level_count.getText().toString().trim().equals("4"))
                    building_img.setImageResource(R.drawable.hospital_level5);

                else if (building_level_count.getText().toString().trim().equals("5"))
                    building_img.setImageResource(R.drawable.hospital_level6);

                else if (building_level_count.getText().toString().trim().equals("6"))
                    building_img.setImageResource(R.drawable.hospital_level7);

                else if (building_level_count.getText().toString().trim().equals("7"))
                    building_img.setImageResource(R.drawable.hospital_level8);

                else
                    building_img.setImageResource(R.drawable.hospital_level9);
            } else if (item.equals("stockyard")) {
                building_level_count.setText(resultModelLogin.getData().getBuildings().getStockyard());
                building_title.setText("stockyard");


                if (building_level_count.getText().toString().trim().equals("1"))
                    building_img.setImageResource(R.drawable.stock_level1);

                else if (building_level_count.getText().toString().trim().equals("2"))
                    building_img.setImageResource(R.drawable.stock_level2);

                else if (building_level_count.getText().toString().trim().equals("3"))
                    building_img.setImageResource(R.drawable.stock_level3);

                else if (building_level_count.getText().toString().trim().equals("4"))
                    building_img.setImageResource(R.drawable.stock_level4);

                else if (building_level_count.getText().toString().trim().equals("5"))
                    building_img.setImageResource(R.drawable.stock_level5);

                else if (building_level_count.getText().toString().trim().equals("6"))
                    building_img.setImageResource(R.drawable.stock_level6);

                else if (building_level_count.getText().toString().trim().equals("7"))
                    building_img.setImageResource(R.drawable.stock_level7);

                else
                    building_img.setImageResource(R.drawable.stock_level8);
            } else if (item.equals("factory")) {
                building_level_count.setText(resultModelLogin.getData().getBuildings().getFactory());
                building_title.setText("factory");


                if (building_level_count.getText().toString().trim().equals("1"))
                    building_img.setImageResource(R.drawable.factory_level1);

                else if (building_level_count.getText().toString().trim().equals("2"))
                    building_img.setImageResource(R.drawable.factory_level2);

                else if (building_level_count.getText().toString().trim().equals("3"))
                    building_img.setImageResource(R.drawable.factory_level3);

                else if (building_level_count.getText().toString().trim().equals("4"))
                    building_img.setImageResource(R.drawable.factory_level4);

                else if (building_level_count.getText().toString().trim().equals("5"))
                    building_img.setImageResource(R.drawable.factory_level5);

                else if (building_level_count.getText().toString().trim().equals("6"))
                    building_img.setImageResource(R.drawable.factory_level6);

                else if (building_level_count.getText().toString().trim().equals("7"))
                    building_img.setImageResource(R.drawable.factory_level7);

                else
                    building_img.setImageResource(R.drawable.factory_level8);

            }
            // on / Off resources
            if (resultModelLogin.getData().getResources().getWater().equals("1"))
                btn_water.setImageResource(R.drawable.wateron);

            if (resultModelLogin.getData().getResources().getElectricity().equals("1"))
                btn_electricity.setImageResource(R.drawable.electricityon);

            if (resultModelLogin.getData().getResources().getDoctors().equals("1"))
                btn_doctors.setImageResource(R.drawable.doctorson);

            if (resultModelLogin.getData().getResources().getFarmers().equals("1"))
                btn_farmers.setImageResource(R.drawable.farmerson);

            if (resultModelLogin.getData().getResources().getWorkers().equals("1"))
                btn_workers.setImageResource(R.drawable.workerson);

            userId = resultModelLogin.getData().getId();
        }
    }


    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public void updateUiCounter(ResultModelBuyItem resultModelBuyItem) {

        inventory inventory = new inventory();

        inventory.setAnimals(resultModelBuyItem.getData().getAnimals());
        inventory.setDrug(resultModelBuyItem.getData().getDrug());
        inventory.setFood(resultModelBuyItem.getData().getFood());
        inventory.setGold(resultModelBuyItem.getData().getGold());

        HomeActivty.resultModelLogin.data.setInventory(inventory);

        txt_money_count.setText(resultModelBuyItem.getData().getGold());
        txt_medicine_count.setText(resultModelBuyItem.getData().getDrug());
        txt_animals_count.setText(resultModelBuyItem.getData().getAnimals());
        txt_food_count.setText(resultModelBuyItem.getData().getFood());
    }

    @Override
    public void updateUiBuilding(ResultModelUpgradeRequest body) {
        updateUiBuildingLevel(userId);
    }

    public void updateUiBuildingLevel(String userId) {


        ApiConnection connection = new ApiConnection();
        Retrofit retrofit = connection.connectWith();

        final UserApi userApi = retrofit.create(UserApi.class);


        final Call<ResultModelBuildingsResponse> getInterestConnection = userApi.getBuilding(userId);

        getInterestConnection.enqueue(new Callback<ResultModelBuildingsResponse>() {
            @Override
            public void onResponse(Call<ResultModelBuildingsResponse> call, Response<ResultModelBuildingsResponse> response) {
                try {

                    if (!response.isSuccessful()) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                        } catch (Exception e) {
                        }
                    } else {
                        updateNewBuildingUI(response.body());

                    }


                } // try
                catch (Exception e) {
                    Log.i("QP", "exception : " + e.toString());
                } // catch
            } // onResponse

            @Override
            public void onFailure(Call<ResultModelBuildingsResponse> call, Throwable t) {
                Log.i("QP", "error : " + t.toString());
            } // on Failure
        });
        // Retrofit
    }


    private void updateNewBuildingUI(ResultModelBuildingsResponse body) {


        buildings buildings = new buildings();

        buildings.setFactory(body.getData().getFactory());
        buildings.setFarm(body.getData().getFarm());
        buildings.setHospital(body.getData().getHospital());
        buildings.setStockyard(body.getData().getStockyard());

        HomeActivty.resultModelLogin.data.setBuildings(buildings);


        // buildings Levels
        if (item.equals("farm")) {
            building_level_count.setText(body.getData().getFarm());
            building_title.setText("farm");

            if (building_level_count.getText().toString().trim().equals("1"))
                building_img.setImageResource(R.drawable.farm_level1);

            else if (building_level_count.getText().toString().trim().equals("2"))
                building_img.setImageResource(R.drawable.farm_level2);

            else if (building_level_count.getText().toString().trim().equals("3"))
                building_img.setImageResource(R.drawable.farm_level3);

            else if (building_level_count.getText().toString().trim().equals("4"))
                building_img.setImageResource(R.drawable.farm_level4);

            else if (building_level_count.getText().toString().trim().equals("5"))
                building_img.setImageResource(R.drawable.farm_level5);

            else if (building_level_count.getText().toString().trim().equals("6"))
                building_img.setImageResource(R.drawable.farm_level6);

            else if (building_level_count.getText().toString().trim().equals("7"))
                building_img.setImageResource(R.drawable.farm_level7);

            else
                building_img.setImageResource(R.drawable.farm_level8);


        } else if (item.equals("hospital")) {
            building_level_count.setText(body.getData().getHospital());
            building_title.setText("hospital");


            if (building_level_count.getText().toString().trim().equals("1"))
                building_img.setImageResource(R.drawable.hospital_level1);

            else if (building_level_count.getText().toString().trim().equals("2"))
                building_img.setImageResource(R.drawable.hospital_level2);

            else if (building_level_count.getText().toString().trim().equals("3"))
                building_img.setImageResource(R.drawable.hospital_level4);

            else if (building_level_count.getText().toString().trim().equals("4"))
                building_img.setImageResource(R.drawable.hospital_level5);

            else if (building_level_count.getText().toString().trim().equals("5"))
                building_img.setImageResource(R.drawable.hospital_level6);

            else if (building_level_count.getText().toString().trim().equals("6"))
                building_img.setImageResource(R.drawable.hospital_level7);

            else if (building_level_count.getText().toString().trim().equals("7"))
                building_img.setImageResource(R.drawable.hospital_level8);

            else
                building_img.setImageResource(R.drawable.hospital_level9);
        } else if (item.equals("stockyard")) {
            building_level_count.setText(body.getData().getStockyard());
            building_title.setText("stockyard");


            if (building_level_count.getText().toString().trim().equals("1"))
                building_img.setImageResource(R.drawable.stock_level1);

            else if (building_level_count.getText().toString().trim().equals("2"))
                building_img.setImageResource(R.drawable.stock_level2);

            else if (building_level_count.getText().toString().trim().equals("3"))
                building_img.setImageResource(R.drawable.stock_level3);

            else if (building_level_count.getText().toString().trim().equals("4"))
                building_img.setImageResource(R.drawable.stock_level4);

            else if (building_level_count.getText().toString().trim().equals("5"))
                building_img.setImageResource(R.drawable.stock_level5);

            else if (building_level_count.getText().toString().trim().equals("6"))
                building_img.setImageResource(R.drawable.stock_level6);

            else if (building_level_count.getText().toString().trim().equals("7"))
                building_img.setImageResource(R.drawable.stock_level7);

            else
                building_img.setImageResource(R.drawable.stock_level8);
        } else if (item.equals("factory")) {
            building_level_count.setText(body.getData().getFactory());
            building_title.setText("factory");


            if (building_level_count.getText().toString().trim().equals("1"))
                building_img.setImageResource(R.drawable.factory_level1);

            else if (building_level_count.getText().toString().trim().equals("2"))
                building_img.setImageResource(R.drawable.factory_level2);

            else if (building_level_count.getText().toString().trim().equals("3"))
                building_img.setImageResource(R.drawable.factory_level3);

            else if (building_level_count.getText().toString().trim().equals("4"))
                building_img.setImageResource(R.drawable.factory_level4);

            else if (building_level_count.getText().toString().trim().equals("5"))
                building_img.setImageResource(R.drawable.factory_level5);

            else if (building_level_count.getText().toString().trim().equals("6"))
                building_img.setImageResource(R.drawable.factory_level6);

            else if (building_level_count.getText().toString().trim().equals("7"))
                building_img.setImageResource(R.drawable.factory_level7);

            else
                building_img.setImageResource(R.drawable.factory_level8);
        }
    }


}
