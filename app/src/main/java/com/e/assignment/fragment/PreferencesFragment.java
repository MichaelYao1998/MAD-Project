package com.e.assignment.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.e.assignment.view.FragmentPreferenceActivity;

import java.util.Map;

public class PreferencesFragment extends PreferenceFragment {
    private static final String LOG_TAG = FragmentPreferenceActivity.class.getName();
    private static final String THRESHOLD_KEY = "noti_threshold";
    private static final String DURATION_KEY = "remind duration";
    private static final String PERIOD_KEY = "noti period";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Load the preferences from an XML file
//        addPreferencesFromResource(R.xml.preferences);

    }

    @Override
    public void onResume() {
        super.onResume();
//        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
//        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * To store value in shared preference
     */
    public void addToSharedPreference() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        String ThresholdValue = "5";
        String DurationValue = "10";
        String PeriodValue = "100";

        //now add extra fielda into sharedPreferences
        sharedPreferences.edit().putString(THRESHOLD_KEY, ThresholdValue).commit();
        sharedPreferences.edit().putString(DURATION_KEY, DurationValue).commit();
        sharedPreferences.edit().putString(PERIOD_KEY, PeriodValue).commit();
        //sharedPreferences.edit().putLong()
        //display the preferences for debugging
        Map<String, ?> prefMap = sharedPreferences.getAll();
        Log.i(LOG_TAG, prefMap.toString());
    }
}
