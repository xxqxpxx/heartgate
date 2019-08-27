package dev.cat.mahmoudelbaz.heartgate.videos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.cat.mahmoudelbaz.heartgate.R;
import dev.cat.mahmoudelbaz.heartgate.myAccount.ModelMyConnections;

import static com.google.android.gms.cast.framework.media.MediaUtils.getImageUri;
import static dev.cat.mahmoudelbaz.heartgate.webServices.Services.MAIN_VIDEOS_URL;

public class videoAdapter extends RecyclerView.Adapter<videoAdapter.MyViewHolder>  implements Filterable {

    Context context ;
    List<VideoResponseModel> cardioUpdatesResponseModels;
    VideoRequestHandler videoRequestHandler;
    Picasso picassoInstance;
    private List<VideoResponseModel> feedItems;
    private List<VideoResponseModel> tempItems;

    SharedPreferences shared;
    String userID;

    public videoAdapter(Context context, List<VideoResponseModel> cardioUpdatesResponseModels) {
        this.context = context;
        this.cardioUpdatesResponseModels = cardioUpdatesResponseModels;
        this.feedItems = cardioUpdatesResponseModels;
        this.tempItems = cardioUpdatesResponseModels;
    }

    @NonNull
    @Override
    public videoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_video_item, parent, false);


        shared = context.getSharedPreferences("id", Context.MODE_PRIVATE);
        userID = shared.getString("id", "0");

        return new videoAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull videoAdapter.MyViewHolder holder, final int position) {
        //      holder.libid.setText(cardioUpdatesResponseModels.get(position).getId());
        holder.libName.setText(cardioUpdatesResponseModels.get(position).getVideo_name());
        holder.desc.setText(cardioUpdatesResponseModels.get(position).getVideo_description());

        videoRequestHandler = new VideoRequestHandler();
        picassoInstance = new Picasso.Builder(context)
                .addRequestHandler(videoRequestHandler)
                .build();

  /*     try {
            picassoInstance
                    .load(getImageUri(context , retriveVideoFrameFromVideo( MAIN_VIDEOS_URL + cardioUpdatesResponseModels.get(position).getVideo_url())))
                    .into(holder.thumbnail);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
*/

        holder.layout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

             /*   Intent intent = new Intent(context, VideoDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("getVideo_url" , cardioUpdatesResponseModels.get(position).getVideo_url() );
                context.startActivity(intent);*/

                String uriPath = MAIN_VIDEOS_URL + cardioUpdatesResponseModels.get(position).getVideo_url()  ;


                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriPath));
                intent.setDataAndType(Uri.parse(uriPath), "video/mp4");
                context.startActivity(intent);

            }
        });


    }


    @Override
    public int getItemCount() {
        return cardioUpdatesResponseModels.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                feedItems = (ArrayList<VideoResponseModel>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    results.values = tempItems;
                    results.count = tempItems.size();
                } else {
                    ArrayList<VideoResponseModel> nProductsList = new ArrayList<>();

                    for (VideoResponseModel p : tempItems) {
                        if (p.getVideo_description().trim().contains(constraint.toString().trim()))
                            nProductsList.add(p);
                    }

                    results.values = nProductsList;
                    results.count = nProductsList.size();

                }
                return results;
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.thumbnail)
        ImageView thumbnail;
        @BindView(R.id.libName)
        TextView libName;
        @BindView(R.id.liblink)
        TextView desc;

        @BindView(R.id.layout)
        LinearLayout layout ;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


}


