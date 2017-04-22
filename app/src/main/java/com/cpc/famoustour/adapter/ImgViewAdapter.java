package com.cpc.famoustour.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import com.cpc.famoustour.R;
import com.squareup.picasso.Picasso;

/**
 * Created by macbook on 4/21/17.
 */

public class ImgViewAdapter extends BaseAdapter {
    private Context mContext;
    private String[] mUrls;
    private LayoutInflater mInflater;

    public ImgViewAdapter(Context context, String[] urls) {
        mContext = context;
        mUrls = urls;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return mUrls.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_img_view, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.imageButton = (ImageButton)
                    convertView.findViewById(R.id.grid_item);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(mContext).load(mUrls[position]).into(viewHolder.imageButton);
        return convertView;
    }

    public class ViewHolder {
        ImageButton imageButton;
    }
}
