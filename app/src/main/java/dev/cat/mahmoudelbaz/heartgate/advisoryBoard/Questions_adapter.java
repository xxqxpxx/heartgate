package dev.cat.mahmoudelbaz.heartgate.advisoryBoard;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import dev.cat.mahmoudelbaz.heartgate.R;

/**
 * Created by mahmoudelbaz on 4/9/18.
 */

public class Questions_adapter extends  RecyclerView.Adapter<Questions_adapter.ViewHolder>  {

    private final Context context;
    private ArrayList<Questions_item> questionsItems;
    private ArrayList<Questions_item> tempItems;

    public Questions_adapter(Context cx, ArrayList<Questions_item> feedItems) {
        this.context = cx;
        this.questionsItems = feedItems;
        this.tempItems = feedItems;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return questionsItems.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Questions_adapter.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.question_list_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//            sectionRef.setText(Html.fromHtml(ref));
        holder.titleView.setText(questionsItems.get(position).getQ_title());
        holder.dateView.setText(questionsItems.get(position).getCreation_date());

        holder.baseholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, QuestionDetails.class);
            //    myIntent.putExtra("key", value); //Optional parameters
                context.startActivity(myIntent);
            }
        });

     //   holder.userNameView.setText(questionsItems.get(position).getu());

        /*if (!questionsItems.get(position).p().equals("")) {
            holder.profilePicView.setVisibility(View.VISIBLE);
            //     Picasso.get().load(item.getProfilePic()).placeholder(R.drawable.progress_animation).error(R.drawable.profile).into(profilePicView);
        } else {
            holder.profilePicView.setVisibility(View.GONE);
        }
*/
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder   {

        private TextView titleView , dateView, userNameView, likesCommentsNoView;
        private ImageView profilePicView;
        private RelativeLayout baseholder;

        public ViewHolder(View convertView) {
            super(convertView);
            titleView = (TextView) convertView.findViewById(R.id.tvTitle);
            dateView = (TextView) convertView.findViewById(R.id.tvDate);
            userNameView = (TextView) convertView.findViewById(R.id.tvUserName);
            profilePicView = (ImageView) convertView.findViewById(R.id.imgProfile);
            baseholder = convertView.findViewById(R.id.baseholder);
            convertView.setTag(this);
        }
    }
}
