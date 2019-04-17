package com.e.assignment.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.e.assignment.R;

public class DialogListDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title)
                .setItems(R.array.select_dialog_items,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /* User clicked so do some stuff */
                                String[] items = getResources().getStringArray(
                                        R.array.select_dialog_items);
                                new AlertDialog.Builder(getActivity()).setMessage(
                                        "You selected: " + which + " , " + items[which])
                                        .show();
                            }
                        }).create();

    }

}
