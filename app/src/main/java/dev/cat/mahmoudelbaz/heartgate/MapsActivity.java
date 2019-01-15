package dev.cat.mahmoudelbaz.heartgate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static dev.cat.mahmoudelbaz.heartgate.R.id.map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    final int RQS_GooglePlayServices = 1;


    //Activity  Code
    TextView tvLocInfo;
    SharedPreferences shared;
    ImageView back;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private ViewGroup infoWindow;
    private TextView infoTitle;
    private TextView infoSnippet;
    private Button infoButton;
    private OnInfoWindowElemTouchListener infoButtonListener;

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);

//        tvLocInfo = (TextView)findViewById(R.id.locinfo);

//        final MapWrapperLayout mapWrapperLayout = (MapWrapperLayout)findViewById(R.id.map_relative_layout);

        mapFragment.getMapAsync(this);


        back = (ImageView) findViewById(R.id.bck);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        shared = getSharedPreferences("id", Context.MODE_PRIVATE);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS is Enabled in your device", Toast.LENGTH_SHORT).show();
        } else {
            showGPSDisabledAlertToUser();
        }


        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        // Add a marker in Sydney and move the camera
//        MarkerOptions markerOption = new MarkerOptions();
//        LatLng place = new LatLng(30.586771, 31.5164356);
//        markerOption.position(place);
//        markerOption.snippet("snippet");
//        markerOption.title("Zagazig");
////        markerOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
//        mMap.addMarker(markerOption);
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place, 12));
//
//
//        MarkerOptions markerOption2 = new MarkerOptions();
//        LatLng place2 = new LatLng(31.586771, 31.5164356);
//        markerOption2.position(place2);
//        markerOption2.snippet("snippet");
//        markerOption2.title("teeeet teeet ");
////        markerOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
//        mMap.addMarker(markerOption2);
////        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place2,12));
//
//
////        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
////        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
////        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

//        mMap.setInfoWindowAdapter(new InfoWindowAdapter(this));

        String myUserID = shared.getString("id", "0");

        String finalUrl = "http://hg.api.digitalcatsite.com/users/nearby/" + myUserID + "/0";
        final StringRequest postsRequest = new StringRequest(
                Request.Method.GET, finalUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray object = new JSONArray(response);

                    for (int i = 0; i < object.length(); i++) {

                        JSONObject current = object.getJSONObject(i);

                        String lat = current.getString("lat");
                        String lng = current.getString("lng");
                        String nme = current.getString("fullname");
                        String speciality = current.getString("speciality");
                        String pic = "http://assets.hg.api.digitalcatsite.com/" + current.getString("image_profile");

                        if (!(lat.equals("") || lng.equals(""))) {
                            LatLng l = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
//                        mMap.setInfoWindowAdapter(new InfoWindowAdapter(MapsActivity.this,nme,speciality,pic));
                            mMap.addMarker(new MarkerOptions().position(l).title(nme).snippet(speciality));
                        }

                    }
                    Log.d("respone", response.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("response", error.toString());

                    }
                }
        );

        Volley.newRequestQueue(this).add(postsRequest);


//
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
//                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
//        } else {
//
//
//            Location userCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//            if (userCurrentLocation != null) {
//
//                double myLatitude = userCurrentLocation.getLatitude();
//                double myLongitude = userCurrentLocation.getLongitude();
//
//
////                String myUserID = shared.getString("id", "0");
//
//
//                String myurl = "http://hg.api.digitalcatsite.com/users/location";
//
//                JSONObject jsobj = new JSONObject();
//                try {
//
//                    jsobj.put("user_id", myUserID);
//                    jsobj.put("lng", myLongitude);
//                    jsobj.put("lat", myLatitude);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                if (!(myUserID == "0")) {
//
//                    JsonObjectRequest postrequest = new JsonObjectRequest(Request.Method.POST, myurl, jsobj, new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//
////                            Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
//                            Log.d("TTT", "onResponse: " + response.toString());
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//
//                            Log.d("TTT", "onResponse: " + error.toString());
////                    Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
//
//
//                    Volley.newRequestQueue(MapsActivity.this).add(postrequest);
//                }
//            }
//        }


    }


    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }


    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            Location userCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (userCurrentLocation != null) {
                MarkerOptions currentUserLocation = new MarkerOptions();
                LatLng currentUserLatLang = new LatLng(userCurrentLocation.getLatitude(), userCurrentLocation.getLongitude());
                currentUserLocation.icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocation));
                currentUserLocation.position(currentUserLatLang);
                currentUserLocation.title("Current Location");
                mMap.addMarker(currentUserLocation);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUserLatLang, 9));


            }
        }

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onConnected(null);
        } else {
            Toast.makeText(MapsActivity.this, "Need Permission To Use this section", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    //Inner Adapter
    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private final View myContentsView;

        MyInfoWindowAdapter() {
            myContentsView = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            TextView tvTitle = ((TextView) myContentsView.findViewById(R.id.title));
            tvTitle.setText(marker.getTitle());

            TextView tvSnippet = ((TextView) myContentsView.findViewById(R.id.snippet));
            tvSnippet.setText(marker.getSnippet());

            ImageView ivIcon = ((ImageView) myContentsView.findViewById(R.id.icon));
            ivIcon.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_gallery));


            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }
    }
}
