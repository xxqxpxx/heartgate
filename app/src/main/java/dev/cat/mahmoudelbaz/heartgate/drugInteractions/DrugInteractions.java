package dev.cat.mahmoudelbaz.heartgate.drugInteractions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import dev.cat.mahmoudelbaz.heartgate.R;
import dev.cat.mahmoudelbaz.heartgate.WebViewer;

public class DrugInteractions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_interactions);

        Intent i = new Intent(this, WebViewer.class);
        i.putExtra("url", "ddi.html");
        startActivity(i);
        finish();
    }
}
