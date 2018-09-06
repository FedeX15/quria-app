package com.fexed.quriacompanion;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class QuriaFragment extends Fragment {
    HomeActivity act;

    public QuriaFragment(HomeActivity act) {
        super();
        this.act = act;
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
        ImageView utharflag = view.findViewById(R.id.utharflag);
        ImageView vableshflag = view.findViewById(R.id.vableshflag);
        ImageView skuoyplanaflag = view.findViewById(R.id.skuoyplanaflag);
        ImageView thauplarflag = view.findViewById(R.id.thauplarflag);
        ImageView farnanflag = view.findViewById(R.id.farnanflag);
        ImageView skailandflag = view.findViewById(R.id.skailandflag);
        ImageView cheylesflag = view.findViewById(R.id.cheylesflag);
        ImageView efreaflag = view.findViewById(R.id.efreaflag);
        ImageView smedalflag = view.findViewById(R.id.smedalflag);
        ImageView gleayaflag = view.findViewById(R.id.gleayaflag);
        ImageView etresilflag = view.findViewById(R.id.etresilflag);
        ImageView adrainflag = view.findViewById(R.id.adrainflag);
        ImageView preitanflag = view.findViewById(R.id.preitanflag);
        ImageView fruelandflag = view.findViewById(R.id.fruelandflag);
        ImageView gluengaflag = view.findViewById(R.id.gluengaflag);
        ImageView ushadflag = view.findViewById(R.id.ushadflag);
        ImageView pobrosflag = view.findViewById(R.id.pobrosflag);
        ImageView naskeuflag = view.findViewById(R.id.naskeuflag);
        ImageView druisalflag = view.findViewById(R.id.druisalflag);
        ImageView shiongaflag = view.findViewById(R.id.shiongaflag);
        ImageView uchijanflag = view.findViewById(R.id.uchijanflag);
        ImageView awhaitflag = view.findViewById(R.id.awhaitflag);
        ImageView ethosflag = view.findViewById(R.id.ethosflag);
        ImageView meplaxflag = view.findViewById(R.id.meplaxflag);
        ImageView seplaxflag = view.findViewById(R.id.seplaxflag);
        ImageView breovsnehnflag = view.findViewById(R.id.breovsnehnflag);
        ImageView fableinflag = view.findViewById(R.id.fableinflag);
        ImageView ogradflag = view.findViewById(R.id.ogradflag);
        ImageView jaskanaflag = view.findViewById(R.id.jaskanaflag);
        final LinearLayout lytinterlink = view.findViewById(R.id.interlinkinfos);
        lytquria.setVisibility(View.GONE);
        lytsovrastato.setVisibility(View.GONE);
        lytnazioni.setVisibility(View.GONE);
        lytinterlink.setVisibility(View.GONE);

        FloatingActionButton pdfbtn = view.findViewById(R.id.pdfbtn);
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
                lytinterlink.setVisibility(View.GONE);
            }
        });

        Button indexbtn_sovrastato = view.findViewById(R.id.indexbtn_sovrastato);
        indexbtn_sovrastato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lytsovrastato.setVisibility(View.VISIBLE);
                lytquria.setVisibility(View.GONE);
                lytnazioni.setVisibility(View.GONE);
                lytinterlink.setVisibility(View.GONE);
            }
        });


        Button indexbtn_nazioni = view.findViewById(R.id.indexbtn_nazioni);
        indexbtn_nazioni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lytnazioni.setVisibility(View.VISIBLE);
                lytquria.setVisibility(View.GONE);
                lytsovrastato.setVisibility(View.GONE);
                lytinterlink.setVisibility(View.GONE);
            }
        });

        Button indexbtn_interlink = view.findViewById(R.id.indexbtn_interlink);
        indexbtn_interlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lytinterlink.setVisibility(View.VISIBLE);
                lytquria.setVisibility(View.GONE);
                lytsovrastato.setVisibility(View.GONE);
                lytnazioni.setVisibility(View.GONE);
            }
        });

        pobrosflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("eboris");
            }
        });
        ushadflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("eboris");
            }
        });
        gluengaflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("eboris");
            }
        });
        fruelandflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("eboris");
            }
        });
        adrainflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("tiabul");
            }
        });
        etresilflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("tiabul");
            }
        });
        gleayaflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("tiabul");
            }
        });
        smedalflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("tiabul");
            }
        });
        uchijanflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("sluzuan");
            }
        });
        shiongaflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("sluzuan");
            }
        });
        ogradflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("onath");
            }
        });
        awhaitflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("kriasira");
            }
        });
        efreaflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("itos");
            }
        });
        skailandflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("itos");
            }
        });
        jaskanaflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("glinux");
            }
        });
        fableinflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("euhivith");
            }
        });
        meplaxflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("eplax");
            }
        });
        seplaxflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("eplax");
            }
        });
        ethosflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("eplax");
            }
        });
        skuoyplanaflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("agos");
            }
        });
        vableshflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("eutrax");
            }
        });
        thauplarflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("monath");
            }
        });
        farnanflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("faeshoris");
            }
        });
        utharflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("itos");
            }
        });
        naskeuflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("itos");
            }
        });
        cheylesflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("quria");
            }
        });
        druisalflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("quria");
            }
        });
        druisalflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("quria");
            }
        });
        preitanflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("eboris");
            }
        });
        breovsnehnflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.visualizzaMappa("quria");
            }
        });

        return view;
    }

}
