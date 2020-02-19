package com.lovanto.keuangan.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lovanto.keuangan.R;

public class TentangActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang);
        this.setTitle("Data Developer");
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
