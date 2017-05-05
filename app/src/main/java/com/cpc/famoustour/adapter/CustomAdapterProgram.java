package com.cpc.famoustour.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cpc.famoustour.R;
import com.cpc.famoustour.model.Day;
import com.cpc.famoustour.model.Schedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by macbook on 4/21/17.
 */

public class CustomAdapterProgram extends BaseAdapter {

    private LayoutInflater mInflater;
    public List<Day> days;
    private ViewHolder mViewHolder;

    public CustomAdapterProgram(Activity activity, List<Day> day) {
        mInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        days = day;
    }


    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Object getItem(int position) {
        return days.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        TextView day_schedule;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_day, parent, false);
            mViewHolder = new ViewHolder();

            mViewHolder.day_schedule = (TextView) convertView.findViewById(R.id.day_schedule);

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        Day day = days.get(position);

        //Log.d("testtest", day.getDAY_PGTOUR_SD());

        mViewHolder.day_schedule.setText("วันที่ : " + day.getDAY_PGTOUR_SD());

        return convertView;
    }
}
