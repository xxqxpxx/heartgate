package dev.cat.mahmoudelbaz.heartgate.home;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import dev.cat.mahmoudelbaz.heartgate.R;

/**
 * Created by mahmoudelbaz on 5/17/17.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<Menu_item> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<Menu_item, List<Child_item>> _listDataChild;
    boolean isHTML;

    public ExpandableListAdapter(Context context, List<Menu_item> listDataHeader,
                                 HashMap<Menu_item, List<Child_item>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {



        Child_item child_item = (Child_item) getChild(groupPosition, childPosition);


        Pattern htmlPattern = Pattern.compile(".*\\<[^>]+>.*", Pattern.DOTALL);
        isHTML = htmlPattern.matcher(child_item.getNameId()).matches();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        View colorchild = (View) convertView.findViewById(R.id.listcolor);
        WebView webView =  convertView.findViewById(R.id.webview);



        if (!isHTML)
            txtListChild.setText(child_item.getNameId());
        else {
            webView.setVisibility(View.VISIBLE);
            txtListChild.setVisibility(View.GONE);
            String str = child_item.getNameId();
            webView.loadDataWithBaseURL(null, str, "text/html", "utf-8", null);
        }
           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
              //  txtListChild.setText(child_item.getHtml());
                txtListChild.setText(Html.fromHtml(child_item.getNameId(), Html.FROM_HTML_MODE_COMPACT));

            } else {
                txtListChild.setText(Html.fromHtml(child_item.getNameId()));
            }*/


        colorchild.setBackground(ContextCompat.getDrawable(_context, child_item.getBackgroundColorResId()));
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        if (!isHTML)
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();

        else
            return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Menu_item menuItem = (Menu_item) getGroup(groupPosition);


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }


        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setText(menuItem.getName());

        ImageView img = (ImageView) convertView
                .findViewById(R.id.imgListHeader);
        img.setImageResource(menuItem.getIconResId());

        View menu_holder = convertView.findViewById(R.id.menu_holder);

//        ColorDrawable r = new ColorDrawable(_context.getResources().getColor(menuItem.getBackgroundColorResId()));
//        r.setAlpha(200);


        menu_holder.setBackground(ContextCompat.getDrawable(_context, menuItem.getBackgroundColorResId()));


        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
