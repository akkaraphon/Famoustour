package com.cpc.famoustour.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cpc.famoustour.R;
import com.cpc.famoustour.model.Day;

import java.util.List;

/**
 * Created by macbook on 4/21/17.
 */

public class CustomAdapterProgram extends BaseAdapter {

    private LayoutInflater mInflater;
    public List<Day> days;
    private ViewHolder mViewHolder;
    int y;
    int m;
    int d;

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

        if (day.getDAY_PGTOUR_SD() == 1) {
            mViewHolder.day_schedule.setText("วันที่ : " + day.getDATE_G_PGTOUR());
        } else if (day.getDAY_PGTOUR_SD() == -1) {
            mViewHolder.day_schedule.setText("ไม่มีโปรแกรมทัวร์ในช่วงนี้");
        } else {
            String str_date = day.getDATE_G_PGTOUR();
            String[] arrDate = str_date.split("-");
            y = Integer.parseInt(arrDate[0]);
            m = Integer.parseInt(arrDate[1]);
            d = Integer.parseInt(arrDate[2]) + 1;
            if (m == 1 && m == 3 && m == 5 && m == 7 && m == 8 && m == 10 && m == 12) {
                if (m <= 9) {
                    if (d <= 31) {
                        if(d <= 9){
                            mViewHolder.day_schedule.setText("วันที่ : " + y + "-0" + m + "-0" + d);
                        }else{
                            mViewHolder.day_schedule.setText("วันที่ : " + y + "-0" + m + "-" + d);
                        }
                    } else {
                        m = m + 1;
                        d = d - 31;
                        mViewHolder.day_schedule.setText("วันที่ : " + y + "-0" + m + "-0" + d);
                    }
                } else {
                    if (d <= 31) {
                        if(d <= 9){
                            mViewHolder.day_schedule.setText("วันที่ : " + y + "-0" + m + "-0" + d);
                        }else{
                            mViewHolder.day_schedule.setText("วันที่ : " + y + "-0" + m + "-" + d);
                        }
                    } else {
                        m = m + 1;
                        d = d - 31;
                        mViewHolder.day_schedule.setText("วันที่ : " + y + "-" + m + "-0" + d);
                    }
                }
            } else if (m == 2) {
                if (d <= 28) {
                    if(d <= 9){
                        mViewHolder.day_schedule.setText("วันที่ : " + y + "-0" + m + "-0" + d);
                    }else{
                        mViewHolder.day_schedule.setText("วันที่ : " + y + "-0" + m + "-" + d);
                    }
                } else {
                    m = m + 1;
                    d = d - 28;
                    mViewHolder.day_schedule.setText("วันที่ : " + y + "-0" + m + "-0" + d);
                }
            } else {
                if (m <= 9) {
                    if (d <= 30) {
                        if(d <= 9){
                            mViewHolder.day_schedule.setText("วันที่ : " + y + "-0" + m + "-0" + d);
                        }else{
                            mViewHolder.day_schedule.setText("วันที่ : " + y + "-0" + m + "-" + d);
                        }
                    } else {
                        m = m + 1;
                        d = d - 30;
                        mViewHolder.day_schedule.setText("วันที่ : " + y + "-0" + m + "-0" + d);
                    }
                } else {
                    if (d <= 30) {
                        if(d <= 9){
                            mViewHolder.day_schedule.setText("วันที่ : " + y + "-0" + m + "-0" + d);
                        }else{
                            mViewHolder.day_schedule.setText("วันที่ : " + y + "-0" + m + "-" + d);
                        }
                    } else {
                        m = m + 1;
                        d = d - 30;
                        mViewHolder.day_schedule.setText("วันที่ : " + y + "-" + m + "-0" + d);
                    }
                }
            }
        }
        return convertView;
    }
}
