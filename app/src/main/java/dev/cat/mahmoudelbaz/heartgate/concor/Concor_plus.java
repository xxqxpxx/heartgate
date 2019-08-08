package dev.cat.mahmoudelbaz.heartgate.concor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.cat.mahmoudelbaz.heartgate.R;
import dev.cat.mahmoudelbaz.heartgate.advisoryBoard.Questions;
import dev.cat.mahmoudelbaz.heartgate.drugInteractions.DrugInteractions;
 import dev.cat.mahmoudelbaz.heartgate.heartPress.CardioUpdates;
import dev.cat.mahmoudelbaz.heartgate.heartPress.OnlineLibrary;
import dev.cat.mahmoudelbaz.heartgate.home.Child_item;
import dev.cat.mahmoudelbaz.heartgate.home.ExpandableListAdapter;
import dev.cat.mahmoudelbaz.heartgate.home.Home;
import dev.cat.mahmoudelbaz.heartgate.home.Menu_item;
import dev.cat.mahmoudelbaz.heartgate.medicalStatistics.BMI;
import dev.cat.mahmoudelbaz.heartgate.medicalStatistics.CardioRiskFactor;
import dev.cat.mahmoudelbaz.heartgate.myAccount.Calender;
import dev.cat.mahmoudelbaz.heartgate.myAccount.ConnectionsTabs;
import dev.cat.mahmoudelbaz.heartgate.myAccount.MyProfile;
import dev.cat.mahmoudelbaz.heartgate.myAccount.NearByDrs;
import dev.cat.mahmoudelbaz.heartgate.poll.Survey;

public class Concor_plus extends AppCompatActivity {

