package com.fexed.quriacompanion;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.api.client.testing.http.javanet.MockHttpURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    private ViewFlipper vf;
    ProgressDialog progressDialog;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    vf.setDisplayedChild(0);
                    return true;
                case R.id.navigation_atlante:
                    vf.setDisplayedChild(1);
                    final SubsamplingScaleImageView atlasView = (SubsamplingScaleImageView) findViewById(R.id.atlasView);
                    Button fisicobtn = (Button) findViewById(R.id.fisicotbtn);
                    Button geograbtn = (Button) findViewById(R.id.geogrbtn);
                    Button politibtn = (Button) findViewById(R.id.politicbtn);
                    final TextView coordtxt = (TextView) findViewById(R.id.coord);

                    atlasView.setImage(ImageSource.resource(R.drawable.quria_fisica));
                    fisicobtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            atlasView.setImage(ImageSource.resource(R.drawable.quria_fisica));
                        }
                    });
                    geograbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            atlasView.setImage(ImageSource.resource(R.drawable.quria_geografica));
                        }
                    });
                    politibtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            atlasView.setImage(ImageSource.resource(R.drawable.quria_politica));
                        }
                    });

                    return true;
                case R.id.navigation_risorse:
                    vf.setDisplayedChild(2);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        vf = (ViewFlipper) findViewById(R.id.vf);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ArrayList<String> titoli = new ArrayList<>();
        ArrayList<String> descrizioni = new ArrayList<>();
        ArrayList<ArrayList<String>> luoghi = new ArrayList<>();
        ArrayList<ArrayList<String>> npc = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            int c = 0;

            do {
                c++;
                String title = "" + c;
                JSONArray m_jArry = obj.getJSONArray(title);
                titoli.add(m_jArry.getString(0));
                descrizioni.add(m_jArry.getString(1));
                ArrayList<String> locos = new ArrayList<>(); luoghi.add(locos);
                JSONArray luoghiarray = m_jArry.getJSONArray(2);
                for (int i = 0; i < luoghiarray.length(); i++) locos.add(luoghiarray.getString(i));
                ArrayList<String> porsos = new ArrayList<>(); npc.add(porsos);
                JSONArray npciarray = m_jArry.getJSONArray(3);
                for (int i = 0; i < npciarray.length(); i++) porsos.add(npciarray.getString(i));
            } while (true);
        } catch (JSONException e) {
            Log.d("JSON", "End");
            RecyclerView recview = (RecyclerView) findViewById(R.id.cards);
            recview.setAdapter(new RecViewAdapter(titoli, descrizioni, luoghi, npc));
            recview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            SnapHelper helper = new LinearSnapHelper() {
                @Override
                public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
                    View centerView = findSnapView(layoutManager);
                    if (centerView == null)
                        return RecyclerView.NO_POSITION;

                    int position = layoutManager.getPosition(centerView);
                    int targetPosition = -1;
                    if (layoutManager.canScrollHorizontally()) {
                        if (velocityX < 0) {
                            targetPosition = position - 1;
                        } else {
                            targetPosition = position + 1;
                        }
                    }
                    if (layoutManager.canScrollVertically()) {
                        if (velocityY < 0) {
                            targetPosition = position - 1;
                        } else {
                            targetPosition = position + 1;
                        }
                    }

                    final int firstItem = 0;
                    final int lastItem = layoutManager.getItemCount() - 1;
                    targetPosition = Math.min(lastItem, Math.max(targetPosition, firstItem));
                    return targetPosition;
                }
            };
            helper.attachToRecyclerView(recview);
        }
    }

    @Override
    public void onBackPressed() {
        //TODO tasto indietro
        super.onBackPressed();
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("document.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
