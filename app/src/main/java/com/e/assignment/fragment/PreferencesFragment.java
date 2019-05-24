package com.e.assignment.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.e.assignment.R;
import com.e.assignment.view.FragmentPreferenceActivity;

import java.util.Map;

public class PreferencesFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String LOG_TAG = FragmentPreferenceActivity.class.getName();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Load the preferences from an XML file
        addPreferencesFromResource(R.xml.preferences);

    }

    @Override
    public void onResume() {
        super.onResume();
        //To store value in shared preference
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        SharedPreferences subSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        //display the preferences for debugging
        Map<String, ?> prefMap = subSharedPreferences.getAll();
        Log.i(LOG_TAG, prefMap.toString());
    }
}
