package com.fexed.quriacompanion;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class QuriaFragment extends Fragment {
    public QuriaFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.quriafragment, container, false);
        final LinearLayout lytquria = view.findViewById(R.id.quriainfos);
        final LinearLayout lytsovrastato = view.findViewById(R.id.sovrastatodiquriainfos);
        final LinearLayout lytnazioni = view.findViewById(R.id.nazioniinfos);
        lytquria.setVisibility(View.GONE);
        lytsovrastato.setVisibility(View.GONE);
        lytnazioni.setVisibility(View.GONE);

        Button pdfbtn = view.findViewById(R.id.pdfbtn);
        pdfbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/open?id=1tZKcnzz6KG4fLDA9LMCK5oQ6_DjUcbKr")));
            }
        });

        Button indexbtn_quria = view.findViewById(R.id.indexbtn_quria);
        indexbtn_quria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lytquria.setVisibility(View.VISIBLE);
                lytsovrastato.setVisibility(View.GONE);
                lytnazioni.setVisibility(View.GONE);
            }
        });

        Button indexbtn_sovrastato = view.findViewById(R.id.indexbtn_sovrastato);
        indexbtn_sovrastato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lytsovrastato.setVisibility(View.VISIBLE);
                lytquria.setVisibility(View.GONE);
                lytnazioni.setVisibility(View.GONE);
            }
        });


        Button indexbtn_nazioni = view.findViewById(R.id.indexbtn_nazioni);
        indexbtn_nazioni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lytnazioni.setVisibility(View.VISIBLE);
                lytquria.setVisibility(View.GONE);
                lytsovrastato.setVisibility(View.GONE);
            }
        });

        return view;
    }

}
