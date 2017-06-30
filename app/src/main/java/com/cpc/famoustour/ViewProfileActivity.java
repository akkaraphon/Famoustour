package com.cpc.famoustour;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewProfileActivity extends AppCompatActivity {
    ImageView profile_photo;
    TextView _name;
    TextView _age;
    TextView _birthday;
    TextView _tel;
    TextView _address;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sp = getSharedPreferences("App_Config", Context.MODE_PRIVATE);
        editor = sp.edit();

        if (sp.getString("SEX_USER", null).equals("M")) {
            sex = "Male";
        } else {
            sex = "Female";
        }

        profile_photo = (ImageView) findViewById(R.id.user_profile_photo);
        _name = (TextView) findViewById(R.id.user_profile_name);
        _age = (TextView) findViewById(R.id.age);
        _address = (TextView) findViewById(R.id.address);
        _tel = (TextView) findViewById(R.id.tel);
        _birthday = (TextView) findViewById(R.id.birthday);

        Picasso.with(this)
                .load(sp.getString("PIC_USER", null))
                .error(R.drawable.user_placeholder_error)
                .into(profile_photo);

        _name.setText(sp.getString("NAME_TH_USER", null)
                + " " + sp.getString("LASTNAME_TH_USER", null)
                + "(" + sex + ")");

        _age.setText("อายุ " + sp.getString("AGE_USER", null) + " ปี");
        _tel.setText("เบอร์โทรศัพท์ " + sp.getString("TEL_USER", null));
        _address.setText("ที่อยู่ " + sp.getString("ADDRESS_USER", null));
        _birthday.setText("วัน/เดือน/ปีเกิด " + sp.getString("BIRTHDAY_USER", null).substring(0, 10));
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
