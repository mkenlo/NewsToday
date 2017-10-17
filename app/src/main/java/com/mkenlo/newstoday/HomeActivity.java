package com.mkenlo.newstoday;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity  {

    public String defaultNewsSection = null;
    BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment frag = selectFragment(item);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, frag);
            fragmentTransaction.commit();
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (!isDeviceConnectedToInternet()) {
            showNotif("Unable to connect to INTERNET");
        }
        defaultNewsSection = getResources().getString(R.string.dummy_section);
        Fragment frag = NewsFragment.newInstance(defaultNewsSection);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, frag);
        fragmentTransaction.commit();
    }

    public boolean isDeviceConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() != null) ? true : false;
    }

    public void showNotif(String msg) {
        Snackbar snackbar = Snackbar.make(navigation, msg, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        snackbar.show();
    }

    public Fragment selectFragment(MenuItem item) {
        Fragment frag = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                frag = NewsFragment.newInstance(defaultNewsSection);
                break;
            case R.id.navigation_dashboard:
                frag = new DashboardFragment();
                break;
            case R.id.navigation_notifications:
                frag = new SettingsFragment();
        }

        return frag;
    }
}
