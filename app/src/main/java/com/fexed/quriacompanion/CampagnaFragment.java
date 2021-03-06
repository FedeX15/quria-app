package com.fexed.quriacompanion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CampagnaFragment extends Fragment {
    public CampagnaFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.campagnafragment, container, false);


        Button eldossier = view.findViewById(R.id.el3_btn2);
        eldossier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/open?id=1SN07c7Nsp9do3QQNuiFuQ3RHmxi8uAB1")));

            }
        });

        Button el3 = view.findViewById(R.id.el3_btn);
        el3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/hUf3Vziyxr4")));

            }
        });

        Button el2 = view.findViewById(R.id.el2_btn);
        el2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/uPEvPGQC47Q")));

            }
        });

        Button el1 = view.findViewById(R.id.el1_btn);
        el1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/NVbkZG-ZICI")));

            }
        });

        Button gof = view.findViewById(R.id.gof_btn);
        gof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/open?id=1ygbvNHZdT_byOzxcSAvScbFWKJVHfRrg")));

            }
        });

        return view;
    }

}
