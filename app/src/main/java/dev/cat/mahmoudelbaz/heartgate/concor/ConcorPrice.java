package dev.cat.mahmoudelbaz.heartgate.concor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.cat.mahmoudelbaz.heartgate.R;

public class ConcorPrice extends AppCompatActivity {

    @BindView(R.id.Recycle_view_price_cornar)
    RecyclerView RecycleViewPriceCornar;
    List<MyModel> cornrPriceModels = new ArrayList<MyModel>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concor_price);
        ButterKnife.bind(this);
        RecycleViewPriceCornar.setHasFixedSize(true);
        RecycleViewPriceCornar.setLayoutManager(new LinearLayoutManager(ConcorPrice.this));

        MyModel myModel = new MyModel("aziz","1290");
        cornrPriceModels.add(myModel);
        RecycleViewPriceCornar.setAdapter(new ConcorAdapter(ConcorPrice.this,cornrPriceModels));

    }
}



