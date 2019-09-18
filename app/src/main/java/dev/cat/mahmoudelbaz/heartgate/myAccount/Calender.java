package dev.cat.mahmoudelbaz.heartgate.myAccount;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import dev.cat.mahmoudelbaz.heartgate.R;

public class Calender extends AppCompatActivity {

    TextView myempty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        myempty = findViewById(R.id.mytxtEmpty);

    }
}
