package com.fexed.quriacompanion;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
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
                    //https://drive.google.com/open?id=1VM2WluVks1qC9g3BFLvpmvWI-9wG79sH

                    return true;
                case R.id.navigation_atlante:
                    vf.setDisplayedChild(1);
                    final SubsamplingScaleImageView atlasView = (SubsamplingScaleImageView) findViewById(R.id.atlasView);
                    Button fisicobtn = (Button) findViewById(R.id.fisicotbtn);
                    Button geograbtn = (Button) findViewById(R.id.geogrbtn);
                    Button politibtn = (Button) findViewById(R.id.politicbtn);
                    final TextView coordtxt = (TextView) findViewById(R.id.coord);
                    float x, y;

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

                    atlasView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            /*float curX = (motionEvent.getX() / atlasView.getScale()) - (atlasView.getLeft() * atlasView.getScale());
                            float curY = (motionEvent.getY() / atlasView.getScale()) - (atlasView.getTop() * atlasView.getScale());

                            coordtxt.setText("X " + curX + " - Y " + curY);
                            Canvas mCanvas = new Canvas();
                            mCanvas.drawCircle(((curX / atlasView.getScale())), ((curY / atlasView.getScale())), atlasView.getWidth() / 2 / atlasView.getScale(), new Paint(Color.RED));
                            atlasView.draw(mCanvas);*/

                            return false;
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

    }

    @Override
    public void onBackPressed() {
        //TODO tasto indietro
        super.onBackPressed();
    }
}
