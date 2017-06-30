package com.cpc.famoustour;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cpc.famoustour.adapter.MemberListAdapter;
import com.cpc.famoustour.model.GPS;
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

import static com.cpc.famoustour.model.StaticClass.IDPGTOUR;

public class CallMemberActivity extends AppCompatActivity {

    SharedPreferences sp;
    StaticClass sc = new StaticClass();
    private ListView mListView;
    private MemberListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("App_Config", Context.MODE_PRIVATE);

        Log.d("idUser", String.valueOf(sp.getInt("ID_USER", -1)));

        setContentView(R.layout.activity_call_member);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mListView = (ListView) findViewById(R.id.listview_member);
        new GetListMember().execute();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg, View arg1, int arg2, long arg3) {
                Intent intent;
                intent = new Intent(CallMemberActivity.this, MemberDetailActivity.class);
                intent.putExtra("position", arg2);
                startActivity(intent);
                Toast.makeText(CallMemberActivity.this, String.valueOf(arg2), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public class GetListMember extends AsyncTask<Object, Object, List<GPS>> {

        @Override
        protected List<GPS> doInBackground(Object... params) {

            try {
                OkHttpClient client = new OkHttpClient();

                RequestBody body = new FormBody.Builder()
                        .add("idUser", String.valueOf(sp.getInt("ID_USER", -1)))
                        .add("idPgtour", String.valueOf(IDPGTOUR))
                        .build();

                Request request = new Request.Builder()
                        .url(sc.URL + "/android_GPS.php?type=getAll")
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
                String result = response.body().string();

                Gson gson = new Gson();
                Type collectionType = new TypeToken<List<GPS>>() {
                }.getType();
                List<GPS> gps = gson.fromJson(result, collectionType);

                //Log.d("StringJsonGPS", String.valueOf(result));

                Log.d("ID", String.valueOf(IDPGTOUR));
                Log.d("TimerTask3", result);

                return gps;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<GPS> s) {
            super.onPostExecute(s);
            showData(s);
        }
    }

    private void showData(List<GPS> jsonString) {
        mAdapter = new MemberListAdapter(this, jsonString);
        mListView.setAdapter(mAdapter);
    }
}
