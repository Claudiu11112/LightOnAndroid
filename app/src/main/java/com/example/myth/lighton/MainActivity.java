package com.example.myth.lighton;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "myth";
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            adb.setTitle("Light On v1.2");
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
            public void run() {
                URL url;
                try {
                    switch (v.getId()) {
                        case R.id.button:
                            url = new URL("http://192.168.1.111/?pin=OFF");
                            Log.i(TAG, "conThread: button1");
                            break;
                        case R.id.button2:
                            url = new URL("http://192.168.1.111/?pin=ON");
                            Log.i(TAG, "conThread: button2");
                            break;
                        case R.id.button3:
                            url = new URL("http://192.168.1.111/?pin1=OFF");
                            Log.i(TAG, "conThread: button3");
                            break;
                        case R.id.button4:
                            url = new URL("http://192.168.1.111/?pin1=ON");
                            Log.i(TAG, "conThread: button4");
                            break;
                        default:
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
        t.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        ViewGroup vg = (ViewGroup) t.getView();
        TextView tev = (TextView) vg.getChildAt(0);
        tev.setTextSize(30);
        t.show();
    }
}
