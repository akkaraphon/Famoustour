package com.cpc.famoustour;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.cpc.famoustour.adapter.CustomAdapterProgram;
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

public class EditProgramActivity extends AppCompatActivity {

    String id_pgtour_sd;
    StaticClass sc = new StaticClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_program);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        id_pgtour_sd = bundle.getString("id_pgtour_sd");
        new GetEdit().execute();


    }

    public class GetEdit extends AsyncTask<Object, Object, List<Schedule>> {

        @Override
        protected List<Schedule> doInBackground(Object... params) {
            String result = feedJson();
            Gson gson = new Gson();


            Type collectionType = new TypeToken<List<Schedule>>() {}.getType();
            List<Schedule> schedules = gson.fromJson(result, collectionType);

            Log.d("Schedule_show",result);

            return schedules;
        }

        @Override
        protected void onPostExecute(List<Schedule> schedules) {

//            EditText Time_s = (EditText) findViewById(R.id.txt_time_S);
//            EditText Time_e = (EditText) findViewById(R.id.txt_time_E);
//            EditText Detail = (EditText) findViewById(R.id.txt_detail);
//
//            Time_s.setText(schedules.get(0).getTIME_S_PGTOUR_SD());
//            Time_e.setText(schedules.get(0).getTIME_E_PGTOUR_SD());
//            Detail.setText(schedules.get(0).getDETAIL_PGTOUR_SD());
        }
    }

    private String feedJson() {
        try {
            OkHttpClient client = new OkHttpClient();

            //Log.d("testestestestestest", String.valueOf(idUser));
            RequestBody body = new FormBody.Builder()
                    .add("id_pgtour_sd", String.valueOf(id_pgtour_sd))
                    .build();

            Request request = new Request.Builder()
                    .url(sc.URL + "/android_edit.php?edit=select")
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            String result = response.body().string();

            return result;
        } catch (Exception e) {
            return null;
        }
    }



    public class SetEdit extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = JSON();

            return result;
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }

    private String JSON() {
        try {
            OkHttpClient client = new OkHttpClient();
            Log.d("intentSession","");

            RequestBody body = new FormBody.Builder()
                    .add("email","")
                    .add("status","logout")
                    .build();

            Request request = new Request.Builder()
                    .url("http://famoustour.apidech.com/android_login.php")
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            String result = response.body().string();

            return result;
        } catch (Exception e) {

        }
        return null;
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
