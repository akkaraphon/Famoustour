package com.cpc.famoustour;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.cpc.famoustour.adapter.TimerTaskAdapter;
import com.cpc.famoustour.model.GPS;
import com.cpc.famoustour.model.LatLngChk;
import com.cpc.famoustour.model.Schedule;
import com.cpc.famoustour.model.StaticClass;
import com.cpc.famoustour.model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.cpc.famoustour.model.StaticClass.AUTH_KEY;
import static com.cpc.famoustour.model.StaticClass.GPS_LOST;

public class MapShowFragment extends Fragment implements OnMapReadyCallback {

    Button _btnLost;
    java.util.Date noteTS;
    Button _btnHere;
    private GoogleMap googleMap;
    StaticClass sc = new StaticClass();
    MapFragment mMap;
    SharedPreferences sp;
    public String _name;
    public String _tel;
    List<GPS> gpsList;
    private TextView mTextView;
    List<Schedule> nameList;
    boolean chk_noti = false;
    SharedPreferences.Editor editor;
    ArrayList<String> TOKEN = new ArrayList<String>();


    public MapShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        sp = getActivity().getSharedPreferences("App_Config", Context.MODE_PRIVATE);
        editor = sp.edit();
        _name = sp.getString("NAME_TH_USER", null) + " " + sp.getString("LASTNAME_TH_USER", null) + "(" + sp.getString("SEX_USER", null) + ")";
        _tel = sp.getString("TEL_USER", null);

        View v = inflater.inflate(R.layout.fragment_map, container, false);
        // Inflate the layout for this fragment
        mMap = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapView);

        TimerTaskAdapter timertask = new TimerTaskAdapter(getActivity(), v);
        new Timer().schedule(timertask, 0, 1000);

        new GetLatLngAtc().execute();
        _btnLost = (Button) v.findViewById(R.id.btn_Lost);
        _btnLost.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                sendWithOtherThread("lost");
                Log.d("Notufy", GPS_LOST);
                //Log.d("lan,lng",getLocation().toString());
                AlertDialog.Builder dialog;
                dialog = new AlertDialog.Builder(v.getContext());
                dialog.setTitle("หลงทาง");
                dialog.setCancelable(true);
                dialog.setMessage("ติดต่อเจ้าหน้าที่เรียบร้อย" + System.lineSeparator() + getLocation().toString());
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                //dialog.setMessage(myLocation.toString());


                dialog.show();
            }
        });


        _btnHere = (Button) v.findViewById(R.id.btn_Here);
        _btnHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CameraPosition cameraPosition = new CameraPosition.Builder().target(getLocation()).zoom(20).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(getLocation()).title(_name).snippet(_tel));
            }
        });

        //new GetGPS().execute();

        mMap.getMapAsync(this);

        return v;
    }


    public void onMapReady(final GoogleMap googlemap) {
        this.googleMap = googlemap;
        new GetGPS().execute();
        CameraPosition cameraPosition = new CameraPosition.Builder().target(getLocation()).zoom(20).build();
        googlemap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        googlemap.clear();
        googlemap.addMarker(new MarkerOptions().position(getLocation()).title(_name).snippet(_tel));
        final Handler handler = new Handler();
        final Runnable worker = new Runnable() {
            @Override
            public void run() {
                googlemap.clear();
                googlemap.addMarker(new MarkerOptions().position(getLocation()).title(_name).snippet(_tel));
                new GetGPS().execute();
                handler.postDelayed(this, 3000);
            }
        };
        handler.post(worker);
    }

    public LatLng getLocation() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        double lng = location.getLongitude();
        double lat = location.getLatitude();
        LatLng myLocation = new LatLng(lat, lng);
        Log.d("Location_Noti", String.valueOf(chk_noti));
        return myLocation;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public class GetGPS extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                OkHttpClient client = new OkHttpClient();

                RequestBody body = new FormBody.Builder()
                        .add("idUser", String.valueOf(sp.getInt("ID_USER", -1)))
                        .add("idPgtour", String.valueOf(sp.getInt("ID_PGTOUR", -1)))
                        .add("type", sp.getString("TYPE_USER", ""))
                        .build();

                Request request = new Request.Builder()
                        .url(sc.URL + "/android_GPS.php?type=get")
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
                String result = response.body().string();

                Log.d("TimerTask2", result);

                return result;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            double lat;
            double lng;
            Gson gson = new Gson();
            Type collectionType = new TypeToken<List<GPS>>() {}.getType();
            List<GPS> gps = gson.fromJson(s, collectionType);
            //Log.d("StringJsonGPS", String.valueOf(gps));
            for (int i = 0; i < gps.size(); i++) {
//                Log.d("StringJsonGPS", String.valueOf(gps.get(i).getLAT_GPS()));
//                Log.d("StringJsonGPS", String.valueOf(i));
                lat = gps.get(i).getLAT_GPS();
                lng = gps.get(i).getLONG_GPS();
                TOKEN.add(gps.get(i).getTOKEN_USER());
                Log.d("StringJsonGPS", TOKEN.get(0));

                LatLng latLng = new LatLng(lat, lng);
                googleMap.addMarker(new MarkerOptions().position(latLng).title(gps.get(i).getNAME()).snippet(gps.get(i).getTEL_USER()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                if (i == gps.size()) {
                    googleMap.clear();
                }
            }

        }
    }


    private void sendWithOtherThread(final String type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pushNotification(type);
            }
        }).start();
    }

    private void pushNotification(String type) {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jData = new JSONObject();
        try {
            switch (type) {
                case "lost":
                    jNotification.put("title", "Help!");
                    jNotification.put("body", "ฉันอยู่ที่นี้ : " + sp.getString("NAME_TH_USER",""));
                    jNotification.put("sound", "default");
                    jNotification.put("badge", "1");
                    jNotification.put("click_action", "OPEN_ACTIVITY_1");
                    break;
                case "out":
                    jNotification.put("title", "OUT!");
                    jNotification.put("body", "ออกนอกเส้นทาง : " + getLocation());
                    jNotification.put("sound", "default");
                    jNotification.put("badge", "1");
                    jNotification.put("click_action", "OPEN_ACTIVITY_1");
                    break;
            }

//            Log.d("StringJsonGPSSS", TOKEN.get(0));
//            Log.d("StringJsonGPSSS", FirebaseInstanceId.getInstance().getToken());
            JSONArray ja = new JSONArray();
            for (int i = 0; i < TOKEN.size(); i++) {
                ja.put(TOKEN.get(i));
            }
            ja.put(FirebaseInstanceId.getInstance().getToken());
            jPayload.put("registration_ids", ja);


            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jData);

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", AUTH_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("Notify", resp);
                }
            });

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }


    public class GetLatLngAtc extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                OkHttpClient client = new OkHttpClient();
                noteTS = Calendar.getInstance().getTime();

                String time = "hhmm";
                Log.d("TIME_TEST", String.valueOf(sp.getInt("ID_PGTOUR", -1)));

                //.add("time", String.valueOf(DateFormat.format(time, noteTS)))

                RequestBody body = new FormBody.Builder()
                        .add("idPgtour", String.valueOf(sp.getInt("ID_PGTOUR", -1)))
                        .add("time", String.valueOf(DateFormat.format(time, noteTS)))
                        .build();

                Request request = new Request.Builder()
                        .url(sc.URL + "/android_latlng.php")
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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d("runtime",result);

            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }
            Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

