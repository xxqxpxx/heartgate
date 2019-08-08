package dev.cat.mahmoudelbaz.heartgate.advisoryBoard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import dev.cat.mahmoudelbaz.heartgate.R;
import dev.cat.mahmoudelbaz.heartgate.webServices.Api;
import dev.cat.mahmoudelbaz.heartgate.webServices.Webservice;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Questions extends AppCompatActivity {

   public static ArrayList<Questions_item> questionsList = new ArrayList<>();

    Questions_adapter myAdapter;
    RecyclerView mylist;
    TextView myempty, addquestion;

    ImageView back;

    Handler handler;

    SwipeRefreshLayout mSwipeRefreshLayout;

    String url;
    private ProgressDialog dialog;
    Retrofit retrofit;
    Api myInterface;

    SharedPreferences shared;

   public static String userId;

    SwipeController swipeController = null;


    @Override
    protected void onResume() {
        super.onResume();
        loadQuestions();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);


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

        mSwipeRefreshLayout = findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        back = findViewById(R.id.bck);
        mylist = findViewById(R.id.mylistView);
        myempty = findViewById(R.id.mytxtEmpty);
        addquestion = findViewById(R.id.txtAddQuestion);


        loadQuestions();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addquestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Questions.this, AddQuestion.class);
                startActivity(intent);
            }
        });

/*        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int questionId = questionsList.get(i).getId();
                Intent intent = new Intent(Questions.this, QuestionDetails.class);
                intent.putExtra("questionId", questionId);
                intent.putExtra("questionTxt",questionsList.get(i).getTitle());
                startActivity(intent);
            }
        });*/


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                loadQuestions();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void loadQuestions() {
        dialog.show();
        myInterface.getQuestions(userId).enqueue(new Callback<ArrayList<Questions_item>>() {
            @Override
            public void onResponse(Call<ArrayList<Questions_item>> call, Response<ArrayList<Questions_item>> response) {

                try {
                    //    JSONArray newsFeedArray = new JSONArray(response.body().toString());

                    if (response.body() == null) {
                        myempty.setVisibility(View.VISIBLE);
                        myempty.setText("No Questions");
                    } else {
                        questionsList.clear();
                        myempty.setVisibility(View.GONE);
                       /* for (int i = 0; i < newsFeedArray.length(); i++) {
                            JSONObject currentobject = newsFeedArray.getJSONObject(i);
                            final int id = currentobject.getInt("id");
                            final String title = currentobject.getString("q_title");
                            final String date = currentobject.getString("creation_date");


                            String userName = "admin";
                            String profileImgUrl = "";

                            JSONArray profileData = currentobject.getJSONArray("user_profile");
                            if (profileData.length() != 0) {
                                JSONObject currentprofile = profileData.getJSONObject(0);
                                profileImgUrl = getResources().getString(R.string.services_url_imgs) + currentprofile.getString("image_profile");
                                userName = currentprofile.getString("username");
                            }


*/
/*
                            questionsList.add(new Questions_item(id, title, date, userName, profileImgUrl));
*/

                        questionsList = response.body();
                        setupSwipe();


                        //        myAdapter.notifyDataSetChanged();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                dialog.dismiss();

            }

            @Override
            public void onFailure(Call<ArrayList<Questions_item>> call, Throwable t) {
                Log.d("Error Throw", t.toString());
                Log.d("commit Test Throw", t.toString());
                Log.d("Call", t.toString());
                Toast.makeText(Questions.this, "Network Error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                myempty.setVisibility(View.VISIBLE);
                myempty.setText("Network Error ,Swipe Down to refresh");
            }
        });
    }

    private void setupSwipe() {
        myAdapter = new Questions_adapter(this, questionsList);
        mylist.setAdapter(myAdapter);
        mylist.setLayoutManager(new LinearLayoutManager(this));

        swipeController = new SwipeController(questionsList, new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {

                removepost(userId, questionsList.get(position).getId());

                questionsList.remove(position);
                myAdapter.notifyDataSetChanged();
                //    myAdapter.notifyItemRangeChanged(position, adapter.getItemCount());
            }

            @Override
            public void onLeftClicked(int position) {
                myAdapter.notifyDataSetChanged();
                //    myAdapter.notifyItemRangeChanged(position, adapter.getItemCount());
            }


        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(mylist);


        mylist.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });


        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));

        mylist.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));

        mylist.setHasFixedSize(true);
    }


    private void removepost(final String id, final String postId) {

        dialog.show();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };

        new Thread() {
            public void run() {

                Response<Object> response = null;
                try {
                    response = Webservice.getInstance().getApi().delete_post(postId, id).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (!response.isSuccessful()) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                    } catch (Exception e) {
                    }
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                }
            }


        }.start();

    }

}