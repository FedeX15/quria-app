package com.fexed.quriacompanion;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by What's That Lambda on 11/6/17.
 */

import android.content.SharedPreferences;
import android.util.Log;

public class InstanceIdService extends FirebaseInstanceIdService {
    public InstanceIdService() {
        super();
        String token = FirebaseInstanceId.getInstance().getToken();
        sendToServer(token);
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();

        //sends this token to the server
        sendToServer(token);
    }

    private void sendToServer(String token) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReferenceFromUrl("https://quriacompanion.firebaseio.com/");
        Log.d("FADB", "ok");

        myRef.child("users").child(HomeActivity.state.getString("pgname", "nonsettato")).setValue(token);
    }
}
