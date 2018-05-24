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

public class RecViewAdapter extends RecyclerView.Adapter<RecViewAdapter.ViewHolder> {
    private ArrayList<String> titoli;
    private ArrayList<String> descrizioni;
    private ArrayList<String> date;
    private ArrayList<String> images;
    private ArrayList<ArrayList<String>> luoghi;
    private ArrayList<ArrayList<String>> npc;
    private Activity act;

    public RecViewAdapter(Activity act, ArrayList<String> titoli, ArrayList<String> descrizioni, ArrayList<ArrayList<String>> luoghi, ArrayList<ArrayList<String>> npc, ArrayList<String> date, ArrayList<String> images) {
        this.titoli = titoli;
        this.descrizioni = descrizioni;
        this.date = date;
        this.luoghi = luoghi;
        this.npc = npc;
        this.images = images;
        this.act = act;
    }

    @Override
    public RecViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardhistory, parent, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        v.setLayoutParams(lp);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        TextView titolo = holder.mCardView.findViewById(R.id.namecard);
        TextView descr = holder.mCardView.findViewById(R.id.desccard);
        TextView datetxt = holder.mCardView.findViewById(R.id.datecard);
        TextView npctxt = holder.mCardView.findViewById(R.id.npcard);
        TextView luoghitxt = holder.mCardView.findViewById(R.id.loccard);
        TextView luoghitagtxt = holder.mCardView.findViewById(R.id.luoghitagtxt);
        TextView npctagtxt = holder.mCardView.findViewById(R.id.npctagtxt);
        final ImageView pic = holder.mCardView.findViewById(R.id.imagecard);

        titolo.setText(titoli.get(position));
        descr.setText(descrizioni.get(position));
        datetxt.setText(date.get(position));

        StringBuilder str = new StringBuilder();
        for (int i = 0; i < luoghi.get(position).size(); i++) str.append(luoghi.get(position).get(i)).append("\n");
        luoghitxt.setText(str.toString());
        if (str.toString() == "" || str.toString().isEmpty()) {luoghitagtxt.setVisibility(View.GONE); luoghitxt.setVisibility(View.GONE);}

        str = new StringBuilder();
        for (int i = 0; i < npc.get(position).size(); i++) str.append(npc.get(position).get(i)).append("\n");
        npctxt.setText(str.toString());
        if (str.toString() == "" || str.toString().isEmpty()) {npctagtxt.setVisibility(View.GONE); npctxt.setVisibility(View.GONE);}
        pic.setVisibility(View.GONE);
        if (images.get(position).contains("http")) {
            new DownloadImageTask(pic).execute(images.get(position));
            pic.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(images.get(position)));
                    act.startActivity(i);
                    return true;
                }
            });
        } else {
            pic.setVisibility(View.GONE);
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
            bmImage.setVisibility(View.VISIBLE);
        }
    }

}
