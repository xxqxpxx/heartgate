package dev.cat.mahmoudelbaz.heartgate.signUp;

import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import dev.cat.mahmoudelbaz.heartgate.R;

public class SignUp extends AppCompatActivity {

    ProgressBar progress;
    ImageView back;
    EditText userName, firstName, middleName, lastName, email, password, confrimPassword, phoneNumber, dateOfBirth, interests;
    Spinner speciality, jobTitle, currentLiving, currentWork, prevWork, yearsOfExperience;
    Button register;
    RadioGroup radioGender;
    RadioButton male, female;
    String url;
    int year = 1991;
    int month = 0;
    int day = 1;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        back = (ImageView) findViewById(R.id.bck);
        userName = (EditText) findViewById(R.id.etUserName);
        firstName = (EditText) findViewById(R.id.etFirstName);
        middleName = (EditText) findViewById(R.id.etMiddleName);
        lastName = (EditText) findViewById(R.id.etLastName);
        email = (EditText) findViewById(R.id.etEmail);
        password = (EditText) findViewById(R.id.etPassword);
        confrimPassword = (EditText) findViewById(R.id.etConfirmPassword);
        phoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        dateOfBirth = (EditText) findViewById(R.id.etDateOfBirth);
        interests = (EditText) findViewById(R.id.etInterests);


        speciality = (Spinner) findViewById(R.id.spinnerSpeciality);
        jobTitle = (Spinner) findViewById(R.id.spinnerJobTitle);
        currentLiving = (Spinner) findViewById(R.id.spinnerCurrentLiving);
        currentWork = (Spinner) findViewById(R.id.spinnerCurrentWork);
        prevWork = (Spinner) findViewById(R.id.spinnerPreviousWork);
        yearsOfExperience = (Spinner) findViewById(R.id.spinnerExperience);

        register = (Button) findViewById(R.id.btnRegister);

        radioGender = (RadioGroup) findViewById(R.id.radioGender);

