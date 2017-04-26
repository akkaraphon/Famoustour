package com.cpc.famoustour;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditProgramActivity extends AppCompatActivity {

    String id_pgtour_sd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_program);

        Bundle bundle = getIntent().getExtras();
        id_pgtour_sd = bundle.getString("id_pgtour_sd");


    }

    public class GetEdit extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = feedJson();

            return result;
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }

    private String feedJson() {
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
}
