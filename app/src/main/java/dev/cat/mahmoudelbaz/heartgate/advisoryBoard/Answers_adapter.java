package dev.cat.mahmoudelbaz.heartgate.advisoryBoard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import dev.cat.mahmoudelbaz.heartgate.R;

/**
 * Created by mahmoudelbaz on 4/9/18.
 */

public class Answers_adapter extends BaseAdapter {

    private final Context context;
    private ArrayList<Answers_item> answersItems;
    private ArrayList<Answers_item> tempItems;

    public Answers_adapter(Context cx, ArrayList<Answers_item> feedItems) {
        this.context = cx;
        this.answersItems = feedItems;
        this.tempItems = feedItems;
    }


    @Override
    public int getCount() {
        return answersItems.size();
    }

    @Override
    public Answers_item getItem(int i) {
        return answersItems.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Answers_adapter.ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater li = LayoutInflater.from(context);
            convertView = li.inflate(R.layout.answer_list_item, parent, false);
            viewHolder = new Answers_adapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (Answers_adapter.ViewHolder) convertView.getTag();
        }

        Answers_item Item = getItem(position);
        if (viewHolder != null) {
            viewHolder.setItem(Item);
        }
        return convertView;
    }


    class ViewHolder {

        private TextView contentView, dateView, userNameView;
        private ImageView profilePicView;

        public ViewHolder(View convertView) {
            contentView = (TextView) convertView.findViewById(R.id.tvContent);
            dateView = (TextView) convertView.findViewById(R.id.tvDate);
            userNameView = (TextView) convertView.findViewById(R.id.tvUserName);
            profilePicView = (ImageView) convertView.findViewById(R.id.imgProfile);

            convertView.setTag(this);
        }

        void setItem(final Answers_item item) {

            contentView.setText(item.getContent());
            dateView.setText(item.getDate());
            userNameView.setText(item.getUserName());

         //  Picasso.get().load(item.getProfilePic()).placeholder(R.drawable.progress_animation).error(R.drawable.profile).into(profilePicView);
            Picasso.with(context).load(item.getProfilePic()).placeholder(R.drawable.profile).error(R.drawable.profile).into(profilePicView);


        }

    }
}
