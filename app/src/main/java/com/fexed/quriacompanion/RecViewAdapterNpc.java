package com.fexed.quriacompanion;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private ArrayList<String> titoli;
    private ArrayList<String> classi;
    private ArrayList<String> descrizioni;
    private ArrayList<String> ages;
    private ArrayList<String> races;
    private ArrayList<String> images;

    public RecViewAdapterNpc(ArrayList<String> titoli, ArrayList<String> classi, ArrayList<String> descrizioni, ArrayList<String> ages, ArrayList<String> races, ArrayList<String> images) {
        this.titoli = titoli;
        this.classi = classi;
        this.descrizioni = descrizioni;
        this.ages = ages;
        this.races = races;
        this.images = images;
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

        titolo.setText(titoli.get(position));
        classe.setText(classi.get(position));
        descr.setText(descrizioni.get(position));
        age.setText(ages.get(position));
        race.setText(races.get(position));
        if (images.get(position).contains("http")) {
            new DownloadImageTask(pic).execute(images.get(position));
        }
    }

    @Override
    public int getItemCount() { return titoli.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public ViewHolder(CardView v) {
            super(v);
            mCardView = v;
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
