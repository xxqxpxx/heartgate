package dev.cat.mahmoudelbaz.heartgate.myAccount;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import dev.cat.mahmoudelbaz.heartgate.R;
import dev.cat.mahmoudelbaz.heartgate.webServices.Webservice;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Favourites extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);


        getAllFavs();
    }

    private void getAllFavs() {

        Webservice.getInstance().getApi().getAllFavorites().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                String message = null;
                int state = 0;
                try {
                    //   JSONArray cast = new JSONArray(response.body());

                    JSONObject res = new JSONObject(response.body().string());
                    message = res.getString("Message");
                    state = res.getInt("state");
                    Log.d("ForgetPassword Response", message + state);


                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (state == 0) {
                    Toast.makeText(Favourites.this, message, Toast.LENGTH_SHORT).show();
                } else if (state == 1) {
                    Toast.makeText(Favourites.this, message, Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Favourites.this, "failure , check your connection", Toast.LENGTH_LONG).show();

            }
        });


    }
}
