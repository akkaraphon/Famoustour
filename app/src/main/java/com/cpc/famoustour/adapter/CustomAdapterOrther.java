package com.cpc.famoustour.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.cpc.famoustour.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.cpc.famoustour.model.StaticClass.DATE_B;
import static com.cpc.famoustour.model.StaticClass.TYPE_USER;

/**
 * Created by macbook on 4/20/17.
 */

public class CustomAdapterOrther extends BaseAdapter {
    private Context mContext;
    private int mStatus;
    SharedPreferences sp;

    public CustomAdapterOrther(Context context, int Status_eva) {
        mContext = context;
        mStatus = Status_eva;
    }

    public int getCount() {
        return 5;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;

        if (convertView == null) {
            textView = new TextView(mContext);
            textView.setLayoutParams(new GridView.LayoutParams(400,400));
            textView.setPadding(4, 4, 4, 4);
        } else {
            textView = (TextView) convertView;
        }
        switch (position){
            case 0:
                textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.profile ,0, 0);
                textView.setText("ข้อมูลส่วนตัว");
                textView.setGravity(Gravity.CENTER);
                break;
            case 1:
                textView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.present ,0, 0);
                textView.setText("ข้อมูลเพื่อนร่วมทริป");
                textView.setGravity(Gravity.CENTER);
                break;
            case 2:
                textView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.pdf ,0, 0);
                textView.setText("ดาวน์โหลดโปรแกรมทัวร์");
                textView.setGravity(Gravity.CENTER);
                break;
            case 3:
                textView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.gallery ,0, 0);
                textView.setText("ดูรูปภาพ");
                textView.setGravity(Gravity.CENTER);
                break;
            default:
                Calendar mCalendar = Calendar.getInstance();
                int  mYear = mCalendar.get(Calendar.YEAR);
                int mMonth = mCalendar.get(Calendar.MONTH);
                int mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
                SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
                mCalendar.set(mYear, mMonth, mDay);
                Date date = mCalendar.getTime();
                String strDate = mdformat.format(date);

                if(TYPE_USER.equals("M") && mStatus != 1 && DATE_B.equals(strDate)) {
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.fromsubmit, 0, 0);
                    textView.setText("ตอบแบบสอบถาม");
                    textView.setGravity(Gravity.CENTER);
                }
                break;
        }
        return textView;
    }


}
