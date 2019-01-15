package dev.cat.mahmoudelbaz.heartgate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.cat.mahmoudelbaz.heartgate.webServices.Webservice;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPassword extends AppCompatActivity {

    EditText editText;
    Button button2;
    ImageView bck;

    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        bck = findViewById(R.id.bck);
        button2 = findViewById(R.id.button2);
        editText = findViewById(R.id.editText);


        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResetPassword();
            }
        });

    }

    private void ResetPassword() {

        String email = editText.getText().toString().trim();

        if (!email.isEmpty() && isEmailValid(email)) {
            Webservice.getInstance().getApi().resetPassword(email).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    String message = null;
                    int state = 0;
                    try {
                        JSONObject res = new JSONObject(response.body().string());
                        message = res.getString("Message");
                        state = res.getInt("state");
                        Log.d("ForgetPassword Response", message + state);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (state == 0) {
                        Toast.makeText(ForgetPassword.this, message, Toast.LENGTH_SHORT).show();
                    } else if (state == 1) {
                        Toast.makeText(ForgetPassword.this, message, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(ForgetPassword.this, "failure , check your connection", Toast.LENGTH_LONG).show();

                }
            });
        } else
            Toast.makeText(this, "Please Enter a Valid Email", Toast.LENGTH_LONG).show();
    }
}
