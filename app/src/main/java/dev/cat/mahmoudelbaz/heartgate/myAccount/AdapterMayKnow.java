package dev.cat.mahmoudelbaz.heartgate.myAccount;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dev.cat.mahmoudelbaz.heartgate.R;

/**
 * Created by mahmoudelbaz on 9/18/17.
 */

public class AdapterMayKnow extends BaseAdapter implements Filterable {

    private final Context context;
    SharedPreferences shared;
    String userID, userName, url;
    private ArrayList<ModelMyConnections> feedItems;
    private ArrayList<ModelMyConnections> tempItems;

    public AdapterMayKnow(Context cx, ArrayList<ModelMyConnections> feedItems) {
        this.context = cx;
        this.feedItems = feedItems;
        this.tempItems = feedItems;
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public ModelMyConnections getItem(int position) {
        return feedItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AdapterMayKnow.ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.connections_peolplemayknow_item, parent, false);
            viewHolder = new AdapterMayKnow.ViewHolder(convertView);
        } else {
            viewHolder = (AdapterMayKnow.ViewHolder) convertView.getTag();
        }

        ModelMyConnections feedItem = getItem(position);
        if (viewHolder != null) {
            viewHolder.setItem(feedItem);
        }

        return convertView;
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                feedItems = (ArrayList<ModelMyConnections>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    results.values = tempItems;
                    results.count = tempItems.size();
                } else {
                    ArrayList<ModelMyConnections> nProductsList = new ArrayList<>();

                    for (ModelMyConnections p : tempItems) {
                        if (p.getName().trim().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                            nProductsList.add(p);
                    }

                    results.values = nProductsList;
                    results.count = nProductsList.size();

                }
                return results;
            }
        };
    }


    class ViewHolder {

        private TextView nameView;
        private TextView jobTitleView;
        private ImageView imageView;
        private Button connectbtn;


        public ViewHolder(View convertView) {
            nameView = (TextView) convertView.findViewById(R.id.txtName);
            jobTitleView = (TextView) convertView.findViewById(R.id.txtTitle);
            imageView = (ImageView) convertView.findViewById(R.id.imgProfile);
            connectbtn = (Button) convertView.findViewById(R.id.btnConnect);
            convertView.setTag(this);
        }

        void setItem(final ModelMyConnections product) {

            shared = context.getSharedPreferences("id", Context.MODE_PRIVATE);

            connectbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userID = shared.getString("id", "0");
                    userName = shared.getString("Name", "0");
                    int receiveId = product.getId();
                    String receiveIdString = Integer.toString(receiveId);


                    url = "http://heartgate.co/api_heartgate/messages/connectuser/add";

                    JSONObject jsobj = new JSONObject();
                    try {
                        jsobj.put("fk_userid_send", userID);
                        jsobj.put("fk_user_id_received", receiveIdString);
                        jsobj.put("create_user_id", userName);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    JsonObjectRequest postrequest = new JsonObjectRequest(Request.Method.POST, url, jsobj, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            feedItems.remove(product);
                            notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                            Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();

                        }
                    });


                    Volley.newRequestQueue(context).add(postrequest);


                }
            });


            nameView.setText(product.getName());
            jobTitleView.setText(product.getJobTitle());
            String url = product.getImageUrl();
            if (url == null) {
                imageView.setImageResource(R.drawable.profile);
                return;
            }

            Picasso.with(context).load(url).placeholder(R.drawable.profile).error(R.drawable.profile).into(imageView);
        }

    }

}