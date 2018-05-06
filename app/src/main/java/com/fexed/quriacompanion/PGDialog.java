package com.fexed.quriacompanion;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PGDialog extends Dialog implements android.view.View.OnClickListener {

    public Activity c;
    public SharedPreferences state;
    public Button yes;

    public PGDialog(Activity a, SharedPreferences state) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.state = state;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pgedit);
        yes = (Button) findViewById(R.id.Okinputbtn);
        yes.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Okinputbtn:
                EditText pgnameinput = findViewById(R.id.pgnameinput);
                EditText pgclassinput = findViewById(R.id.pgclassinput);
                EditText pglvinput = findViewById(R.id.pglvinput);

                if (pgnameinput.getText().toString() == "" || pgclassinput.getText().toString() == "" || pglvinput.getText().toString() == "") break;

                state.edit().putString("pgname", pgnameinput.getText().toString()).apply();
                state.edit().putString("pgclass", pgclassinput.getText().toString()).apply();
                state.edit().putInt("pglv", Integer.parseInt(pglvinput.getText().toString())).apply();
                state.edit().commit();

                TextView pgnametxt = c.findViewById(R.id.pgnametxt);
                TextView pgclasstxt = c.findViewById(R.id.pgclasstxt);
                TextView pglvtxt = c.findViewById(R.id.pglvtxt);

                pgnametxt.setText(state.getString("pgname", "errore"));
                pgclasstxt.setText(state.getString("pgclass", "errore"));
                pglvtxt.setText(state.getInt("pglv", 0) + "");

                this.dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}
