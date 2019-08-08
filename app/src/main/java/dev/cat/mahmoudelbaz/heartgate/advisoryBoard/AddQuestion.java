package dev.cat.mahmoudelbaz.heartgate.advisoryBoard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dev.cat.mahmoudelbaz.heartgate.R;
import dev.cat.mahmoudelbaz.heartgate.webServices.Api;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddQuestion extends AppCompatActivity {


    Button ask;
    ImageView bck;
    EditText content;
    String url, userId, userName,  contenttxt;
    SharedPreferences shared;

    private ProgressDialog dialog;
    Retrofit retrofit;
    Api myInterface;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please, Wait");
        dialog.setCancelable(false);


        url = getResources().getString(R.string.services_url);

        retrofit = new Retrofit.Builder()
                .baseUrl(url).
                        addConverterFactory(GsonConverterFactory.create()).build();
        myInterface = retrofit.create(Api.class);

        shared = getSharedPreferences("id", Context.MODE_PRIVATE);
        userId = shared.getString("id", "0");
        userName = shared.getString("name", "0");
        bck = findViewById(R.id.bck);
        content = findViewById(R.id.etContent);
        ask = findViewById(R.id.btnAsk);

        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contenttxt = content.getText().toString();

                if (contenttxt.length() == 0) {
                    Toast.makeText(AddQuestion.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    addQuestion();
                }


            }
        });

    }


    private void addQuestion() {
        Map<String, String> map = new HashMap<>();

        map.put("q_title", contenttxt);
        map.put("username", userName);
        dialog.show();
        myInterface.addQuestion(userId, map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                String message = null;
                int state = 0;

                try {
                    JSONObject res = new JSONObject(response.body().string());
                    message = res.getString("Message");
                    state = res.getInt("state");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (state == 0) {
                    Toast.makeText(AddQuestion.this, message, Toast.LENGTH_SHORT).show();

                }else if (state == 1) {
                    Toast.makeText(AddQuestion.this, message, Toast.LENGTH_SHORT).show();
                    onBackPressed();

                }
                dialog.dismiss();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AddQuestion.this, "Network Error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}
