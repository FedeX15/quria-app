package com.fexed.quriacompanion;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class PersonalFragment extends Fragment {
    public PersonalFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.personalfragment, container, false);

        FloatingActionButton dmchat = (FloatingActionButton) view.findViewById(R.id.dmchatbtn);
        dmchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent messages = new Intent(getActivity(), Oldmsg.class);
                startActivity(messages);
            }
        });

        EditText backgroundtxt = (EditText) view.findViewById(R.id.personaltxt);
        backgroundtxt.setText(view.getContext().getApplicationContext().getSharedPreferences(getString(R.string.state), Context.MODE_PRIVATE).getString("personalnotes", ""));
        backgroundtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                view.getContext().getApplicationContext().getSharedPreferences(getString(R.string.state), Context.MODE_PRIVATE).edit().putString("personalnotes", editable.toString()).apply();
            }
        });

        return view;
    }

}