        male = (RadioButton) findViewById(R.id.radioMale);
        female = (RadioButton) findViewById(R.id.radioFemale);

        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);

        dateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    DatePickerDialog datePick = new DatePickerDialog(SignUp.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            int month = monthOfYear + 1;

                            dateOfBirth.setText(year + "-" + String.format("%02d", month) + "-" + String.format("%02d", dayOfMonth));
                        }
                    }, year, month, day);


                    datePick.show();
                }
            }
        });

        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePick = new DatePickerDialog(SignUp.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;

                        dateOfBirth.setText(year + "-" + String.format("%02d", month) + "-" + String.format("%02d", dayOfMonth));
                    }
                }, year, month, day);


                datePick.show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userNametxt = userName.getText().toString();
                final String firstNametxt = firstName.getText().toString();
                final String middleNametxt = middleName.getText().toString();
                final String lastNametxt = lastName.getText().toString();
                final String emailtxt = email.getText().toString();
                final String passwordtxt = password.getText().toString();
                final String confirmPasswordtxt = confrimPassword.getText().toString();
                final String phoneNumbertxt = phoneNumber.getText().toString();
                final String dateOfBirthtxt = dateOfBirth.getText().toString();
                final String intereststxt = interests.getText().toString();

                final int selectedSpeciality = speciality.getSelectedItemPosition();
                final String selectedSpecialitytxt = Integer.toString(selectedSpeciality);

                final int selectedJobTitle = jobTitle.getSelectedItemPosition();
                final String selectedJobTitletxt = Integer.toString(selectedJobTitle);

                final int selectedCurrentLiving = currentLiving.getSelectedItemPosition();
                final String selectedCurrentLivingtxt = Integer.toString(selectedCurrentLiving);

                final int selectedCurrentWork = currentWork.getSelectedItemPosition();
                final String selectedCurrentWorktxt = Integer.toString(selectedCurrentWork);

                final int selectedPrevWork = prevWork.getSelectedItemPosition();
                final String selectedPrevWorktxt = Integer.toString(selectedPrevWork);

                final int selectedExperience = yearsOfExperience.getSelectedItemPosition();
                final String selectedExperiencetxt = Integer.toString(selectedExperience);

                int selectedGender = radioGender.getCheckedRadioButtonId();
                View radioButton = radioGender.findViewById(selectedGender);
                final int genderidx = radioGender.indexOfChild(radioButton);
                final String genderidxtxt = Integer.toString(genderidx);

                if (userNametxt.length() == 0 || firstNametxt.length() == 0
                        || middleNametxt.length() == 0 || lastNametxt.length() == 0
                        || emailtxt.length() == 0 || passwordtxt.length() == 0
                        || confirmPasswordtxt.length() == 0 || phoneNumbertxt.length() == 0
                        || dateOfBirthtxt.length() == 0 || selectedSpeciality == 0
                        || selectedJobTitle == 0 || selectedCurrentLiving == 0
                        || selectedCurrentWork == 0 || selectedExperience == 0 || genderidx == -1 || genderidx == 0) {

                    Toast.makeText(SignUp.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (passwordtxt == confirmPasswordtxt) {
                    Toast.makeText(SignUp.this, "Password and Confirm Password not matched", Toast.LENGTH_SHORT).show();
                } else {

                    progress.setVisibility(View.VISIBLE);
//                    url="https://www.digitalcatsite.com/api_heartgate/users/add";
                    url = "http://hg.api.digitalcatsite.com/users/add";

                    JSONObject jsobj = new JSONObject();
                    try {
                        jsobj.put("firstname", firstNametxt);
                        jsobj.put("midname", middleNametxt);
                        jsobj.put("lastname", lastNametxt);
                        jsobj.put("username", userNametxt);
                        jsobj.put("password", passwordtxt);
                        jsobj.put("confirm_password", confirmPasswordtxt);
                        jsobj.put("email", emailtxt);
                        jsobj.put("mobile_number", phoneNumbertxt);
                        jsobj.put("birthdate", dateOfBirthtxt);
                        jsobj.put("fk_gender_id", genderidx);
                        jsobj.put("fk_job_id", selectedJobTitle);
                        jsobj.put("fk_speciality_id", selectedSpeciality);
                        jsobj.put("fk_current_living_place", selectedCurrentLiving);
                        jsobj.put("fk_current_work_place", selectedCurrentLiving);
                        jsobj.put("fk_previous_work_place", selectedPrevWork);
                        jsobj.put("fk_year_of_experience", selectedExperience);
                        jsobj.put("interests", intereststxt);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest postrequest = new JsonObjectRequest(Request.Method.POST, url, jsobj, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            progress.setVisibility(View.INVISIBLE);
                            String message = null;
                            int state = 0;

                            try {

                                message = response.getString("Message");
                                state = response.getInt("state");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (state == 0) {
                                Toast.makeText(SignUp.this, message, Toast.LENGTH_SHORT).show();
                            } else if (state == 1) {
                                Toast.makeText(SignUp.this, message, Toast.LENGTH_SHORT).show();
                                //       Toast.makeText(SignUp.this, response.toString(), Toast.LENGTH_SHORT).show();
//                                    onBackPressed();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            progress.setVisibility(View.INVISIBLE);


                            Toast.makeText(SignUp.this, error.toString(), Toast.LENGTH_SHORT).show();

                            Toast.makeText(SignUp.this, "Network Error", Toast.LENGTH_SHORT).show();

                        }
                    });


//                    StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            progress.setVisibility(View.INVISIBLE);
//                            String message = null;
//                            int state = 0;
//                            JSONObject res = null;
//                            try {
//                                res = new JSONObject(response);
//                                message = res.getString("Message");
//                                state = res.getInt("state");
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                                if (state == 0){
//                                    Toast.makeText(SignUp.this, message, Toast.LENGTH_SHORT).show();
//                                }
//                                else if (state == 1){
//                                    Toast.makeText(SignUp.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
////                                    onBackPressed();
//                                }
//
//
//
//
//
//
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            progress.setVisibility(View.INVISIBLE);
//
//
//                            Toast.makeText(SignUp.this, error.toString(), Toast.LENGTH_SHORT).show();
//
//                            Toast.makeText(SignUp.this, "Network Error", Toast.LENGTH_SHORT).show();
//
//                        }
//                    }){
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//                            HashMap map = new HashMap();
//                            map.put("firstname",firstNametxt);
//                            map.put("midname",middleNametxt);
//                            map.put("lastname",lastNametxt);
//                            map.put("username",userNametxt);
//                            map.put("password",passwordtxt);
//                            map.put("confirm_password",confirmPasswordtxt);
//                            map.put("email",emailtxt);
//                            map.put("mobile_number",phoneNumbertxt);
//                            map.put("birthdate",dateOfBirthtxt);
//                            map.put("fk_gender_id",genderidx);
//                            map.put("fk_job_id",selectedJobTitle);
//                            map.put("fk_speciality_id",selectedSpeciality);
//                            map.put("fk_current_living_place",selectedCurrentLiving);
//                            map.put("fk_current_work_place",selectedCurrentLiving);
//                            map.put("fk_previous_work_place",selectedPrevWork);
//                            map.put("fk_year_of_experience",selectedExperience);
//                            map.put("interests",intereststxt);
//
//                            return map;
//                        }
//                    };

                    Volley.newRequestQueue(SignUp.this).add(postrequest);

                }

            }
        });


//        Spinner spinnerSpeciality = (Spinner) findViewById(R.id.spinnerSpeciality);
//// Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> specialityAdapter = ArrayAdapter.createFromResource(this,
//                R.array.speciality_array, android.R.layout.simple_spinner_item);
//// Specify the layout to use when the list of choices appears
//        specialityAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
//        spinnerSpeciality.setPrompt("Speciality");
//// Apply the adapter to the spinner
//        spinnerSpeciality.setAdapter(
//                new NothingSelectedSpinnerAdapter(
//                        specialityAdapter,
//                        R.layout.contact_spinner_row_nothing_selected,
//                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
//                        this));


    }

}
