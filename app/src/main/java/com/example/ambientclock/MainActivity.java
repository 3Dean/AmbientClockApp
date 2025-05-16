package com.example.firetvclockapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends FragmentActivity {
    private TextView timeTextView;
    private TextView dateTextView;
    private TextView formatToggleHint;
    private boolean is24HourFormat = false;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private SimpleDateFormat timeFormat12;
    private SimpleDateFormat timeFormat24;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeTextView = findViewById(R.id.time_text);
        dateTextView = findViewById(R.id.date_text);
        formatToggleHint = findViewById(R.id.format_toggle_hint);

        timeFormat12 = new SimpleDateFormat("h:mm:ss a", Locale.getDefault());
        timeFormat24 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault());

        startClock();
    }

    private void startClock() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                updateTime();
                handler.postDelayed(this, 1000); // Update every second
            }
        });
    }

    private void updateTime() {
        Date now = new Date();
        timeTextView.setText(is24HourFormat ?
                timeFormat24.format(now) : timeFormat12.format(now));
        dateTextView.setText(dateFormat.format(now));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Toggle between 12/24 hour format when SELECT button is pressed
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
            is24HourFormat = !is24HourFormat;
            updateTime();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Hide system UI (immersive mode)
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
