package dev.cat.mahmoudelbaz.heartgate.chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import dev.cat.mahmoudelbaz.heartgate.Login;
import dev.cat.mahmoudelbaz.heartgate.R;
import dev.cat.mahmoudelbaz.heartgate.home.Home;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class chatActivity extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

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


 /*       if (imageUrl == null) {
            imageView.setImageResource(R.drawable.profile);
            return;
        }
        Picasso.with(this).load(imageUrl).placeholder(R.drawable.profile).error(R.drawable.profile).into(imageView);*/


        IO.Options mOptions = new IO.Options();
        mOptions.query = "id=" + receiveId;

        try {
            socket = IO.socket("http://160.153.246.213:5999", mOptions);
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }

        socket.on(Socket.EVENT_CONNECT, onConnected);
        socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        socket.on(Socket.EVENT_MESSAGE, onMessage);
        socket.on("chatListRes", onChatListRes);
        socket.on("typing", ontyping);
        //  socket.on("getMessageResponse", getMessageResponse);
        socket.on("getMessagesResponse", getMessagesResponse);
        socket.on("getMessages", getMessages);
        socket.on("addMessageResponse", addMessageResponse);


        socket.connect();

        socket.emit("chatList", userID);

        // message send action
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //retrieve the nickname and the message content and fire the event messagedetectionif(!messagetxt.getText().toString().isEmpty()){
                HashMap map = new HashMap();
                map.put("fromUserId", userID);
                map.put("toUserId", reciver.getId());
                map.put("toSocketId", reciver.getSocket_id());
                map.put("message", messagetxt.getText().toString());
                map.put("time", "01:48 PM");
                map.put("filePath", "");
                map.put("fileFormat", "");
                map.put("type", "text");
                map.put("date", "2019-05-7");

                JSONObject obj = new JSONObject(map);


                Message m = new Message(nickname, messagetxt.getText().toString() , new SimpleDateFormat("hh:mm a").toString() , myimageUrl);
                //add the message to the messageList
                MessageList.add(m);
                // add the new updated list to the dapter
                // notify the adapter to update the recycler view
                chatBoxAdapter.notifyDataSetChanged();
                //set the adapter for the recycler view

                messagetxt.setText("");
                socket.emit("addMessage", obj);
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
        userAdapter = new UsersAdapter(userslist);
        userAdapter.notifyDataSetChanged();
        userRecylerView.setAdapter(userAdapter);

        RecyclerView.LayoutManager mzLayoutManager = new LinearLayoutManager(getApplicationContext());
        userRecylerView.setLayoutManager(mzLayoutManager);
        userRecylerView.setItemAnimator(new DefaultItemAnimator());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Unable to connect to NodeJS server", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onConnected = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "connected to NodeJS server", Toast.LENGTH_LONG).show();
                    socket.emit("chatList", receiveId);
                }
            });
        }
    };


    private Emitter.Listener onMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "onMessage from NodeJS server", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onChatListRes = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "onChatListRes from NodeJS server", Toast.LENGTH_LONG).show();
                    Log.d("socket", "run: " + args);
                    JSONObject data = (JSONObject) args[0];

                    //          reciver.setSocket_id(data.getString("socket_id"));

                    /*    ArrayList<String> listdata = new ArrayList<String>();
                        JSONArray jArray = data.getJSONArray("chatList");

                        if (jArray != null) {
                            for (int i = 0; i < jArray.length(); i++) {
                                //extract data from fired event
                                String id = jArray.getJSONObject(i).getString("id");
                                String name = jArray.getJSONObject(i).getString("name");
                                String socket_id = jArray.getJSONObject(i).getString("socket_id");
                                String online = jArray.getJSONObject(i).getString("online");
                                String updated_at = jArray.getJSONObject(i).getString("updated_at");

                                User m = new User(id, name, socket_id, online, updated_at);

                                userslist.add(m);
                            }
                        }*/
                    // add the new updated list to the dapter
                    // notify the adapter to update the recycler view
                    userAdapter.notifyDataSetChanged();
                    //set the adapter for the recycler view

                }
            });
        }
    };

    private Emitter.Listener ontyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "user typing" + args.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
    };


    private Emitter.Listener getMessagesResponse = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), args.toString(), Toast.LENGTH_LONG).show();

                }
            });
        }
    };

    private Emitter.Listener getMessages = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), args.toString(), Toast.LENGTH_LONG).show();

                }
            });
        }
    };


    private Emitter.Listener addMessageResponse = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), args.toString(), Toast.LENGTH_LONG).show();
                    JSONObject data = (JSONObject) args[0];
                    String text = null;
                    //extract data from fired event
                    try {
                        text = data.getString("message");
                        MessageList.add(
                                new Message(
                                        reciver.getName(),
                                        text,
                                        data.getString("time"),
                                        imageUrl
                                ));

                  //      reciver.setSocket_id(data.getString("toSocketId"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    chatBoxAdapter.notifyDataSetChanged();
                    //set the adapter for the recycler view}
                }
            });
        }
    };
}
