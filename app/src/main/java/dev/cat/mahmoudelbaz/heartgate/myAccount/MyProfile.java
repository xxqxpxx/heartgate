package dev.cat.mahmoudelbaz.heartgate.myAccount;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dev.cat.mahmoudelbaz.heartgate.R;

public class MyProfile extends AppCompatActivity {

    SharedPreferences shared;
    String userID;
    TextView name, email, mobile, dateOfBirth, gender, speciality, jobTitle, currentLiving, currentWork, prevWork, experience, interests;


    ImageView imgprofile;
    ProgressBar progress;
    String url;
    TypedArray specialityArr, jobTitleArr, currentLivingArr, currentWorkArr, prevWorkArr, expArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        shared = getSharedPreferences("id", Context.MODE_PRIVATE);
        userID = shared.getString("id", "0");


        specialityArr = getResources().obtainTypedArray(R.array.speciality_array);
        jobTitleArr = getResources().obtainTypedArray(R.array.jobTitle_array);
        currentLivingArr = getResources().obtainTypedArray(R.array.currentLiving_array);
        currentWorkArr = getResources().obtainTypedArray(R.array.currentWork_array);
        prevWorkArr = getResources().obtainTypedArray(R.array.previousWork_array);
        expArr = getResources().obtainTypedArray(R.array.yearsOfExp_array);

//        TypedArray plantArray=getResources().obtainTypedArray(R.array.speciality_array);
//        //obtain the referenced arrays
//        CharSequence[] ginkoArray=plantArray.getTextArray(0);
//        //...
//        CharSequence[] orlayaArray=plantArray.getTextArray(3);


        imgprofile = (ImageView) findViewById(R.id.imgProfile);
        progress = (ProgressBar) findViewById(R.id.progressBar);

        name = (TextView) findViewById(R.id.txtName);
        email = (TextView) findViewById(R.id.txtEmail);
        mobile = (TextView) findViewById(R.id.txtMob);
        dateOfBirth = (TextView) findViewById(R.id.txtBirthDate);
        gender = (TextView) findViewById(R.id.txtGender);
        speciality = (TextView) findViewById(R.id.txtSpeciality);
        jobTitle = (TextView) findViewById(R.id.txtJobTitle);
        currentLiving = (TextView) findViewById(R.id.txtCurrentLivingPlace);
        currentWork = (TextView) findViewById(R.id.txtCurrentWorkPlace);
        prevWork = (TextView) findViewById(R.id.txtPrevWorkPlace);
        experience = (TextView) findViewById(R.id.txtExperience);
        interests = (TextView) findViewById(R.id.txtInterests);

        url = "http://hg.api.digitalcatsite.com/users/current/" + userID;

        StringRequest loginRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {


                    JSONArray usersarray = new JSONArray(response);
                    JSONObject res = usersarray.getJSONObject(0);

                    final String namestring = res.getString("fullname");
                    final String emailstring = res.getString("email");
                    final String mobilestring = res.getString("mobile_number");
                    final String dateOfBirthstring = res.getString("birthdate");
                    final int genderint = res.getInt("fk_gender_id");
                    final int specialityint = res.getInt("fk_speciality_id");
                    final String specialitystring = (String) specialityArr.getText(specialityint);
                    final int jobTitleint = res.getInt("fk_job_id");
                    final String jobTitlestring = (String) jobTitleArr.getText(jobTitleint);
                    final int currentLivingint = res.getInt("fk_current_living_place");
                    final String currentLivingstring = (String) currentLivingArr.getText(currentLivingint);
                    final int currentWorkint = res.getInt("fk_current_work_place");
                    final String currentWorkintstring = (String) currentWorkArr.getText(currentWorkint);
                    final int prevWorkint = res.getInt("fk_previous_work_place");
                    final String prevWorkstring = (String) prevWorkArr.getText(prevWorkint);
                    final int experienceint = res.getInt("fk_year_of_experience");
                    final String experiencestring = (String) expArr.getText(experienceint);
                    final String intereststring = res.getString("interests");

                    final String imgstring = res.getString("image_profile");
                    final String imgurl = "http://assets.hg.api.digitalcatsite.com/" + imgstring;

                    name.setText(namestring);
                    email.setText(emailstring);
                    mobile.setText(mobilestring);
                    dateOfBirth.setText(dateOfBirthstring);
                    speciality.setText(specialitystring);
                    jobTitle.setText(jobTitlestring);
                    currentLiving.setText(currentLivingstring);
                    currentWork.setText(currentWorkintstring);
                    prevWork.setText(prevWorkstring);
                    experience.setText(experiencestring);
                    interests.setText(intereststring);


                    if (genderint == 1) {
                        gender.setText("Male");
                    } else if (genderint == 2) {
                        gender.setText("Female");
                    }


                    Picasso.with(MyProfile.this).load(imgurl).placeholder(R.drawable.profile).error(R.drawable.profile).into(imgprofile);


                    progress.setVisibility(View.INVISIBLE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progress.setVisibility(View.INVISIBLE);
                Toast.makeText(MyProfile.this, "Network Error", Toast.LENGTH_SHORT).show();


            }
        });

        Volley.newRequestQueue(MyProfile.this).add(loginRequest);


    }
}
