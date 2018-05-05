package com.fexed.quriacompanion;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.autofill.AutofillValue;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import org.w3c.dom.Text;

public class HomeActivity extends AppCompatActivity {

    private ViewFlipper vf;

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
