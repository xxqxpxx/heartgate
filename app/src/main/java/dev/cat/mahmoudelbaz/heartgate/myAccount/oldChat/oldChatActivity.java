package dev.cat.mahmoudelbaz.heartgate.myAccount.oldChat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.cat.mahmoudelbaz.heartgate.R;
import dev.cat.mahmoudelbaz.heartgate.chat.ChatBoxAdapter;
import dev.cat.mahmoudelbaz.heartgate.chat.Message;
import dev.cat.mahmoudelbaz.heartgate.chat.User;
import dev.cat.mahmoudelbaz.heartgate.chat.UsersAdapter;
import dev.cat.mahmoudelbaz.heartgate.webServices.Webservice;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;

public class oldChatActivity extends AppCompatActivity {

    public static Socket socket;
    private String nickname;
    public RecyclerView myRecylerView;
    public ArrayList<Message> MessageList;
    public ChatBoxAdapter chatBoxAdapter;
    public EditText messagetxt;
    public Button send;
    String url;
    TextView name;
    public RecyclerView userRecylerView;
    public List<User> userslist;
    public UsersAdapter userAdapter;
    public static User reciver;
    SharedPreferences shared;
    private String userID , receiveId , imageUrl , myimageUrl ;
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oldchat);

        setupView();
    }

    private void setupView() {


        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog



        messagetxt = findViewById(R.id.message);
        send = findViewById(R.id.send);
        name = findViewById(R.id.name);


        shared = getSharedPreferences("id", Context.MODE_PRIVATE);

        userID = shared.getString("id", "0");


        url = "http://heartgate.co/api_heartgate/users/current/" + userID;

        StringRequest loginRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray usersarray = new JSONArray(response);
                    JSONObject res = usersarray.getJSONObject(0);

                    nickname = res.getString("fullname");
                    name.setText(nickname);

                    final String imgstring = res.getString("image_profile");
                    myimageUrl= "http://heartgate.co/api_heartgate/layout/images/" + imgstring;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

        Volley.newRequestQueue(this).add(loginRequest);

        // get the nickame of the user
        //connect you socket client to the servertry {

        Bundle bundle = getIntent().getExtras();
        receiveId = String.valueOf(bundle.getInt("receiveId"));

        imageUrl = bundle.getString("imageUrl");

        //    reciver.setId(receiveId);
        //    reciver.setName();
        reciver = new User( receiveId,
                bundle.getString("name"),
                ""
                ,""
                ,"");


        getAllMessages();

        // message send action
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addNewMessage(userID , receiveId , messagetxt.toString().trim() );

            }
        });


        //setting up recyler
        MessageList = new ArrayList<>();
        myRecylerView = (RecyclerView) findViewById(R.id.messagelist);
        chatBoxAdapter = new ChatBoxAdapter(MessageList , this);
        myRecylerView.setAdapter(chatBoxAdapter);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        myRecylerView.setLayoutManager(mLayoutManager);
        myRecylerView.setItemAnimator(new DefaultItemAnimator());

        userslist = new ArrayList<>();
        userRecylerView = (RecyclerView) findViewById(R.id.userslist);
        // add the new updated list to the dapter

        RecyclerView.LayoutManager mzLayoutManager = new LinearLayoutManager(getApplicationContext());
        userRecylerView.setLayoutManager(mzLayoutManager);
        userRecylerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void addNewMessage(String userID, String receiveId, String trim) {


        progress.show();

        final HashMap<String, String> data = new HashMap<>();

        data.put("fk_userid_send", userID);
        data.put("fk_userid_received", receiveId);
        data.put("message", trim);
        data.put("create_user_id", nickname);


        Webservice.getInstance().getApi().sendNewMessage(data).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {

                if (!response.isSuccessful()) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(oldChatActivity.this,"Error", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(oldChatActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    progress.dismiss();
                } else {

                    Message m = new Message(nickname, messagetxt.getText().toString() , new SimpleDateFormat("hh:mm a").toString() , myimageUrl);
                    //add the message to the messageList
                    MessageList.add(m);
                    // add the new updated list to the dapter
                    // notify the adapter to update the recycler view
                    chatBoxAdapter.notifyDataSetChanged();
                    //set the adapter for the recycler view

                    messagetxt.setText("");

                    progress.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(oldChatActivity.this, "failure , check your connection", Toast.LENGTH_LONG).show();
                Log.e("login", "onFailure: ", t);
                progress.dismiss();
            }
        });


    }

    private void getAllMessages() {


            progress.show();

            Webservice.getInstance().getApi().getAllMessagesOld(userID , receiveId).enqueue(new Callback<ArrayList<AllMessagesResponse>>() {
                @Override
                public void onResponse(Call<ArrayList<AllMessagesResponse>> call, retrofit2.Response<ArrayList<AllMessagesResponse>> response) {

                    if (!response.isSuccessful()) {
                  //      Toast.makeText(oldChatActivity.this, "Not Sent", Toast.LENGTH_LONG).show();
                        progress.dismiss();
                    } else {
                        ArrayList<AllMessagesResponse> allMessagesResponse = response.body();
                        if (allMessagesResponse != null) {
                            for (int i = 0 ; i < allMessagesResponse.size() ; ++i)
                            {
                                MessageList.add(
                                        new Message(reciver.getName(), allMessagesResponse.get(i).getUserMessage(), "", imageUrl));
                            }
                        }
                        chatBoxAdapter.notifyDataSetChanged();
                        //set the adapter for the recycler view}
                        progress.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<AllMessagesResponse>> call, Throwable t) {
                    Toast.makeText(oldChatActivity.this, "failure , check your connection", Toast.LENGTH_LONG).show();
                    Log.e("login", "onFailure: ", t);
                    progress.dismiss();
                }
            });

        }

}
