package com.cpc.famoustour;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cpc.famoustour.adapter.TimerTaskAdapter;
import com.cpc.famoustour.model.GPS;
import com.cpc.famoustour.model.LatLngChk;
import com.cpc.famoustour.model.StaticClass;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.cpc.famoustour.model.StaticClass.AUTH_KEY;
import static com.cpc.famoustour.model.StaticClass.IDPGTOUR;

public class MapShowOwnerFragment extends Fragment implements OnMapReadyCallback {

    Button _btnNavi;
    java.util.Date noteTS;
    Button _btnHere;
    private GoogleMap googleMap;
    StaticClass sc = new StaticClass();
    MapFragment mMap;
    SharedPreferences sp;
    public String _name;
    String Name_help = "ไม่มี";
    public String _tel;
    boolean chk_noti = false;
    boolean chk_noti_time = false;
    SharedPreferences.Editor editor;
    String ID_Help = " ";
    String timeNow;
    ArrayList<String> TOKEN = new ArrayList<String>();


    public MapShowOwnerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        sp = getActivity().getSharedPreferences("App_Config", Context.MODE_PRIVATE);
        editor = sp.edit();
        Log.d("TypeU", sp.getString("TYPE_USER", ""));
        _name = sp.getString("NAME_TH_USER", null) + " " + sp.getString("LASTNAME_TH_USER", null) + "(" + sp.getString("SEX_USER", null) + ")";
        _tel = sp.getString("TEL_USER", null);

        View v = inflater.inflate(R.layout.fragment_map_show_owner, container, false);
        // Inflate the layout for this fragment
        mMap = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapView);

        TimerTaskAdapter timertask = new TimerTaskAdapter(getActivity(), v);
        new Timer().schedule(timertask, 0, 1000);

        _btnHere = (Button) v.findViewById(R.id.btn_Here);
        _btnHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CameraPosition cameraPosition = new CameraPosition.Builder().target(getLocation()).zoom(20).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(getLocation()).title(_name).snippet(_tel)).showInfoWindow();
            }
        });

        _btnNavi = (Button) v.findViewById(R.id.btn_navi);
        _btnNavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("lan,lng",getLocation().toString());
                Intent intent;
                intent = new Intent(getActivity(), CallMemberActivity.class);
                startActivity(intent);
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
        //googlemap.clear();
        googlemap.addMarker(new MarkerOptions().position(getLocation()).title(_name).snippet(_tel)).showInfoWindow();
        final Handler handler = new Handler();
        final Runnable worker = new Runnable() {
            @Override
            public void run() {
                googlemap.clear();
                googlemap.addMarker(new MarkerOptions().position(getLocation()).title(_name).snippet(_tel)).showInfoWindow();
                new GetGPS().execute();
                new GetLatLngAtc().execute();
                handler.postDelayed(this, 5000);
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
                        .add("idPgtour", String.valueOf(IDPGTOUR))
                        .add("type", "O")
                        .build();

                Request request = new Request.Builder()
                        .url(sc.URL + "/android_GPS.php")
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
                String result = response.body().string();

                //Log.d("StringJsonGPS", String.valueOf(result));

                Log.d("ID", String.valueOf(sp.getInt("ID_PGTOUR", -1)));
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
            Type collectionType = new TypeToken<List<GPS>>() {
            }.getType();
            List<GPS> gps = gson.fromJson(s, collectionType);

            for (int i = 0; i < gps.size(); i++) {
//                Log.d("StringJsonGPS", String.valueOf(gps.get(i).getLAT_GPS()));
//                Log.d("StringJsonGPS", String.valueOf(i));
                lat = gps.get(i).getLAT_GPS();
                lng = gps.get(i).getLONG_GPS();
                TOKEN.add(gps.get(i).getTOKEN_USER());
                Log.d("StringJsonGPS", TOKEN.get(0));
                String name = gps.get(i).getNAME();
                String tel = gps.get(i).getTEL_USER();
                Log.d("TEL_USER", s);

                LatLng latLng = new LatLng(lat, lng);
                Log.d("gpstest", String.valueOf(gps.get(i).getSTATUS_GPS()));
                if (gps.get(i).getSTATUS_GPS() == 1) {
                    googleMap.addMarker(new MarkerOptions().position(latLng).title(name).snippet(tel).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))).showInfoWindow();
                    if (i == gps.size()) {
                        googleMap.clear();
                    }
                } else if (gps.get(i).getSTATUS_GPS() == 2) {
                    googleMap.addMarker(new MarkerOptions().position(latLng).title(name).snippet(tel).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))).showInfoWindow();
                    if (i == gps.size()) {
                        googleMap.clear();
                    }
                } else {
                    googleMap.addMarker(new MarkerOptions().position(latLng).title(name).snippet(tel).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))).showInfoWindow();
                    if (i == gps.size()) {
                        googleMap.clear();
                    }
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
            JSONArray ja = new JSONArray();
            switch (type) {
                case "out":
                    jNotification.put("title", "ออกจากเส้นทาง!");
                    jNotification.put("body", "ออกนอกเส้นทาง : " + sp.getString("NAME_TH_USER", ""));
                    jNotification.put("sound", "default");
                    jNotification.put("badge", "1");
                    jNotification.put("click_action", "OPEN_ACTIVITY_1");
                    ja.put(FirebaseInstanceId.getInstance().getToken());
                    break;
                case "timeout":
                    jNotification.put("title", "ใกล้เวลาเปลี่ยนสถานที่!");
                    jNotification.put("body", "ใกล้หมดเวลารบกวนรวมตัวเพื่อเปลี่ยนสถานที่");
                    jNotification.put("sound", "default");
                    jNotification.put("badge", "1");
                    jNotification.put("click_action", "OPEN_ACTIVITY_1");
                    ja.put(FirebaseInstanceId.getInstance().getToken());
                    break;
            }

