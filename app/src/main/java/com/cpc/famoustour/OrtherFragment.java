package com.cpc.famoustour;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.cpc.famoustour.adapter.CustomAdapterOrther;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrtherFragment extends Fragment {

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Button _btnlogOut;
    Intent intent;


    public OrtherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_orther, container, false);
        // Inflate the layout for this fragment
        GridView gridview = (GridView) v.findViewById(R.id.gridview);
        gridview.setAdapter(new CustomAdapterOrther(getContext()));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                switch (position){
                    case 0:
                        Toast.makeText(getActivity(), "ข้อมูลส่วนตัว", Toast.LENGTH_SHORT).show();
                        intent = new Intent(getActivity(), ViewProfileActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Toast.makeText(getActivity(), "ข้อเพื่อนทริป", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getActivity(), "ดาวโหลดโปรแกรมทัวร์", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getActivity(), "ข้อมูลรูปภาพ", Toast.LENGTH_SHORT).show();
                        intent = new Intent(getActivity(), ImgViewActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        Toast.makeText(getActivity(), "ดาวโหลดโปรแกรมทัวร์", Toast.LENGTH_SHORT).show();
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

}
