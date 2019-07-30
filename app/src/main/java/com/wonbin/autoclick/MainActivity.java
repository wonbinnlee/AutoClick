package com.wonbin.autoclick;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

/**
 * Created by wilburnLee on 2019/4/22.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mStart;
    private EditText mInterval;
    private RadioGroup mCheckMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mStart = findViewById(R.id.start);
        mInterval = findViewById(R.id.interval);
        mCheckMode = findViewById(R.id.check_mode);
        mStart.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, AutoService.class);
        switch (v.getId()) {
            case R.id.start:
                intent.putExtra(AutoService.ACTION, AutoService.SHOW);
                intent.putExtra("interval", Integer.valueOf(mInterval.getText().toString()));
                int id = mCheckMode.getCheckedRadioButtonId();
                intent.putExtra(AutoService.MODE, id == R.id.swipe ? AutoService.SWIPE : AutoService.TAP);
                break;
//            case R.id.btn_hide:
////                intent.putExtra(AutoService.ACTION, AutoService.HIDE);
////                break;
        }
        startService(intent);
        finish();
    }
}
