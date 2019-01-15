package dev.cat.mahmoudelbaz.heartgate.game.Presenter;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import dev.cat.mahmoudelbaz.heartgate.R;
import dev.cat.mahmoudelbaz.heartgate.game.Adapter.PlayerAdapter;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ApiInterface.UserApi;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelTopPlayer;
import dev.cat.mahmoudelbaz.heartgate.game.Retrofit.ApiConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CustomPlayerDialog extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    String userId;
    ImageView img_buidling_type;
    TextView info;
    Button btn_close;

    Handler handler;
    ProgressBar progress;

    ResultModelTopPlayer resultModelTopPlayer;

    RecyclerView rcvOffers;
    PlayerAdapter adapter;

    public CustomPlayerDialog(Activity a, String userId) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.userId = userId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.dialog_topplayers);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        rcvOffers = findViewById(R.id.rcv_layout);

        btn_close = findViewById(R.id.btn_close);
        progress = findViewById(R.id.progressBar);


        getTopPlayers();


        btn_close.setOnClickListener(this);
    }

    private void getTopPlayers() {

      /*  progress = new ProgressDialog(c);
        progress.setTitle(R.string.pleaseWait);
        progress.setMessage(c.getString(R.string.loading));
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

                final UserApi requestApi = retrofit.create(UserApi.class);

                final Call<ResultModelTopPlayer> getInterestConnection = requestApi.getTopPLayers();

                getInterestConnection.enqueue(new Callback<ResultModelTopPlayer>() {
                    @Override
                    public void onResponse(Call<ResultModelTopPlayer> call, Response<ResultModelTopPlayer> response) {
                        try {

                            if (!response.isSuccessful()) {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Toast.makeText(c, jObjError.getString("data"), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Toast.makeText(c, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                resultModelTopPlayer = response.body();
                                setData(resultModelTopPlayer);
                            }

                            progress.setVisibility(View.GONE);

                        } // try
                        catch (Exception e) {
                            Log.i("QP", "exception : " + e.toString());
                            progress.setVisibility(View.GONE);
                        } // catch
                    } // onResponse

                    @Override
                    public void onFailure(Call<ResultModelTopPlayer> call, Throwable t) {
                        Log.i("QP", "error : " + t.toString());
                        progress.setVisibility(View.GONE);
                    } // on Failure
                });
                // Retrofit
            }
        }.start();

    }

    private void setData(ResultModelTopPlayer resultModelUserRequests) {

        adapter = new PlayerAdapter(resultModelUserRequests, c);
        rcvOffers.setAdapter(adapter);
        rcvOffers.setLayoutManager(new LinearLayoutManager(c));

       /* DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        rcvOffers.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));*/

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        rcvOffers.addItemDecoration(dividerItemDecoration);

        rcvOffers.setHasFixedSize(true);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.upgrade:
                upgradeBuilding();
                break;*/
            case R.id.btn_close:
                dismiss();
                break;
            default:
                break;
        }
    }
}


