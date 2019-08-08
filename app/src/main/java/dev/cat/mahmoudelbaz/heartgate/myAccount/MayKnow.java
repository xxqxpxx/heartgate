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

public class MayKnow extends Fragment {

    ConnectionsTabs activity;


    String url, userId;
    ArrayList<ModelMyConnections> mayKnow = new ArrayList<ModelMyConnections>();

    Boolean isLoading;
    AdapterMayKnow MayKnowAdapter;

    ListView maylist;
    ProgressBar mayprogress;
    TextView mayempty;
    SharedPreferences shared;
    EditText maysearchView;
    int pageId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        pageId = 1;
        mayKnow.clear();
        isLoading = false;


        View view = inflater.inflate(R.layout.fragment_may_know, container, false);


        shared = this.getActivity().getSharedPreferences("id", Context.MODE_PRIVATE);
        userId = shared.getString("id", "0");


        activity = (ConnectionsTabs) getActivity();

        maysearchView = (EditText) view.findViewById(R.id.maysearch_view);
        maylist = (ListView) view.findViewById(R.id.mayKnowlistView);
//        maylist.setTranscriptMode(1);
        mayprogress = (ProgressBar) view.findViewById(R.id.mayprogressBar);
        mayempty = (TextView) view.findViewById(R.id.maytxtEmpty);
        maylist.setEmptyView(mayempty);


        MayKnowAdapter = new AdapterMayKnow(activity, mayKnow);


        url = "http://heartgate.co/api_heartgate/connections/find_connections/" + userId + "/" + pageId;

        StringRequest productsRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
//                    JSONObject object = new JSONObject(response);
                    JSONArray usersarray = new JSONArray(response);
                    if (usersarray.length() == 0) {
                        mayprogress.setVisibility(View.INVISIBLE);
                    } else {

                        pageId++;
                        for (int i = 0; i < usersarray.length(); i++) {
                            JSONObject currentobject = usersarray.getJSONObject(i);
                            final int id = currentobject.getInt("id");
                            final String fullName = currentobject.getString("fullname");
                            final String jobTitle = currentobject.getString("speciality");
                            final String picture = currentobject.getString("image_profile");
                            final String imageUrl = "http://heartgate.co/api_heartgate/layout/images/" + picture;

//                            mayKnow.add(0,new ModelMyConnections(0, id, fullName, jobTitle, imageUrl));
                            mayKnow.add(new ModelMyConnections(0, id, fullName, jobTitle, imageUrl));
                            maylist.setAdapter(MayKnowAdapter);

                            mayprogress.setVisibility(View.INVISIBLE);

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


        maylist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (maylist.getAdapter() == null)
                    return;

                if (maylist.getAdapter().getCount() == 0)
                    return;

                int l = visibleItemCount + firstVisibleItem;
                if (l >= totalItemCount && !isLoading) {
                    // It is time to add new data. We call the listener
                    isLoading = true;
                    loadData();
                }

            }
        });


        maysearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                MayKnowAdapter.getFilter().filter(s.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void loadData() {


        mayprogress.setVisibility(View.VISIBLE);

        url = "http://heartgate.co/api_heartgate/connections/find_connections/" + userId + "/" + pageId;

        StringRequest productsRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
//                    JSONObject object = new JSONObject(response);
                    JSONArray usersarray = new JSONArray(response);

                    if (usersarray.length() == 0) {
                        isLoading = true;
                        mayprogress.setVisibility(View.INVISIBLE);
                    } else {
                        isLoading = false;
                        pageId++;
                    }


                    for (int i = 0; i < usersarray.length(); i++) {
                        JSONObject currentobject = usersarray.getJSONObject(i);
                        final int id = currentobject.getInt("id");
                        final String fullName = currentobject.getString("fullname");
                        final String jobTitle = currentobject.getString("speciality");
                        final String picture = currentobject.getString("image_profile");
                        final String imageUrl = "http://heartgate.co/api_heartgate/layout/images/" + picture;

                        mayKnow.add(new ModelMyConnections(0, id, fullName, jobTitle, imageUrl));


                        mayprogress.setVisibility(View.INVISIBLE);
                        MayKnowAdapter.notifyDataSetChanged();

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