//            Gson gson = new Gson();
//            LatLngChk[] latLngChks = gson.fromJson(result, LatLngChk[].class);
//            Log.d("runtime", String.valueOf(latLngChks[0]));

            Gson gson = new Gson();
            Type collectionType = new TypeToken<List<LatLngChk>>() {}.getType();
            List<LatLngChk> latLngChks = gson.fromJson(result, collectionType);

            double lng = location.getLongitude();
            double lat = location.getLatitude();

            if(latLngChks.get(0).getID_ATTRAC().equals("-1")){

            }else{
                double latST = latLngChks.get(0).getLAT_ST_ATTRAC();
                double lngST = latLngChks.get(0).getLNG_ST_ATTRAC();
                double latND = latLngChks.get(0).getLAT_ND_ATTRAC();
                double lngND = latLngChks.get(0).getLNG_ND_ATTRAC();

                Log.d("runtime",latLngChks.get(0).getNAME_TH_ATTRAC());

                if(lat >= latST && lng <= lngST && lat >= latND && lng <= lngND && chk_noti == false){
                    sendWithOtherThread("out");
                    chk_noti = true;
                }
            }
        }
    }

//    public void ChkArea(double lat1, double lng1, double lat2, double lng2) {
        //getLatLng();
//        if (lat >= lat1 && lng <= lng1 && lat >= lat2 && lng <= lng2) {
//            if (chk_noti == false) {
//                sendWithOtherThread("out");
//                chk_noti = true;
//            }
//            //sendWithOtherThread("out");
//
//        } else {
//            chk_noti = false;
//        }

//    }
}
