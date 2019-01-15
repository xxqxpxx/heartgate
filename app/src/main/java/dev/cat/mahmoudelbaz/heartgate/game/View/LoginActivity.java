package dev.cat.mahmoudelbaz.heartgate.game.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.cat.mahmoudelbaz.heartgate.R;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ApiInterface.UserApi;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelLogin;
import dev.cat.mahmoudelbaz.heartgate.game.Retrofit.ApiConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {


    public static ResultModelLogin resultModelLogin;
    String userName = "", password = "";
    @BindView(R.id.txIn_userName)
    TextInputEditText txIn_userName;

    @BindView(R.id.txIn_password)
    TextInputEditText txIn_password;

    @BindView(R.id.chckRemember)
    CheckBox chckRemember;


    Handler handler;
    ProgressDialog progress;


    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_login);


        // init ButterKnife
        ButterKnife.bind(this);


        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();


        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            txIn_userName.setText(loginPreferences.getString("username", ""));
            txIn_password.setText(loginPreferences.getString("password", ""));
            chckRemember.setChecked(true);
        }


    }


    @OnClick(R.id.btn_pdf)
    public void openPdf()

    {
        Intent mainIntent = new Intent(LoginActivity.this, pdfviewer.class);
        LoginActivity.this.startActivity(mainIntent);
    }


    @OnClick(R.id.btn_login)
    public void loginButton() {

// get data from user
        userName = txIn_userName.getText().toString();
        password = txIn_password.getText().toString();

        // check empty field and password != confirmation
        if (userName.equals("") || password.equals("")) {
            Toast.makeText(this, getString(R.string.emptyField), Toast.LENGTH_LONG).show();
        } // if empty filed found
        else {

            loginApi();

        } // all field contain data


    } // function of login Button


    @OnClick(R.id.btn_signup)
    public void signUpButton() {
        /* Create an Intent that will start the RegisterScreen. */
        Intent mainIntent = new Intent(LoginActivity.this, RegisterScreen.class);
        LoginActivity.this.startActivity(mainIntent);
    } // function of signUp Button


    private void loginApi() {


        progress = new ProgressDialog(this);
        progress.setTitle(R.string.pleaseWait);
        progress.setMessage(getApplicationContext().getString(R.string.loading));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progress.dismiss();
                super.handleMessage(msg);
            }

        };
        progress.show();
        new Thread() {
            public void run() {
                //Retrofit
                ApiConnection connection = new ApiConnection();
                Retrofit retrofit = connection.connectWith();

                final UserApi userApi = retrofit.create(UserApi.class);


                final HashMap<String, String> data = new HashMap<>();

                data.put("email", userName);
                data.put("password", password);

                final Call<ResultModelLogin> getInterestConnection = userApi.login(data);

                getInterestConnection.enqueue(new Callback<ResultModelLogin>() {
                    @Override
                    public void onResponse(Call<ResultModelLogin> call, Response<ResultModelLogin> response) {
                        try {

                            if (!response.isSuccessful()) {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Toast.makeText(LoginActivity.this, jObjError.getString("data"), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                resultModelLogin = response.body();

                                if (chckRemember.isChecked()) {
                                    loginPrefsEditor.putBoolean("saveLogin", true);
                                    loginPrefsEditor.putString("username", userName);
                                    loginPrefsEditor.putString("password", password);
                                    loginPrefsEditor.commit();
                                } else {
                                    loginPrefsEditor.clear();
                                    loginPrefsEditor.commit();
                                }

                                goToHome();
                            }

                            progress.dismiss();

                        } // try
                        catch (Exception e) {
                            Log.i("QP", "exception : " + e.toString());
                            progress.dismiss();
                        } // catch
                    } // onResponse

                    @Override
                    public void onFailure(Call<ResultModelLogin> call, Throwable t) {
                        Log.i("QP", "error : " + t.toString());
                        Toast.makeText(LoginActivity.this, "Wrong Username or Password", Toast.LENGTH_LONG).show();

                        progress.dismiss();
                    } // on Failure
                });
                // Retrofit
            }
        }.start();
    } // function of Login

    private void goToHome() {
        /* Create an Intent that will start the RegisterScreen. */
        Intent mainIntent = new Intent(LoginActivity.this, HomeActivty.class);
        LoginActivity.this.startActivity(mainIntent);
        LoginActivity.this.finish();

    }

}
