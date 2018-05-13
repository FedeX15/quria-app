package com.fexed.quriacompanion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final SharedPreferences state = getApplicationContext().getSharedPreferences(getString(R.string.state), Context.MODE_PRIVATE);
        setTitle("Preferenze");

        final CheckBox pinchk = findViewById(R.id.pincheck);
        pinchk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                state.edit().putBoolean("pref_pins", pinchk.isChecked()).apply();
            }
        });
        pinchk.setChecked(state.getBoolean("pref_pins", true));

        final CheckBox syncchk = findViewById(R.id.syncheck);
        syncchk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                state.edit().putBoolean("pref_sync", syncchk.isChecked()).apply();
            }
        });
        syncchk.setChecked(state.getBoolean("pref_sync", true));
    }

}