//            Log.d("StringJsonGPSSS", TOKEN.get(0));
//            Log.d("StringJsonGPSSS", FirebaseInstanceId.getInstance().getToken());
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

                String time = "HHmm";
                Log.d("TIME_TEST", String.valueOf(DateFormat.format(time, noteTS)));

                Calendar mCalendar = Calendar.getInstance();
                int mYear = mCalendar.get(Calendar.YEAR);
                int mMonth = mCalendar.get(Calendar.MONTH);
                int mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
                SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
                mCalendar.set(mYear, mMonth, mDay);
                Date date = mCalendar.getTime();
                String strDate = mdformat.format(date);

                timeNow = String.valueOf(DateFormat.format(time, noteTS));

                Log.d("DATENOW", strDate);
                Log.d("DATENOW", String.valueOf(IDPGTOUR));
                Log.d("DATENOW", timeNow);

                //.add("time", String.valueOf(DateFormat.format(time, noteTS)))

                RequestBody body = new FormBody.Builder()
                        .add("idPgtour", String.valueOf(IDPGTOUR))
                        .add("time", timeNow)
                        .add("date", strDate)
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

            Log.d("runtime", result);

            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }
            Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

//            Gson gson = new Gson();
//            LatLngChk[] latLngChks = gson.fromJson(result, LatLngChk[].class);
//            Log.d("runtime", String.valueOf(latLngChks[0]));

            Gson gson = new Gson();
            Type collectionType = new TypeToken<List<LatLngChk>>() {
            }.getType();
            List<LatLngChk> latLngChks = gson.fromJson(result, collectionType);

            double lng = location.getLongitude();
            double lat = location.getLatitude();

            if (latLngChks.get(0).getID_ATTRAC().equals("-1")) {
                Log.d("testtest", "chk_noti : " + String.valueOf(chk_noti));

//                PolylineOptions rectLine = new PolylineOptions()
//                        .add(new LatLng(13.779438180291, 100.55793603029))
//                        .add(new LatLng(13.779438180291, 100.55523806971))
//                        .add(new LatLng(13.776740219709, 100.55523806971))
//                        .add(new LatLng(13.776740219709, 100.55793603029))
//                        .add(new LatLng(13.779438180291, 100.55793603029))
//                        .color(Color.RED);;
//
//                googleMap.addPolyline(rectLine);
//
//
//                if((lat > 13.779438180291 || lng > 100.55793603029) && chk_noti == false){
//                    sendWithOtherThread("out");
//                    chk_noti = true;
//                }
//                if((lat < 13.776740219709 || lng < 100.55523806971) && chk_noti == false){
//                    sendWithOtherThread("out");
//                    chk_noti = true;
//                }
//                if(lat < 13.779438180291 && lng < 100.55793603029 && lat > 13.776740219709 && lng > 100.55523806971 && chk_noti == true){
//                    chk_noti = false;
//                }
            } else {
                double latST = latLngChks.get(0).getLAT_ST_ATTRAC();
                double lngST = latLngChks.get(0).getLNG_ST_ATTRAC();
                double latND = latLngChks.get(0).getLAT_ND_ATTRAC();
                double lngND = latLngChks.get(0).getLNG_ND_ATTRAC();

                PolylineOptions rectLine = new PolylineOptions()
                        .add(new LatLng(latST, lngST))
                        .add(new LatLng(latST, lngND))
                        .add(new LatLng(latND, lngND))
                        .add(new LatLng(latND, lngST))
                        .add(new LatLng(latST, lngST));

                googleMap.addPolyline(rectLine);
                int mTime = Integer.parseInt(timeNow);
                Log.d("mTime", "chk_noti : " + String.valueOf(chk_noti_time));

                if (latLngChks.get(0).getTIME_E_PGTOUR_SD() - 10 == mTime && !chk_noti_time) {
                    chk_noti_time = true;
                    Log.d("mTime2", "chk_noti if: " + String.valueOf(chk_noti_time));
                    sendWithOtherThread("timeout");
                } else if (latLngChks.get(0).getTIME_S_PGTOUR_SD() == mTime) {
                    Log.d("mTime2", "chk_noti elseif: " + String.valueOf(mTime));
                    chk_noti_time = false;
                }

                Log.d("testtest", "chk_noti : " + String.valueOf(chk_noti));
                if ((lat > latST || lng > lngST) && chk_noti == false) {
                    sendWithOtherThread("out");
                    chk_noti = true;
                }
                if ((lat < latND || lng < lngND) && chk_noti == false) {
                    sendWithOtherThread("out");
                    chk_noti = true;
                }
                if (lat < latST && lng < lngST && lat > latND && lng > lngND && chk_noti == true) {
                    chk_noti = false;
                }
            }
        }
    }
}
