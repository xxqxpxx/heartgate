package dev.cat.mahmoudelbaz.heartgate.game.View;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import dev.cat.mahmoudelbaz.heartgate.R;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ApiInterface.UserApi;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelBuildingsResponse;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelBuyItem;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelFiterbyResource;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelInventory;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelLogin;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelResources;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelUpgradeRequest;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.inventory;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.resources;
import dev.cat.mahmoudelbaz.heartgate.game.Presenter.CustomMarketDialog;
import dev.cat.mahmoudelbaz.heartgate.game.Presenter.CustomPlayerDialog;
import dev.cat.mahmoudelbaz.heartgate.game.Presenter.customBuildingDialog;
import dev.cat.mahmoudelbaz.heartgate.game.Presenter.customRequestsDialog;
import dev.cat.mahmoudelbaz.heartgate.game.Presenter.customResourcesDialog;
import dev.cat.mahmoudelbaz.heartgate.game.Presenter.wrapper;
import dev.cat.mahmoudelbaz.heartgate.game.Retrofit.ApiConnection;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeActivty extends AppCompatActivity implements wrapper {

    public static String item, count;
    public static ResultModelLogin resultModelLogin;
    public static ResultModelBuyItem resultModelBuyItem;
    TextView stock_level_count, factorylevelcount, farm_count_level, hospital_level_count, txt_money_count, txt_food_count, txt_medicine_count, txt_animals_count;
    ImageView img_resources, btn_water, btn_electricity, btn_doctors, btn_farmers, btn_workers, img_requests, img_market, img_stock, img_farm, img_factory, img_hospital, img_profile;
    CustomMarketDialog marketDialog;
    customBuildingDialog buildingDialog;
    customResourcesDialog resourceDialog;
    customRequestsDialog requestsDialog;
    CustomPlayerDialog customPlayerDialog;
    Handler handler;
    ProgressDialog progress;
    List<ResultModelFiterbyResource> resultModelFiterbyResources;
    String userId;
    ResultModelInventory resultModelInventory;
    // Our handler for received Intents. This will be called whenever an Intent
    // with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            String type = intent.getStringExtra("type");
            Object objects = intent.getExtras().get("data");

            JSONObject data = null;
            try {
                data = new JSONObject((String) objects);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("receiver", "Got message: " + message);

            if (type.equals("inventory")) {

                ResultModelInventory resultModelResources = new ResultModelInventory();
                String mJsonString = data.toString();
                JsonParser parser = new JsonParser();
                JsonElement mJson = parser.parse(mJsonString);
                Gson gson = new Gson();
                ResultModelInventory.data object = gson.fromJson(mJson, ResultModelInventory.data.class);
                resultModelResources.setData(object);

                updateInventoryUi(resultModelResources);

            } else if (type.equals("resources")) {
                ResultModelResources resultModelResources = new ResultModelResources();
                String mJsonString = data.toString();
                JsonParser parser = new JsonParser();
                JsonElement mJson = parser.parse(mJsonString);
                Gson gson = new Gson();
                ResultModelResources.data object = gson.fromJson(mJson, ResultModelResources.data.class);
                resultModelResources.setData(object);

                updateResourcesUi(resultModelResources);
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac);

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        setupUI();


        // Get User Inventory

        resultModelLogin = LoginActivity.resultModelLogin;

        if (resultModelLogin != null) {
            setdata(resultModelLogin);

        } else {
            goToLogin();
        }

        initView();
        // getResourcesLists();


      /*  // Init
         final Handler handler = new Handler();
         Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getNewUidata();
                handler.postDelayed(this, 5000);
            }
        };

            //Start
        handler.postDelayed(runnable, 1000);*/


        changePlayerID();

        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-event-name".
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));

    }

    private void goToLogin() {

        Intent mainIntent = new Intent(HomeActivty.this, LoginActivity.class);
        HomeActivty.this.startActivity(mainIntent);
        HomeActivty.this.finish();


    }

    private void getNewUidata() {


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }

        };

        new Thread() {
            public void run() {
                //Retrofit
                ApiConnection connection = new ApiConnection();
                Retrofit retrofit = connection.connectWith();

                final UserApi userApi = retrofit.create(UserApi.class);


                final Call<ResultModelInventory> getInterestConnection = userApi.getInventory(userId);

                getInterestConnection.enqueue(new Callback<ResultModelInventory>() {
                    @Override
                    public void onResponse(Call<ResultModelInventory> call, Response<ResultModelInventory> response) {
                        try {

                            if (!response.isSuccessful()) {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                } catch (Exception e) {
                                }
                            } else {
                                resultModelInventory = response.body();
                                updateInventoryUi(resultModelInventory);
                            }


                        } // try
                        catch (Exception e) {
                            Log.i("QP", "exception : " + e.toString());
                        } // catch
                    } // onResponse

                    @Override
                    public void onFailure(Call<ResultModelInventory> call, Throwable t) {
                        Log.i("QP", "error : " + t.toString());
                    } // on Failure
                });
                // Retrofit
            }
        }.start();


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }

        };

        new Thread() {
            public void run() {
                //Retrofit
                ApiConnection connection = new ApiConnection();
                Retrofit retrofit = connection.connectWith();

                final UserApi userApi = retrofit.create(UserApi.class);


                final Call<ResultModelResources> getInterestConnection = userApi.getResources(userId);

                getInterestConnection.enqueue(new Callback<ResultModelResources>() {
                    @Override
                    public void onResponse(Call<ResultModelResources> call, Response<ResultModelResources> response) {
                        try {

                            if (!response.isSuccessful()) {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                } catch (Exception e) {
                                }
                            } else {

                                updateResourcesUi(response.body());
                            }


                        } // try
                        catch (Exception e) {
                            Log.i("QP", "exception : " + e.toString());
                        } // catch
                    } // onResponse

                    @Override
                    public void onFailure(Call<ResultModelResources> call, Throwable t) {
                        Log.i("QP", "error : " + t.toString());
                    } // on Failure
                });
                // Retrofit
            }
        }.start();


    }

    private void updateResourcesUi(ResultModelResources body) {

        resources resources = new resources();

        resources.setDoctors(body.getData().getDoctors());
        resources.setElectricity(body.getData().getElectricity());
        resources.setFarmers(body.getData().getFarmers());
        resources.setWater(body.getData().getWater());
        resources.setWorkers(body.getData().getWorkers());


        resultModelLogin.data.setResources(resources);


        // on / Off resources
        if (body.getData().getWater().equals("1"))
            btn_water.setImageResource(R.drawable.wateron);

        if (body.getData().getElectricity().equals("1"))
            btn_electricity.setImageResource(R.drawable.electricityon);

        if (body.getData().getDoctors().equals("1"))
            btn_doctors.setImageResource(R.drawable.doctorson);

        if (body.getData().getFarmers().equals("1"))
            btn_farmers.setImageResource(R.drawable.farmerson);

        if (body.getData().getWorkers().equals("1"))
            btn_workers.setImageResource(R.drawable.workerson);


    }

    private void setupUI() {

        img_resources = findViewById(R.id.img_resources);
        img_market = findViewById(R.id.img_market);
        img_requests = findViewById(R.id.img_requests);
        img_farm = findViewById(R.id.img_farm);
        img_stock = findViewById(R.id.img_stock);
        img_factory = findViewById(R.id.img_factory);
        img_hospital = findViewById(R.id.img_hospital);
        img_profile = findViewById(R.id.img_profile);

        txt_food_count = findViewById(R.id.txt_food_count);
        txt_animals_count = findViewById(R.id.txt_animals_count);
        txt_medicine_count = findViewById(R.id.txt_medicine_count);
        txt_money_count = findViewById(R.id.txt_money_count);

        stock_level_count = findViewById(R.id.stock_level_count);
        hospital_level_count = findViewById(R.id.hospital_level_count);
        farm_count_level = findViewById(R.id.farm_count_level);
        factorylevelcount = findViewById(R.id.factorylevelcount);

        btn_water = findViewById(R.id.btn_water);
        btn_electricity = findViewById(R.id.btn_electricity);
        btn_doctors = findViewById(R.id.btn_doctors);
        btn_farmers = findViewById(R.id.btn_farmers);
        btn_workers = findViewById(R.id.btn_workers);
    }

    private void initView() {


        marketDialog = new CustomMarketDialog(this, userId);


        resourceDialog = new customResourcesDialog(this, userId, resultModelFiterbyResources);
        requestsDialog = new customRequestsDialog(this, userId);

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


        img_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marketDialog.show();
            }
        });

        img_requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestsDialog.show();
            }
        });

        img_farm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBuildingInfo("farm");
            }
        });

        img_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBuildingInfo("stockyard");
            }
        });

        img_factory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBuildingInfo("factory");
            }
        });

        img_hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBuildingInfo("hospital");
            }
        });

    } // intialization of farmDialogs

    private void setdata(ResultModelLogin resultModelLogin) {

        if (resultModelLogin.getData() != null) {
            // users counts
            txt_money_count.setText(resultModelLogin.getData().getInventory().getGold());
            txt_medicine_count.setText(resultModelLogin.getData().getInventory().getDrug());
            txt_animals_count.setText(resultModelLogin.getData().getInventory().getAnimals());
            txt_food_count.setText(resultModelLogin.getData().getInventory().getFood());


            // buildings Levels
            farm_count_level.setText(resultModelLogin.getData().getBuildings().getFarm());
            hospital_level_count.setText(resultModelLogin.getData().getBuildings().getHospital());
            stock_level_count.setText(resultModelLogin.getData().getBuildings().getStockyard());
            factorylevelcount.setText(resultModelLogin.getData().getBuildings().getFactory());

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
        txt_money_count.setText(resultModelBuyItem.getData().getGold());
        txt_medicine_count.setText(resultModelBuyItem.getData().getDrug());
        txt_animals_count.setText(resultModelBuyItem.getData().getAnimals());
        txt_food_count.setText(resultModelBuyItem.getData().getFood());
    }

    @Override
    public void updateUiBuilding(ResultModelUpgradeRequest body) {
        updateUiBuildingLevel(userId);
    }

    private void goToBuildingInfo(String buildingType) {
        /* Create an Intent that will start the RegisterScreen. */
        Intent mainIntent = new Intent(HomeActivty.this, BuildingActivity.class);
        mainIntent.putExtra("type", buildingType);
        HomeActivty.this.startActivity(mainIntent);
    }


    public void updateInventoryUi(ResultModelInventory resultModelBuyItem) {

        inventory inventory = new inventory();

        inventory.setAnimals(resultModelBuyItem.getData().getAnimals());
        inventory.setDrug(resultModelBuyItem.getData().getDrug());
        inventory.setFood(resultModelBuyItem.getData().getFood());
        inventory.setGold(resultModelBuyItem.getData().getGold());

        resultModelLogin.data.setInventory(inventory);


        txt_money_count.setText(resultModelBuyItem.getData().getGold());
        txt_medicine_count.setText(resultModelBuyItem.getData().getDrug());
        txt_animals_count.setText(resultModelBuyItem.getData().getAnimals());
        txt_food_count.setText(resultModelBuyItem.getData().getFood());
    }

    public void updateUiBuildingLevel(String userId) {


        final String id = userId;
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progress.dismiss();
                super.handleMessage(msg);
            }

        };

        new Thread() {
            public void run() {


                ApiConnection connection = new ApiConnection();
                Retrofit retrofit = connection.connectWith();

                final UserApi userApi = retrofit.create(UserApi.class);

                final Call<ResultModelBuildingsResponse> getInterestConnection = userApi.getBuilding(id);

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
        }.start();
    }

    private void updateNewBuildingUI(ResultModelBuildingsResponse body) {
        // buildings Levels
        farm_count_level.setText(body.getData().getFarm());
        hospital_level_count.setText(body.getData().getHospital());
        stock_level_count.setText(body.getData().getStockyard());
        factorylevelcount.setText(body.getData().getFactory());

    }


    public void changePlayerID() {


        ApiConnection connection = new ApiConnection();
        Retrofit retrofit = connection.connectWith();

        final UserApi userApi = retrofit.create(UserApi.class);


        final HashMap<String, String> data = new HashMap<>();


        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        status.getPermissionStatus().getEnabled();

        String playerId = status.getSubscriptionStatus().getUserId();
        //   Log.d("PlayerId", playerId);


        data.put("user_id", userId);
        data.put("player_id", playerId);

        final Call<ResponseBody> getInterestConnection = userApi.updatePlayerId(data);


//            dialog.show();
        getInterestConnection.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String message = null;
                int state = 0;
                try {
                    JSONObject res = new JSONObject(response.body().string());
                    message = res.getString("Message");
                    state = res.getInt("state");
                    Log.d("Response", message + state);


                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (state == 0) {
//                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                } else if (state == 1) {
//                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }


        });
    }


    public void updateBackgroundNotificationStatus(String s) {

        ApiConnection connection = new ApiConnection();
        Retrofit retrofit = connection.connectWith();

        final UserApi userApi = retrofit.create(UserApi.class);

        final HashMap<String, String> data = new HashMap<>();


        data.put("user_id", userId);
        data.put("flag", s);

        final Call<Object> getInterestConnection = userApi.updateNotificationStatus(data);


//            dialog.show();
        getInterestConnection.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                String message = null;
                int state = 0;
                try {
                    JSONObject res = new JSONObject(response.toString());
                    message = res.getString("Message");
                    state = res.getInt("state");
                    Log.d("Response", message + state);


                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (state == 0) {
//                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                } else if (state == 1) {
//                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }


        });
    }


    @Override
    public void onResume() {
        super.onResume();
        updateBackgroundNotificationStatus("1");
        setdata(resultModelLogin);
    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        updateBackgroundNotificationStatus("0");

        super.onPause();
        //  LocalBroadcastManager.getInstance(this).unregisterReceiver(mMyBroadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        updateBackgroundNotificationStatus("0");
        super.onDestroy();
    }

}

