package dev.cat.mahmoudelbaz.heartgate;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;
import permission.auron.com.marshmallowpermissionhelper.PermissionUtils;

public class WebViewer extends ActivityManagePermission implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

//    ImageView back;

    private static final int MY_PERMISSION_REQUEST_CODE = 7171;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 7172;
    private static final String TAG = WebViewer.class.getSimpleName();
    private final static int FCR = 1;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int UPDATE_INTERVAL = 5000; // SEC
    private static int FATEST_INTERVAL = 3000; // SEC
    private static int DISPLACEMENT = 10; // METERS
    ImageView imgprofile;
    SharedPreferences shared;
    String userId, RecieverId, userNameId;
    String idcheck;
    WebView webView;
    ProgressBar progress;
    private TextView txtCoordinates;
    private Button btnGetCoordinates, btnLocationUpdates;
    private boolean mRequestingLocationUpdates = false;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPlayServices())
                        buildGoogleApiClient();
                    createLocationRequest();
                }
                break;
        }
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

            imgprofile.setImageBitmap(BitmapHelper.decodeFile(picturePath, 200, 200, true));

            Bitmap bitmap = ((BitmapDrawable) imgprofile.getDrawable()).getBitmap();
            String encoded = ImageBase64.encodeTobase64(bitmap);


            changePhoto(encoded);


        }
    }

    @SuppressLint({"SetJavaScriptEnabled", "WrongViewCast"})

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (event.getAction() == KeyEvent.ACTION_DOWN) {
//            switch (keyCode) {
//                case KeyEvent.KEYCODE_BACK:
//                    if (myWebView.canGoBack()) {
//                        myWebView.goBack();
//                    } else {
//                        finish();
//                    }
//                    return true;
//            }
//
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_viewer);

        imgprofile = (ImageView) findViewById(R.id.imgProfile);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);

//        back = (ImageView) findViewById(R.id.bck);
//
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });


//        txtCoordinates = (TextView) findViewById(R.id.txtCoordinates);
//        btnGetCoordinates = (Button) findViewById(R.id.btnGetCoordinates);
//        btnLocationUpdates = (Button) findViewById(R.id.btnTrackLocation);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Run-time request permission
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        } else {
            if (checkPlayServices()) {
                buildGoogleApiClient();
                createLocationRequest();
            }
        }

//        displayLocation();

//        btnGetCoordinates.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                displayLocation();
//            }
//        });
//
//        btnLocationUpdates.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                tooglePeriodicLoctionUpdates();
//            }
//        });


        shared = getSharedPreferences("id", Context.MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(WebViewer.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        }

        webView = (WebView) findViewById(R.id.ifView);
        assert webView != null;
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(WebViewer.this), "AndroidFunction");


        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setLoadWithOverviewMode(true);
//        myWebView.setInitialScale(100);
        webView.canGoBack();

        webSettings.setAllowFileAccess(true);
        webSettings.setSupportMultipleWindows(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
//        webSettings.getSettings().setAppCacheEnabled(true);
//        webSettings.getSettings().setSavePassword(false);

        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);


        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(0);
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT < 19) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webView.setWebViewClient(new Callback());


        if (shared.contains("id")) {
            idcheck = shared.getString("id", "0");
        } else {
            idcheck = "0";
        }

//        String html_value = "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\"><title>Lorem Ipsum</title></head><body style=\"width:300px; color: #00000; \"><p><strong> About us</strong> </p><p><strong> Lorem Ipsum</strong> is simply dummy text .</p><p><strong> Lorem Ipsum</strong> is simply dummy text </p><p><strong> Lorem Ipsum</strong> is simply dummy text </p></body></html>";
//        String html_value = "News Content Goes Here...<\\/span><\\/p>";
//
//        webView.loadData(html_value, "text/html", "UTF-8");


        if (idcheck.equals("0")) {
            webView.loadUrl("file:///android_asset/www/index.html");
        } else {
            webView.loadUrl("file:///android_asset/www/home.html");
        }


        webView.setWebChromeClient(new WebChromeClient() {
            //For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                WebViewer.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FCR);
            }

            // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                WebViewer.this.startActivityForResult(
                        Intent.createChooser(i, "File Browser"),
                        FCR);
            }

            //For Android 4.1+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                WebViewer.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), WebViewer.FCR);
            }

            //For Android 5.0+
            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    WebChromeClient.FileChooserParams fileChooserParams) {
                if (mUMA != null) {
                    mUMA.onReceiveValue(null);
                }
                mUMA = filePathCallback;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(WebViewer.this.getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCM);
                    } catch (IOException ex) {
                        Log.e(TAG, "Image file creation failed", ex);
                    }
                    if (photoFile != null) {
                        mCM = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }
                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("*/*");
                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(chooserIntent, FCR);
                return true;
            }
        });


//        if (shared.contains("id")) {
//           idcheck =  shared.getString("id", "0");
//        } else {
//            idcheck = "0";
//        }
//
//
////        myWebView.loadUrl("file:///android_asset/www/index.html");
//
//
//        if (idcheck.equals("0")) {
//            myWebView.loadUrl("file:///android_asset/www/index.html");
//        } else {
//            myWebView.loadUrl("file:///android_asset/www/home.html");
//        }

    }

    // Create an image file
    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
