package com.lovanto.keuangan.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.lovanto.keuangan.adapter.ViewPagerAdapter;
import com.lovanto.keuangan.fragment.HomeFragment;
import com.lovanto.keuangan.fragment.PendapatanFragment;
import com.lovanto.keuangan.fragment.PengeluaranFragment;
import com.lovanto.keuangan.R;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static MainActivity main;
    public ViewPager viewPager;
    public BottomNavigationView navigation;
    boolean doubleBackToExitPressedOnce = false;

    MenuItem prevMenuItem;
    PendapatanFragment chatFragment;
    HomeFragment homeFragment;
    PengeluaranFragment contactsFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(MainActivity.this);
        navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
        main = this;

        viewPager = (ViewPager) findViewById(R.id.fragment_container);
        setupViewPager(viewPager);
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                navigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = navigation.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        ViewPager viewPager = (ViewPager) findViewById(R.id.fragment_container);
        BottomNavigationView navigation = findViewById(R.id.navigation);

        switch (item.getItemId()) {
            case R.id.navigation_home:
                setupViewPager(viewPager);
                viewPager.setCurrentItem(1);
                navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
                break;

            case R.id.navigation_pendapatan:
                setupViewPager(viewPager);
                viewPager.setCurrentItem(0);
                navigation.getMenu().findItem(R.id.navigation_pendapatan).setChecked(true);
                break;

            case R.id.navigation_pengeluaran:
                setupViewPager(viewPager);
                viewPager.setCurrentItem(2);
                navigation.getMenu().findItem(R.id.navigation_pengeluaran).setChecked(true);
                break;
        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Klik lagi untuk keluar aplikasi", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        chatFragment = new PendapatanFragment();
        homeFragment = new HomeFragment();
        contactsFragment = new PengeluaranFragment();
        adapter.addFragment(chatFragment);
        adapter.addFragment(homeFragment);
        adapter.addFragment(contactsFragment);
        viewPager.setAdapter(adapter);
    }
}
