package dev.cat.mahmoudelbaz.heartgate.poll;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.cat.mahmoudelbaz.heartgate.R;
import dev.cat.mahmoudelbaz.heartgate.heartPress.heartPressAdapter;
import dev.cat.mahmoudelbaz.heartgate.webServices.Webservice;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Survey extends AppCompatActivity {

    @BindView(R.id.Recycle_view_cardoivascular)
    ListView RecycleViewCardoivascular;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    EditText nearBysearchView;
    SurveyAdapter nearByConnectionsAdapter;
    TextView myempty;

    SharedPreferences shared;
    String userID;
    List<SurveryResponseModel> cardioUpdatesResponseModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        ButterKnife.bind(this);

        shared = getSharedPreferences("id", Context.MODE_PRIVATE);

        userID = shared.getString("id", "0");

        nearBysearchView = (EditText) findViewById(R.id.nearBysearch_view);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        myempty = findViewById(R.id.mytxtEmpty);

        getData();

        //      nearByConnectionsAdapter = new heartPressAdapter(this, cardioUpdatesResponseModels);

/*
        nearBysearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                nearByConnectionsAdapter.getFilter().filter(s.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
*/
    }


        private void getData() {

            Webservice.getInstance().getApi().getSurvey(userID).enqueue(new Callback< List<SurveryResponseModel> >() {
                @Override
                public void onResponse(Call< List<SurveryResponseModel> > call, Response< List<SurveryResponseModel> > response) {
                    if (!response.isSuccessful()) {
                    //    assert response.errorBody() != null;
                        Toast.makeText(Survey.this, response.errorBody().toString() ,  Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        myempty.setVisibility(View.VISIBLE);

                    } else {
                        cardioUpdatesResponseModels = response.body();
                        nearByConnectionsAdapter = new SurveyAdapter(Survey.this, cardioUpdatesResponseModels);
                        myempty.setVisibility(View.GONE);

                /*    RecycleViewCardoivascular.setHasFixedSize(true);
                    RecycleViewCardoivascular.setLayoutManager(new LinearLayoutManager(CardioUpdates.this));*/
                        RecycleViewCardoivascular.setAdapter(nearByConnectionsAdapter);

                        progressBar.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onFailure(Call< List<SurveryResponseModel> > call, Throwable t) {
                    Toast.makeText(Survey.this, "failure , check your connection", Toast.LENGTH_LONG).show();
                    Log.e("CardioUpdates", "onFailure: ", t);
                    progressBar.setVisibility(View.GONE);
                    myempty.setVisibility(View.VISIBLE);

                }
            });


        }


}
