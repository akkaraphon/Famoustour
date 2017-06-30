package com.cpc.famoustour;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.cpc.famoustour.model.StaticClass.IDPGTOUR;
import static com.cpc.famoustour.model.StaticClass.URL;

public class QuestionnaireActivity extends AppCompatActivity {

    private Spinner mScore1;
    private Spinner mScore2;
    private Spinner mScore3;
    int score1;
    int score2;
    int score3;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Button mOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mScore1 = (Spinner) findViewById(R.id.score1);
        mScore2 = (Spinner) findViewById(R.id.score2);
        mScore3 = (Spinner) findViewById(R.id.score3);
        mOk = (Button) findViewById(R.id.btnSubmit);

        String[] score = getResources().getStringArray(R.array.club);
        ArrayAdapter<String> arrScore = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, score);
        mScore1.setAdapter(arrScore);
        mScore2.setAdapter(arrScore);
        mScore3.setAdapter(arrScore);

        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score1 = mScore1.getSelectedItemPosition() + 1;
                score2 = mScore2.getSelectedItemPosition() + 1;
                score3 = mScore3.getSelectedItemPosition() + 1;
                Log.d("SCORE",score1 + " : " + score2 + " : " + score3);
                new SetScore().execute();
                AlertDialog.Builder dialog;
                dialog = new AlertDialog.Builder(v.getContext());
                dialog.setTitle("ประเมิน");
                dialog.setCancelable(true);
                dialog.setMessage("ประเมินเรียบร้อย");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                //dialog.setMessage(myLocation.toString());
                dialog.show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public class SetScore extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {

                //Log.d("TimerTask2", String.valueOf(STATUS_GPS));

                sp = getSharedPreferences("App_Config", Context.MODE_PRIVATE);
                editor = sp.edit();
                editor.putInt("STATUS_EVA",1);
                editor.commit();
                OkHttpClient client = new OkHttpClient();

                RequestBody body = new FormBody.Builder()
                        .add("idPgtour", String.valueOf(IDPGTOUR))
                        .add("score1", String.valueOf(score1))
                        .add("score2", String.valueOf(score2))
                        .add("score3", String.valueOf(score3))
                        .build();

                Request request = new Request.Builder()
                        .url(URL + "/android_QN.php")
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
                String result = response.body().string();


                //Log.d("TimerTask222", result);

                return result;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("TimerTask222",s);
        }
    }
}
