package com.cpc.famoustour;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.cpc.famoustour.model.StaticClass;
import com.cpc.famoustour.model.User;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {


    String _email;
    EditText txt_login;
    Button btn_login;
    String token;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    StaticClass sc = new StaticClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("TesttKey", "Key: " + key + " Value: " + value);
            }
        }

        sp = getSharedPreferences("App_Config", Context.MODE_PRIVATE);
        btn_login = (Button) findViewById(R.id.mbtnLogin);
        txt_login = (EditText) findViewById(R.id.mtxtLogin);
        _email = sp.getString("EMAIL_USER",null);
        //Log.d("sssssssssssssss",sp.getString("EMAIL_USER",null));

        if(_email != null && !_email.equals("0")){
            _email = sp.getString("EMAIL_USER",null);
            Permision(_email);
            token = FirebaseInstanceId.getInstance().getToken();
            Log.d("Testtoken","Token : " + token);
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                _email = txt_login.getText().toString().trim();
                //Log.d("Email",_email);
                if (_email.length() > 0) {
                    token = FirebaseInstanceId.getInstance().getToken();
                    Log.d("Testtoken","Token : " + token);
                    new GetLogin().execute();

                } else {
                    Toast.makeText(LoginActivity.this, "กรุณากรอก E-mail", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public class GetLogin extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {


            editor = sp.edit();

            String result = feedJson();
            Gson gson = new Gson();
            Type collectionType = new TypeToken<Collection<User>>() {}.getType();
            Collection<User> enums = gson.fromJson(result, collectionType);
            User[] user = enums.toArray(new User[enums.size()]);

            editor.putInt("ID_USER",user[0].getID_USER());
            editor.putString("TITLE_TH_USER",user[0].getTITLE_TH_USER());
            editor.putString("NAME_TH_USER",user[0].getNAME_TH_USER());
            editor.putString("LASTNAME_TH_USER",user[0].getLASTNAME_TH_USER());
            editor.putString("AGE_USER",user[0].getAGE_USER());
            editor.putString("SEX_USER",user[0].getSEX_USER());
            editor.putString("BIRTHDAY_USER",user[0].getBIRTHDAY_USER());
            editor.putString("IDPASS_USER",user[0].getIDPASS_USER());
            editor.putString("EXPPASS_USER",user[0].getEXPPASS_USER());
            editor.putString("EMAIL_USER",user[0].getEMAIL_USER());
            editor.putString("TEL_USER",user[0].getTEL_USER());
            editor.putString("ADDRESS_USER",user[0].getADDRESS_USER());
            editor.putInt("NUMTOUR_USER",user[0].getNUMTOUR_USER());
            editor.putInt("STATUS_USER",user[0].getSTATUS_USER());
            editor.putString("IDNUM_USER",user[0].getIDNUM_USER());
            editor.putString("COMPANY_USER",user[0].getCOMPANY_USER());
            editor.putString("PIC_USER","http://famoustour.pe.hu/users/profile_images/" + user[0].getPIC_USER());
            editor.putString("TYPE_USER",user[0].getTYPE_USER());
            editor.putString("TOKEN_USER",user[0].getTOKEN_USER());
            editor.commit();

            Log.d("Email Session", sp.getString("EMAIL_USER",null));
            //Log.d("Type Session", sp.getString("TYPE_USER",null));

            return sp.getString("EMAIL_USER",null);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Permision(s);
        }
    }

    private String feedJson() {
        try {
            OkHttpClient client = new OkHttpClient();
            //Log.d("sendData", _email);
            RequestBody body = new FormBody.Builder()
                    .add("email", _email)
                    .add("status","login")
                    .add("token",token)
                    .build();

            Request request = new Request.Builder()
                    .url(sc.URL + "/android_login.php")
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            String result = response.body().string();
            Log.d("Test", result);
            return result;
        } catch (Exception e) {

        }
        return null;
    }


    public void Permision(String s){
        String Type = sp.getString("TYPE_USER",null);
        int Status = sp.getInt("STATUS_USER",-2);
        //Log.d("session",Type);
        if(Status == -1) {
            //มีคน login แล้ว
            onShowDialog();
        } else {
            if (!s.equals(_email)) {
                Toast.makeText(LoginActivity.this, "E-mail ไม่ถูกต้อง", Toast.LENGTH_LONG).show();
            } else if (Type.equals("")) {
                Toast.makeText(LoginActivity.this, "Session ERROR", Toast.LENGTH_LONG).show();
            } else {
                if (Type.equals("O")) {
                    Toast.makeText(LoginActivity.this, "Login Success with Owner", Toast.LENGTH_LONG).show();
                } else if (Type.equals("E")) {
                    Toast.makeText(LoginActivity.this, "Login Success with Employee", Toast.LENGTH_LONG).show();
                } else if (Type.equals("G")) {
                    Toast.makeText(LoginActivity.this, "Login Success with Guide", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("type", Type);
                intent.putExtra("email", _email);
                intent.putExtra("token", token);
                startActivity(intent);
                finish();
            }
        }
    }


    public void onShowDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("E-mail");
        dialog.setCancelable(true);
        dialog.setMessage("E-mail นี้มีคนใช้งานอยู่กรุณาใช้ E-mail อื่น");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }
}