//        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void tooglePeriodicLoctionUpdates() {
        if (!mRequestingLocationUpdates) {
            btnLocationUpdates.setText("Stop location update");
            mRequestingLocationUpdates = true;
            startLocationUpdates();
        } else {
            btnLocationUpdates.setText("Start location update");
            mRequestingLocationUpdates = false;
            stopLocationUpdates();
        }
    }

    private void displayLocation(double longitude, double latitude) {

        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        status.getPermissionStatus().getEnabled();

        String playerId = status.getSubscriptionStatus().getUserId();

        String myUserID = shared.getString("id", "0");


        String myurl = "http://hg.api.digitalcatsite.com/users/location";

        JSONObject jsobj = new JSONObject();
        try {

            jsobj.put("user_id", myUserID);
            jsobj.put("lng", longitude);
            jsobj.put("lat", latitude);
            jsobj.put("player_id", playerId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!(myUserID == "0")) {

            JsonObjectRequest postrequest = new JsonObjectRequest(Request.Method.POST, myurl, jsobj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

//                            Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: " + response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


//                    Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();

                }
            });


            Volley.newRequestQueue(WebViewer.this).add(postrequest);

        } else {
        }

    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);

    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        //Fix first time run app if permission doesn't grant yet so can't get anything
        mGoogleApiClient.connect();


    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), "This device is not supported", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();

            Log.d("onConnected: ", "Lat = " + latitude + "\nLong = " + longitude);

            displayLocation(latitude, longitude);
        }
        if (mRequestingLocationUpdates)
            startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();

            Log.d("onConnected: ", "Lat = " + latitude + "\nLong = " + longitude);

            displayLocation(latitude, longitude);
        }
    }

    private void changePhoto(String encoded) {


        progress.setVisibility(View.VISIBLE);
        String signUpUrl = "http://hg.api.digitalcatsite.com/users/update_imageprofile_ios/" + shared.getString("id", "0");


        final JSONObject jsobj = new JSONObject();
        try {
            jsobj.put("image_profile", encoded);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest postrequest = new JsonObjectRequest(Request.Method.POST, signUpUrl, jsobj, new Response.Listener<JSONObject>() {
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
                    Toast.makeText(WebViewer.this, message, Toast.LENGTH_SHORT).show();
                    webView.reload();
//                                Log.d("sent data", jsobj.toString());
                } else if (state == 1) {
                    Toast.makeText(WebViewer.this, message, Toast.LENGTH_SHORT).show();
                    webView.reload();
//                                Toast.makeText(SignUp.this, response.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progress.setVisibility(View.INVISIBLE);


                Log.d("Error", error.toString());

                Toast.makeText(WebViewer.this, "Network Error", Toast.LENGTH_SHORT).show();

            }
        });

        Volley.newRequestQueue(WebViewer.this).add(postrequest);


    }

    private void webChangePhoto() {
        boolean isGranted = isPermissionsGranted(WebViewer.this, new String[]{PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE});

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
                }

                @Override
                public void permissionDenied() {
                    //permission denied
                    //replace with your action
                    Toast.makeText(WebViewer.this, "You Cannot use this ferature Granting permission", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void permissionForeverDenied() {
                    // user has check 'never ask again'
                    // you need to open setting manually
                    Toast.makeText(WebViewer.this, "Please Enable Storage Permission", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            });


        }
    }

    public class Callback extends WebViewClient {
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(getApplicationContext(), "Failed loading app!", Toast.LENGTH_SHORT).show();
        }
    }

    public class MyJavaScriptInterface {
        Context mcontext;

        MyJavaScriptInterface(Context c) {
            mcontext = c;
        }


        @JavascriptInterface
        public void changeProfile() {
            webChangePhoto();

        }

        @JavascriptInterface
        public String getUserId() {
            return shared.getString("id", "0");
//            return userId;
        }

        @JavascriptInterface
        public void setUserId(String i) {
//            userId = i;

            SharedPreferences.Editor myEdit = shared.edit();
            myEdit.putString("id", i);
            myEdit.commit();

        }

        @JavascriptInterface
        public String getRecieverId() {
            return RecieverId;
        }

        @JavascriptInterface
        public void setRecieverId(String i) {
            RecieverId = i;

        }

        @JavascriptInterface
        public String getUserName() {
            return userNameId;
        }

        @JavascriptInterface
        public void setUserName(String i) {
            userNameId = i;

        }

        @JavascriptInterface
        public void nearBy() {
            Intent i = new Intent(WebViewer.this, MapsActivity.class);
            startActivity(i);
//            return userNameId;
        }

        @JavascriptInterface
        public void callSupport() {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:+201146121111"));

            if (ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                askCompactPermissions(new String[]{PermissionUtils.Manifest_CALL_PHONE}, new PermissionResult() {
                    @Override
                    public void permissionGranted() {
                        //permission granted
                        //replace with your action
                    }

                    @Override
                    public void permissionDenied() {
                        //permission denied
                        //replace with your action
                        Toast.makeText(mcontext, "You Cannot Make Call without Granting permission", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void permissionForeverDenied() {
                        // user has check 'never ask again'
                        // you need to open setting manually
                        Toast.makeText(mcontext, "Please Enable Call Permission", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                });
                return;
            }
            startActivity(intent);
        }


    }

}



