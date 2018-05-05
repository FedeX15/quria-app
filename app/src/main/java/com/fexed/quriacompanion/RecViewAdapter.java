package com.fexed.quriacompanion;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class RecViewAdapter extends RecyclerView.Adapter<RecViewAdapter.ViewHolder> {
    private ArrayList<String> titoli;
    private ArrayList<String> descrizioni;
    private ArrayList<ArrayList<String>> luoghi;
    private ArrayList<ArrayList<String>> npc;

    public RecViewAdapter(ArrayList<String> titoli, ArrayList<String> descrizioni, ArrayList<ArrayList<String>> luoghi, ArrayList<ArrayList<String>> npc) {
        this.titoli = titoli;
        this.descrizioni = descrizioni;
        this.luoghi = luoghi;
        this.npc = npc;
    }

    @Override
    public RecViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        TextView titolo = holder.mCardView.findViewById(R.id.titolocard);
        TextView descr = holder.mCardView.findViewById(R.id.desccard);
        TextView npctxt = holder.mCardView.findViewById(R.id.npcard);
        TextView luoghitxt = holder.mCardView.findViewById(R.id.loccard);

        titolo.setText(titoli.get(position));
        descr.setText(descrizioni.get(position));

        StringBuilder str = new StringBuilder();
        for (int i = 0; i < luoghi.get(position).size(); i++) str.append(luoghi.get(position).get(i)).append("\n");
        luoghitxt.setText(str.toString());

        str = new StringBuilder();
        for (int i = 0; i < npc.get(position).size(); i++) str.append(npc.get(position).get(i)).append("\n");
        npctxt.setText(str.toString());
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
}
