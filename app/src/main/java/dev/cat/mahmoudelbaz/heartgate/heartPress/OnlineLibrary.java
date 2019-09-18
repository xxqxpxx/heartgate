package dev.cat.mahmoudelbaz.heartgate.heartPress;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.cat.mahmoudelbaz.heartgate.R;
import dev.cat.mahmoudelbaz.heartgate.webServices.Webservice;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnlineLibrary extends AppCompatActivity {

    @BindView(R.id.Recycle_view_cardoivascular)
    RecyclerView RecycleViewCardoivascular;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    SharedPreferences shared;
    String userID;
    TextView myempty;
    List<onlineLibraryResponseModel> myModels = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_library);

        ButterKnife.bind(this);
        shared = getSharedPreferences("id", Context.MODE_PRIVATE);
        myempty = findViewById(R.id.mytxtEmpty);

        userID = shared.getString("id", "0");


        getData();

    }

    private void getData() {

        Webservice.getInstance().getApi().getCurrentNewsLibrary(userID).enqueue(new Callback< List<onlineLibraryResponseModel> >() {
            @Override
            public void onResponse(Call< List<onlineLibraryResponseModel> > call, Response< List<onlineLibraryResponseModel> > response) {
                if (!response.isSuccessful()) {
                    assert response.errorBody() != null;
                    Toast.makeText(OnlineLibrary.this, response.errorBody().toString() ,  Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    myempty.setVisibility(View.VISIBLE);

                } else {
                    myModels = response.body();
                    myempty.setVisibility(View.GONE);

                    RecycleViewCardoivascular.setHasFixedSize(true);
                    RecycleViewCardoivascular.setLayoutManager(new LinearLayoutManager(OnlineLibrary.this));
                    RecycleViewCardoivascular.setAdapter(new OnlineLibraryAdapter(OnlineLibrary.this,myModels));

                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call< List<onlineLibraryResponseModel> > call, Throwable t) {
                Toast.makeText(OnlineLibrary.this, "failure , check your connection", Toast.LENGTH_LONG).show();
                Log.e("login", "onFailure: ", t);
                progressBar.setVisibility(View.GONE);
                myempty.setVisibility(View.VISIBLE);

            }
        });


    }
}
