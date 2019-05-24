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
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        EventsModel em = EventsModelImpl.getSingletonInstance(getApplicationContext());
        Map<Date, Event> map = em.sortTheEventList(false);
        Date current= Calendar.getInstance().getTime();
        int count = 0;
        for (Map.Entry<Date, Event> entry : map.entrySet()) {
            if (count>2){
                break;
            }
            if (current.before(entry.getKey())){
                String [] location = entry.getValue().getLocation().split(",");
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
        if (count==0){
            LatLng mel = new LatLng(-37.814795, 144.966119);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mel,5));
        }
    }
}
