package dev.cat.mahmoudelbaz.heartgate.medicalStatistics;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import dev.cat.mahmoudelbaz.heartgate.R;
import dev.cat.mahmoudelbaz.heartgate.WebViewer;

public class CardioRiskFactor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardio_risk_factor);

        Intent i = new Intent(this, WebViewer.class);
        i.putExtra("url", "cardio_risk.html");
        startActivity(i);
        finish();

    }
}
