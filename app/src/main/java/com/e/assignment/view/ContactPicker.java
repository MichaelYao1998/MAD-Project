package com.e.assignment.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

/*
 *  The method class, using to set the add attendee button
 *  Also responsible for checking the permission of reading contact on runtime and
 *  if no permission,, ask for permission to access user's contacts data
 */
class ContactPicker {
    /*
     *  @param  addContactButton    the button refference
     *  @param  permissions         check the permission for reading user's contact
     */
    public void setPickerOnButton(Button addContactButton, String[] permissions, final Activity activity)
    {
        final int PICK_CONTACT = 2015;
        if (CheckPermission(activity.getApplicationContext(), permissions[0])) {
            addContactButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    activity.startActivityForResult(i, PICK_CONTACT);
                }
            });
        } else {
            // you do not have permission go request runtime permissions
            RequestPermission(activity, permissions);
        }
    }
    /*
     *  check the permission of reading contact on runtime
     */
    private boolean CheckPermission(Context context, String Permission) {
        return ContextCompat.checkSelfPermission(context,
                Permission) == PackageManager.PERMISSION_GRANTED;
    }

    /*
     *  when no permission on runtime,
     *  request the permission
     */
    private void RequestPermission(Activity thisActivity, String[] Permission) {
        if (ContextCompat.checkSelfPermission(thisActivity,
                Permission[0])
                != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                    Permission[0])) {
                        ActivityCompat.requestPermissions(thisActivity, Permission,
                                123);
                    }
        }
    }
}
