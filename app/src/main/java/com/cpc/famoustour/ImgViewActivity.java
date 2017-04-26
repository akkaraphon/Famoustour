package com.cpc.famoustour;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import com.cpc.famoustour.adapter.ImgViewAdapter;
import com.cpc.famoustour.model.ImgUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ImgViewActivity extends AppCompatActivity {

    private ImgViewAdapter mAdapter;
    private GridView mGridView;
    int id_user;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sp = getSharedPreferences("App_Config", Context.MODE_PRIVATE);
        editor = sp.edit();
        id_user = sp.getInt("ID_USER",-1);

        new GetImg().execute();


    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    public class GetImg extends AsyncTask<Object, Object, List<ImgUser>> {

        @Override
        protected List<ImgUser> doInBackground(Object... params) {

            String result = feedJson();
            Gson gson = new Gson();

            Type collectionType = new TypeToken<List<ImgUser>>() {}.getType();
            List<ImgUser> urlss = gson.fromJson(result, collectionType);

            //Log.d("imguser", String.valueOf(imguser));

            return urlss;
        }

        @Override
        protected void onPostExecute(List<ImgUser> imgUsers) {
            super.onPostExecute(imgUsers);

            mGridView = (GridView) findViewById(R.id.gridview);
            mAdapter = new ImgViewAdapter(ImgViewActivity.this, imgUsers);
            mGridView.setAdapter(mAdapter);
        }
    }

    private String feedJson() {
        try {
            OkHttpClient client = new OkHttpClient();
            //Log.d("json", String.valueOf(id_user));
            RequestBody body = new FormBody.Builder()
                    .add("id_user", String.valueOf(id_user))
                    .build();

            Request request = new Request.Builder()
                    .url("http://famoustour.apidech.com/android_imgView.php")
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            String result = response.body().string();
            Log.d("json show", result);
            return result;
        } catch (Exception e) {

        }
        return null;
    }
}
