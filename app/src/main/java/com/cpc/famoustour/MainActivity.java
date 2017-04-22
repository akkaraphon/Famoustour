package com.cpc.famoustour;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity{

    private SectionsPagerAdapter mSectionsPagerAdapter;

    String typeUser;
    String emailUser;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String tokenUser;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("TesttKey", "Key: " + key + " Value: " + value);
            }
        }

        sp = getSharedPreferences("App_Config", Context.MODE_PRIVATE);
        editor = sp.edit();

        Bundle bundle = getIntent().getExtras();
        typeUser = bundle.getString("type");
        emailUser = bundle.getString("email");


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_Logout) {
            new GetLogout().execute();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onLogoutPressed() {
        sp = getSharedPreferences("App_Config",Context.MODE_PRIVATE);
        editor = sp.edit();
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("ออกจากระบบ");
        dialog.setCancelable(true);
        dialog.setMessage("คุณต้องการออกจากระบบใช่หรือไม่");
        dialog.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                editor.clear();
                editor.commit();
                finish();
            }
        });

        dialog.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("ออกจากโปรแกรม");
        dialog.setCancelable(true);
        dialog.setMessage("คุณต้องการออกจากโปรแกรมใช้หรือไม่");
        dialog.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        dialog.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
    }



    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("Type Session",typeUser);

            if(typeUser.equals("M")){
                switch (position){
                    case 0 :
                        return new ProgramFragment();
                    case 1 :
                        return new MapShowFragment();
                    default:
                        return new OrtherFragment();
                }
            }else if(typeUser.equals("O")){
                switch (position){
                    case 0 :
                        return new ProgramFragment();
                    case 1 :
                        return new MapShowFragment();
                    default:
                        return new OrtherFragment();
                }
            }else if(typeUser.equals("E") || typeUser.equals("G")){
                switch (position){
                    case 0 :
                        return new ProgramManageFragment();
                    case 1 :
                        return new MapShowFragment();
                    default:
                        return new OrtherFragment();
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "โปรแกรมทัวร์";
                case 1:
                    return "แผนที่";
                case 2:
                    return "อื่น ๆ ";
            }
            return null;
        }
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }


    public class GetLogout extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = feedJson();

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            onLogoutPressed();
        }
    }

    private String feedJson() {
        try {
            OkHttpClient client = new OkHttpClient();
            Log.d("intentSession",emailUser);

            RequestBody body = new FormBody.Builder()
                    .add("email",emailUser)
                    .add("status","logout")
                    .build();

            Request request = new Request.Builder()
                    .url("http://famoustour.pe.hu/android_login.php")
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
