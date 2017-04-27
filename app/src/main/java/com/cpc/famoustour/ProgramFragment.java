package com.cpc.famoustour;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.cpc.famoustour.adapter.CustomAdapterProgram;
import com.cpc.famoustour.model.Schedule;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.LogRecord;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProgramFragment extends Fragment {

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    int idUser;
    private ListView mListView;
    private CustomAdapterProgram mAdapter;
    ProgressBar progressBar;
    private static final int REFRESH_SCREEN = 1;
    Handler handle;
    Runnable runable;


    public ProgramFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_program, container, false);
        sp = getActivity().getSharedPreferences("App_Config", Context.MODE_PRIVATE);
        idUser = sp.getInt("ID_USER", -1);

        progressBar = (ProgressBar) v.findViewById(R.id.pg_progress);

        mListView = (ListView) v.findViewById(R.id.listView);
        mListView.setVisibility(View.INVISIBLE);

        //new GetSchedule().execute();
        startScan();


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

        ((MainActivity) getActivity())
                .setActionBarTitle("โปรแกรมทัวร์");
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

            //Log.d("testtest", result);
            return schedules;
        }

        @Override
        protected void onPostExecute(List<Schedule> s) {
            showData(s);
        }
    }

    private void showData(List<Schedule> jsonString) {
        mListView.setVisibility(View.VISIBLE);
        mAdapter = new CustomAdapterProgram(getActivity(), jsonString);
        mListView.setAdapter(mAdapter);
    }

    private String feedJson() {
        try {
            OkHttpClient client = new OkHttpClient();

            //Log.d("testestestestestest", String.valueOf(idUser));
            RequestBody body = new FormBody.Builder()
                    .add("idUser", String.valueOf(idUser))
                    .build();

            Request request = new Request.Builder()
                    .url("http://famoustour.apidech.com/android_pgSchedule.php")
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            String result = response.body().string();
            Log.d("Schedule", result);
            return result;
        } catch (Exception e) {

        }
        return null;
    }
}
