package com.cpc.famoustour;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.cpc.famoustour.adapter.CustomAdapterSchedule;
import com.cpc.famoustour.model.Schedule;
import com.cpc.famoustour.model.StaticClass;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ScheduleActivity extends AppCompatActivity {

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    int idUser;
    java.util.Date noteTS;
    private ListView mListView;
    private CustomAdapterSchedule mAdapter;
    ProgressBar progressBar;
    private static final int REFRESH_SCREEN = 1;
    StaticClass sc = new StaticClass();
    int mDAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle bundle = getIntent().getExtras();
        mDAY = bundle.getInt("day");
        idUser = bundle.getInt("IdUser");

        Log.d("dayday", String.valueOf(mDAY));

//        sp = getSharedPreferences("App_Config", Context.MODE_PRIVATE);
//        idUser = sp.getInt("ID_USER", -1);

        progressBar = (ProgressBar) findViewById(R.id.pg_progress);

        mListView = (ListView) findViewById(R.id.listview_sc);
        mListView.setVisibility(View.INVISIBLE);

        //new GetSchedule().execute();
        startScan();

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
                    new GetSchedule().execute();
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
    }


    public class GetSchedule extends AsyncTask<Object, Object, List<Schedule>> {

        @Override
        protected List<Schedule> doInBackground(Object... params) {
            String result = feedJson();
            Gson gson = new Gson();


            Type collectionType = new TypeToken<List<Schedule>>() {
            }.getType();
            List<Schedule> schedules = gson.fromJson(result, collectionType);
            //Schedule[] schedule = enums.toArray(new Schedule[enums.size()]);

            return schedules;
        }

        @Override
        protected void onPostExecute(List<Schedule> s) {
            showData(s);
        }
    }

    private void showData(List<Schedule> jsonString) {
        mListView.setVisibility(View.VISIBLE);
        mAdapter = new CustomAdapterSchedule(this, jsonString);
        mListView.setAdapter(mAdapter);
    }

    private String feedJson() {
        try {
            OkHttpClient client = new OkHttpClient();
            //Log.d("ooooooooooooo", String.valueOf(idUser));

            //Log.d("testestestestestest", String.valueOf(idUser));
            RequestBody body = new FormBody.Builder()
                    .add("day", String.valueOf(mDAY))
                    .add("idUser", String.valueOf(idUser))
                    .build();

            Request request = new Request.Builder()
                    .url(sc.URL + "/android_pgSchedule.php?type=time")
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            String result = response.body().string();
            Log.d("Schedule", result);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
