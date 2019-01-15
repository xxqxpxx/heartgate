package dev.cat.mahmoudelbaz.heartgate.game.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dev.cat.mahmoudelbaz.heartgate.R;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelTopPlayer;


public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.MyViewHolder> {


    Context context;
    ResultModelTopPlayer resultModelUserRequests;
    String userId;


    Handler handler;
    ProgressDialog progress;

    public PlayerAdapter(ResultModelTopPlayer resultModelUserRequests, Context context) {
        this.context = context;
        this.resultModelUserRequests = resultModelUserRequests;
    }

    @NonNull
    @Override
    public PlayerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.custom_players_layout, parent, false);
        return new PlayerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerAdapter.MyViewHolder holder, final int position) {

        final int pos = position;
        holder.txt_name.setText(resultModelUserRequests.getData().get(position).getUsername());
        holder.txt_rank.setText(String.valueOf(pos + 1));
        holder.txt_animals_count.setText(resultModelUserRequests.getData().get(position).getAnimals());
        holder.txt_food_count.setText(resultModelUserRequests.getData().get(position).getFood());
        holder.txt_medicine_count.setText(resultModelUserRequests.getData().get(position).getDrug());
        holder.txt_money_count.setText(resultModelUserRequests.getData().get(position).getGold());
    }


    @Override
    public int getItemCount() {
        return resultModelUserRequests.getData().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_name, txt_rank, txt_money_count, txt_food_count, txt_medicine_count, txt_animals_count;


        public MyViewHolder(View itemView) {
            super(itemView);

            txt_name = itemView.findViewById(R.id.txt_name);
            txt_rank = itemView.findViewById(R.id.txt_rank);
            txt_money_count = itemView.findViewById(R.id.txt_money_count);
            txt_food_count = itemView.findViewById(R.id.txt_food_count);
            txt_medicine_count = itemView.findViewById(R.id.txt_medicine_count);
            txt_animals_count = itemView.findViewById(R.id.txt_animals_count);

        }
    }
}
