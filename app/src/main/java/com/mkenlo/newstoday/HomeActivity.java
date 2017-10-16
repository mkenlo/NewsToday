package com.mkenlo.newstoday;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;

public class HomeActivity extends AppCompatActivity implements OnTaskCompleted {

    public JSONArray newsList;
    public FetchNewsTask myTask;
    public String defaultNewsSection = null;

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

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (!isDeviceConnectedToInternet()) {
            Log.d("DEBUG", "internet connection");
        }

        if (savedInstanceState != null)
            restoreInstanceState(savedInstanceState);
        else {
            newsList = new JSONArray();
        }

        defaultNewsSection = getResources().getString(R.string.dummy_section);

        myTask = new FetchNewsTask(this);
        myTask.execute(defaultNewsSection);


    }

    public boolean isDeviceConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() != null) ? true : false;
    }

    public void restoreInstanceState(Bundle savedInstanceState) {
        try {
            newsList = new JSONArray(savedInstanceState.getString("newsList"));
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("newsList", newsList.toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        restoreInstanceState(savedInstanceState);
    }

    @Override
    public void onTaskCompleted(JSONArray list) {
        newsList = list;
        Fragment frag = NewsFragment.newInstance(newsList.toString());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, frag);
        fragmentTransaction.commit();

    }

    public Fragment selectFragment(MenuItem item) {
        Fragment frag = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                frag = NewsFragment.newInstance(newsList.toString());
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
