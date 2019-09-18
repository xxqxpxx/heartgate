package dev.cat.mahmoudelbaz.heartgate.videos;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import dev.cat.mahmoudelbaz.heartgate.R;
import dev.cat.mahmoudelbaz.heartgate.webServices.Webservice;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideosListActivity extends AppCompatActivity {

    @BindView(R.id.Recycle_view_cardoivascular)
    RecyclerView RecycleViewCardoivascular;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    SharedPreferences shared;
    String userID;
    List<VideoResponseModel> cardioUpdatesResponseModels = new ArrayList<>();
    List<VideoResponseModel> videoResponseModel;
    EditText mysearchView;
    videoAdapter myVideoAdapter;

    TextView myempty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos_list);

        ButterKnife.bind(this);
        mysearchView = (EditText) findViewById(R.id.mysearch_view);

        shared = getSharedPreferences("id", Context.MODE_PRIVATE);

        userID = shared.getString("id", "0");

        myempty = findViewById(R.id.mytxtEmpty);

        mysearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                myVideoAdapter.getFilter().filter(s.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        getData();

    }

    private void getData() {

        Webservice.getInstance().getApi().getAllVideos().enqueue(new Callback<List<VideoResponseModel> >() {
            @Override
            public void onResponse(Call< List<VideoResponseModel>> call, Response< List<VideoResponseModel> > response) {
                if (!response.isSuccessful()) {
                    assert response.errorBody() != null;
                    Toast.makeText(VideosListActivity.this, response.errorBody().toString() ,  Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    myempty.setVisibility(View.VISIBLE);

                } else {
                    videoResponseModel = response.body();
                    myempty.setVisibility(View.GONE);

                    RecycleViewCardoivascular.setHasFixedSize(true);
                    RecycleViewCardoivascular.setLayoutManager(new LinearLayoutManager(VideosListActivity.this));
                    myVideoAdapter = new videoAdapter(VideosListActivity.this, videoResponseModel);
                    RecycleViewCardoivascular.setAdapter(myVideoAdapter);

                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call< List<VideoResponseModel> > call, Throwable t) {
                Toast.makeText(VideosListActivity.this, "failure , check your connection", Toast.LENGTH_LONG).show();
                Log.e("login", "onFailure: ", t);
                progressBar.setVisibility(View.GONE);
                myempty.setVisibility(View.VISIBLE);

            }
        });


    }

}
