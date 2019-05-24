package com.e.assignment.view;

import android.app.Activity;
import android.os.Bundle;

import com.e.assignment.fragment.PreferencesFragment;

public class FragmentPreferenceActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //Display the fragment
        getFragmentManager().beginTransaction().replace(android.R.id.content,new PreferencesFragment()).commit();
    }

}
