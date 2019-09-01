package dev.cat.mahmoudelbaz.heartgate.poll;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dev.cat.mahmoudelbaz.heartgate.R;
 import dev.cat.mahmoudelbaz.heartgate.heartPress.heartPressAdapter;
import dev.cat.mahmoudelbaz.heartgate.webServices.Webservice;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ahmed Abdul Fatah on 9/1/19.
 */
public class SurveyAdapter extends BaseAdapter implements Filterable {

    Context context ;
    List<SurveryResponseModel> cardioUpdatesResponseModels;

    private List<SurveryResponseModel> feedItems;
    private List<SurveryResponseModel> tempItems;

    SharedPreferences shared;
    String userID;


    public SurveyAdapter(Context context, List<SurveryResponseModel> cardioUpdatesResponseModels) {
        this.context = context;
        this.cardioUpdatesResponseModels = cardioUpdatesResponseModels;

        this.feedItems = cardioUpdatesResponseModels;
        this.tempItems = cardioUpdatesResponseModels;
    }




    private void markAsread(String userID, String id) {
        Webservice.getInstance().getApi().markAsRead(userID , id).enqueue(new Callback< Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (!response.isSuccessful()) {

                } else {
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("login", "onFailure: ", t);
            }
        });

    }


    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                feedItems = (List<SurveryResponseModel>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    results.values = tempItems;
                    results.count = tempItems.size();
                } else {
                    List<SurveryResponseModel> nProductsList = new ArrayList<>();

                    for (SurveryResponseModel p : tempItems) {
                        if (p.getSurvey_name().trim().toUpperCase().contains(constraint.toString().toUpperCase()))
                            nProductsList.add(p);
                    }

                    results.values = nProductsList;
                    results.count = nProductsList.size();

                }
                return results;
            }
        };
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public SurveryResponseModel getItem(int position)  {
        return feedItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        shared = context.getSharedPreferences("id", Context.MODE_PRIVATE);
        userID = shared.getString("id", "0");


        SurveyAdapter.MyViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.row_heart_press, parent, false);
            viewHolder = new SurveyAdapter.MyViewHolder(convertView);
        } else {
            viewHolder = (SurveyAdapter.MyViewHolder) convertView.getTag();
        }

        SurveryResponseModel feedItem = getItem(position);
        if (viewHolder != null) {
            viewHolder.setItem(feedItem);
        }

        return convertView;
    }



    public class MyViewHolder {
        //  @BindView(R.id.libid)
        TextView libid;
        //@BindView(R.id.libName)
        TextView libName;
        //@BindView(R.id.liblink)
        TextView liblink;

        // @BindView(R.id.layout)
        LinearLayout layout ;

        public MyViewHolder(View itemView) {
            libName = (TextView) itemView.findViewById(R.id.libName);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
            itemView.setTag(this);
//            ButterKnife.bind(this,itemView);
        }

        void setItem(final SurveryResponseModel product) {
            shared = context.getSharedPreferences("id", Context.MODE_PRIVATE);
            libName.setText(product.getSurvey_name());

     /*       layout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    markAsread(userID , product.getId());
                    WebView mWebView =new WebView(context);
                    mWebView.getSettings().setJavaScriptEnabled(true);
                    mWebView.loadUrl(product.getId());
                }
            });*/


/*

            //      holder.libid.setText(cardioUpdatesResponseModels.get(position).getId());
            holder.libName.setText(cardioUpdatesResponseModels.get(position).getTitle());
            //      holder.liblink.setText(cardioUpdatesResponseModels.get(position).getLink());


            holder.layout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    markAsread(userID , cardioUpdatesResponseModels.get(position).getId());
                    WebView mWebView =new WebView(context);
                    mWebView.getSettings().setJavaScriptEnabled(true);
                    mWebView.loadUrl(cardioUpdatesResponseModels.get(position).getLink());
                }
            });

*/


        }

    }

}
