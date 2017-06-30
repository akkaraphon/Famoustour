package com.cpc.famoustour;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cpc.famoustour.adapter.CustomAdapterProgram;
import com.cpc.famoustour.model.Day;
import com.cpc.famoustour.model.StaticClass;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.cpc.famoustour.model.StaticClass.DATE_SESSION;
import static com.cpc.famoustour.model.StaticClass.IDPGTOUR;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProgramFragment extends Fragment {

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private com.fourmob.datetimepicker.date.DatePickerDialog mDatePicker;

    TextView namePGTOUR;
    int idUser;
    private ListView mListView;
    java.util.Date noteTS;
    private CustomAdapterProgram mAdapter;
    ProgressBar progressBar;
    private static final int REFRESH_SCREEN = 1;
    StaticClass sc = new StaticClass();
    View v;


    public ProgramFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_program, container, false);
        sp = getActivity().getSharedPreferences("App_Config", Context.MODE_PRIVATE);
        idUser = sp.getInt("ID_USER", -1);

        progressBar = (ProgressBar) v.findViewById(R.id.pg_progress);
        namePGTOUR = (TextView) v.findViewById(R.id.namePGTOUR);


        mListView = (ListView) v.findViewById(R.id.listView);
        mListView.setVisibility(View.INVISIBLE);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg, View arg1, int arg2, long arg3) {
                Intent intent;
                intent = new Intent(getActivity(), ScheduleActivity.class);
                intent.putExtra("day", arg2 + 1);
                Log.d("dayday", String.valueOf(arg2 + 1));
                intent.putExtra("IdUser", idUser);
                Log.d("ooooooooooooo2", String.valueOf(sp.getInt("ID_USER", -1)));
                startActivity(intent);

                Toast.makeText(getActivity(), "วันที่ " + String.valueOf(arg2 + 1), Toast.LENGTH_LONG).show();
            }
        });

        //new GetSchedule().execute();
        startScan();

//        mCalendar = Calendar.getInstance();
//        mYear = mCalendar.get(Calendar.YEAR);
//        mMonth = mCalendar.get(Calendar.MONTH);
//        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
//        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
//        mCalendar.set(mYear, mMonth, mDay);
//        Date date = mCalendar.getTime();
//        DATE_SESSION = mdformat.format(date);
//
//        Log.d("dateSess",DATE_SESSION);


//        TextDate = (TextView) v.findViewById(R.id.data_date);
//        DatePickerDialog.OnDateSetListener onDateSetListener =
//                new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
//                        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
//                        mCalendar.set(year, month, day);
//                        Date date = mCalendar.getTime();
//                        DATE_SESSION = mdformat.format(date);
//                        TextDate.setText(DATE_SESSION);
//                        new GetDay().execute();
//                    }
//                };
//
//        mDatePicker = DatePickerDialog.newInstance(onDateSetListener,
//                mCalendar.get(Calendar.YEAR),       // ปี
//                mCalendar.get(Calendar.MONTH),      // เดือน
//                mCalendar.get(Calendar.DAY_OF_MONTH),// วัน (1-31)
//                false);
//
//        mDateButton = (Button) v.findViewById(R.id.button_date);
//
//        mDateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDatePicker.show(getActivity().getSupportFragmentManager(), "datePicker");
//            }
//        });

        return v;

}

    public void startScan() {
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(4000);
                    hRefresh.sendEmptyMessage(REFRESH_SCREEN);
                } catch (Exception e) {
                }
            }
        }.start();
    }

    Handler hRefresh = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_SCREEN:
                    new GetDay().execute();
                    progressBar.setVisibility(View.INVISIBLE); // Hide ProgressBar
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public void onResume() {
        super.onResume();

        ((MainActivity) getActivity())
                .setActionBarTitle("โปรแกรมทัวร์");
    }


public class GetDay extends AsyncTask<Object, Object, List<Day>> {

    @Override
    protected List<Day> doInBackground(Object... params) {
        String result = feedJson();
        Gson gson = new Gson();


        Type collectionType = new TypeToken<List<Day>>() {
        }.getType();
        List<Day> days = gson.fromJson(result, collectionType);
        //Schedule[] schedule = enums.toArray(new Schedule[enums.size()]);

//        editor = sp.edit();
//        editor.putInt("ID_PGTOUR", days.get(0).getID_PGTOUR());
//        editor.commit();
        IDPGTOUR = days.get(0).getID_PGTOUR();
        Log.d("IDPGTOUR", String.valueOf(days.get(0).getID_PGTOUR()));
        Log.d("testtest", String.valueOf(sp.getInt("ID_PGTOUR", -1)));

        //Log.d("testtest", days.get());


        return days;
    }

    @Override
    protected void onPostExecute(List<Day> s) {
        showData(s);
    }

}

    private void showData(List<Day> jsonString) {
        mListView.setVisibility(View.VISIBLE);
        mAdapter = new CustomAdapterProgram(getActivity(), jsonString);
        mListView.setAdapter(mAdapter);
        new GetNameTH().execute();
    }

    private String feedJson() {
        try {
            OkHttpClient client = new OkHttpClient();

            Calendar calander = Calendar.getInstance();
            int cDay = calander.get(Calendar.DAY_OF_MONTH);
            int cMonth = calander.get(Calendar.MONTH) + 1;
            String Month = null;
            if (cMonth <= 9) {
                cMonth = calander.get(Calendar.MONTH) + 1;
                Month = "0" + String.valueOf(cMonth);
            } else {
                Month = String.valueOf(cMonth);
            }
            int cYear = calander.get(Calendar.YEAR);

            Log.d("datetime", String.valueOf(DATE_SESSION));
            Log.d("datetime", String.valueOf(idUser));

            RequestBody body = new FormBody.Builder()
                    .add("idUser", String.valueOf(idUser))
                    .add("time", cYear + "-" + Month + "-" + cDay)
                    .build();

            Request request = new Request.Builder()
                    .url(sc.URL + "/android_pgSchedule.php?type=day")
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            String result = response.body().string();
            Log.d("Schedulesss", result);

            return result;
        } catch (Exception e) {

        }
        return null;
    }


    public class GetNameTH extends AsyncTask<Object, Object, String> {

        @Override
        protected String doInBackground(Object... params) {
            String result = feedJson2();
            Gson gson = new Gson();
            Type collectionType = new TypeToken<Collection<Day>>() {}.getType();
            Collection<Day> enums = gson.fromJson(result, collectionType);
            Day[] user = enums.toArray(new Day[enums.size()]);

            return user[0].getNAME_TH_PGTOUR();
        }

        @Override
        protected void onPostExecute(String s) {
            if(!s.equals("-1")){
                namePGTOUR.setText(s);
            }
        }

    }

    private String feedJson2() {
        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody body = new FormBody.Builder()
                    .add("id_pgtour", String.valueOf(IDPGTOUR))
                    .build();

            Request request = new Request.Builder()
                    .url(sc.URL + "/android_pgSchedule.php?type=name")
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            String result = response.body().string();
            Log.d("Schedulesss", result);

            return result;
        } catch (Exception e) {

        }
        return null;
    }
}
