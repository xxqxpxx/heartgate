package dev.cat.mahmoudelbaz.heartgate.game.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.cat.mahmoudelbaz.heartgate.R;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ApiInterface.UserApi;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelSignUp;
import dev.cat.mahmoudelbaz.heartgate.game.Retrofit.ApiConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterScreen extends AppCompatActivity {


    String name = "", userName = "", email = "", password = "", confirmPassword = "", state = "", data = "";

    @BindView(R.id.txIn_name)
    TextInputEditText txIn_name;

    @BindView(R.id.txIn_userName)
    TextInputEditText txIn_userName;

    @BindView(R.id.txIn_email)
    TextInputEditText txIn_email;

    @BindView(R.id.txIn_password)
    TextInputEditText txIn_password;

    @BindView(R.id.txIn_confirmPassword)
    TextInputEditText txIn_confirmPassword;


    Handler handler;
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        // init ButterKnife
        ButterKnife.bind(this);


    } // function of onCreate


    @OnClick(R.id.bt_signUp)
    public void signUpButton() {
        // get data from user
        name = txIn_name.getText().toString();
        userName = txIn_userName.getText().toString();
        email = txIn_email.getText().toString();
        password = txIn_password.getText().toString();
        confirmPassword = txIn_confirmPassword.getText().toString();

        // check empty field and password != confirmation
        if (name.equals("") || userName.equals("") || email.equals("") ||
                password.equals("") || confirmPassword.equals("")) {
            Toast.makeText(RegisterScreen.this, getString(R.string.emptyField), Toast.LENGTH_LONG).show();
        } // if empty filed found
        else {
            if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterScreen.this, R.string.worngPassword, Toast.LENGTH_LONG).show();
            } else {
                signUpWithApi();
            }
        } // all field contain data


    } // function of signUp Button

    private void signUpWithApi() {
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

                final UserApi signUpApi = retrofit.create(UserApi.class);

                final Call<ResultModelSignUp> getInterestConnection = signUpApi.
                        signUp(name, userName, email, password, confirmPassword);

                getInterestConnection.enqueue(new Callback<ResultModelSignUp>() {
                    @Override
                    public void onResponse(Call<ResultModelSignUp> call, Response<ResultModelSignUp> response) {
                        try {

                            if (!response.isSuccessful()) {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Toast.makeText(RegisterScreen.this, jObjError.getString("data"), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Toast.makeText(RegisterScreen.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }

                            } else {

                                Toast.makeText(RegisterScreen.this, "Registration Successful, moving you to Login now.", Toast.LENGTH_LONG).show();
                                goToLogin();
                                progress.dismiss();

                            }
                            progress.dismiss();

                        } // try
                        catch (Exception e) {
                            Toast.makeText(RegisterScreen.this, response.errorBody().toString(), Toast.LENGTH_LONG).show();
                            Log.i("QP", "exception : " + e.toString());
                            progress.dismiss();
                        } // catch
                    } // onResponse

                    @Override
                    public void onFailure(Call<ResultModelSignUp> call, Throwable t) {
                        Toast.makeText(RegisterScreen.this, data.trim(), Toast.LENGTH_LONG).show();

                        Log.i("QP", "error : " + t.toString());
                        progress.dismiss();
                    } // on Failure
                });
// Retrofit
            }
        }.start();
    } // function of SignUpWithApi

    private void goToLogin() {

        Intent mainIntent = new Intent(RegisterScreen.this, LoginActivity.class);
        RegisterScreen.this.startActivity(mainIntent);
        RegisterScreen.this.finish();


    }

    @Override
    public void onBackPressed() {
        RegisterScreen.super.onBackPressed();
    }


} // class of RegisterScreen
