package dev.cat.mahmoudelbaz.heartgate.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.cat.mahmoudelbaz.heartgate.Login;
import dev.cat.mahmoudelbaz.heartgate.R;
import dev.cat.mahmoudelbaz.heartgate.concor.Concor;
import dev.cat.mahmoudelbaz.heartgate.concor.ConcorPrice;
import dev.cat.mahmoudelbaz.heartgate.concor.Concor_plus;
import dev.cat.mahmoudelbaz.heartgate.drugInteractions.DrugInteractions;
 import dev.cat.mahmoudelbaz.heartgate.heartPress.CardioUpdates;
import dev.cat.mahmoudelbaz.heartgate.heartPress.OnlineLibrary;
import dev.cat.mahmoudelbaz.heartgate.medicalStatistics.BMI;
import dev.cat.mahmoudelbaz.heartgate.medicalStatistics.CardioRiskFactor;
import dev.cat.mahmoudelbaz.heartgate.myAccount.Calender;
import dev.cat.mahmoudelbaz.heartgate.myAccount.ConnectionsTabs;
import dev.cat.mahmoudelbaz.heartgate.myAccount.MyProfile;
import dev.cat.mahmoudelbaz.heartgate.myAccount.NearByDrs;
import dev.cat.mahmoudelbaz.heartgate.pharamcy.Pharamcy;
import dev.cat.mahmoudelbaz.heartgate.poll.Survey;
import dev.cat.mahmoudelbaz.heartgate.advisoryBoard.Questions;
import dev.cat.mahmoudelbaz.heartgate.videos.VideosListActivity;

public class Home extends AppCompatActivity {
    ProgressBar progress;
    ImageView imgprofile, img_home, img_connections, img_conor_price, img_neaby_drs, img_drug_interactions;
    TextView name, email, txt_call_support;
    String url;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<Menu_item> listDataHeader;
    HashMap<Menu_item, List<Child_item>> listDataChild;
    TextView logOut;
    SharedPreferences shared;
    public static String userID;
    Button btnEdit;
    private int lastExpandedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        imgprofile = (ImageView) findViewById(R.id.imgProfile);
        name = (TextView) findViewById(R.id.txtName);
        email = (TextView) findViewById(R.id.txtEmail);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        btnEdit = findViewById(R.id.btnEdit);
        img_home = findViewById(R.id.img_home);
        img_connections = findViewById(R.id.img_connections);
        img_conor_price = findViewById(R.id.img_conor_price);
        img_neaby_drs = findViewById(R.id.img_neaby_drs);
        img_drug_interactions = findViewById(R.id.img_drug_interactions);

        txt_call_support = findViewById(R.id.txt_call_support);


        shared = getSharedPreferences("id", Context.MODE_PRIVATE);

        userID = shared.getString("id", "0");


        url = "http://heartgate.co/api_heartgate/users/current/" + userID;

