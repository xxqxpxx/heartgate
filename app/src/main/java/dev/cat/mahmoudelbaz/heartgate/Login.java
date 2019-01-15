package dev.cat.mahmoudelbaz.heartgate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dev.cat.mahmoudelbaz.heartgate.home.Home;
import dev.cat.mahmoudelbaz.heartgate.signUp.Disclaimer;

public class Login extends AppCompatActivity {

    ProgressBar progress;
    Button login, signup;
    EditText email, password;
    TextView forgotpassword;
    String url;
    SharedPreferences shared;
    String idcheck;
    ImageView gateTop, gateBottom;
    Animation animSlidUp, animSlidDown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        shared = getSharedPreferences("id", Context.MODE_PRIVATE);


        if (shared.contains("id")) {
            idcheck = shared.getString("id", "0");
        } else {
            idcheck = "0";
        }


        if (!idcheck.equals("0")) {
            Intent i = new Intent(Login.this, Home.class);
            startActivity(i);
            finish();
        }


        gateTop = (ImageView) findViewById(R.id.topGate);
        gateBottom = (ImageView) findViewById(R.id.bottmGate);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        login = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signUp);

        forgotpassword = (TextView) findViewById(R.id.forgotpass);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        // load the animation
        animSlidUp = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.abc_slide_out_top);

        animSlidDown = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.abc_slide_out_bottom);

        progress.setVisibility(View.INVISIBLE);


        gateBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gateTop.startAnimation(animSlidUp);
                gateBottom.startAnimation(animSlidDown);

                gateTop.setVisibility(View.GONE);
                gateBottom.setVisibility(View.GONE);

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailtxt = email.getText().toString();
                String passwordtxt = password.getText().toString();
                if (emailtxt.length() == 0 || passwordtxt.length() == 0) {
                    Toast.makeText(Login.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    progress.setVisibility(View.VISIBLE);
                    url = "http://hg.api.digitalcatsite.com/login/" + emailtxt + "/" + passwordtxt;

                    StringRequest loginRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {


//                                JSONArray usersarray = new JSONArray(response);

                                JSONObject res = new JSONObject(response);
                                final int responseValue = res.getInt("state");


                                if (responseValue == 0) {

                                    Toast.makeText(Login.this, "Wrong UserName or Password", Toast.LENGTH_SHORT).show();

                                } else if (responseValue == 1) {


                                    Toast.makeText(Login.this, "Welcome", Toast.LENGTH_SHORT).show();

                                    JSONArray userdata = res.getJSONArray("userdata");
                                    JSONObject currentUserData = userdata.getJSONObject(0);
                                    final int userId = currentUserData.getInt("id");
                                    final String userIdString = Integer.toString(userId);


                                    Toast.makeText(Login.this, "Welcome", Toast.LENGTH_SHORT).show();

                                    SharedPreferences.Editor myEdit = shared.edit();
                                    myEdit.putString("id", userIdString);
                                    myEdit.putString("Name", currentUserData.getString("username"));
                                    myEdit.commit();

                                    Intent i = new Intent(Login.this, Home.class);
                                    startActivity(i);
                                    finish();

                                }

                                progress.setVisibility(View.INVISIBLE);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(Login.this, error.toString(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(Login.this, "Network Error", Toast.LENGTH_SHORT).show();
                            progress.setVisibility(View.INVISIBLE);

                        }
                    });

                    Volley.newRequestQueue(Login.this).add(loginRequest);
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Disclaimer.class);
                startActivity(i);
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, ForgetPassword.class);
                startActivity(i);
            }
        });
    }
}
