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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import dev.cat.mahmoudelbaz.heartgate.R;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ApiInterface.RequestApi;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelGeneric;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelUserRequests;
import dev.cat.mahmoudelbaz.heartgate.game.Retrofit.ApiConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.MyViewHolder> {


    Context context;
    ResultModelUserRequests resultModelUserRequests;
    String userId;


    Handler handler;
    ProgressDialog progress;

    public RequestsAdapter(ResultModelUserRequests resultModelUserRequests, Context context, String userId) {
        this.context = context;
        this.resultModelUserRequests = resultModelUserRequests;
        this.userId = userId;
    }

    @NonNull
    @Override
    public RequestsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.custom_requests_layout, parent, false);
        return new RequestsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestsAdapter.MyViewHolder holder, final int position) {

        final int pos = position;
        holder.txt_name.setText(resultModelUserRequests.getData().get(position).getUser_name());

        holder.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptRequest(pos);
            }
        });


        holder.btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectRequest(pos);
            }
        });


    }

    private void rejectRequest(final int pos) {

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

                data.put("userid", userId);

                String requstId = resultModelUserRequests.getData().get(pos).getId();
                final RequestApi requestApi = retrofit.create(RequestApi.class);

                final Call<ResultModelGeneric> getInterestConnection = requestApi.Reject_Request(requstId, data);

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
                            notifyDataSetChanged();
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

    private void acceptRequest(final int pos) {
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

                data.put("user_id", userId);

                String requstId = resultModelUserRequests.getData().get(pos).getId();
                final RequestApi requestApi = retrofit.create(RequestApi.class);

                final Call<ResultModelGeneric> getInterestConnection = requestApi.Confirm_Request(requstId, data);

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
                                resultModelUserRequests.getData().remove(pos);
                                notifyDataSetChanged();
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

    @Override
    public int getItemCount() {
        return resultModelUserRequests.getData().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_name;
        ImageView btn_accept, btn_reject;


        public MyViewHolder(View itemView) {
            super(itemView);

            txt_name = itemView.findViewById(R.id.txt_name);
            btn_accept = itemView.findViewById(R.id.btn_accept);
            btn_reject = itemView.findViewById(R.id.btn_reject);

        }
    }
}
