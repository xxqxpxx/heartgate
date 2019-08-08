package dev.cat.mahmoudelbaz.heartgate.myAccount;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import com.squareup.picasso.Picasso;

import dev.cat.mahmoudelbaz.heartgate.BitmapHelper;
import dev.cat.mahmoudelbaz.heartgate.ImageBase64;
import dev.cat.mahmoudelbaz.heartgate.heartPress.CardioUpdates;
import dev.cat.mahmoudelbaz.heartgate.heartPress.CardioUpdatesResponseModel;
import dev.cat.mahmoudelbaz.heartgate.heartPress.heartPressAdapter;
import dev.cat.mahmoudelbaz.heartgate.webServices.Webservice;
import okhttp3.ResponseBody;
import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;
import permission.auron.com.marshmallowpermissionhelper.PermissionUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.cat.mahmoudelbaz.heartgate.R;
import retrofit2.Call;
import retrofit2.Callback;

public class MyProfile extends ActivityManagePermission {

    SharedPreferences shared;
    String userID;
    EditText name, email, mobile, dateOfBirth, gender, speciality, jobTitle, currentLiving, currentWork, prevWork, experience, interests;

    Button btnUpdate;
    ImageView imgprofile;
    ProgressBar progress;
    String url;
    TypedArray specialityArr, jobTitleArr, currentLivingArr, currentWorkArr, prevWorkArr, expArr;

    JSONObject res;
    private static int RESULT_LOAD_IMAGE = 1;

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
        btnUpdate = findViewById(R.id.btnUpdate);
        name = findViewById(R.id.txtName);
        email = findViewById(R.id.txtEmail);
        mobile = findViewById(R.id.txtMob);
        dateOfBirth = findViewById(R.id.txtBirthDate);
        gender = findViewById(R.id.txtGender);
        speciality = findViewById(R.id.txtSpeciality);
        jobTitle = findViewById(R.id.txtJobTitle);
        currentLiving = findViewById(R.id.txtCurrentLivingPlace);
        currentWork = findViewById(R.id.txtCurrentWorkPlace);
        prevWork = findViewById(R.id.txtPrevWorkPlace);
        experience = findViewById(R.id.txtExperience);
        interests = findViewById(R.id.txtInterests);

        url = "http://heartgate.co/api_heartgate/users/current/" + userID;

        StringRequest loginRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray usersarray = new JSONArray(response);
                      res = usersarray.getJSONObject(0);

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
                    final String imgurl = "http://heartgate.co/api_heartgate/layout/images/" + imgstring;

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


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    updateUserdata();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        imgprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isGranted = isPermissionsGranted(MyProfile.this, new String[]{PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE});
                if (isGranted) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                } else {
                    askCompactPermissions(new String[]{PermissionUtils.Manifest_READ_EXTERNAL_STORAGE}, new PermissionResult() {
                        @Override
                        public void permissionGranted() {
                            //permission granted
                            //replace with your action

                            Intent i = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            startActivityForResult(i, RESULT_LOAD_IMAGE);
                        }

                        @Override
                        public void permissionDenied() {
                            //permission denied
                            //replace with your action
                            Toast.makeText(MyProfile.this, "You Cannot use this ferature Granting permission", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void permissionForeverDenied() {
                            // user has check 'never ask again'
                            // you need to open setting manually
                            Toast.makeText(MyProfile.this, "Please Enable Storage Permission", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }

    private void updateUserdata() throws JSONException {


        HashMap<String, Object> map = new HashMap<>();

        String[] splited = res.getString("fullname").split("\\s+");
        map.put("firstname",  splited[0]);
        map.put("midname",  splited[1]);
        map.put("lastname", splited[2]);
        map.put("username",  res.getString("username"));
        map.put("password",  res.getString("password"));
        map.put("confirm_password",  res.getString("confirm_password"));
        map.put("email",  res.getString("email"));
        map.put("mobile_number",  res.getString("mobile_number"));
        map.put("birthdate",  res.getString("birthdate"));
        map.put("fk_gender_id",  res.getString("fk_gender_id"));
        map.put("fk_speciality_id",  res.getString("fk_speciality_id"));
        map.put("fk_job_id",  res.getInt("fk_job_id"));
        map.put("fk_current_living_place",  res.getInt("fk_current_living_place"));
        map.put("fk_current_work_place",  res.getInt("fk_current_work_place"));
        map.put("fk_previous_work_place",  res.getInt("fk_previous_work_place"));
        map.put("fk_year_of_experience",  res.getInt("fk_year_of_experience"));
        map.put("interests",  res.getString("interests"));
        map.put("image_profile",  res.getString("image_profile"));

        progress.setVisibility(View.VISIBLE);

        Log.d("sent Item", map.toString());
        Log.d("Id", userID);


        Webservice.getInstance().getApi().updateUser(userID , map).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call< Object> call, retrofit2.Response< Object > response) {
                if (!response.isSuccessful()) {
                    assert response.errorBody() != null;
                    Toast.makeText(MyProfile.this, response.errorBody().toString() ,  Toast.LENGTH_LONG).show();
                    progress.setVisibility(View.GONE);
                } else {
                    /*cardioUpdatesResponseModels = response.body();
                    nearByConnectionsAdapter = new heartPressAdapter(MyProfile.this, cardioUpdatesResponseModels);

                *//*    RecycleViewCardoivascular.setHasFixedSize(true);
                    RecycleViewCardoivascular.setLayoutManager(new LinearLayoutManager(CardioUpdates.this));*//*
                    RecycleViewCardoivascular.setAdapter(nearByConnectionsAdapter);
*/
                    progress.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(MyProfile.this, "failure , check your connection", Toast.LENGTH_LONG).show();
                Log.e("login", "onFailure: ", t);
                progress.setVisibility(View.GONE);
            }
        });




    }

    private void changePhoto(String encoded) {
        HashMap<String, String> map = new HashMap<>();
        map.put("image_profile", encoded);
        progress.setVisibility(View.VISIBLE);

        Log.d("sent Item", map.toString());
        Log.d("Id", userID);


        Webservice.getInstance().getApi().changeImage(userID , map).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call< Object > call, retrofit2.Response< Object > response) {
                if (!response.isSuccessful()) {
                    assert response.errorBody() != null;
                    Toast.makeText(MyProfile.this, response.errorBody().toString() ,  Toast.LENGTH_LONG).show();
                    progress.setVisibility(View.GONE);
                } else {
                    /*cardioUpdatesResponseModels = response.body();
                    nearByConnectionsAdapter = new heartPressAdapter(MyProfile.this, cardioUpdatesResponseModels);

                *//*    RecycleViewCardoivascular.setHasFixedSize(true);
                    RecycleViewCardoivascular.setLayoutManager(new LinearLayoutManager(CardioUpdates.this));*//*
                    RecycleViewCardoivascular.setAdapter(nearByConnectionsAdapter);
*/
                    progress.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call< Object > call, Throwable t) {
                Toast.makeText(MyProfile.this, "failure , check your connection", Toast.LENGTH_LONG).show();
                Log.e("login", "onFailure: ", t);
                progress.setVisibility(View.GONE);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            imgprofile.setImageBitmap(BitmapHelper.decodeFile(picturePath, 300, 300, true));

            Bitmap bitmap = ((BitmapDrawable) imgprofile.getDrawable()).getBitmap();
            String encoded = ImageBase64.encodeTobase64(bitmap);

            changePhoto(encoded);


        }
    }


}
