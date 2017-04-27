package com.cpc.famoustour;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.cpc.famoustour.adapter.TimerTaskAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Timer;
public class MapShowFragment extends Fragment implements OnMapReadyCallback {

    Button _btnLost;
    Button _btnHere;
    private GoogleMap googleMap;
    MapFragment mMap;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String url = "https://fcm.googleapis.com/fcm/send";
    String clientkey = "key=AAAAlXDJM5Y:APA91bE0stNmqTeqh-oVJiyRcy1S9c_j5gXqw-esUduHehl7B3I1pl7Rqmh3GvTAOKYlzc2YfjUJ78xJzNbklbAAhRcGD3iYcEuPnumEwdVE40C0Kny6yAoSqRAsooCFR4AwsjY3HKD8";


    public MapShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_map, container, false);
        // Inflate the layout for this fragment

        mMap = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapView);

        TimerTaskAdapter timertask = new TimerTaskAdapter(getActivity(), v);
        new Timer().schedule(timertask, 0, 3000);

        _btnLost = (Button) v.findViewById(R.id.btn_Lost);
        _btnLost.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

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

        mMap.getMapAsync(this);

        return v;
    }


    public void onMapReady(final GoogleMap googlemap) {
        this.googleMap = googlemap;

        sp = getActivity().getSharedPreferences("App_Config", Context.MODE_PRIVATE);
        editor = sp.edit();
        final String name = sp.getString("NAME_TH_USER", null) + " " + sp.getString("LASTNAME_TH_USER", null) + "(" + sp.getString("SEX_USER", null) + ")";
        final String tel = sp.getString("TEL_USER", null);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }

        CameraPosition cameraPosition = new CameraPosition.Builder().target(getLocation()).zoom(20).build();
        googlemap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        googlemap.addMarker(new MarkerOptions().position(getLocation()).title(name).snippet(tel));


        _btnHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CameraPosition cameraPosition = new CameraPosition.Builder().target(getLocation()).zoom(20).build();
                googlemap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                googlemap.clear();
                googlemap.addMarker(new MarkerOptions().position(getLocation()).title(name).snippet(tel));
            }
        });
    }

    public LatLng getLocation() {

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double lng = location.getLongitude();
        double lat = location.getLatitude();
        LatLng myLocation = new LatLng(lat, lng);
        return myLocation;
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
