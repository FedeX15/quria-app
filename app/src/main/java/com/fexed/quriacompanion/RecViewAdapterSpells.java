package com.fexed.quriacompanion;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecViewAdapterSpells extends RecyclerView.Adapter<RecViewAdapterSpells.ViewHolder> {
    private ArrayList<String> spells;

    public RecViewAdapterSpells(ArrayList<String> spells) {
        this.spells = spells;
    }

    @Override
    public RecViewAdapterSpells.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.spelllistitem, parent, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        v.setLayoutParams(lp);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        TextView txtv = holder.textview;
        txtv.setText(spells.get(position));
        txtv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
    }

    @Override
    public int getItemCount() { return spells.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textview;
        public ViewHolder(TextView v) {
            super(v);
            textview = v;
        }
    }
}
