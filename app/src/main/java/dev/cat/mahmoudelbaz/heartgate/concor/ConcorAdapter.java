package dev.cat.mahmoudelbaz.heartgate.concor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.cat.mahmoudelbaz.heartgate.R;

public class ConcorAdapter extends RecyclerView.Adapter<ConcorAdapter.MyViewHolder> {


    Context context;
    List<MyModel> cornrPriceModels ;

    public ConcorAdapter(Context context, List<MyModel> cornrPriceModels) {
        this.context = context;
        this.cornrPriceModels = cornrPriceModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cornow_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txName.setText(cornrPriceModels.get(position).getName());
        holder.txPrice.setText(cornrPriceModels.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return cornrPriceModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txName)
        TextView txName;
        @BindView(R.id.txPrice)
        TextView txPrice;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

