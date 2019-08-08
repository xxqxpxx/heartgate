package dev.cat.mahmoudelbaz.heartgate.myAccount;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class SentConnections extends Fragment {

    ConnectionsTabs activity;


    String url, userId;
    ArrayList<ModelMyConnections> sentConnections = new ArrayList<ModelMyConnections>();

    Boolean isLoading;
    AdapterSentConnections sentConnectionsAdapter;

    ListView sentlist;
    ProgressBar sentprogress;
    TextView sentempty;
    SharedPreferences shared;
    EditText sentsearchView;
    int pageId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        pageId = 1;
        sentConnections.clear();
        isLoading = false;


        View view = inflater.inflate(R.layout.fragment_sent_connections, container, false);


        shared = this.getActivity().getSharedPreferences("id", Context.MODE_PRIVATE);
        userId = shared.getString("id", "0");


        activity = (ConnectionsTabs) getActivity();

        sentsearchView = (EditText) view.findViewById(R.id.sentsearch_view);
        sentlist = (ListView) view.findViewById(R.id.sentlistView);
        sentprogress = (ProgressBar) view.findViewById(R.id.sentprogressBar);
        sentempty = (TextView) view.findViewById(R.id.senttxtEmpty);
        sentlist.setEmptyView(sentempty);


        sentConnectionsAdapter = new AdapterSentConnections(activity, sentConnections);


        url = "http://heartgate.co/api_heartgate/connections/sent_connections/" + userId + "/" + pageId;

        StringRequest productsRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
//                    JSONObject object = new JSONObject(response);
                    JSONArray usersarray = new JSONArray(response);

                    if (usersarray.length() == 0) {
                        sentprogress.setVisibility(View.INVISIBLE);
                    } else {

                        pageId++;
                        for (int i = 0; i < usersarray.length(); i++) {
                            JSONObject currentobject = usersarray.getJSONObject(i);
                            final int id = currentobject.getInt("id");
                            final int stateId = currentobject.getInt("state_id");
                            final String fullName = currentobject.getString("fullname");
                            final String jobTitle = currentobject.getString("speciality");
                            final String picture = currentobject.getString("image_profile");
                            final String imageUrl = "http://heartgate.co/api_heartgate/layout/images/" + picture;

                            sentConnections.add(new ModelMyConnections(stateId, id, fullName, jobTitle, imageUrl));
                            sentlist.setAdapter(sentConnectionsAdapter);

                            sentprogress.setVisibility(View.INVISIBLE);

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();

            }
        });

        Volley.newRequestQueue(activity).add(productsRequest);


        sentlist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (sentlist.getAdapter() == null)
                    return;

                if (sentlist.getAdapter().getCount() == 0)
                    return;

                int l = visibleItemCount + firstVisibleItem;
                if (l >= totalItemCount && !isLoading) {
                    // It is time to add new data. We call the listener
                    isLoading = true;
                    loadData();
                }

            }
        });


        sentsearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                sentConnectionsAdapter.getFilter().filter(s.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }


    private void loadData() {


        sentprogress.setVisibility(View.VISIBLE);

        url = "http://heartgate.co/api_heartgate/connections/sent_connections/" + userId + "/" + pageId;

        StringRequest productsRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
//                    JSONObject object = new JSONObject(response);
                    JSONArray usersarray = new JSONArray(response);

                    if (usersarray.length() == 0) {
                        isLoading = true;
                        sentprogress.setVisibility(View.INVISIBLE);
                    } else {
                        isLoading = false;
                        pageId++;
                    }


                    for (int i = 0; i < usersarray.length(); i++) {
                        JSONObject currentobject = usersarray.getJSONObject(i);
                        final int id = currentobject.getInt("id");
                        final int stateId = currentobject.getInt("state_id");
                        final String fullName = currentobject.getString("fullname");
                        final String jobTitle = currentobject.getString("speciality");
                        final String picture = currentobject.getString("image_profile");
                        final String imageUrl = "http://heartgate.co/api_heartgate/layout/images/" + picture;

                        sentConnections.add(new ModelMyConnections(stateId, id, fullName, jobTitle, imageUrl));


                        sentprogress.setVisibility(View.INVISIBLE);
                        sentConnectionsAdapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();

            }
        });

        Volley.newRequestQueue(activity).add(productsRequest);


    }


}
