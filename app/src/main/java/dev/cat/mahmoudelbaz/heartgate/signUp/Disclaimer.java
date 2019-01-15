package dev.cat.mahmoudelbaz.heartgate.signUp;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import dev.cat.mahmoudelbaz.heartgate.R;

public class Disclaimer extends AppCompatActivity {

    WebView myWebView;
    Button agree, disagree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);

        myWebView = (WebView) findViewById(R.id.webDisclaimer);
        agree = (Button) findViewById(R.id.btnAgree);
        disagree = (Button) findViewById(R.id.btnDisagree);

        WebSettings websettings = myWebView.getSettings();

        websettings.setBuiltInZoomControls(true);
        websettings.setDisplayZoomControls(false);
        websettings.setLoadWithOverviewMode(true);
//        myWebView.setInitialScale(100);
        websettings.setJavaScriptEnabled(true);
        websettings.setSupportMultipleWindows(true);
        websettings.setJavaScriptCanOpenWindowsAutomatically(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            websettings.setMediaPlaybackRequiresUserGesture(false);
        }
        myWebView.requestFocusFromTouch();

        final Activity activity = this;
        myWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(android.webkit.WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                activity.setProgress(progress * 1000);
            }
        });
        myWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(android.webkit.WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });


        myWebView.loadUrl("file:///android_asset/disclaimers.html");


        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Disclaimer.this, SignUp.class);
                startActivity(i);
                finish();
            }
        });

        disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
