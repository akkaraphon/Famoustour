package com.cpc.famoustour;


import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.cpc.famoustour.adapter.CustomAdapterOrther;
import com.cpc.famoustour.model.FileDownloader;
import com.cpc.famoustour.model.Schedule;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.cpc.famoustour.model.StaticClass.IDPGTOUR;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrtherFragment extends Fragment {

    SharedPreferences sp;

    ProgressDialog progressBar;
    int click_do = 0;
    int size;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    private long fileSize = 2000000;

    Intent intent;
    String str_pdf;
    private static String file_url = "http://famoustour.apidech.com/pdf/";


    public OrtherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_orther, container, false);
        sp = getActivity().getSharedPreferences("App_Config", Context.MODE_PRIVATE);
        // Inflate the layout for this fragment
        GridView gridview = (GridView) v.findViewById(R.id.gridview);
        gridview.setAdapter(new CustomAdapterOrther(getContext(), sp.getInt("STATUS_EVA", -1)));
        new GetPDF().execute();

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
                        Log.d("idUser", String.valueOf(sp.getInt("ID_USER", 0)));
                        intent = new Intent(getActivity(), CallMemberActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        click_do = 1;
                        new DownloadFile().execute(file_url + str_pdf, str_pdf);
                        progressBar = new ProgressDialog(v.getContext());
                        progressBar.setCancelable(true);
                        progressBar.setMessage("File downloading ...");
                        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressBar.setProgress(0);
                        progressBar.setMax(100);
                        progressBar.show();

                        progressBarStatus = 0;

                        new Thread(new Runnable() {
                            public void run() {
                                while (progressBarStatus < 100) {
                                    progressBarStatus = doSomeTasks();
                                    Log.d("progressBarStatus", String.valueOf(progressBarStatus));
                                    progressBarHandler.post(new Runnable() {
                                        public void run() {
                                            progressBar.setProgress(progressBarStatus);
                                        }
                                    });
                                }
                                if (progressBarStatus >= 100) {
                                    try {
                                        Thread.sleep(2000);
                                        progressBar.dismiss();
                                        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/PDF PROGRAMTOUR/" + str_pdf);
                                        Uri path = Uri.fromFile(pdfFile);
                                        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                                        pdfIntent.setDataAndType(path, "application/pdf");
                                        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                        try {
                                            startActivity(pdfIntent);
                                        } catch (ActivityNotFoundException e) {
                                            Toast.makeText(getActivity(), "No Application available to view PDF", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }).start();
                        break;
                    case 3:
                        Toast.makeText(getActivity(), "ข้อมูลรูปภาพ", Toast.LENGTH_SHORT).show();
                        intent = new Intent(getActivity(), ImgViewActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        Toast.makeText(getActivity(), "ตอบแบบสอบถาม", Toast.LENGTH_SHORT).show();
                        intent = new Intent(getActivity(), QuestionnaireActivity.class);
                        startActivity(intent);
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


    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "PDF PROGRAMTOUR");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);
            Log.d("catch_log", fileUrl);
            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                Log.d("catch_log", String.valueOf(e));
                e.printStackTrace();
            }
            if (click_do == 1) {
                FileDownloader.downloadFile(fileUrl, pdfFile);
            }
            return null;
        }
    }

    public class GetPDF extends AsyncTask<Object, Object, String> {

        @Override
        protected String doInBackground(Object... params) {

            try {
                OkHttpClient client = new OkHttpClient();
                //Log.d("sendData", _email);
                RequestBody body = new FormBody.Builder()
                        .add("id_pgtour", String.valueOf(IDPGTOUR))
                        .build();

                Request request = new Request.Builder()
                        .url("http://famoustour.apidech.com/android_pgSchedule.php?type=pdf")
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
                String result = response.body().string();

                return result;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Gson gson = new Gson();
            Type collectionType = new TypeToken<List<Schedule>>() {
            }.getType();
            List<Schedule> schedule = gson.fromJson(s, collectionType);
            str_pdf = schedule.get(0).getFILE_PGTOUR();
            new DownloadFile().execute(file_url + str_pdf, str_pdf);
        }

    }


    public int doSomeTasks() {
        while (size <= fileSize) {
            size++;
            Log.d("progressBarStatus", String.valueOf(size));
            if (size == fileSize * 0.1) {
                return 10;
            } else if (size == fileSize * 0.2) {
                return 20;
            } else if (size == fileSize * 0.3) {
                return 30;
            } else if (size == fileSize * 0.4) {
                return 40;
            } else if (size == fileSize * 0.5) {
                return 50;
            } else if (size == fileSize * 0.6) {
                return 60;
            } else if (size == fileSize * 0.7) {
                return 70;
            } else if (size == fileSize * 0.8) {
                return 80;
            } else if (size == fileSize * 0.9) {
                return 90;
            }
        }
        return 100;
    }

}
