package dev.cat.mahmoudelbaz.heartgate.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import dev.cat.mahmoudelbaz.heartgate.R;

public class ChatBoxAdapter  extends RecyclerView.Adapter<ChatBoxAdapter.MyViewHolder> {
    private List<Message> MessageList;
    Context context;
    public  class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nickname;
        public TextView message;
        public TextView date;
        public ImageView pic;

        public MyViewHolder(View view) {
            super(view);
            nickname = (TextView) view.findViewById(R.id.nickname);
            message = (TextView) view.findViewById(R.id.message);
            date= (TextView) view.findViewById(R.id.text_message_time);
            pic = view.findViewById(R.id.image_message_profile);
        }
    }

// in this adaper constructor we add the list of messages as a parameter so that
// we will passe  it when making an instance of the adapter object in our activity

    public ChatBoxAdapter(List<Message> MessagesList , Context context) {
        this.MessageList = MessagesList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return MessageList.size();
    }
    @Override
    public ChatBoxAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new ChatBoxAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ChatBoxAdapter.MyViewHolder holder, final int position) {
        //binding the data from our ArrayList of object to the item.xml using the viewholder
        Message m = MessageList.get(position);
        holder.nickname.setText(m.getNickname());
        holder.message.setText(m.getMessage() );
        holder.date.setText(m.getDate());



        if (m.getImageUrl() == null) {
          //  imageView.setImageResource(R.drawable.profile);
            Picasso.with(context).load(m.getImageUrl()).placeholder(R.drawable.profile).error(R.drawable.profile).into(  holder.pic);
        }
        else
          Picasso.with(context).load(m.getImageUrl()).placeholder(R.drawable.profile).error(R.drawable.profile).into(  holder.pic);

    }

}
