package dev.cat.mahmoudelbaz.heartgate.game.Presenter;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import dev.cat.mahmoudelbaz.heartgate.R;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ApiInterface.MarketApi;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelBuyItem;
import dev.cat.mahmoudelbaz.heartgate.game.Retrofit.ApiConnection;
import dev.cat.mahmoudelbaz.heartgate.game.View.HomeActivty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static dev.cat.mahmoudelbaz.heartgate.game.View.HomeActivty.count;
import static dev.cat.mahmoudelbaz.heartgate.game.View.HomeActivty.item;

public class CustomMarketDialog extends Dialog implements
        View.OnClickListener {

    public Dialog d;
    public Button btn_buy_animal, btn_sell_animal, btn_buy_medicine, btn_sell_medicine, btn_buy_food, btn_sell_food, btn_close;
    public EditText count_animal, count_medicine, count_food;
    wrapper wrapper;
    String userId;
    Handler handler;
    ProgressBar progress;
    ResultModelBuyItem resultModelBuyItem;


    public CustomMarketDialog(wrapper wrapper, String userId) {
        super(wrapper.getActivity());
        // TODO Auto-generated constructor stub
        this.wrapper = wrapper;
        this.userId = userId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.dialog_market);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        btn_buy_animal = (Button) findViewById(R.id.btn_buy_animal);
        btn_sell_animal = (Button) findViewById(R.id.btn_sell_animal);
        btn_buy_medicine = (Button) findViewById(R.id.btn_buy_medicine);
        btn_sell_medicine = (Button) findViewById(R.id.btn_sell_medicine);
        btn_buy_food = (Button) findViewById(R.id.btn_buy_food);
        btn_sell_food = (Button) findViewById(R.id.btn_sell_food);
        btn_close = (Button) findViewById(R.id.btn_close);
        count_animal = findViewById(R.id.count_animal);
        count_medicine = findViewById(R.id.count_medicine);
        count_food = findViewById(R.id.count_food);

     /*   rogress = new ProgressDialog(wrapper.getActivity());
        progress.setTitle(R.string.pleaseWait);
        progress.setMessage(wrapper.getActivity().getString(R.string.loading));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);*/


        progress = findViewById(R.id.progressBar);


        btn_buy_animal.setOnClickListener(this);
        btn_sell_animal.setOnClickListener(this);
        btn_buy_medicine.setOnClickListener(this);
        btn_sell_medicine.setOnClickListener(this);
        btn_buy_food.setOnClickListener(this);
        btn_sell_food.setOnClickListener(this);
        btn_close.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_buy_animal:
                item = "animals";
                count = count_animal.getText().toString().trim();
                buyitem(item, count);
                break;
            case R.id.btn_sell_animal:
                item = "animals";
                count = count_animal.getText().toString().trim();
                sellItem(item, count);
                break;
            case R.id.btn_buy_medicine:
                item = "medicine";
                count = count_medicine.getText().toString().trim();
                buyitem(item, count);
                break;
            case R.id.btn_sell_medicine:
                item = "medicine";
                count = count_medicine.getText().toString().trim();
                sellItem(item, count);

                break;
            case R.id.btn_buy_food:
                item = "food";
                count = count_food.getText().toString().trim();
                buyitem(item, count);

                break;
            case R.id.btn_sell_food:
                item = "food";
                count = count_food.getText().toString().trim();
                sellItem(item, count);

                break;
            case R.id.btn_close:
                dismiss();
                break;
            default:
                break;
        }
    }

    private void sellItem(final String item, final String count) {


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progress.setVisibility(View.GONE);
                super.handleMessage(msg);
            }

        };

        progress.setVisibility(View.VISIBLE);


        new Thread() {
            public void run() {
                //Retrofit
                ApiConnection connection = new ApiConnection();
                Retrofit retrofit = connection.connectWith();

                final HashMap<String, String> data = new HashMap<>();

                data.put("user_id", userId);
                data.put("item", item);
                data.put("quantity", count);

                final MarketApi marketApi = retrofit.create(MarketApi.class);

                final Call<ResultModelBuyItem> getInterestConnection = marketApi.sell_item(data);

                getInterestConnection.enqueue(new Callback<ResultModelBuyItem>() {
                    @Override
                    public void onResponse(Call<ResultModelBuyItem> call, Response<ResultModelBuyItem> response) {
                        try {

                            if (!response.isSuccessful()) {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Toast.makeText(wrapper.getActivity(), jObjError.getString("data"), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Toast.makeText(wrapper.getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                HomeActivty.resultModelBuyItem = response.body();
                                wrapper.updateUiCounter(response.body());

                            }

                            progress.setVisibility(View.GONE);

                        } // try
                        catch (Exception e) {
                            Log.i("QP", "exception : " + e.toString());
                            progress.setVisibility(View.GONE);
                        } // catch
                    } // onResponse

                    @Override
                    public void onFailure(Call<ResultModelBuyItem> call, Throwable t) {
                        Log.i("QP", "error : " + t.toString());
                        progress.setVisibility(View.GONE);
                    } // on Failure
                });
                // Retrofit
            }
        }.start();

    }


    private void buyitem(final String item, final String count) {


        /*progress = new ProgressDialog(wrapper.getActivity());
        progress.setTitle(R.string.pleaseWait);
        progress.setMessage(wrapper.getActivity().getString(R.string.loading));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);*/


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progress.setVisibility(View.GONE);
                super.handleMessage(msg);
            }

        };

        progress.setVisibility(View.VISIBLE);

        new Thread() {
            public void run() {
                //Retrofit
                ApiConnection connection = new ApiConnection();
                Retrofit retrofit = connection.connectWith();

                final HashMap<String, String> data = new HashMap<>();

                data.put("user_id", userId);
                data.put("item", item);
                data.put("quantity", count);

                final MarketApi marketApi = retrofit.create(MarketApi.class);

                final Call<ResultModelBuyItem> getInterestConnection = marketApi.buy_item(data);

                getInterestConnection.enqueue(new Callback<ResultModelBuyItem>() {
                    @Override
                    public void onResponse(Call<ResultModelBuyItem> call, Response<ResultModelBuyItem> response) {
                        try {

                            if (!response.isSuccessful()) {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Toast.makeText(wrapper.getActivity(), jObjError.getString("data"), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Toast.makeText(wrapper.getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            } else {

                                HomeActivty.resultModelBuyItem = response.body();
                                wrapper.updateUiCounter(response.body());
                            }

                            progress.setVisibility(View.GONE);

                        } // try
                        catch (Exception e) {
                            Log.i("QP", "exception : " + e.toString());
                            progress.setVisibility(View.GONE);
                        } // catch
                    } // onResponse

                    @Override
                    public void onFailure(Call<ResultModelBuyItem> call, Throwable t) {
                        Log.i("QP", "error : " + t.toString());
                        progress.setVisibility(View.GONE);
                    } // on Failure
                });
                // Retrofit
            }
        }.start();

    }


}