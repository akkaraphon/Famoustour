package com.cpc.famoustour;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpc.famoustour.model.GPS;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.cpc.famoustour.model.StaticClass.IDPGTOUR;
import static com.cpc.famoustour.model.StaticClass.ID_USER_LIST;
import static com.cpc.famoustour.model.StaticClass.TYPE_USER;
import static com.cpc.famoustour.model.StaticClass.URL;

public class MemberDetailActivity extends AppCompatActivity {

    int position;
    TextView name;
    TextView tel;
    TextView status;
    Button btnOK;
    Button btnTEL;
    ImageView statusMEM_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        position = bundle.getInt("position");

        name = (TextView) findViewById(R.id.nameMEM);
        tel = (TextView) findViewById(R.id.telMEM);
        status = (TextView) findViewById(R.id.statusMEM);
        statusMEM_img = (ImageView) findViewById(R.id.statusMEM_img);
        btnOK = (Button) findViewById(R.id.btnOK);
        //btnTEL = (Button) findViewById(R.id.btnTel);




        new GetListMember().execute();
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SetNewStatus().execute();
//                STATUS_LIST.clear();
//                NAME_LIST.clear();
//                TEL_LIST.clear();
//                ID_USER_LIST.clear();
                AlertDialog.Builder dialog;
                dialog = new AlertDialog.Builder(v.getContext());
                dialog.setTitle("ตอบรับ");
                dialog.setCancelable(true);
                dialog.setMessage("ตอบรับการช่วยเหลือแล้ว");
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

    public class SetNewStatus extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                OkHttpClient client = new OkHttpClient();

                RequestBody body = new FormBody.Builder()
                        .add("idUser", ID_USER_LIST.get(position))
                        .add("idPgtour", String.valueOf(IDPGTOUR))
                        .build();

                Request request = new Request.Builder()
                        .url(URL + "/android_GPS.php?type=setH")
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
                String result = response.body().string();

                return result;
            } catch (Exception e) {
                return null;
            }
        }
    }

    public class GetListMember extends AsyncTask<Object, Object, List<GPS>> {

        @Override
        protected List<GPS> doInBackground(Object... params) {

            try {
                OkHttpClient client = new OkHttpClient();

                RequestBody body = new FormBody.Builder()
                        .add("idUser", String.valueOf(ID_USER_LIST.get(position)))
                        .add("idPgtour", String.valueOf(IDPGTOUR))
                        .build();

                Request request = new Request.Builder()
                        .url(URL + "/android_GPS.php?type=getOne")
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
        protected void onPostExecute(List<GPS> _gps) {
            super.onPostExecute(_gps);
            if(_gps.get(0).getTYPE_USER().equals("M")) {
                name.setText(_gps.get(0).getNAME());
                status.setVisibility(View.INVISIBLE);
            }else{
                name.setText(_gps.get(0).getNAME());
                status.setText(" (หัวหน้าทัวร์)");
            }
            tel.setText(_gps.get(0).getTEL_USER());
            //ID_USER_LIST;
            if(TYPE_USER.equals("M")){
                btnOK.setVisibility(View.INVISIBLE);
            }else{
                if(_gps.get(0).getSTATUS_GPS() == 1){
                    status.setText("สถานะ : ปกติ");
                    btnOK.setVisibility(View.INVISIBLE);
                    Picasso.with(MemberDetailActivity.this).load(R.drawable.icon_success).into(statusMEM_img);
                }else if(_gps.get(0).getSTATUS_GPS() == 0){
                    status.setText("สถานะ : หลงทาง");
                    btnOK.setVisibility(View.INVISIBLE);
                    Picasso.with(MemberDetailActivity.this).load(R.drawable.icon_lost).into(statusMEM_img);
                }else{
                    status.setText("สถานะ : ขอความช่วยเหลือ");
                    Picasso.with(MemberDetailActivity.this).load(R.drawable.icon_help).into(statusMEM_img);
                }
            }
        }
    }
}
