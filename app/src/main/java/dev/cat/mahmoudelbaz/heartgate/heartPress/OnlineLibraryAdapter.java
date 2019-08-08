package dev.cat.mahmoudelbaz.heartgate.heartPress;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.cat.mahmoudelbaz.heartgate.R;

public class OnlineLibraryAdapter  extends RecyclerView.Adapter<OnlineLibraryAdapter.MyViewHolder> {

    Context context ;
    List<onlineLibraryResponseModel> myModels ;

    public OnlineLibraryAdapter(Context context, List<onlineLibraryResponseModel> myModels) {
        this.context = context;
        this.myModels = myModels;
    }

    @NonNull
    @Override
    public OnlineLibraryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_online_library_item, parent, false);
        return new OnlineLibraryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnlineLibraryAdapter.MyViewHolder holder, final int position) {
  //      holder.libid.setText(myModels.get(position).getId());
        holder.libName.setText(myModels.get(position).getTitle());
//        holder.liblink.setText(myModels.get(position).getLink());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                WebView mWebView =new WebView(context);
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.loadUrl(myModels.get(position).getLink());
            }
        });


    }

    @Override
    public int getItemCount() {
        return myModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.libid)
        TextView libid;
        @BindView(R.id.libName)
        TextView libName;
        @BindView(R.id.liblink)
        TextView liblink;

        @BindView(R.id.layout)
        LinearLayout layout ;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
