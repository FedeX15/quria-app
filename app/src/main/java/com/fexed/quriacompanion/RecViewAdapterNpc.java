package com.fexed.quriacompanion;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

public class RecViewAdapterNpc extends RecyclerView.Adapter<RecViewAdapterNpc.ViewHolder> {
    private ArrayList<NPC> npclst;
    private Activity act;
    private ImageLoader imgloader;

    public RecViewAdapterNpc(Activity act, ArrayList<NPC> npclst) {
        this.npclst = npclst;
        this.act = act;
        this.imgloader = new ImageLoader(act.getApplicationContext());
    }

    @Override
    public RecViewAdapterNpc.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardnpc, parent, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        v.setLayoutParams(lp);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        TextView titolo = holder.mCardView.findViewById(R.id.namecard);
        TextView classe = holder.mCardView.findViewById(R.id.classcard);
        TextView descr = holder.mCardView.findViewById(R.id.desccard);
        TextView age = holder.mCardView.findViewById(R.id.agecard);
        TextView race = holder.mCardView.findViewById(R.id.racecard);
        ImageView pic = holder.mCardView.findViewById(R.id.npcpiccard);
        titolo.setText(npclst.get(position).name);
        classe.setText(npclst.get(position).clas);
        descr.setText(npclst.get(position).desc);
        age.setText(npclst.get(position).age);
        race.setText(npclst.get(position).race);
        if (npclst.get(position).img.contains("http")) {
            pic.setImageDrawable(act.getResources().getDrawable(R.drawable.ic_file_download_black_24dp));
            pic.setAdjustViewBounds(true);
            imgloader.DisplayImage(npclst.get(position).img, pic);
            pic.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(npclst.get(position).img));
                    act.startActivity(i);
                    return true;
                }
            });
        } else {
            pic.setImageDrawable(act.getResources().getDrawable(R.drawable.ic_person_black_24dp));
        }
    }

    @Override
    public int getItemCount() { return npclst.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public ViewHolder(CardView v) {
            super(v);
            mCardView = v;
        }
    }
}
