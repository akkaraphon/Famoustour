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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
        TextView date_sd;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_day, parent, false);
            mViewHolder = new ViewHolder();

            mViewHolder.day_schedule = (TextView) convertView.findViewById(R.id.day_schedule);
            mViewHolder.date_sd = (TextView) convertView.findViewById(R.id.date_sd);

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        Day day = days.get(position);

        //Log.d("testtest", day.getDAY_PGTOUR_SD());

        if (day.getDAY_PGTOUR_SD() == 1) {
            String testDateString = day.getDATE_G_PGTOUR();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date d1 = df.parse(testDateString);
                mViewHolder.date_sd.setText(new SimpleDateFormat("dd-MMM-yy").format(d1));
                mViewHolder.day_schedule.setText("วันที่ 1 ");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (day.getDAY_PGTOUR_SD() == -1) {
            mViewHolder.day_schedule.setText("ไม่มีโปรแกรมทัวร์ในช่วงนี้");
        } else {
            String testDateString = day.getDATE_G_PGTOUR();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date d1 = df.parse(testDateString);
                Date newD1 = addDays(d1,position);
                mViewHolder.date_sd.setText(new SimpleDateFormat("dd-MMM-yy").format(newD1));
                mViewHolder.day_schedule.setText("วันที่ " + (position + 1));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return convertView;
    }

    public static Date addDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);

        return cal.getTime();
    }
}
