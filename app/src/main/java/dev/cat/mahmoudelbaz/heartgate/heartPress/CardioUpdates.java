package dev.cat.mahmoudelbaz.heartgate.heartPress;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.cat.mahmoudelbaz.heartgate.R;
import dev.cat.mahmoudelbaz.heartgate.concor.ConcorAdapter;
import dev.cat.mahmoudelbaz.heartgate.concor.ConcorPrice;

public class CardioUpdates extends AppCompatActivity {

    @BindView(R.id.Recycle_view_cardoivascular)
    RecyclerView RecycleViewCardoivascular;

    List<MyModel> myModels = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardio_updates);
        ButterKnife.bind(this);

        RecycleViewCardoivascular.setHasFixedSize(true);
        RecycleViewCardoivascular.setLayoutManager(new LinearLayoutManager(CardioUpdates.this));
        MyModel myModel = new MyModel("aziz","1290");
        myModels.add(myModel);
        RecycleViewCardoivascular.setAdapter(new heartPressAdapter(CardioUpdates.this,myModels));
    }
}
