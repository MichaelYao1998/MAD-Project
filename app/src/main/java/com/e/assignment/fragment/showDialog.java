package com.e.assignment.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.e.assignment.R;



public class showDialog extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.dialog_layout, container, false);
        Button button1 = view.findViewById(R.id.first_event);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogListDialogFragment().show(getFragmentManager(),"dl");
            }
        });
        return view;
    }
}
