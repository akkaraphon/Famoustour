package com.cpc.famoustour;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.cpc.famoustour.adapter.CustomAdapterOrther;
import com.cpc.famoustour.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrtherFragment extends Fragment {

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Intent intent;
    String str_pdf;


    public OrtherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_orther, container, false);
        // Inflate the layout for this fragment
        GridView gridview = (GridView) v.findViewById(R.id.gridview);
        gridview.setAdapter(new CustomAdapterOrther(getContext()));
        //new GetPDF().execute();

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                switch (position) {
                    case 0:
                        Toast.makeText(getActivity(), "ข้อมูลส่วนตัว", Toast.LENGTH_SHORT).show();
                        intent = new Intent(getActivity(), ViewProfileActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Toast.makeText(getActivity(), "ข้อมูลเพื่อนทริป", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getActivity(), "ดาวโหลดโปรแกรมทัวร์", Toast.LENGTH_SHORT).show();

//                        File file = new File("http://famoustour.apidech.com/pdf/" + str_pdf);
//
//                        if (file.exists()) {
//                            Uri path = Uri.fromFile(file);
//                            Intent intent = new Intent(Intent.ACTION_VIEW);
//                            intent.setDataAndType(path, "application/pdf");
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                            try {
//                                startActivity(intent);
//                            } catch (ActivityNotFoundException e) {
//                                Toast.makeText(getActivity(), "No Application Available to View PDF",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        }
                        break;
                    case 3:
                        Toast.makeText(getActivity(), "ข้อมูลรูปภาพ", Toast.LENGTH_SHORT).show();
                        intent = new Intent(getActivity(), ImgViewActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        Toast.makeText(getActivity(), "ตอบแบบสอบถาม", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        ((MainActivity) getActivity())
                .setActionBarTitle("อื่นๆ");
    }

//    public class GetPDF extends AsyncTask<Object, Object, User> {
//
//        @Override
//        protected User doInBackground(Object... params) {
//
//            String result = feedJson();
//            Gson gson = new Gson();
//            Type collectionType = new TypeToken<List<User>>() {}.getType();
//            List<User> enums = gson.fromJson(result, collectionType);
//            User[] user = enums.toArray(new User[enums.size()]);
//
//            return user[0];
//        }
//
//        @Override
//        protected void onPostExecute(User s) {
//            super.onPostExecute(s);
//            str_pdf = String.valueOf(s);
//        }
//    }
//
//    private String feedJson() {
//        try {
//            OkHttpClient client = new OkHttpClient();
//            //Log.d("sendData", _email);
//            RequestBody body = new FormBody.Builder()
//                    .add("id_pgtour", sp.getString("ID_PGTOUR", null))
//                    .build();
//
//            Request request = new Request.Builder()
//                    .url("http://famoustour.apidech.com/android_pgSchedule.php?pdf=true")
//                    .post(body)
//                    .build();
//
//            Response response = client.newCall(request).execute();
//            String result = response.body().string();
//
//            return result;
//        } catch (Exception e) {
//            return null;
//        }
//    }

}
