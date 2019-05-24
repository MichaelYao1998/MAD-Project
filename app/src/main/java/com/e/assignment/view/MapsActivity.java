package com.e.assignment.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.e.assignment.R;
import com.e.assignment.model.Event;
import com.e.assignment.model.EventsModel;
import com.e.assignment.model.EventsModelImpl;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker based on the events map
        EventsModel em = EventsModelImpl.getSingletonInstance(getApplicationContext());
        Map<Date, Event> map = em.sortTheEventList(false);
        Date current= Calendar.getInstance().getTime();
        int count = 0;
        for (Map.Entry<Date, Event> entry : map.entrySet()) {
            if (count>2){
                break;
            }
            // only display the event marker which is in future
            if (current.before(entry.getKey())){
                String [] location = entry.getValue().getLocation().split(",");
                // avoid null
                if (location.length!=0){
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(location[0]),Double.parseDouble(location[1])))
                            .title(entry.getValue().getTitle()));
                    if (count==0)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(Double.parseDouble(location[0]),Double.parseDouble(location[1])),5));
                    count++;
                }
            }
        }

        // If no event in future
        if (count==0){
            LatLng mel = new LatLng(-37.814795, 144.966119);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mel,5));
        }
    }
}
