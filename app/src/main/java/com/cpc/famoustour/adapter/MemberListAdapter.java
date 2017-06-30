package com.cpc.famoustour.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpc.famoustour.R;
import com.cpc.famoustour.model.GPS;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.cpc.famoustour.model.StaticClass.ID_USER_LIST;
import static com.cpc.famoustour.model.StaticClass.NAME_LIST;
import static com.cpc.famoustour.model.StaticClass.STATUS_LIST;
import static com.cpc.famoustour.model.StaticClass.TEL_LIST;
import static com.cpc.famoustour.model.StaticClass.TYPE_USER;

/**
 * Created by macbook on 6/25/17.
 */

public class MemberListAdapter extends BaseAdapter {
    public List<GPS> _gps;
    Context mContext;
    private ViewHolder mViewHolder;

    public MemberListAdapter(Context context, List<GPS> gps) {
        this.mContext = context;
        _gps = gps;
    }

    @Override
    public int getCount() {
        return _gps.size();
    }

    @Override
    public Object getItem(int position) {
        return _gps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        TextView name;
        LinearLayout statusBar;
        TextView detail;
        TextView status;
        ImageView status_photo;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_member_detail, parent, false);
            mViewHolder = new MemberListAdapter.ViewHolder();

            mViewHolder.statusBar = (LinearLayout) convertView.findViewById(R.id.statusBar);
            mViewHolder.name = (TextView) convertView.findViewById(R.id.name);
            mViewHolder.status = (TextView) convertView.findViewById(R.id.status);
            mViewHolder.detail = (TextView) convertView.findViewById(R.id.detail);
            mViewHolder.status_photo = (ImageView) convertView.findViewById(R.id.status_photo);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MemberListAdapter.ViewHolder) convertView.getTag();
        }

        GPS gps = _gps.get(position);
        if(gps.getNAME().equals("-1")){
            mViewHolder.statusBar.setVisibility(View.INVISIBLE);
            mViewHolder.name.setText("ไม่มีรายการทัวร์");
        }else{
            if(TYPE_USER.equals("M")){
                mViewHolder.statusBar.setVisibility(View.INVISIBLE);
                mViewHolder.name.setText(gps.getNAME());
                mViewHolder.detail.setText(gps.getTEL_USER());
            }else{
                mViewHolder.statusBar.setVisibility(View.VISIBLE);
                mViewHolder.name.setText(gps.getNAME());
                if(gps.getSTATUS_GPS() == 1){
                    mViewHolder.status.setText("ปกติ");
                    Picasso.with(mContext).load(R.drawable.icon_success).into(mViewHolder.status_photo);
                }else if(gps.getSTATUS_GPS() == 0){
                    mViewHolder.status.setText("หลงทาง");
                    Picasso.with(mContext).load(R.drawable.icon_lost).into(mViewHolder.status_photo);
                }else{
                    mViewHolder.status.setText("ขอความช่วยเหลือ");
                    Picasso.with(mContext).load(R.drawable.icon_help).into(mViewHolder.status_photo);
                }
                mViewHolder.detail.setText(gps.getTEL_USER());
            }
            NAME_LIST.add(gps.getNAME());
            TEL_LIST.add(gps.getTEL_USER());
            STATUS_LIST.add(String.valueOf(gps.getSTATUS_GPS()));
            ID_USER_LIST.add(gps.getID_USER());
        }
        return convertView;
    }

}

