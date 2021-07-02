package com.example.myth.lighton;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "myth";
    private TextView tv;
    //String s = "192.168.1.111";
    SharedPreferences sharedPref;
    EditText et;
    EditText et2;
    private HandlerThread ht;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (ht != null) ht.quit();
        ht = new HandlerThread("ht");
        ht.start();
        final Handler h = new Handler(ht.getLooper());
        final long MINUTE = 1000 * 5 * 60;
        new Thread() {
            @Override
            public void run() {
                h.postDelayed(() -> getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON), MINUTE);   //5 minute
            }
        }.start();
        String s1 = sharedPref.getString("room1", "Light room 1");
        String s2 = sharedPref.getString("room2", "Light room 2");
        if (s1 != null) {
            Log.i(TAG, s1);
        }
        if (s2 != null) {
            Log.i(TAG, s2);
        }
        et = findViewById(R.id.editText);
        et.setText(s1);
        et2 = findViewById(R.id.editText2);
        et2.setText(s2);

        et2.setFocusable(false);
        et.setFocusable(false);
//        if (someCondition)
//            editTextField.setFocusable(false);
//        else
//            editTextField.setFocusableInTouchMode(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_m, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.about) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setPositiveButton("OK", null);
            adb.setTitle("Light On/Off v1.4");
            adb.setMessage("Development: Stark C.");
            AlertDialog ad = adb.create();
            ad.show();
        } else if (item.getItemId() == R.id.contents) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setPositiveButton("OK", null);
            adb.setTitle("Help");
            adb.setMessage("???");
            AlertDialog ad = adb.create();
            ad.show();
        } else if (item.getItemId() == R.id.ip) {
            String s = sharedPref.getString("user_id", "192.168.1.111");
            final AlertDialog.Builder adb = new AlertDialog.Builder(this);
            final EditText userInput = new EditText(this);
            adb.setView(userInput);
            adb.setPositiveButton("OK", (dialog, id) -> {
                String sip = userInput.getText().toString();
                sharedPref.edit().putString("user_id", sip).apply();
            });
            adb.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            adb.setTitle("Set IP:");
            adb.setMessage("IP : " + s);
            AlertDialog ad = adb.create();
            ad.show();
        } else if (item.getItemId() == R.id.edit) {
            et.setFocusableInTouchMode(true);
            et2.setFocusableInTouchMode(true);
        } else if (item.getItemId() == R.id.editoff) {
            et = findViewById(R.id.editText);
            String room1 = et.getText().toString();
            et2 = findViewById(R.id.editText2);
            String room2 = et2.getText().toString();
            sharedPref.edit().putString("room1", room1).apply();
            sharedPref.edit().putString("room2", room2).apply();
            et.setFocusable(false);
            et2.setFocusable(false);
        }
        return super.onOptionsItemSelected(item);
    }

    public void offM1(View v) {
        tv = findViewById(R.id.textView);
        if (tv.getText().equals("ON")) {
            tv.setText(getResources().getString(R.string.off));
            tv.setTextColor(Color.MAGENTA);
            conThread(v);
        }
    }

    public void onM1(View v) {
        tv = findViewById(R.id.textView);
        if (tv.getText().equals("OFF")) {
            tv.setText(getResources().getString(R.string.on));
            tv.setTextColor(Color.GREEN);
            conThread(v);
        }
    }

    public void offM2(View v) {
        tv = findViewById(R.id.textView4);
        if (tv.getText().equals("ON")) {
            tv.setText(getResources().getString(R.string.off));
            tv.setTextColor(Color.MAGENTA);
            conThread(v);
        }
    }

    public void onM2(View v) {
        tv = findViewById(R.id.textView4);
        if (tv.getText().equals("OFF")) {
            tv.setText(getResources().getString(R.string.on));
            tv.setTextColor(Color.GREEN);
            conThread(v);
        }
    }

    private void conThread(final View v) {

        Thread th = new Thread(new Runnable() {
            final String s = sharedPref.getString("user_id", "192.168.1.111");

            public void run() {
                URL url;
                try {
                    int id = v.getId();
                    if (id == R.id.button) {
                        url = new URL("http://" + s + "/?pin=OFF");
                        Log.i(TAG, "conThread: button1");
                        Log.i(TAG, url.toString());
                    } else if (id == R.id.button2) {
                        url = new URL("http://" + s + "/?pin=ON");
                        Log.i(TAG, "conThread: button2");
                        Log.i(TAG, url.toString());
                    } else if (id == R.id.button3) {
                        url = new URL("http://" + s + "/?pin1=OFF");
                        Log.i(TAG, "conThread: button3");
                        Log.i(TAG, url.toString());
                    } else if (id == R.id.button4) {
                        url = new URL("http://" + s + "/?pin1=ON");
                        Log.i(TAG, "conThread: button4");
                        Log.i(TAG, url.toString());
                    } else {
                        url = null;
                    }
                    HttpURLConnection huc;
                    if (url != null) {
                        huc = (HttpURLConnection) url.openConnection();
                        InputStream in = huc.getInputStream();
                        in.close();
                    }
                    //InputStream in = new BufferedInputStream(huc.getInputStream());
                    //new BufferedReader(new InputStreamReader(in));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        th.start();
        Toast t = Toast.makeText(v.getContext(), "Wait...", Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL, 0, 0);
//        ViewGroup vg = (ViewGroup) t.getView();
//        TextView tev = null;
//        if (vg != null) {
//            tev = (TextView) vg.getChildAt(0);
//        }
//        if (tev != null) {
//            tev.setTextSize(25);
//        }
//        if (tev != null) {
//            tev.setTextColor(Color.BLUE);
//        }
        t.show();
    }
}
/*
*  try {
                    switch (v.getId()) {
                        case R.id.button:
                            url = new URL("http://" + s + "/?pin=OFF");
                            Log.i(TAG, "conThread: button1");
                            Log.i(TAG, url.toString());
                            break;
                        case R.id.button2:
                            url = new URL("http://" + s + "/?pin=ON");
                            Log.i(TAG, "conThread: button2");
                            Log.i(TAG, url.toString());
                            break;
                        case R.id.button3:
                            url = new URL("http://" + s + "/?pin1=OFF");
                            Log.i(TAG, "conThread: button3");
                            Log.i(TAG, url.toString());
                            break;
                        case R.id.button4:
                            url = new URL("http://" + s + "/?pin1=ON");
                            Log.i(TAG, "conThread: button4");
                            Log.i(TAG, url.toString());
                            break;
                        default:
                            url = null;
                    }
* */