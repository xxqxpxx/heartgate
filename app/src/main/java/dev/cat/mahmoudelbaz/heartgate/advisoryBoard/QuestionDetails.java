package dev.cat.mahmoudelbaz.heartgate.advisoryBoard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
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

public class QuestionDetails extends AppCompatActivity {

    ArrayList<Answers_item> answersList = new ArrayList<Answers_item>();

    Answers_adapter myAdapter;
    ListView mylist;
    TextView myempty, addnews;

    ImageView back;

    ImageView addAnswer;
    EditText etAnswer;


    SwipeRefreshLayout mSwipeRefreshLayout;


    String url;
    private ProgressDialog dialog;
    Retrofit retrofit;
    Api myInterface;

    SharedPreferences shared;

    String userId,answertxt,userName;
    int questionId;
    String questionStr;
    TextView txtQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_details);

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

        Intent intent = getIntent();
        questionId = intent.getIntExtra("questionId", 0);
        questionStr = intent.getStringExtra("questionTxt");

        txtQuestion = findViewById(R.id.txtQuestion);

        txtQuestion.setText(questionStr);

        mSwipeRefreshLayout = findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        back = findViewById(R.id.bck);
        mylist = findViewById(R.id.mylistView);
        myempty = findViewById(R.id.mytxtEmpty);
 //       addnews = findViewById(R.id.txtAddNews);
        etAnswer = findViewById(R.id.etAnswer);
        addAnswer = findViewById(R.id.imgAddAnswer);
        mylist.setEmptyView(myempty);

        myAdapter = new Answers_adapter(this, answersList);
        mylist.setAdapter(myAdapter);

        loadAnswers();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                loadAnswers();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        addAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answertxt = etAnswer.getText().toString();
                if (answertxt.length() == 0) {
                    Toast.makeText(QuestionDetails.this, "Answer can not be empty", Toast.LENGTH_SHORT).show();

                } else {
                    sendAnswer();
                    loadAnswers();
                }
            }
        });
    }


    public void loadAnswers() {
        dialog.show();
        myInterface.getAnswers(questionId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {


                    JSONArray newsFeedArray = new JSONArray(response.body().string());

                    if (newsFeedArray.length() == 0) {
                        myempty.setText("No Answers");
                     } else {
                        answersList.clear();

                        for (int i = 0; i < newsFeedArray.length(); i++) {
                            JSONObject currentobject = newsFeedArray.getJSONObject(i);
                            final int id = currentobject.getInt("id");
                            final String content = currentobject.getString("a_title");
                            final String date = currentobject.getString("creation_date");

                            String userName = "admin";
                            String profileImgUrl = "";

                            JSONArray profileData = currentobject.getJSONArray("user_profile");
                            if (profileData.length() != 0) {
                                JSONObject currentprofile = profileData.getJSONObject(0);
                                profileImgUrl = getResources().getString(R.string.services_url_imgs) + currentprofile.getString("image_profile");
                        //        Picasso.with(QuestionDetails.this).load(profileImgUrl).placeholder(R.drawable.profile).error(R.drawable.profile).into(imgprofile);
                                userName = currentprofile.getString("username");
                            }


                            answersList.add(new Answers_item(id, content, date, userName, profileImgUrl));
                            myAdapter.notifyDataSetChanged();


                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                dialog.dismiss();


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Error Throw", t.toString());
                Log.d("commit Test Throw", t.toString());
                Log.d("Call", t.toString());
                Toast.makeText(QuestionDetails.this, "Network Error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                myempty.setText("Network Error ,Swipe Down to refresh");
            }
        });
    }

    public void sendAnswer() {
        Map<String, String> map = new HashMap<>();

        map.put("a_title", answertxt);
        map.put("username", userName);

        Log.d("Answer", map.toString());
        dialog.show();

        myInterface.addAnswer(questionId,userId, map).enqueue(new Callback<ResponseBody>() {
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
                    Toast.makeText(QuestionDetails.this, message, Toast.LENGTH_SHORT).show();
//                                Log.d("sent data", jsobj.toString());
                } else if (state == 1) {

                    Toast.makeText(QuestionDetails.this, message, Toast.LENGTH_SHORT).show();
                    etAnswer.setText("");
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    loadAnswers();

                }

                dialog.dismiss();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(QuestionDetails.this, "Network Error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }
}