    ImageView img_home, img_connections, img_conor_price, img_neaby_drs, img_drug_interactions;
    String url;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<Menu_item> listDataHeader;
    HashMap<Menu_item, List<Child_item>> listDataChild;
    SharedPreferences shared;
    String userID;
    private int lastExpandedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concor_plus);

        img_home = findViewById(R.id.img_home);
        img_connections = findViewById(R.id.img_connections);
        img_conor_price = findViewById(R.id.img_conor_price);
        img_neaby_drs = findViewById(R.id.img_neaby_drs);
        img_drug_interactions = findViewById(R.id.img_drug_interactions);


        shared = getSharedPreferences("id", Context.MODE_PRIVATE);

        userID = shared.getString("id", "0");


        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);


        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Concor_plus.this, Home.class);
                startActivity(i);
                finish();

            }
        });


        img_connections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Concor_plus.this, ConnectionsTabs.class);
                startActivity(i);

            }
        });


        img_conor_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Concor_plus.this, ConcorPrice.class);
                startActivity(i);

            }
        });


        img_neaby_drs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Concor_plus.this, NearByDrs.class);
                startActivity(i);

            }
        });


        img_drug_interactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Concor_plus.this, DrugInteractions.class);
                startActivity(i);

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
                            i = new Intent(Concor_plus.this, Pharamcy.class);
                            startActivity(i);
                            finish();
                        }


                       else if (groupPosition == 8) {
                            i = new Intent(Concor_plus.this, SplachScreen.class);
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
                            i = new Intent(Concor_plus.this, MyProfile.class);
                            startActivity(i);
                            break; // optional

                        case 1:
                            i = new Intent(Concor_plus.this, NearByDrs.class);
                            startActivity(i);
                            break; // optional


                        case 2:
                            i = new Intent(Concor_plus.this, ConnectionsTabs.class);
                            startActivity(i);
                            break; // optional

                        case 3:
                            // i = new Intent(Concor_plus.this, Favourites.class);
                            i = new Intent(Concor_plus.this, ConnectionsTabs.class);

                            startActivity(i);
                            break; // optional

                        case 4:
                            i = new Intent(Concor_plus.this, Calender.class);
                            startActivity(i);
                            break; // optional

                        default: // Optional
                            // Statements
                    }

                } else if (groupPosition == 1) {

                    switch (childPosition) {
                        case 0:
                            i = new Intent(Concor_plus.this, Concor_plus.class);
                            startActivity(i);
                            break; // optional

                        case 1:
                            i = new Intent(Concor_plus.this, Concor_plus.class);
                            startActivity(i);
                            break; // optional

                        case 2:
                            i = new Intent(Concor_plus.this, ConcorPrice.class);
                            startActivity(i);
                            break; // optional

                        default: // Optional
                            // Statements
                    }

                } else if (groupPosition == 2) {

                    switch (childPosition) {
                        case 0:
                            i = new Intent(Concor_plus.this, CardioUpdates.class);
                            startActivity(i);
                            break; // optional

                        case 1:
                            i = new Intent(Concor_plus.this, OnlineLibrary.class);
                            startActivity(i);
                            break; // optional

                        default: // Optional
                            // Statements
                    }

                } else if (groupPosition == 3) {

                    switch (childPosition) {
                        case 0:
                            i = new Intent(Concor_plus.this, BMI.class);
                            startActivity(i);
                            break; // optional

                        case 1:
                            i = new Intent(Concor_plus.this, CardioRiskFactor.class);
                            startActivity(i);
                            break; // optional

                        default: // Optional
                            // Statements
                    }

                } else if (groupPosition == 4) {

                    switch (childPosition) {
                        case 0:
                            i = new Intent(Concor_plus.this, Questions.class);
                            startActivity(i);
                            break; // optional

                        default: // Optional
                            // Statements
                    }

                } else if (groupPosition == 5) {

                    switch (childPosition) {
                        case 0:
                            i = new Intent(Concor_plus.this, DrugInteractions.class);
                            startActivity(i);
                            break; // optional

                        default: // Optional
                            // Statements
                    }

                } else if (groupPosition == 6) {

                    switch (childPosition) {
                        case 0:
                            i = new Intent(Concor_plus.this, Survey.class);
                            startActivity(i);
                            break; // optional

                        default: // Optional
                            // Statements
                    }

                } /*else if (groupPosition == 7) {

                    i = new Intent(Concor_plus.this, LoginActivity.class);
                    startActivity(i);
                    finish();

                }*/


                return true;
            }
        });



        /*
        i = new Intent(Concor_plus.this, MyProfile.class);
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
        listDataHeader.add(new Menu_item("Composition And Pharmaceutical Form", R.drawable.white_frame, R.drawable.iconaccount));
        listDataHeader.add(new Menu_item("Indication", R.drawable.white_frame, R.drawable.iconconcor));
        listDataHeader.add(new Menu_item("Dosage / Administration", R.drawable.white_frame, R.drawable.iconheartpress));
        listDataHeader.add(new Menu_item("Contraindications / Interaction", R.drawable.white_frame, R.drawable.iconstatics));
        listDataHeader.add(new Menu_item("Composition And Pharmaceutical Form", R.drawable.white_frame, R.drawable.iconadvisor));
        listDataHeader.add(new Menu_item("Undesirable Effects / Effects On Ability To Drive And Use Machines", R.drawable.white_frame, R.drawable.iconsurvey));
        listDataHeader.add(new Menu_item("Overdose", R.drawable.white_frame, R.drawable.ic_stat_onesignal_default));

        // Adding child data
        List<Child_item> Composition = new ArrayList<Child_item>();
        Composition.add(new Child_item( getString(R.string.Composition_plus) , R.drawable.accountbg));


        List<Child_item> Indication = new ArrayList<Child_item>();
        Composition.add(new Child_item( getString(R.string.Indication_plus) , R.drawable.accountbg));


        List<Child_item> Dosage = new ArrayList<Child_item>();
        Dosage.add(new Child_item(getString(R.string.Dosage_plus), R.drawable.heartpressbg));


        List<Child_item> Contraindications = new ArrayList<Child_item>();
        Contraindications.add(new Child_item(getString(R.string.Contraindications_plus), R.drawable.medicalstaticsbg));


        List<Child_item> Warnings = new ArrayList<Child_item>();
        Warnings.add(new Child_item(getString(R.string.Composition_plus), R.drawable.advisebg));



        List<Child_item> Effects = new ArrayList<Child_item>();
        Effects.add(new Child_item(getString(R.string.Effects_plus), R.drawable.pullbg));

        List<Child_item> Overdose = new ArrayList<Child_item>();
        Overdose.add(new Child_item(getString(R.string.Overdose_plus), R.drawable.pullbg));




        listDataChild.put(listDataHeader.get(0), Composition); // Header, Child data
        listDataChild.put(listDataHeader.get(1), Indication);
        listDataChild.put(listDataHeader.get(2), Dosage);
        listDataChild.put(listDataHeader.get(3), Contraindications);
        listDataChild.put(listDataHeader.get(4), Warnings);
        listDataChild.put(listDataHeader.get(5), Effects);
        listDataChild.put(listDataHeader.get(6), Overdose);


    }
}
