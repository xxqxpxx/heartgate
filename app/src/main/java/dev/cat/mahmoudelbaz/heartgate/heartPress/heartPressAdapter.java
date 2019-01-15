package dev.cat.mahmoudelbaz.heartgate.heartPress;

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

public class heartPressAdapter extends RecyclerView.Adapter<heartPressAdapter.MyViewHolder> {

    Context context ;
    List<MyModel> myModels ;

    public heartPressAdapter(Context context, List<MyModel> myModels) {
        this.context = context;
        this.myModels = myModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_heart_press, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txName.setText(myModels.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return myModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txName)
        TextView txName;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

