package com.e.assignment.model.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.e.assignment.model.Event;
import com.e.assignment.model.EventsModel;
import com.e.assignment.model.EventsModelImpl;

import java.util.Map;

public class AttendeeListViewModel extends AndroidViewModel {
    //event list
    private MutableLiveData<Map<String, String>> attendeeLiveData;
    //event list view model
    private  String eventID;
    public AttendeeListViewModel(Application application, String eventID){
        super(application);
        this.eventID=eventID;
    }

    public LiveData<Map<String,String>> getAttendees(){
        if(attendeeLiveData == null){
            attendeeLiveData = new MutableLiveData<>();
            EventsModel events = EventsModelImpl.getSingletonInstance(getApplication());
            //load data
            attendeeLiveData.setValue(events.getEventById(eventID).getAttendees());
        }
        return attendeeLiveData;
    }
}