        StringRequest loginRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {


                    JSONArray usersarray = new JSONArray(response);
                    JSONObject res = usersarray.getJSONObject(0);

                    final String namestring = res.getString("fullname");
                    final String emailstring = res.getString("email");
                    final String imgstring = res.getString("image_profile");
                    final String imgurl = "http://heartgate.co/api_heartgate/layout/images/" + imgstring;

                    name.setText(namestring);
                    email.setText(emailstring);
                    Picasso.with(Home.this).load(imgurl).placeholder(R.drawable.profile).error(R.drawable.profile).into(imgprofile);


                    progress.setVisibility(View.INVISIBLE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progress.setVisibility(View.INVISIBLE);
                Toast.makeText(Home.this, "Network Error", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Home.this, Login.class);
                startActivity(i);
                finish();

            }
        });

        Volley.newRequestQueue(Home.this).add(loginRequest);


        logOut = (TextView) findViewById(R.id.txtLogout);
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);


        txt_call_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:00201146121111"));

                if (ActivityCompat.checkSelfPermission(Home.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);


            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Home.this, MyProfile.class);
                startActivity(i);

            }
        });

        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Home.this, Home.class);
                startActivity(i);
                finish();

            }
        });


        img_connections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Home.this, ConnectionsTabs.class);
                startActivity(i);

            }
        });


        img_conor_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Home.this, ConcorPrice.class);
                startActivity(i);

            }
        });


        img_neaby_drs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Home.this, NearByDrs.class);
                startActivity(i);

            }
        });


        img_drug_interactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Home.this, DrugInteractions.class);
                startActivity(i);

            }
        });


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor myEdit = shared.edit();
                myEdit.putString("id", "0");
                myEdit.commit();


                Intent i = new Intent(Home.this, Login.class);
                startActivity(i);
                finish();
            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });

       /* expListView.setOnGroupClickListener(
                new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long id) {

                        Intent i;

                        if (groupPosition == 7) {
                            i = new Intent(Home.this, Pharamcy.class);
                            startActivity(i);
                            finish();
                        }


                       else if (groupPosition == 8) {
                            i = new Intent(Home.this, SplachScreen.class);
                            startActivity(i);
                            finish();

                        }


                        return true;
                    }

                }
        );*/

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                Intent i;

                if (groupPosition == 0) {

                    switch (childPosition) {
                        case 0:
                            i = new Intent(Home.this, MyProfile.class);
                            startActivity(i);
                            break; // optional

                        case 1:
                            i = new Intent(Home.this, NearByDrs.class);
                            startActivity(i);
                            break; // optional


                        case 2:
                            i = new Intent(Home.this, ConnectionsTabs.class);
                            startActivity(i);
                            break; // optional

                        case 3:
                            // i = new Intent(Home.this, Favourites.class);
                            i = new Intent(Home.this, ConnectionsTabs.class);

                            startActivity(i);
                            break; // optional

                        case 4:
                            i = new Intent(Home.this, Calender.class);
                            startActivity(i);
                            break; // optional

                        default: // Optional
                            // Statements
                    }

                } else if (groupPosition == 1) {

                    switch (childPosition) {
                        case 0:
                            i = new Intent(Home.this, Concor.class);
                            startActivity(i);
                            break; // optional

                        case 1:
                            i = new Intent(Home.this, Concor_plus.class);
                            startActivity(i);
                            break; // optional

                        case 2:
                            i = new Intent(Home.this, ConcorPrice.class);
                            startActivity(i);
                            break; // optional

                        default: // Optional
                            // Statements
                    }

                } else if (groupPosition == 2) {

                    switch (childPosition) {
                        case 0:
                            i = new Intent(Home.this, CardioUpdates.class);
                            startActivity(i);
                            break; // optional

                        case 1:
                            i = new Intent(Home.this, OnlineLibrary.class);
                            startActivity(i);
                            break; // optional

                        default: // Optional
                            // Statements
                    }

                } else if (groupPosition == 3) {

                    switch (childPosition) {
                        case 0:
                            i = new Intent(Home.this, BMI.class);
                            startActivity(i);
                            break; // optional

                        case 1:
                            i = new Intent(Home.this, CardioRiskFactor.class);
                            startActivity(i);
                            break; // optional

                        default: // Optional
                            // Statements
                    }

                } else if (groupPosition == 4) {

                    switch (childPosition) {
                        case 0:
                           i = new Intent(Home.this, Questions.class);
                           startActivity(i);
                            break; // optional

                        default: // Optional
                            // Statements
                    }

                } else if (groupPosition == 5) {

                    switch (childPosition) {
                        case 0:
                            i = new Intent(Home.this, DrugInteractions.class);
                            startActivity(i);
                            break; // optional

                        default: // Optional
                            // Statements
                    }

                } else if (groupPosition == 6) {

                    switch (childPosition) {
                        case 0:
                            i = new Intent(Home.this, Survey.class);
                            startActivity(i);
                            break; // optional

                        default: // Optional
                            // Statements
                    }

                } else if (groupPosition == 7) {

                    i = new Intent(Home.this, Pharamcy.class);
                    startActivity(i);

                }

                else if (groupPosition == 8) {

                    i = new Intent(Home.this, VideosListActivity.class);
                    startActivity(i);

                }



                return true;
            }
        });



        /*
        i = new Intent(Home.this, MyProfile.class);
                            startActivity(i);
         */

    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<Menu_item>();
        listDataChild = new HashMap<Menu_item, List<Child_item>>();

        // Adding child data
        listDataHeader.add(new Menu_item("My Account", R.drawable.white_frame, R.drawable.iconaccount));
        listDataHeader.add(new Menu_item("Concor", R.drawable.white_frame, R.drawable.iconconcor));
        listDataHeader.add(new Menu_item("Heartpress", R.drawable.white_frame, R.drawable.iconheartpress));
        listDataHeader.add(new Menu_item("Medical Statistics Claculator", R.drawable.white_frame, R.drawable.iconstatics));
        listDataHeader.add(new Menu_item("Advisory Board", R.drawable.white_frame, R.drawable.iconadvisor));
        listDataHeader.add(new Menu_item("Drug Interactions", R.drawable.white_frame, R.drawable.icondrug));
        listDataHeader.add(new Menu_item("Poll", R.drawable.white_frame, R.drawable.iconsurvey));
        listDataHeader.add(new Menu_item("Pharmacy", R.drawable.white_frame, R.drawable.ic_stat_onesignal_default));
        listDataHeader.add(new Menu_item("Video", R.drawable.white_frame, R.drawable.iconvideo));

        //      listDataHeader.add(new Menu_item("Game", R.drawable.white_frame, R.drawable.food_ico));


        // Adding child data
        List<Child_item> myAccount = new ArrayList<Child_item>();
        myAccount.add(new Child_item("My profile", R.drawable.accountbg));
        myAccount.add(new Child_item("Nearby Drs", R.drawable.accountbg));
        myAccount.add(new Child_item("Connections", R.drawable.accountbg));
        myAccount.add(new Child_item("Favourites", R.drawable.accountbg));
        myAccount.add(new Child_item("Calender", R.drawable.accountbg));

        List<Child_item> concor = new ArrayList<Child_item>();
        concor.add(new Child_item("Concor", R.drawable.concorbg));
        concor.add(new Child_item("Concor plus", R.drawable.concorbg));
        concor.add(new Child_item("Price", R.drawable.concorbg));


        List<Child_item> heartPress = new ArrayList<Child_item>();
        heartPress.add(new Child_item("Cardiovascular updates", R.drawable.heartpressbg));
        heartPress.add(new Child_item("Library", R.drawable.heartpressbg));


        List<Child_item> medicalStatics = new ArrayList<Child_item>();
        medicalStatics.add(new Child_item("Adult BMI", R.drawable.medicalstaticsbg));
        medicalStatics.add(new Child_item("Cardiovascular Risk Factor", R.drawable.medicalstaticsbg));


        List<Child_item> advisory = new ArrayList<Child_item>();
        advisory.add(new Child_item("Ask Experts", R.drawable.advisebg));


        List<Child_item> drug = new ArrayList<Child_item>();
        drug.add(new Child_item("Drug Interactions", R.drawable.drugbg));


        List<Child_item> poll = new ArrayList<>();
        poll.add(new Child_item("Public Survey", R.drawable.pullbg));
        poll.add(new Child_item("Private Survey", R.drawable.pullbg));

        List<Child_item> Pharmacy = new ArrayList<Child_item>();
        Pharmacy.add(new Child_item("Pharmacy", R.drawable.drugbg));

        List<Child_item> Game = new ArrayList<Child_item>();
        Game.add(new Child_item("Game", R.drawable.drugbg));

        List<Child_item> video = new ArrayList<Child_item>();
        video.add(new Child_item("video", R.drawable.drugbg));

        listDataChild.put(listDataHeader.get(0), myAccount); // Header, Child data
        listDataChild.put(listDataHeader.get(1), concor);
        listDataChild.put(listDataHeader.get(2), heartPress);
        listDataChild.put(listDataHeader.get(3), medicalStatics);
        listDataChild.put(listDataHeader.get(4), advisory);
        listDataChild.put(listDataHeader.get(5), drug);
        listDataChild.put(listDataHeader.get(6), poll);
        listDataChild.put(listDataHeader.get(7), Pharmacy);
        listDataChild.put(listDataHeader.get(8), video);

        //      listDataChild.put(listDataHeader.get(6), Game);

    }
}

