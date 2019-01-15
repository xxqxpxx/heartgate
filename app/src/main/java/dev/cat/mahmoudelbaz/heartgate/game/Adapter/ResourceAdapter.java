package dev.cat.mahmoudelbaz.heartgate.game.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import dev.cat.mahmoudelbaz.heartgate.R;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ApiInterface.RequestApi;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelFiterbyResource;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelGeneric;
import dev.cat.mahmoudelbaz.heartgate.game.Retrofit.ApiConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by ahmed on 10/7/2018.
 */

public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.MyViewHolder> {


    ResultModelFiterbyResource resultModelFiterbyResource;
    Context context;
    String userId, id;


    Handler handler;
    ProgressDialog progress;


    public ResourceAdapter(Context context, ResultModelFiterbyResource resultModelFiterbyResource, String userId, String id) {
        this.context = context;
        this.resultModelFiterbyResource = resultModelFiterbyResource;
        this.userId = userId;
        this.id = id;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.custom_resource_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final int pos = position;

        holder.txIn_userName.setText(resultModelFiterbyResource.getData().get(position).getName());

        holder.btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestResource(pos);
            }
        });

        if (resultModelFiterbyResource.getData().get(pos).getBtn().equals("reminder"))
            holder.btn_request.setBackgroundResource(R.drawable.reminder);

    }

    @Override
    public int getItemCount() {
        return resultModelFiterbyResource.getData().size();
    }

    public void requestResource(final int pos) {

        progress = new ProgressDialog(context);
        progress.setTitle(R.string.pleaseWait);
        progress.setMessage(context.getString(R.string.loading));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progress.dismiss();
                super.handleMessage(msg);
            }

        };
        progress.show();
        new Thread() {
            public void run() {
                //Retrofit
                ApiConnection connection = new ApiConnection();
                Retrofit retrofit = connection.connectWith();

                final HashMap<String, String> data = new HashMap<>();

                String supplier_id = resultModelFiterbyResource.getData().get(pos).getId();


                data.put("user_id", userId);
                data.put("supplier_id", supplier_id);
                data.put("resource_id", id);


                final RequestApi requestApi = retrofit.create(RequestApi.class);

                final Call<ResultModelGeneric> getInterestConnection = requestApi.create_request(data);

                getInterestConnection.enqueue(new Callback<ResultModelGeneric>() {
                    @Override
                    public void onResponse(Call<ResultModelGeneric> call, Response<ResultModelGeneric> response) {
                        try {

                            if (!response.isSuccessful()) {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Toast.makeText(context, jObjError.getString("data"), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(context, response.body().getData().trim(), Toast.LENGTH_LONG).show();
                            }

                            progress.dismiss();

                        } // try
                        catch (Exception e) {
                            Log.i("QP", "exception : " + e.toString());
                            progress.dismiss();
                        } // catch
                    } // onResponse

                    @Override
                    public void onFailure(Call<ResultModelGeneric> call, Throwable t) {
                        Log.i("QP", "error : " + t.toString());
                        progress.dismiss();
                    } // on Failure
                });
                // Retrofit
            }
        }.start();


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        Button btn_request;
        TextView txIn_userName;

        public MyViewHolder(View itemView) {
            super(itemView);

            btn_request = itemView.findViewById(R.id.btn_request);
            txIn_userName = itemView.findViewById(R.id.txIn_userName);

        }
    }
}
