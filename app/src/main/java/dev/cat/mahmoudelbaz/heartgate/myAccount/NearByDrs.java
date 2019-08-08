package dev.cat.mahmoudelbaz.heartgate.myAccount;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.ArrayList;

import dev.cat.mahmoudelbaz.heartgate.R;

import static android.content.ContentValues.TAG;

public class NearByDrs extends AppCompatActivity {

    String url, userId;
    ArrayList<ModelMyConnections> nearByConnections = new ArrayList<ModelMyConnections>();

    Boolean isLoading;
    AdapterNearBy nearByConnectionsAdapter;

    ListView nearBylist;
    ProgressBar nearByprogress;
    TextView nearByempty;
    SharedPreferences shared;
    EditText nearBysearchView;
    int pageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_drs);

        pageId = 1;
        nearByConnections.clear();
        isLoading = false;

        shared = this.getSharedPreferences("id", Context.MODE_PRIVATE);
        userId = shared.getString("id", "0");


        nearBysearchView = (EditText) findViewById(R.id.nearBysearch_view);
        nearBylist = (ListView) findViewById(R.id.nearBylistView);
        nearByprogress = (ProgressBar) findViewById(R.id.nearByprogressBar);
        nearByempty = (TextView) findViewById(R.id.nearBytxtEmpty);
        nearBylist.setEmptyView(nearByempty);


        nearByConnectionsAdapter = new AdapterNearBy(this, nearByConnections);


        url = "http://heartgate.co/api_heartgate/nearby_drs/" + userId + "/" + pageId;

        Log.d(TAG, "Recieved: " + url);

        StringRequest productsRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
//                    JSONObject object = new JSONObject(response);
                    JSONArray usersarray = new JSONArray(response);
                    if (usersarray.length() == 0) {
                        nearByprogress.setVisibility(View.INVISIBLE);
                    } else {

                        pageId++;
                        for (int i = 0; i < usersarray.length(); i++) {
                            JSONObject currentobject = usersarray.getJSONObject(i);
                            final int id = currentobject.getInt("id");
                            final int stateId = 0;
                            final String fullName = currentobject.getString("fullname");
                            final String jobTitle = currentobject.getString("speciality");
                            final String picture = currentobject.getString("image_profile");
                            final String imageUrl = "http://heartgate.co/api_heartgate/layout/images/" + picture;

                            nearByConnections.add(new ModelMyConnections(stateId, id, fullName, jobTitle, imageUrl));
                            nearBylist.setAdapter(nearByConnectionsAdapter);

                            nearByprogress.setVisibility(View.INVISIBLE);

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NearByDrs.this, "Network Error", Toast.LENGTH_SHORT).show();

            }
        });

        Volley.newRequestQueue(this).add(productsRequest);


        nearBylist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (nearBylist.getAdapter() == null)
                    return;

                if (nearBylist.getAdapter().getCount() == 0)
                    return;

                int l = visibleItemCount + firstVisibleItem;
                if (l >= totalItemCount && !isLoading) {
                    // It is time to add new data. We call the listener
                    isLoading = true;
                    loadData();
                }

            }
        });


        nearBysearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                nearByConnectionsAdapter.getFilter().filter(s.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void loadData() {


        nearByprogress.setVisibility(View.VISIBLE);

        url = "http://heartgate.co/api_heartgate/nearby_drs/" + userId + "/" + pageId;

        StringRequest productsRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
//                    JSONObject object = new JSONObject(response);
                    JSONArray usersarray = new JSONArray(response);

                    if (usersarray.length() == 0) {
                        isLoading = true;
                        nearByprogress.setVisibility(View.INVISIBLE);
                    } else {
                        isLoading = false;
                        pageId++;
                    }


                    for (int i = 0; i < usersarray.length(); i++) {
                        JSONObject currentobject = usersarray.getJSONObject(i);
                        final int id = currentobject.getInt("id");
                        final int stateId = 0;
                        final String fullName = currentobject.getString("fullname");
                        final String jobTitle = currentobject.getString("speciality");
                        final String picture = currentobject.getString("image_profile");
                        final String imageUrl = "http://heartgate.co/api_heartgate/layout/images/" + picture;

                        nearByConnections.add(new ModelMyConnections(stateId, id, fullName, jobTitle, imageUrl));


                        nearByprogress.setVisibility(View.INVISIBLE);
                        nearByConnectionsAdapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NearByDrs.this, "Network Error", Toast.LENGTH_SHORT).show();

            }
        });

        Volley.newRequestQueue(this).add(productsRequest);


    }
}
