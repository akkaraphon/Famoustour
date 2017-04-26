package com.cpc.famoustour.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import com.cpc.famoustour.R;
import com.cpc.famoustour.model.ImgUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by macbook on 4/21/17.
 */

public class ImgViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<ImgUser> mUrls;
    private LayoutInflater mInflater;
    String IMG_URL;

    public ImgViewAdapter(Context context, List<ImgUser> urls) {
        mContext = context;
        mUrls = urls;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return mUrls.size();
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

        ImgUser imgUser = mUrls.get(position);

        if (imgUser.getURL_PIC().equals("-1")) {
            imgUser.setURL_PIC("https://placeholdit.imgix.net/~text?txtsize=33&txt=NO%20IMAGE&w=200&h=200");
            IMG_URL = imgUser.getURL_PIC();

        } else {
            IMG_URL = "http://famoustour.apidech.com/picture/user/"
                    + imgUser.getID_USER()
                    + "/" + imgUser.getID_PGTOUR()
                    + "/" + imgUser.getURL_PIC();
        }

        Log.d("urlIMG", IMG_URL);

        Picasso.with(mContext).load(IMG_URL).into(viewHolder.imageButton);
        return convertView;
    }

    public class ViewHolder {
        ImageButton imageButton;
    }
}
