package com.fexed.quriacompanion;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.davemorrissey.labs.subscaleview.ImageSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.floor;

public class HomeActivity extends AppCompatActivity {

    private ViewFlipper vf;
    static SharedPreferences state;
    static int[] prof = {2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7, 8, 8, 8, 8, 9, 9, 9, 9, 10, 10, 10, 10, 11, 11, 11, 11, 12, 12, 12, 12, 13};
    ArrayList<PointF> locationspoints;
    ArrayList<String> locationstags;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    vf.setDisplayedChild(0);
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.cards);
                    recyclerView.smoothScrollToPosition(0);
                    return true;
                case R.id.navigation_atlante:
                    vf.setDisplayedChild(1);
                    return true;
                case R.id.navigation_scheda:
                    vf.setDisplayedChild(2);
                    ScrollView mainscrollv = (ScrollView) findViewById(R.id.mainscroll);
                    mainscrollv.smoothScrollTo(0, 0);
                    return true;
                case R.id.navigation_risorse:
                    vf.setDisplayedChild(3);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        state = getApplicationContext().getSharedPreferences(getString(R.string.state), Context.MODE_PRIVATE);
        vf = (ViewFlipper) findViewById(R.id.vf);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        preparaAtlante();
        preparaSchedaPG();
        preparaRisorse();
        preparaHome();
    }

    private void preparaHome() {
        ImageButton startbtn = (ImageButton) findViewById(R.id.endlistbtn);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.cards);
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
            }
        });

        updateFromWEB();
    }

    private void putJsonInRecview(String json) {
        if (json == "") {
            json = FileHelper.ReadFile(this.getApplicationContext(), "story.json");
            if (json == "-error") json = loadJSONFromAsset();
        }

        ArrayList<String> titoli = new ArrayList<>();
        ArrayList<String> descrizioni = new ArrayList<>();
        ArrayList<ArrayList<String>> luoghi = new ArrayList<>();
        ArrayList<ArrayList<String>> npc = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(json);
            int c = 0;
            do {
                c++;
                String title = "" + c;
                JSONArray m_jArry = obj.getJSONArray(title);
                titoli.add(m_jArry.getString(0));
                descrizioni.add(m_jArry.getString(1));
                ArrayList<String> locos = new ArrayList<>();
                luoghi.add(locos);
                JSONArray luoghiarray = m_jArry.getJSONArray(2);
                for (int i = 0; i < luoghiarray.length(); i++)
                    locos.add(luoghiarray.getString(i));
                ArrayList<String> porsos = new ArrayList<>();
                npc.add(porsos);
                JSONArray npciarray = m_jArry.getJSONArray(3);
                for (int i = 0; i < npciarray.length(); i++) porsos.add(npciarray.getString(i));
            } while (true);
        } catch (JSONException e) {
            Collections.reverse(titoli);
            Collections.reverse(descrizioni);
            Collections.reverse(luoghi);
            Collections.reverse(npc);
            Log.d("JSON", "End");
            RecyclerView recview = (RecyclerView) findViewById(R.id.cards);
            recview.setOnFlingListener(null);
            recview.setAdapter(null);
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
            recview.getAdapter().notifyDataSetChanged();
        }
    }

    private void preparaAtlante() {
        final PinView atlasView = (PinView) findViewById(R.id.atlasView);
        Button fisicobtn = (Button) findViewById(R.id.quriafisica);
        Button geograbtn = (Button) findViewById(R.id.quriageografica);
        Button politibtn = (Button) findViewById(R.id.quriapolitica);
        Button ayonbtn = (Button) findViewById(R.id.ayon);
        Button faeshorisbtn = (Button) findViewById(R.id.faeshoris);
        Button novaaeriabtn = (Button) findViewById(R.id.novaaeria);
        atlasView.setImage(ImageSource.resource(R.drawable.mappa_quriafisica));
        if (locationstags != null)
            for (String s : locationstags)
                if (s.contains("Quria"))
                    atlasView.setPin(locationspoints.get(locationstags.indexOf(s)), s);

        fisicobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locationstags != null) for (String s : atlasView.getPinNames()) atlasView.removePin(s);
                atlasView.setImage(ImageSource.resource(R.drawable.mappa_quriafisica));
                try {
                    if (locationstags != null)
                        for (String s : locationstags)
                            if (s.contains("Quria"))
                                atlasView.setPin(locationspoints.get(locationstags.indexOf(s)), s);
                } catch (Exception e) {}
            }
        });
        geograbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locationstags != null) for (String s : atlasView.getPinNames()) atlasView.removePin(s);
                atlasView.setImage(ImageSource.resource(R.drawable.mappa_quriageografica));
                try {
                    if (locationstags != null)
                        for (String s : locationstags)
                            if (s.contains("Quria"))
                                atlasView.setPin(locationspoints.get(locationstags.indexOf(s)), s);
                } catch (Exception e) {}
            }
        });
        politibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locationstags != null) for (String s : atlasView.getPinNames()) atlasView.removePin(s);
                atlasView.setImage(ImageSource.resource(R.drawable.mappa_quriapolitica));
                try {
                    if (locationstags != null)
                        for (String s : locationstags)
                            if (s.contains("Quria"))
                                atlasView.setPin(locationspoints.get(locationstags.indexOf(s)), s);
                } catch (Exception e) {}
            }
        });
        ayonbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locationstags != null) for (String s : atlasView.getPinNames()) atlasView.removePin(s);
                atlasView.setImage(ImageSource.resource(R.drawable.mappa_ayon));
                try {
                    if (locationstags != null)
                        for (String s : locationstags)
                            if (s.contains("Ayon"))
                                atlasView.setPin(locationspoints.get(locationstags.indexOf(s)), s);
                } catch (Exception e) {}
            }
        });
        faeshorisbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locationstags != null) for (String s : atlasView.getPinNames()) atlasView.removePin(s);
                atlasView.setImage(ImageSource.resource(R.drawable.mappa_faeshoris));
                try {
                    if (locationstags != null)
                        for (String s : locationstags)
                            if (s.contains("Faeshoris"))
                                atlasView.setPin(locationspoints.get(locationstags.indexOf(s)), s);
                } catch (Exception e) {}
            }
        });
        novaaeriabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locationstags != null) for (String s : atlasView.getPinNames()) atlasView.removePin(s);
                atlasView.setImage(ImageSource.resource(R.drawable.mappa_novaaeria));
                try {
                    if (locationstags != null)
                        for (String s : locationstags)
                            if (s.contains("NovaAeria"))
                                atlasView.setPin(locationspoints.get(locationstags.indexOf(s)), s);
                } catch (Exception e) {}
            }
        });
    }

    private void preparaSchedaPG() {
        final TextView FOR = (TextView) findViewById(R.id.FOR);
        final TextView FORmod = (TextView) findViewById(R.id.FORmod);
        final TextView DEX = (TextView) findViewById(R.id.DEX);
        final TextView DEXmod = (TextView) findViewById(R.id.DEXmod);
        final TextView COS = (TextView) findViewById(R.id.COS);
        final TextView COSmod = (TextView) findViewById(R.id.COSmod);
        final TextView INT = (TextView) findViewById(R.id.INT);
        final TextView INTmod = (TextView) findViewById(R.id.INTmod);
        final TextView SAG = (TextView) findViewById(R.id.SAG);
        final TextView SAGmod = (TextView) findViewById(R.id.SAGmod);
        final TextView CAR = (TextView) findViewById(R.id.CAR);
        final TextView CARmod = (TextView) findViewById(R.id.CARmod);
        final TextView lvtxt = (TextView) findViewById(R.id.pglvtxt);
        final TextView nametxt = (TextView) findViewById(R.id.pgnametxt);
        final TextView classtxt = (TextView) findViewById(R.id.pgclasstxt);
        final TextView proftxt = (TextView) findViewById(R.id.proftxt);
        final TextView CA = (TextView) findViewById(R.id.CA);
        final TextView PF = (TextView) findViewById(R.id.PF);
        final TextView PFmax = (TextView) findViewById(R.id.PFmax);
        final TextView pgfirma = (TextView) findViewById(R.id.pgfirma);
        final TextView abilitatalenti = (TextView) findViewById(R.id.skillstitle);
        final TextView inventario = (TextView) findViewById(R.id.invtitle);
        final TextView background = (TextView) findViewById(R.id.bgtitle);
        final TextView attacchi = (TextView) findViewById(R.id.atktitle);
        final TextView spellatk = (TextView) findViewById(R.id.spellatktxt);
        final TextView spellcd = (TextView) findViewById(R.id.spellcdtxt);
        final TextView spellstat = (TextView) findViewById(R.id.spelstatselection);
        final Button PFplus = (Button) findViewById(R.id.pfplus);
        final Button PFminus = (Button) findViewById(R.id.pfminus);
        final Button addranged = (Button) findViewById(R.id.addrangedatk);
        final Button addmelee = (Button) findViewById(R.id.addmeleeatk);
        final Button spellapp = (Button) findViewById(R.id.spellappbtn);
        final TableLayout rangedatks = (TableLayout) findViewById(R.id.rangedatks);
        final TableLayout meleeatks = (TableLayout) findViewById(R.id.meleeatks);
        int pntfor; int modfor;
        int pntdex; int moddex;
        int pntcos; int modcos;
        int pntint; int modint;
        int pntsag; int modsag;
        int pntcar; int modcar;


        String pgname = state.getString("pgname", null);

        if (pgname == null) {
            PGDialog inputdialog = new PGDialog(this, state);
            inputdialog.show();
        } else {
            TextView pgnametxt = findViewById(R.id.pgnametxt);
            TextView pgclasstxt = findViewById(R.id.pgclasstxt);
            TextView pglvtxt = findViewById(R.id.pglvtxt);
            pgnametxt.setText(state.getString("pgname", "errore"));
            pgfirma.setText(state.getString("pgname", "errore"));
            pgclasstxt.setText(state.getString("pgclass", "errore"));
            pglvtxt.setText(state.getInt("pglv", 1) + "");
            proftxt.setText("+" + prof[state.getInt("pglv", 1) - 1]);

            pntfor = state.getInt("FOR", 10);
            modfor = mod(pntfor);
            String suffix = (modfor >= 0) ? "+" : "";
            FOR.setText("" + pntfor); FORmod.setText(suffix + modfor);

            pntdex = state.getInt("DEX", 10);
            moddex = mod(pntdex);
            suffix = (moddex >= 0) ? "+" : "";
            DEX.setText("" + pntdex); DEXmod.setText(suffix + moddex);

            pntcos = state.getInt("COS", 10);
            modcos = mod(pntcos);
            suffix = (modcos >= 0) ? "+" : "";
            COS.setText("" + pntcos); COSmod.setText(suffix + modcos);

            pntint = state.getInt("INT", 10);
            modint = mod(pntint);
            suffix = (modint >= 0) ? "+" : "";
            INT.setText("" + pntint); INTmod.setText(suffix + modint);

            pntsag = state.getInt("SAG", 10);
            modsag = mod(pntsag);
            suffix = (modsag >= 0) ? "+" : "";
            SAG.setText("" + pntsag); SAGmod.setText(suffix + modsag);

            pntcar = state.getInt("CAR", 10);
            modcar = mod(pntcar);
            suffix = (modcar >= 0) ? "+" : "";
            CAR.setText("" + pntcar); CARmod.setText(suffix + modcar);

            int ca = state.getInt("CA", 10);
            CA.setText("" + ca);

            int pf = state.getInt("PF", 0);
            PF.setText("" + pf);

            int pfmax = state.getInt("PFMAX", 0);
            PFmax.setText("" + pfmax);

        }

        String stat = state.getString("SPELLSTAT", "INT");
        spellstat.setText(stat);
        int lv = state.getInt("pglv", 1);
        int bonus = prof[lv-1] + mod(state.getInt(stat, 10));
        String suffix = (bonus < 0) ? "" : "+";
        spellatk.setText(suffix + bonus);
        spellcd.setText("" + (8 + bonus));

        spellstat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder b = new AlertDialog.Builder(HomeActivity.this);
                b.setTitle("Seleziona la statistica con cui lanci incantesimi");
                String[] types = {"INT", "SAG", "CAR"};
                b.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String stat = "";
                        switch(which) {
                            case 0:
                                stat = "INT";
                                break;
                            case 1:
                                stat = "SAG";
                                break;
                            case 2:
                                stat = "CAR";
                                break;
                        }
                        int lv = state.getInt("pglv", 1);
                        state.edit().putString("SPELLSTAT", stat).apply();
                        spellstat.setText(stat);
                        int bonus = prof[lv-1] + mod(state.getInt(stat, 10));
                        String suffix = (bonus < 0) ? "" : "+";
                        spellatk.setText(suffix + bonus);
                        spellcd.setText("" + (8 + bonus));
                    }

                });

                b.show();

            }
        });

        abilitatalenti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout skilllyt = (LinearLayout) findViewById(R.id.skills);
                if (skilllyt.getVisibility() == View.VISIBLE) skilllyt.setVisibility(View.GONE);
                else skilllyt.setVisibility(View.VISIBLE);
            }
        });

        inventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout invlyt = (LinearLayout) findViewById(R.id.inventory);
                if (invlyt.getVisibility() == View.VISIBLE) invlyt.setVisibility(View.GONE);
                else invlyt.setVisibility(View.VISIBLE);
            }
        });

        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout bglyt = (LinearLayout) findViewById(R.id.background);
                if (bglyt.getVisibility() == View.VISIBLE) bglyt.setVisibility(View.GONE);
                else bglyt.setVisibility(View.VISIBLE);
            }
        });

        attacchi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout atklyt = (LinearLayout) findViewById(R.id.atk);
                if (atklyt.getVisibility() == View.VISIBLE) atklyt.setVisibility(View.GONE);
                else atklyt.setVisibility(View.VISIBLE);
            }
        });


        lvtxt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
                final EditText input = new EditText(HomeActivity.this.getApplicationContext());
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setRawInputType(Configuration.KEYBOARD_12KEY);
                alert.setView(input);
                alert.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Put actions for CANCEL button here, or leave in blank
                    }
                });
                final AlertDialog alertd = alert.create();
                alert.setTitle("Inserisci il livello di " + state.getString("pgname", null));
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Put actions for OK button here
                        int lv = Integer.parseInt(input.getText().toString());
                        if (lv <= 0) lv = 1;
                        if (lv > 45) lv = 45;

                        lvtxt.setText(lv + "");
                        proftxt.setText("+" + prof[lv-1]);
                        state.edit().putInt("pglv", lv).apply();
                        dialog.cancel();
                        alertd.dismiss();
                        preparaSchedaPG();
                    }
                });
                alert.show();
                return true;
            }
        });

        proftxt.setText("+" + prof[state.getInt("pglv",1)-1]);

        nametxt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
                final EditText input = new EditText(HomeActivity.this.getApplicationContext());
                alert.setView(input);
                alert.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Put actions for CANCEL button here, or leave in blank
                    }
                });
                final AlertDialog alertd = alert.create();
                alert.setTitle("Inserisci il nuovo nome di " + state.getString("pgname", null));
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Put actions for OK button here
                        String name = input.getText().toString();

                        nametxt.setText(name);
                        pgfirma.setText(name);
                        state.edit().putString("pgname", name).apply();
                        dialog.cancel();
                        alertd.dismiss();
                    }
                });
                alert.show();
                return true;
            }
        });

        classtxt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
                final EditText input = new EditText(HomeActivity.this.getApplicationContext());
                alert.setView(input);
                alert.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Put actions for CANCEL button here, or leave in blank
                    }
                });
                final AlertDialog alertd = alert.create();
                alert.setTitle("Inserisci la nuova classe di " + state.getString("pgname", null));
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Put actions for OK button here
                        String classs = input.getText().toString();

                        classtxt.setText(classs);
                        state.edit().putString("pgclass", classs).apply();
                        dialog.cancel();
                        alertd.dismiss();
                    }
                });
                alert.show();
                return true;
            }
        });

        CA.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
                final EditText input = new EditText(HomeActivity.this.getApplicationContext());
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setRawInputType(Configuration.KEYBOARD_12KEY);
                input.setText(state.getInt("CA", 0) + "");
                alert.setView(input);
                alert.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Put actions for CANCEL button here, or leave in blank
                    }
                });
                final AlertDialog alertd = alert.create();
                alert.setTitle("Inserisci CA");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Put actions for OK button here
                        int pnt = Integer.parseInt(input.getText().toString());

                        CA.setText(pnt + "");
                        state.edit().putInt("CA", pnt).apply();
                        dialog.cancel();
                        alertd.dismiss();
                        preparaSchedaPG();
                    }
                });
                alert.show();
                return true;
            }
        });

        PF.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
                final EditText input = new EditText(HomeActivity.this.getApplicationContext());
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setRawInputType(Configuration.KEYBOARD_12KEY);
                input.setText(state.getInt("PF", 0) + "");
                alert.setView(input);
                alert.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Put actions for CANCEL button here, or leave in blank
                    }
                });
                final AlertDialog alertd = alert.create();
                alert.setTitle("Inserisci PF");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Put actions for OK button here
                        int pnt = Integer.parseInt(input.getText().toString());
                        if (pnt > state.getInt("PFMAX", pnt)) {
                            Toast.makeText(HomeActivity.this, "I PF attuali non possono essere maggiori dei PF massimi", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                            alertd.dismiss();
                        }
                        else {
                            PF.setText(pnt + "");
                            state.edit().putInt("PF", pnt).apply();
                            dialog.cancel();
                            alertd.dismiss();
                            preparaSchedaPG();
                        }
                    }
                });
                alert.show();
                return true;
            }
        });

        PFplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pf = state.getInt("PF", 0);
                pf++;
                int pfmax = state.getInt("PFMAX", pf);
                if (pf > pfmax) pf = pfmax;
                state.edit().putInt("PF", pf).apply();
                PF.setText(pf + "");
                saveSchedaPG();
            }
        });

        PFminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pf = state.getInt("PF", 0);
                pf--;
                state.edit().putInt("PF", pf).apply();
                PF.setText(pf + "");
                saveSchedaPG();
            }
        });


        PFmax.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
                final EditText input = new EditText(HomeActivity.this.getApplicationContext());
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setRawInputType(Configuration.KEYBOARD_12KEY);
                input.setText(state.getInt("PFMAX", 0) + "");
                alert.setView(input);
                alert.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Put actions for CANCEL button here, or leave in blank
                    }
                });
                final AlertDialog alertd = alert.create();
                alert.setTitle("Inserisci PF massimi");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Put actions for OK button here
                        int pnt = Integer.parseInt(input.getText().toString());

                        PFmax.setText(pnt + "");
                        state.edit().putInt("PFMAX", pnt).apply();
                        dialog.cancel();
                        alertd.dismiss();
                        preparaSchedaPG();
                    }
                });
                alert.show();
                return true;
            }
        });

        FOR.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
                final EditText input = new EditText(HomeActivity.this.getApplicationContext());
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setRawInputType(Configuration.KEYBOARD_12KEY);
                alert.setView(input);
                alert.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Put actions for CANCEL button here, or leave in blank
                    }
                });
                final AlertDialog alertd = alert.create();
                alert.setTitle("Inserisci FOR");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Put actions for OK button here
                        int pnt = Integer.parseInt(input.getText().toString());
                        int mod = mod(pnt);
                        String suffix = (mod >= 0) ? "+" : "";

                        FOR.setText(pnt + "");
                        FORmod.setText(suffix + mod);
                        state.edit().putInt("FOR", pnt).apply();
                        dialog.cancel();
                        alertd.dismiss();
                        preparaSchedaPG();
                    }
                });
                alert.show();
                return true;
            }
        });

        DEX.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
                final EditText input = new EditText(HomeActivity.this.getApplicationContext());
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setRawInputType(Configuration.KEYBOARD_12KEY);
                alert.setView(input);
                alert.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Put actions for CANCEL button here, or leave in blank
                    }
                });
                final AlertDialog alertd = alert.create();
                alert.setTitle("Inserisci DEX");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Put actions for OK button here
                        int pnt = Integer.parseInt(input.getText().toString());
                        int mod = mod(pnt);
                        String suffix = (mod >= 0) ? "+" : "";

                        DEX.setText(pnt + "");
                        DEXmod.setText(suffix + mod);
                        state.edit().putInt("DEX", pnt).apply();
                        dialog.cancel();
                        alertd.dismiss();
                        preparaSchedaPG();
                    }
                });
                alert.show();
                return true;
            }
        });

        COS.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
                final EditText input = new EditText(HomeActivity.this.getApplicationContext());
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setRawInputType(Configuration.KEYBOARD_12KEY);
                alert.setView(input);
                alert.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Put actions for CANCEL button here, or leave in blank
                    }
                });
                final AlertDialog alertd = alert.create();
                alert.setTitle("Inserisci COS");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Put actions for OK button here
                        int pnt = Integer.parseInt(input.getText().toString());
                        int mod = mod(pnt);
                        String suffix = (mod >= 0) ? "+" : "";

                        COS.setText(pnt + "");
                        COSmod.setText(suffix + mod);
                        state.edit().putInt("COS", pnt).apply();
                        dialog.cancel();
                        alertd.dismiss();
                        preparaSchedaPG();
                    }
                });
                alert.show();
                return true;
            }
        });

        INT.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
                final EditText input = new EditText(HomeActivity.this.getApplicationContext());
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setRawInputType(Configuration.KEYBOARD_12KEY);
                alert.setView(input);
                alert.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Put actions for CANCEL button here, or leave in blank
                    }
                });
                final AlertDialog alertd = alert.create();
                alert.setTitle("Inserisci INT");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Put actions for OK button here
                        int pnt = Integer.parseInt(input.getText().toString());
                        int mod = mod(pnt);
                        String suffix = (mod >= 0) ? "+" : "";

                        INT.setText(pnt + "");
                        INTmod.setText(suffix + mod);
                        state.edit().putInt("INT", pnt).apply();
                        dialog.cancel();
                        alertd.dismiss();
                        preparaSchedaPG();
                    }
                });
                alert.show();
                return true;
            }
        });

        SAG.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
                final EditText input = new EditText(HomeActivity.this.getApplicationContext());
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setRawInputType(Configuration.KEYBOARD_12KEY);
                alert.setView(input);
                alert.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Put actions for CANCEL button here, or leave in blank
                    }
                });
                final AlertDialog alertd = alert.create();
                alert.setTitle("Inserisci SAG");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Put actions for OK button here
                        int pnt = Integer.parseInt(input.getText().toString());
                        int mod = mod(pnt);
                        String suffix = (mod >= 0) ? "+" : "";

                        SAG.setText(pnt + "");
                        SAGmod.setText(suffix + mod);
                        state.edit().putInt("SAG", pnt).apply();
                        dialog.cancel();
                        alertd.dismiss();
                        preparaSchedaPG();
                    }
                });
                alert.show();
                return true;
            }
        });

        CAR.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
                final EditText input = new EditText(HomeActivity.this.getApplicationContext());
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setRawInputType(Configuration.KEYBOARD_12KEY);
                alert.setView(input);
                alert.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Put actions for CANCEL button here, or leave in blank
                    }
                });
                final AlertDialog alertd = alert.create();
                alert.setTitle("Inserisci CAR");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Put actions for OK button here
                        int pnt = Integer.parseInt(input.getText().toString());
                        int mod = mod(pnt);
                        String suffix = (mod >= 0) ? "+" : "";

                        CAR.setText(pnt + "");
                        CARmod.setText(suffix + mod);
                        state.edit().putInt("CAR", pnt).apply();
                        dialog.cancel();
                        alertd.dismiss();
                        preparaSchedaPG();
                    }
                });
                alert.show();
                return true;
            }
        });

        final TextView tsfortxt = (TextView) findViewById(R.id.TSFOR);
        final CheckBox comptsfor = (CheckBox) findViewById(R.id.comptsfor);
        comptsfor.setChecked(state.getBoolean("comptsfor", false));
        int ts = mod(state.getInt("FOR", 10)) + ((comptsfor.isChecked()) ? prof[lv-1] : 0);
        suffix = (ts >= 0) ? "+" : "";
        tsfortxt.setText(suffix + ts);
        comptsfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lv = state.getInt("pglv", 1);
                state.edit().putBoolean("comptsfor", comptsfor.isChecked()).apply();
                int ts = mod(state.getInt("FOR", 10)) + ((comptsfor.isChecked()) ? prof[lv-1] : 0);
                String suffix = (ts >= 0) ? "+" : "";
                tsfortxt.setText(suffix + ts);
            }
        });

        final TextView tsdextxt = (TextView) findViewById(R.id.TSDEX);
        final CheckBox comptsdex = (CheckBox) findViewById(R.id.comptsdex);
        comptsdex.setChecked(state.getBoolean("comptsdex", false));
        ts = mod((state.getInt("DEX", 10))) + ((comptsdex.isChecked()) ? prof[lv-1] : 0);
        suffix = (ts >= 0) ? "+" : "";
        tsdextxt.setText(suffix + ts);
        comptsdex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lv = state.getInt("pglv", 1);
                state.edit().putBoolean("comptsdex", comptsdex.isChecked()).apply();
                int ts = mod((state.getInt("DEX", 10))) + ((comptsdex.isChecked()) ? prof[lv-1] : 0);
                String suffix = (ts >= 0) ? "+" : "";
                tsdextxt.setText(suffix + ts);
            }
        });

        final TextView tscostxt = (TextView) findViewById(R.id.TSCOS);
        final CheckBox comptscos = (CheckBox) findViewById(R.id.comptscos);
        comptscos.setChecked(state.getBoolean("comptscos", false));
        ts = mod((state.getInt("COS", 10))) + ((comptscos.isChecked()) ? prof[lv-1] : 0);
        suffix = (ts >= 0) ? "+" : "";
        tscostxt.setText(suffix + ts);
        comptscos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lv = state.getInt("pglv", 1);
                state.edit().putBoolean("comptscos", comptscos.isChecked()).apply();
                int ts = mod((state.getInt("COS", 10))) + ((comptscos.isChecked()) ? prof[lv-1] : 0);
                String suffix = (ts >= 0) ? "+" : "";
                tscostxt.setText(suffix + ts);
            }
        });

        final TextView tsinttxt = (TextView) findViewById(R.id.TSINT);
        final CheckBox comptsint = (CheckBox) findViewById(R.id.comptsint);
        comptsint.setChecked(state.getBoolean("comptsint", false));
        ts = mod((state.getInt("INT", 10))) + ((comptsint.isChecked()) ? prof[lv-1] : 0);
        suffix = (ts >= 0) ? "+" : "";
        tsinttxt.setText(suffix + ts);
        comptsint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lv = state.getInt("pglv", 1);
                state.edit().putBoolean("comptsint", comptsint.isChecked()).apply();
                int ts = mod((state.getInt("INT", 10))) + ((comptsint.isChecked()) ? prof[lv-1] : 0);
                String suffix = (ts >= 0) ? "+" : "";
                tsinttxt.setText(suffix + ts);
            }
        });

        final TextView tssagtxt = (TextView) findViewById(R.id.TSSAG);
        final CheckBox comptssag = (CheckBox) findViewById(R.id.comptssag);
        comptssag.setChecked(state.getBoolean("comptssag", false));
        ts = mod((state.getInt("SAG", 10))) + ((comptssag.isChecked()) ? prof[lv-1] : 0);
        suffix = (ts >= 0) ? "+" : "";
        tssagtxt.setText(suffix + ts);
        comptssag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lv = state.getInt("pglv", 1);
                state.edit().putBoolean("comptssag", comptssag.isChecked()).apply();
                int ts = mod((state.getInt("SAG", 10))) + ((comptssag.isChecked()) ? prof[lv-1] : 0);
                String suffix = (ts >= 0) ? "+" : "";
                tssagtxt.setText(suffix + ts);
            }
        });

        final TextView tscartxt = (TextView) findViewById(R.id.TSCAR);
        final CheckBox comptscar = (CheckBox) findViewById(R.id.comptscar);
        comptscar.setChecked(state.getBoolean("comptscar", false));
        ts = mod((state.getInt("CAR", 10))) + ((comptscar.isChecked()) ? prof[lv-1] : 0);
        suffix = (ts >= 0) ? "+" : "";
        tscartxt.setText(suffix + ts);
        comptscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lv = state.getInt("pglv", 1);
                state.edit().putBoolean("comptscar", comptscar.isChecked()).apply();
                int ts = mod((state.getInt("CAR", 10)))+ ((comptscar.isChecked()) ? prof[lv-1] : 0);
                String suffix = (ts >= 0) ? "+" : "";
                tscartxt.setText(suffix + ts);
            }
        });

        final TextView atletica = (TextView) findViewById(R.id.atletica);
        final CheckBox compatletica = (CheckBox) findViewById(R.id.compatletica);
        final CheckBox expatletica = (CheckBox) findViewById(R.id.expatletica);
        compatletica.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) expatletica.setVisibility(View.VISIBLE);
                else expatletica.setVisibility(View.INVISIBLE);
                state.edit().putBoolean("compatletica", compatletica.isChecked()).apply();
                int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("FOR", 10))) + ((compatletica.isChecked()) ? ((expatletica.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                atletica.setText(suffix + bonus);
            }
        });
        expatletica.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                state.edit().putBoolean("expatletica", expatletica.isChecked()).apply();int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("FOR", 10)))+ ((compatletica.isChecked()) ? ((expatletica.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                atletica.setText(suffix + bonus);
            }
        });
        compatletica.setChecked(state.getBoolean("compatletica", false));
        expatletica.setChecked(state.getBoolean("expatletica", false));
        bonus = mod((state.getInt("FOR", 10)))+ ((compatletica.isChecked()) ? ((expatletica.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
        suffix = (bonus >= 0) ? "+" : "";
        atletica.setText(suffix + bonus);

        final TextView acrobazia = (TextView) findViewById(R.id.acrobazia);
        final CheckBox compacrobazia = (CheckBox) findViewById(R.id.compacrobazia);
        final CheckBox expacrobazia = (CheckBox) findViewById(R.id.expacrobazia);
        compacrobazia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) expacrobazia.setVisibility(View.VISIBLE);
                else expacrobazia.setVisibility(View.INVISIBLE);
                state.edit().putBoolean("compacrobazia", compacrobazia.isChecked()).apply();
                int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("DEX", 10)))+ ((compacrobazia.isChecked()) ? ((expacrobazia.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                acrobazia.setText(suffix + bonus);
            }
        });
        expacrobazia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                state.edit().putBoolean("expacrobazia", expacrobazia.isChecked()).apply();int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("DEX", 10)))+ ((compacrobazia.isChecked()) ? ((expacrobazia.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                acrobazia.setText(suffix + bonus);
            }
        });
        compacrobazia.setChecked(state.getBoolean("compacrobazia", false));
        expacrobazia.setChecked(state.getBoolean("expacrobazia", false));
        bonus = mod((state.getInt("DEX", 10)))+ ((compacrobazia.isChecked()) ? ((expacrobazia.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
        suffix = (bonus >= 0) ? "+" : "";
        acrobazia.setText(suffix + bonus);

        final TextView furtivita = (TextView) findViewById(R.id.furtivita);
        final CheckBox compfurtivita = (CheckBox) findViewById(R.id.compfurtivita);
        final CheckBox expfurtivita = (CheckBox) findViewById(R.id.expfurtivita);
        compfurtivita.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) expfurtivita.setVisibility(View.VISIBLE);
                else expfurtivita.setVisibility(View.INVISIBLE);
                state.edit().putBoolean("compfurtivita", compfurtivita.isChecked()).apply();
                int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("DEX", 10)))+ ((compfurtivita.isChecked()) ? ((expfurtivita.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                furtivita.setText(suffix + bonus);
            }
        });
        expfurtivita.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                state.edit().putBoolean("expfurtivita", expfurtivita.isChecked()).apply();int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("DEX", 10)))+ ((compfurtivita.isChecked()) ? ((expfurtivita.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                furtivita.setText(suffix + bonus);
            }
        });
        compfurtivita.setChecked(state.getBoolean("compfurtivita", false));
        expfurtivita.setChecked(state.getBoolean("expfurtivita", false));
        bonus = mod((state.getInt("DEX", 10)))+ ((compfurtivita.isChecked()) ? ((expfurtivita.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
        suffix = (bonus >= 0) ? "+" : "";
        furtivita.setText(suffix + bonus);

        final TextView rapiditadimano = (TextView) findViewById(R.id.rapiditadimano);
        final CheckBox comprapiditadimano = (CheckBox) findViewById(R.id.comprapiditadimano);
        final CheckBox exprapiditadimano = (CheckBox) findViewById(R.id.exprapiditadimano);
        comprapiditadimano.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) exprapiditadimano.setVisibility(View.VISIBLE);
                else exprapiditadimano.setVisibility(View.INVISIBLE);
                state.edit().putBoolean("comprapiditadimano", comprapiditadimano.isChecked()).apply();
                int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("DEX", 10)))+ ((comprapiditadimano.isChecked()) ? ((exprapiditadimano.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                rapiditadimano.setText(suffix + bonus);
            }
        });
        exprapiditadimano.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                state.edit().putBoolean("exprapiditadimano", exprapiditadimano.isChecked()).apply();int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("DEX", 10)))+ ((comprapiditadimano.isChecked()) ? ((exprapiditadimano.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                rapiditadimano.setText(suffix + bonus);
            }
        });
        comprapiditadimano.setChecked(state.getBoolean("comprapiditadimano", false));
        exprapiditadimano.setChecked(state.getBoolean("exprapiditadimano", false));
        bonus = mod((state.getInt("DEX", 10)))+ ((comprapiditadimano.isChecked()) ? ((exprapiditadimano.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
        suffix = (bonus >= 0) ? "+" : "";
        rapiditadimano.setText(suffix + bonus);

        final TextView resistenzafisica = (TextView) findViewById(R.id.resistenzafisica);
        final CheckBox compresistenzafisica = (CheckBox) findViewById(R.id.compresistenzafisica);
        final CheckBox expresistenzafisica = (CheckBox) findViewById(R.id.expresistenzafisica);
        compresistenzafisica.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) expresistenzafisica.setVisibility(View.VISIBLE);
                else expresistenzafisica.setVisibility(View.INVISIBLE);
                state.edit().putBoolean("compresistenzafisica", compresistenzafisica.isChecked()).apply();
                int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("COS", 10)))+ ((compresistenzafisica.isChecked()) ? ((expresistenzafisica.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                resistenzafisica.setText(suffix + bonus);
            }
        });
        expresistenzafisica.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                state.edit().putBoolean("expresistenzafisica", expresistenzafisica.isChecked()).apply();int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("COS", 10)))+ ((compresistenzafisica.isChecked()) ? ((expresistenzafisica.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                resistenzafisica.setText(suffix + bonus);
            }
        });
        compresistenzafisica.setChecked(state.getBoolean("compresistenzafisica", false));
        expresistenzafisica.setChecked(state.getBoolean("expresistenzafisica", false));
        bonus = mod((state.getInt("COS", 10)))+ ((compresistenzafisica.isChecked()) ? ((expresistenzafisica.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
        suffix = (bonus >= 0) ? "+" : "";
        resistenzafisica.setText(suffix + bonus);

        final TextView investigare = (TextView) findViewById(R.id.investigare);
        final CheckBox compinvestigare = (CheckBox) findViewById(R.id.compinvestigare);
        final CheckBox expinvestigare = (CheckBox) findViewById(R.id.expinvestigare);
        compinvestigare.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) expinvestigare.setVisibility(View.VISIBLE);
                else expinvestigare.setVisibility(View.INVISIBLE);
                state.edit().putBoolean("compinvestigare", compinvestigare.isChecked()).apply();
                int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("INT", 10)))+ ((compinvestigare.isChecked()) ? ((expinvestigare.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                investigare.setText(suffix + bonus);
            }
        });
        expinvestigare.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                state.edit().putBoolean("expinvestigare", expinvestigare.isChecked()).apply();int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("INT", 10))) + ((compinvestigare.isChecked()) ? ((expinvestigare.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                investigare.setText(suffix + bonus);
            }
        });
        compinvestigare.setChecked(state.getBoolean("compinvestigare", false));
        expinvestigare.setChecked(state.getBoolean("expinvestigare", false));
        bonus = mod((state.getInt("INT", 10))) + ((compinvestigare.isChecked()) ? ((expinvestigare.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
        suffix = (bonus >= 0) ? "+" : "";
        investigare.setText(suffix + bonus);

        final TextView arcano = (TextView) findViewById(R.id.arcano);
        final CheckBox comparcano = (CheckBox) findViewById(R.id.comparcano);
        final CheckBox exparcano = (CheckBox) findViewById(R.id.exparcano);
        comparcano.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) exparcano.setVisibility(View.VISIBLE);
                else exparcano.setVisibility(View.INVISIBLE);
                state.edit().putBoolean("comparcano", comparcano.isChecked()).apply();
                int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("INT", 10))) + ((comparcano.isChecked()) ? ((exparcano.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                arcano.setText(suffix + bonus);
            }
        });
        exparcano.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                state.edit().putBoolean("exparcano", exparcano.isChecked()).apply();int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("INT", 10))) + ((comparcano.isChecked()) ? ((exparcano.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                arcano.setText(suffix + bonus);
            }
        });
        comparcano.setChecked(state.getBoolean("comparcano", false));
        exparcano.setChecked(state.getBoolean("exparcano", false));
        bonus = mod((state.getInt("INT", 10))) + ((comparcano.isChecked()) ? ((exparcano.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
        suffix = (bonus >= 0) ? "+" : "";
        arcano.setText(suffix + bonus);

        final TextView storia = (TextView) findViewById(R.id.storia);
        final CheckBox compstoria = (CheckBox) findViewById(R.id.compstoria);
        final CheckBox expstoria = (CheckBox) findViewById(R.id.expstoria);
        compstoria.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) expstoria.setVisibility(View.VISIBLE);
                else expstoria.setVisibility(View.INVISIBLE);
                state.edit().putBoolean("compstoria", compstoria.isChecked()).apply();
                int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("INT", 10))) + ((compstoria.isChecked()) ? ((expstoria.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                storia.setText(suffix + bonus);
            }
        });
        expstoria.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                state.edit().putBoolean("expstoria", expstoria.isChecked()).apply();int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("INT", 10))) + ((compstoria.isChecked()) ? ((expstoria.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                storia.setText(suffix + bonus);
            }
        });
        compstoria.setChecked(state.getBoolean("compstoria", false));
        expstoria.setChecked(state.getBoolean("expstoria", false));
        bonus = mod((state.getInt("INT", 10))) + ((compstoria.isChecked()) ? ((expstoria.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
        suffix = (bonus >= 0) ? "+" : "";
        storia.setText(suffix + bonus);

        final TextView religionefolklore = (TextView) findViewById(R.id.religionefolklore);
        final CheckBox compreligionefolklore = (CheckBox) findViewById(R.id.compreligionefolklore);
        final CheckBox expreligionefolklore = (CheckBox) findViewById(R.id.expreligionefolklore);
        compreligionefolklore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) expreligionefolklore.setVisibility(View.VISIBLE);
                else expreligionefolklore.setVisibility(View.INVISIBLE);
                state.edit().putBoolean("compreligionefolklore", compreligionefolklore.isChecked()).apply();
                int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("INT", 10))) + ((compreligionefolklore.isChecked()) ? ((expreligionefolklore.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                religionefolklore.setText(suffix + bonus);
            }
        });
        expreligionefolklore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                state.edit().putBoolean("expreligionefolklore", expreligionefolklore.isChecked()).apply();int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("INT", 10))) + ((compreligionefolklore.isChecked()) ? ((expreligionefolklore.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                religionefolklore.setText(suffix + bonus);
            }
        });
        compreligionefolklore.setChecked(state.getBoolean("compreligionefolklore", false));
        expreligionefolklore.setChecked(state.getBoolean("expreligionefolklore", false));
        bonus = mod((state.getInt("INT", 10))) + ((compreligionefolklore.isChecked()) ? ((expreligionefolklore.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
        suffix = (bonus >= 0) ? "+" : "";
        religionefolklore.setText(suffix + bonus);

        final TextView natura = (TextView) findViewById(R.id.natura);
        final CheckBox compnatura = (CheckBox) findViewById(R.id.compnatura);
        final CheckBox expnatura = (CheckBox) findViewById(R.id.expnatura);
        compnatura.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) expnatura.setVisibility(View.VISIBLE);
                else expnatura.setVisibility(View.INVISIBLE);
                state.edit().putBoolean("compnatura", compnatura.isChecked()).apply();
                int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("INT", 10))) + ((compnatura.isChecked()) ? ((expnatura.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                natura.setText(suffix + bonus);
            }
        });
        expnatura.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                state.edit().putBoolean("expnatura", expnatura.isChecked()).apply();int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("INT", 10))) + ((compnatura.isChecked()) ? ((expnatura.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                natura.setText(suffix + bonus);
            }
        });
        compnatura.setChecked(state.getBoolean("compnatura", false));
        expnatura.setChecked(state.getBoolean("expnatura", false));
        bonus = mod((state.getInt("INT", 10))) + ((compnatura.isChecked()) ? ((expnatura.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
        suffix = (bonus >= 0) ? "+" : "";
        natura.setText(suffix + bonus);

        final TextView fauna = (TextView) findViewById(R.id.fauna);
        final CheckBox compfauna = (CheckBox) findViewById(R.id.compfauna);
        final CheckBox expfauna = (CheckBox) findViewById(R.id.expfauna);
        compfauna.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) expfauna.setVisibility(View.VISIBLE);
                else expfauna.setVisibility(View.INVISIBLE);
                state.edit().putBoolean("compfauna", compfauna.isChecked()).apply();
                int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("SAG", 10))) + ((compfauna.isChecked()) ? ((expfauna.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                fauna.setText(suffix + bonus);
            }
        });
        expfauna.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                state.edit().putBoolean("expfauna", expfauna.isChecked()).apply();int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("SAG", 10))) + ((compfauna.isChecked()) ? ((expfauna.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                fauna.setText(suffix + bonus);
            }
        });
        compfauna.setChecked(state.getBoolean("compfauna", false));
        expfauna.setChecked(state.getBoolean("expfauna", false));
        bonus = mod((state.getInt("SAG", 10))) + ((compfauna.isChecked()) ? ((expfauna.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
        suffix = (bonus >= 0) ? "+" : "";
        fauna.setText(suffix + bonus);

        final TextView sopravvivenza = (TextView) findViewById(R.id.sopravvivenza);
        final CheckBox compsopravvivenza = (CheckBox) findViewById(R.id.compsopravvivenza);
        final CheckBox expsopravvivenza = (CheckBox) findViewById(R.id.expsopravvivenza);
        compsopravvivenza.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) expsopravvivenza.setVisibility(View.VISIBLE);
                else expsopravvivenza.setVisibility(View.INVISIBLE);
                state.edit().putBoolean("compsopravvivenza", compsopravvivenza.isChecked()).apply();
                int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("SAG", 10))) + ((compsopravvivenza.isChecked()) ? ((expsopravvivenza.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                sopravvivenza.setText(suffix + bonus);
            }
        });
        expsopravvivenza.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                state.edit().putBoolean("expsopravvivenza", expsopravvivenza.isChecked()).apply();int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("SAG", 10))) + ((compsopravvivenza.isChecked()) ? ((expsopravvivenza.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                sopravvivenza.setText(suffix + bonus);
            }
        });
        compsopravvivenza.setChecked(state.getBoolean("compsopravvivenza", false));
        expsopravvivenza.setChecked(state.getBoolean("expsopravvivenza", false));
        bonus = mod((state.getInt("SAG", 10))) + ((compsopravvivenza.isChecked()) ? ((expsopravvivenza.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
        suffix = (bonus >= 0) ? "+" : "";
        sopravvivenza.setText(suffix + bonus);

        final TextView medicina = (TextView) findViewById(R.id.medicina);
        final CheckBox compmedicina = (CheckBox) findViewById(R.id.compmedicina);
        final CheckBox expmedicina = (CheckBox) findViewById(R.id.expmedicina);
        compmedicina.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) expmedicina.setVisibility(View.VISIBLE);
                else expmedicina.setVisibility(View.INVISIBLE);
                state.edit().putBoolean("compmedicina", compmedicina.isChecked()).apply();
                int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("SAG", 10))) + ((compmedicina.isChecked()) ? ((expmedicina.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                medicina.setText(suffix + bonus);
            }
        });
        expmedicina.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                state.edit().putBoolean("expmedicina", expmedicina.isChecked()).apply();int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("SAG", 10))) + ((compmedicina.isChecked()) ? ((expmedicina.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                medicina.setText(suffix + bonus);
            }
        });
        compmedicina.setChecked(state.getBoolean("compmedicina", false));
        expmedicina.setChecked(state.getBoolean("expmedicina", false));
        bonus = mod((state.getInt("SAG", 10))) + ((compmedicina.isChecked()) ? ((expmedicina.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
        suffix = (bonus >= 0) ? "+" : "";
        medicina.setText(suffix + bonus);

        final TextView percezione = (TextView) findViewById(R.id.percezione);
        final CheckBox comppercezione = (CheckBox) findViewById(R.id.comppercezione);
        final CheckBox exppercezione = (CheckBox) findViewById(R.id.exppercezione);
        comppercezione.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) exppercezione.setVisibility(View.VISIBLE);
                else exppercezione.setVisibility(View.INVISIBLE);
                state.edit().putBoolean("comppercezione", comppercezione.isChecked()).apply();
                int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("SAG", 10))) + ((comppercezione.isChecked()) ? ((exppercezione.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                percezione.setText(suffix + bonus);
            }
        });
        exppercezione.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                state.edit().putBoolean("exppercezione", exppercezione.isChecked()).apply();int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("SAG", 10))) + ((comppercezione.isChecked()) ? ((exppercezione.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                percezione.setText(suffix + bonus);
            }
        });
        comppercezione.setChecked(state.getBoolean("comppercezione", false));
        exppercezione.setChecked(state.getBoolean("exppercezione", false));
        bonus = mod((state.getInt("SAG", 10))) + ((comppercezione.isChecked()) ? ((exppercezione.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
        suffix = (bonus >= 0) ? "+" : "";
        percezione.setText(suffix + bonus);

        final TextView intuizione = (TextView) findViewById(R.id.intuizione);
        final CheckBox compintuizione = (CheckBox) findViewById(R.id.compintuizione);
        final CheckBox expintuizione = (CheckBox) findViewById(R.id.expintuizione);
        compintuizione.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) expintuizione.setVisibility(View.VISIBLE);
                else expintuizione.setVisibility(View.INVISIBLE);
                state.edit().putBoolean("compintuizione", compintuizione.isChecked()).apply();
                int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("SAG", 10))) + ((compintuizione.isChecked()) ? ((expintuizione.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                intuizione.setText(suffix + bonus);
            }
        });
        expintuizione.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                state.edit().putBoolean("expintuizione", expintuizione.isChecked()).apply();int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("SAG", 10))) + ((compintuizione.isChecked()) ? ((expintuizione.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                intuizione.setText(suffix + bonus);
            }
        });
        compintuizione.setChecked(state.getBoolean("compintuizione", false));
        expintuizione.setChecked(state.getBoolean("expintuizione", false));
        bonus = mod((state.getInt("SAG", 10))) + ((compintuizione.isChecked()) ? ((expintuizione.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
        suffix = (bonus >= 0) ? "+" : "";
        intuizione.setText(suffix + bonus);

        final TextView intimidire = (TextView) findViewById(R.id.intimidire);
        final CheckBox compintimidire = (CheckBox) findViewById(R.id.compintimidire);
        final CheckBox expintimidire = (CheckBox) findViewById(R.id.expintimidire);
        compintimidire.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) expintimidire.setVisibility(View.VISIBLE);
                else expintimidire.setVisibility(View.INVISIBLE);
                state.edit().putBoolean("compintimidire", compintimidire.isChecked()).apply();
                int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("CAR", 10))) + ((compintimidire.isChecked()) ? ((expintimidire.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                intimidire.setText(suffix + bonus);
            }
        });
        expintimidire.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                state.edit().putBoolean("expintimidire", expintimidire.isChecked()).apply();int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("CAR", 10))) + ((compintimidire.isChecked()) ? ((expintimidire.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                intimidire.setText(suffix + bonus);
            }
        });
        compintimidire.setChecked(state.getBoolean("compintimidire", false));
        expintimidire.setChecked(state.getBoolean("expintimidire", false));
        bonus = mod((state.getInt("CAR", 10))) + ((compintimidire.isChecked()) ? ((expintimidire.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
        suffix = (bonus >= 0) ? "+" : "";
        intimidire.setText(suffix + bonus);

        final TextView ingannare = (TextView) findViewById(R.id.ingannare);
        final CheckBox compingannare = (CheckBox) findViewById(R.id.compingannare);
        final CheckBox expingannare = (CheckBox) findViewById(R.id.expingannare);
        compingannare.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) expingannare.setVisibility(View.VISIBLE);
                else expingannare.setVisibility(View.INVISIBLE);
                state.edit().putBoolean("compingannare", compingannare.isChecked()).apply();
                int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("CAR", 10))) + ((compingannare.isChecked()) ? ((expingannare.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                ingannare.setText(suffix + bonus);
            }
        });
        expingannare.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                state.edit().putBoolean("expingannare", expingannare.isChecked()).apply();int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("CAR", 10))) + ((compingannare.isChecked()) ? ((expingannare.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                ingannare.setText(suffix + bonus);
            }
        });
        compingannare.setChecked(state.getBoolean("compingannare", false));
        expingannare.setChecked(state.getBoolean("expingannare", false));
        bonus = mod((state.getInt("CAR", 10))) + ((compingannare.isChecked()) ? ((expingannare.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
        suffix = (bonus >= 0) ? "+" : "";
        ingannare.setText(suffix + bonus);

        final TextView intrattenere = (TextView) findViewById(R.id.intrattenere);
        final CheckBox compintrattenere = (CheckBox) findViewById(R.id.compintrattenere);
        final CheckBox expintrattenere = (CheckBox) findViewById(R.id.expintrattenere);
        compintrattenere.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) expintrattenere.setVisibility(View.VISIBLE);
                else expintrattenere.setVisibility(View.INVISIBLE);
                state.edit().putBoolean("compintrattenere", compintrattenere.isChecked()).apply();
                int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("CAR", 10))) + ((compintrattenere.isChecked()) ? ((expintrattenere.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                intrattenere.setText(suffix + bonus);
            }
        });
        expintrattenere.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                state.edit().putBoolean("expintrattenere", expintrattenere.isChecked()).apply();int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("CAR", 10))) + ((compintrattenere.isChecked()) ? ((expintrattenere.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                intrattenere.setText(suffix + bonus);
            }
        });
        compintrattenere.setChecked(state.getBoolean("compintrattenere", false));
        expintrattenere.setChecked(state.getBoolean("expintrattenere", false));
        bonus = mod((state.getInt("CAR", 10))) + ((compintrattenere.isChecked()) ? ((expintrattenere.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
        suffix = (bonus >= 0) ? "+" : "";
        intrattenere.setText(suffix + bonus);

        final TextView persuadere = (TextView) findViewById(R.id.persuadere);
        final CheckBox comppersuadere = (CheckBox) findViewById(R.id.comppersuadere);
        final CheckBox exppersuadere = (CheckBox) findViewById(R.id.exppersuadere);
        comppersuadere.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) exppersuadere.setVisibility(View.VISIBLE);
                else exppersuadere.setVisibility(View.INVISIBLE);
                state.edit().putBoolean("comppersuadere", comppersuadere.isChecked()).apply();
                int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("CAR", 10))) + ((comppersuadere.isChecked()) ? ((exppersuadere.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                persuadere.setText(suffix + bonus);
            }
        });
        exppersuadere.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                state.edit().putBoolean("exppersuadere", exppersuadere.isChecked()).apply();int lv = state.getInt("pglv", 1);
                int bonus = mod((state.getInt("CAR", 10))) + ((comppersuadere.isChecked()) ? ((exppersuadere.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
                String suffix = (bonus >= 0) ? "+" : "";
                persuadere.setText(suffix + bonus);
            }
        });
        comppersuadere.setChecked(state.getBoolean("comppersuadere", false));
        exppersuadere.setChecked(state.getBoolean("exppersuadere", false));
        bonus = mod((state.getInt("CAR", 10))) + ((comppersuadere.isChecked()) ? ((exppersuadere.isChecked()) ? prof[lv-1]*2 : prof[lv-1]) : 0);
        suffix = (bonus >= 0) ? "+" : "";
        persuadere.setText(suffix + bonus);

        EditText linguetxt = (EditText) findViewById(R.id.linguetxt);
        linguetxt.setText(state.getString("linguetxt", ""));
        linguetxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                state.edit().putString("linguetxt", editable.toString()).apply();
            }
        });
        linguetxt.clearFocus();

        EditText armitxt = (EditText) findViewById(R.id.armitxt);
        armitxt.setText(state.getString("armitxt", ""));
        armitxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                state.edit().putString("armitxt", editable.toString()).apply();
            }
        });
        armitxt.clearFocus();

        EditText talentitxt = (EditText) findViewById(R.id.talentitxt);
        talentitxt.setText(state.getString("talentitxt", ""));
        talentitxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                state.edit().putString("talentitxt", editable.toString()).apply();
            }
        });
        talentitxt.clearFocus();

        EditText abilitatxt = (EditText) findViewById(R.id.abilitatxt);
        abilitatxt.setText(state.getString("abilitatxt", ""));
        abilitatxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                state.edit().putString("abilitatxt", editable.toString()).apply();
            }
        });
        abilitatxt.clearFocus();

        EditText credititxt = (EditText) findViewById(R.id.credititxt);
        credititxt.setText(state.getInt("crediti", 0) + "");
        credititxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    state.edit().putInt("crediti", Integer.parseInt(editable.toString())).apply();
                } catch (NumberFormatException ex) {
                    state.edit().putInt("crediti", 0).apply();
                }
            }
        });
        credititxt.clearFocus();

        EditText invtxt = (EditText) findViewById(R.id.invtxt);
        invtxt.setText(state.getString("inv", ""));
        invtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                saveSchedaPG();

            }

            @Override
            public void afterTextChanged(Editable editable) {
                state.edit().putString("inv", editable.toString()).apply();
            }
        });
        invtxt.clearFocus();

        EditText backgroundtxt = (EditText) findViewById(R.id.backgroundtxt);
        backgroundtxt.setText(state.getString("background", ""));
        backgroundtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                state.edit().putString("background", editable.toString()).apply();
            }
        });
        backgroundtxt.clearFocus();

        rangedatks.removeAllViews();

        TableRow header = (TableRow) LayoutInflater.from(HomeActivity.this).inflate(R.layout.rangedrow, null);
        TextView name = (TextView) header.findViewById(R.id.rangedname);
        TextView range = (TextView) header.findViewById(R.id.range);
        TextView bonusrange = (TextView) header.findViewById(R.id.rangedbonus);
        TextView damage = (TextView) header.findViewById(R.id.rangeddamage);
        Button removebtn = (Button) header.findViewById(R.id.removeranged);
        name.setText("Nome");
        range.setText("Range");
        bonusrange.setText("Bonus DEX");
        damage.setText("Danno");
        removebtn.setText("");
        rangedatks.addView(header);

        final Set<String> rangedset = new HashSet<>(state.getStringSet("rangedatks", new HashSet<String>()));
        for (String str : rangedset) {
            String[] ranged = str.split("%");
            final TableRow newrow = (TableRow) LayoutInflater.from(HomeActivity.this).inflate(R.layout.rangedrow, null);
            name = (TextView) newrow.findViewById(R.id.rangedname);
            range = (TextView) newrow.findViewById(R.id.range);
            bonusrange = (TextView) newrow.findViewById(R.id.rangedbonus);
            damage = (TextView) newrow.findViewById(R.id.rangeddamage);

            int bonusb = mod(state.getInt("DEX", 10));
            String suffixb = (bonus >= 0) ? "+" : "";

            name.setText(ranged[0]);
            range.setText(ranged[1]);
            bonusrange.setText(suffixb + bonusb);
            damage.setText(ranged[2]);

            removebtn = (Button) newrow.findViewById(R.id.removeranged);
            final String strf = str;
            removebtn.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Set<String> rangedset = new HashSet<>(state.getStringSet("rangedatks", new HashSet<String>()));
                    rangedset.remove(strf);
                    state.edit().putStringSet("rangedatks", rangedset).apply();
                    rangedatks.removeView(newrow);
                    return true;
                }
            });
            rangedatks.addView(newrow);
        }

        meleeatks.removeAllViews();

        header = (TableRow) LayoutInflater.from(HomeActivity.this).inflate(R.layout.meleerow, null);
        name = (TextView) header.findViewById(R.id.meleename);
        bonusrange = (TextView) header.findViewById(R.id.meleebonus);
        damage = (TextView) header.findViewById(R.id.meleedamage);
        removebtn = (Button) header.findViewById(R.id.removemelee);
        name.setText("Nome");
        bonusrange.setText("Bonus FOR");
        damage.setText("Danno");
        removebtn.setText("");
        meleeatks.addView(header);

        final Set<String> meleeset = new HashSet<>(state.getStringSet("meleeatks", new HashSet<String>()));
        for (String str : meleeset) {
            String[] melee = str.split("%");
            final TableRow newrow = (TableRow) LayoutInflater.from(HomeActivity.this).inflate(R.layout.meleerow, null);
            name = (TextView) newrow.findViewById(R.id.meleename);
            bonusrange = (TextView) newrow.findViewById(R.id.meleebonus);
            damage = (TextView) newrow.findViewById(R.id.meleedamage);

            int bonusb = mod(state.getInt("FOR", 10));
            String suffixb = (bonus >= 0) ? "+" : "";

            name.setText(melee[0]);
            bonusrange.setText(suffixb + bonusb);
            damage.setText(melee[1]);

            removebtn = (Button) newrow.findViewById(R.id.removemelee);
            final String strf = str;
            removebtn.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Set<String> meleeset = new HashSet<>(state.getStringSet("meleeatks", new HashSet<String>()));
                    meleeset.remove(strf);
                    state.edit().putStringSet("meleeatks", meleeset).apply();
                    meleeatks.removeView(newrow);
                    return true;
                }
            });
            meleeatks.addView(newrow);
        }

        addranged.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TableRow newrow = (TableRow) LayoutInflater.from(HomeActivity.this).inflate(R.layout.rangedrow, null);
                RangedDialog inputdialog = new RangedDialog(HomeActivity.this, state, newrow, rangedatks);
                inputdialog.show();
            }
        });

        addmelee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TableRow newrow = (TableRow) LayoutInflater.from(HomeActivity.this).inflate(R.layout.meleerow, null);
                MeleeDialog inputdialog = new MeleeDialog(HomeActivity.this, state, newrow, meleeatks);
                inputdialog.show();
            }
        });

        /*spellapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.package.address");
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }

            }
        });*/
        spellapp.setVisibility(View.GONE);

        saveSchedaPG();
    }

    private void preparaRisorse() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.framelyt, new QuriaFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new QuriaFragment();
                        break;
                    case 1:
                        fragment = new PGFragment();
                        break;
                    case 2:
                        fragment = new CampagnaFragment();
                        break;
                    case 3:
                        fragment = new PersonalFragment();
                        break;
                }
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.framelyt, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void saveSchedaPG() {
        String str = new StringBuilder("").append(state.getString("pgname", null)).append("|")
                .append(state.getString("pgclass", null)).append("|")
                .append(state.getInt("pglv", 1)).append("|")
                .append(state.getInt("CA", 10)).append("|")
                .append(state.getInt("PF", -1)).append("|")
                .append(state.getInt("PFMAX", -1)).append("|")
                .append(state.getInt("FOR", 10)).append("|")
                .append(state.getInt("DEX", 10)).append("|")
                .append(state.getInt("COS", 10)).append("|")
                .append(state.getInt("INT", 10)).append("|")
                .append(state.getInt("SAG", 10)).append("|")
                .append(state.getInt("CAR", 10)).append("|")
                .append(state.getBoolean("comptsfor", false)).append("|")
                .append(state.getBoolean("comptsdex", false)).append("|")
                .append(state.getBoolean("comptscos", false)).append("|")
                .append(state.getBoolean("comptsint", false)).append("|")
                .append(state.getBoolean("comptssag", false)).append("|")
                .append(state.getBoolean("comptscar", false)).append("|")
                .append(state.getBoolean("compatletica", false)).append("|")
                .append(state.getBoolean("expatletica", false)).append("|")
                .append(state.getBoolean("compacrobazia", false)).append("|")
                .append(state.getBoolean("expacrobazia", false)).append("|")
                .append(state.getBoolean("compfurtivita", false)).append("|")
                .append(state.getBoolean("expfurtivita", false)).append("|")
                .append(state.getBoolean("comprapiditadimano", false)).append("|")
                .append(state.getBoolean("exprapiditadimano", false)).append("|")
                .append(state.getBoolean("compresistenzafisica", false)).append("|")
                .append(state.getBoolean("expresistenzafisica", false)).append("|")
                .append(state.getBoolean("compinvestigare", false)).append("|")
                .append(state.getBoolean("expinvestigare", false)).append("|")
                .append(state.getBoolean("comparcano", false)).append("|")
                .append(state.getBoolean("exparcano", false)).append("|")
                .append(state.getBoolean("compstoria", false)).append("|")
                .append(state.getBoolean("expstoria", false)).append("|")
                .append(state.getBoolean("compreligionefolklore", false)).append("|")
                .append(state.getBoolean("expreligionefolklore", false)).append("|")
                .append(state.getBoolean("compreligionefolklore", false)).append("|")
                .append(state.getBoolean("expreligionefolklore", false)).append("|")
                .append(state.getBoolean("compnatura", false)).append("|")
                .append(state.getBoolean("expnatura", false)).append("|")
                .append(state.getBoolean("compfauna", false)).append("|")
                .append(state.getBoolean("expfauna", false)).append("|")
                .append(state.getBoolean("compsopravvivenza", false)).append("|")
                .append(state.getBoolean("expsopravvivenza", false)).append("|")
                .append(state.getBoolean("compmedicina", false)).append("|")
                .append(state.getBoolean("expmedicina", false)).append("|")
                .append(state.getBoolean("comppercezione", false)).append("|")
                .append(state.getBoolean("exppercezione", false)).append("|")
                .append(state.getBoolean("compintuizione", false)).append("|")
                .append(state.getBoolean("expintuizione", false)).append("|")
                .append(state.getBoolean("compintimidire", false)).append("|")
                .append(state.getBoolean("expintimidire", false)).append("|")
                .append(state.getBoolean("compingannare", false)).append("|")
                .append(state.getBoolean("expingannare", false)).append("|")
                .append(state.getBoolean("compintrattenere", false)).append("|")
                .append(state.getBoolean("expintrattenere", false)).append("|")
                .append(state.getBoolean("comppersuadere", false)).append("|")
                .append(state.getBoolean("exppersuadere", false)).append("|")
                .append(state.getInt("crediti", 0)).append("|")
                .append(state.getString("inv", "")).append("\n")
                .toString();
        Log.d("FILE", str);
        FileHelper.saveToFile(str, getApplicationContext(), state.getString("pgname", null) + "PGDATA.txt");
    }
    
    public static int mod(int punteggio) {
        double pnt = punteggio;
        return (int) floor(((pnt - 10) / 2));
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

    public void updateFromWEB() {
        final String urlstory = "http://quria.altervista.org/story.json";
        final String urlloc = "http://quria.altervista.org/locations.txt";
        final String filestory = "story.json";
        final String fileloc = "locations.txt";
        final ProgressDialog dialog = ProgressDialog.show(this, "Aggiornamento", "Sto aggiornando i dati dall'interlink", true);

        Thread t = new Thread(new Runnable(){
            public void run() {
                dialog.show();
                StringBuilder data = new StringBuilder(""); //to read each line
                try {
                    // Create a URL for the desired page
                    URL url = new URL(urlstory); //My text file location
                    //First open the connection
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String str;
                    while ((str = in.readLine()) != null) {
                        data.append(str);
                    }
                    in.close();
                    final String json = data.toString();
                    FileHelper.saveToFile(json, HomeActivity.this.getApplicationContext(), filestory);

                    HomeActivity.this.runOnUiThread(new Runnable(){
                        public void run(){
                            Toast.makeText(HomeActivity.this.getApplicationContext(), "Dati aggiornati dall'interlink", Toast.LENGTH_SHORT).show();
                            putJsonInRecview(json);
                            dialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    Log.d("WEBUPDATE",e.toString());
                    HomeActivity.this.runOnUiThread(new Runnable(){
                        public void run(){
                            Toast.makeText(HomeActivity.this.getApplicationContext(), "Utilizzo dati salvati in locale", Toast.LENGTH_SHORT).show();
                            putJsonInRecview("");
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
        t.start();

        Thread p = new Thread(new Runnable(){
            public void run() {
                StringBuilder data = new StringBuilder(""); //to read each line
                try {
                    // Create a URL for the desired page
                    URL url = new URL(urlloc); //My text file location
                    //First open the connection
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String str;
                    while ((str = in.readLine()) != null) {
                        data.append(str);
                    }
                    in.close();
                    final String locations = data.toString();
                    FileHelper.saveToFile(locations, HomeActivity.this.getApplicationContext(), fileloc);
                    updateLocations(locations);
                } catch (Exception e) {
                    updateLocations("");
                }
            }
        });
        p.start();
    }

    public void updateLocations(String locations){
        locationstags = new ArrayList<>();
        locationspoints = new ArrayList<>();
        if (locations != "") {
            String[] locvect = locations.split(":");
            for (int i = 0; i < locvect.length; i = i + 3) {
                locationstags.add(locvect[i]);
                locationspoints.add(new PointF(Float.parseFloat(locvect[i + 1]), Float.parseFloat(locvect[i + 2])));
            }
        } else {
            locations = FileHelper.ReadFile(this.getApplicationContext(), "locations.txt");
            if (locations != "-erorr") {
                String[] locvect = locations.split(":");
                for (int i = 0; i < locvect.length; i = i + 3) {
                    locationstags.add(locvect[i]);
                    locationspoints.add(new PointF(Float.parseFloat(locvect[i + 1]), Float.parseFloat(locvect[i + 2])));
                }
            }
        }
        HomeActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                preparaAtlante();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //TODO tasto indietro
        super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
