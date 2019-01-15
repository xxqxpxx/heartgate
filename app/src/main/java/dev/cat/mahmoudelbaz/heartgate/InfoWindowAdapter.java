package dev.cat.mahmoudelbaz.heartgate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import dev.cat.mahmoudelbaz.heartgate.myAccount.ModelMyConnections;

/**
 * Created by mahmoudelbaz on 10/26/17.
 */

public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    //    private final View myContentsView;
    private final Context context;
    String name, title, pic;
    private ModelMyConnections item;

    InfoWindowAdapter(Context cx, String name, String title, String pic) {
        this.context = cx;
        this.name = name;
        this.title = title;
        this.pic = pic;
        this.item = item;
    }

    @Override
    public View getInfoContents(Marker marker) {

        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myContentsView = li.inflate(R.layout.custom_info_contents, null);

        TextView tvTitle = ((TextView) myContentsView.findViewById(R.id.title));
        TextView tvSnippet = ((TextView) myContentsView.findViewById(R.id.snippet));
        ImageView ivIcon = ((ImageView) myContentsView.findViewById(R.id.icon));

        tvTitle.setText(name);
        tvSnippet.setText(title);
        Picasso.with(context).load(pic).placeholder(R.drawable.profile).error(R.drawable.profile).into(ivIcon);

        return myContentsView;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        // TODO Auto-generated method stub
        return null;
    }
}