package dev.cat.mahmoudelbaz.heartgate.chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import dev.cat.mahmoudelbaz.heartgate.R;

import static dev.cat.mahmoudelbaz.heartgate.chat.chatActivity.socket;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {

    private List<User> MessageList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView id, name, socket_id, online, updated_at;
        LinearLayout userlayout;

        public MyViewHolder(View view) {
            super(view);
            id = view.findViewById(R.id.id);
            name = view.findViewById(R.id.name);
            socket_id = view.findViewById(R.id.socket_id);
            online = view.findViewById(R.id.online);
            updated_at = view.findViewById(R.id.updated_at);
            userlayout = view.findViewById(R.id.userlayout);
        }
    }

    UsersAdapter(List<User> MessagesList) {
        this.MessageList = MessagesList;
    }

    @Override
    public int getItemCount() {
        return MessageList.size();
    }


    @Override
    public UsersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.useritem, parent, false);
        return new UsersAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final UsersAdapter.MyViewHolder holder, final int position) {
        //binding the data from our ArrayList of object to the item.xml using the viewholder
        final User m = MessageList.get(position);
        holder.id.setText(m.getId());
        holder.name.setText(m.getName());
        holder.socket_id.setText(m.getSocket_id());
        holder.online.setText(m.getOnline());
        holder.updated_at.setText(m.getUpdated_at());

        HashMap map = new HashMap();
        map.put("fromUserId", 3);
        map.put("toUserId", m.getId());
        final JSONObject obj = new JSONObject(map);

        holder.userlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket.emit("getMessages", obj);
                chatActivity.reciver = m;
            }
        });
    }

}

