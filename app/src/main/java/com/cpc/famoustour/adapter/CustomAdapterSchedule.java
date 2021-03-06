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
import com.cpc.famoustour.model.Schedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by macbook on 5/3/17.
 */

public class CustomAdapterSchedule extends BaseAdapter{

    private LayoutInflater mInflater;
    public List<Schedule> schedules;
    public ArrayList<Integer> arrID;
    private CustomAdapterSchedule.ViewHolder mViewHolder;

    String time_s;
    String time_e;

    public CustomAdapterSchedule(Activity activity, List<Schedule> schedule) {
        mInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        schedules = schedule;
        arrID = new ArrayList<Integer>();
    }


    @Override
    public int getCount() {
        return schedules.size();
    }

    @Override
    public Object getItem(int position) {
        return schedules.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        TextView time;
        TextView place;
        TextView detail;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_detail, parent, false);
            mViewHolder = new CustomAdapterSchedule.ViewHolder();

            mViewHolder.time = (TextView) convertView.findViewById(R.id.time);
            mViewHolder.place = (TextView) convertView.findViewById(R.id.place);
            mViewHolder.detail = (TextView) convertView.findViewById(R.id.detail);

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (CustomAdapterSchedule.ViewHolder) convertView.getTag();
        }

        Schedule schedule = schedules.get(position);


        if (schedule.getTIME_S_PGTOUR_SD().length() > 1) {
            time_s = schedule.getTIME_S_PGTOUR_SD().substring(0, 2) + ":" + schedule.getTIME_S_PGTOUR_SD().substring(2, 4);
        } if (schedule.getTIME_E_PGTOUR_SD().length() > 1) {
            time_e = schedule.getTIME_E_PGTOUR_SD().substring(0, 2) + ":" + schedule.getTIME_E_PGTOUR_SD().substring(2, 4);
        }else if(schedule.getTIME_S_PGTOUR_SD().length() == 0 && schedule.getTIME_E_PGTOUR_SD().length() == 0){
            time_e = "";time_s = "";
        }

        mViewHolder.time.setText(time_s + " - " + time_e);
        mViewHolder.place.setText(schedule.getNAME_TH());
        mViewHolder.detail.setText(schedule.getDETAIL_PGTOUR_SD());

        Log.d("schedule_ID", String.valueOf(schedule.getID_PGTOUR_SD()));

        arrID.add(schedule.getID_PGTOUR_SD());

        return convertView;
    }
}
