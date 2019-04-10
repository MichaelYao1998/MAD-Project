package com.e.assignment.model.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.e.assignment.model.Event;
import com.e.assignment.model.EventImpl;
import com.e.assignment.model.EventsModel;
import com.e.assignment.model.EventsModelImpl;
import com.e.assignment.model.Movie;

import java.util.Map;

public class EventListViewModel extends AndroidViewModel {
    //event list
    private MutableLiveData<Map<String, Event>>eventLiveData;
    //event list view model
    public EventListViewModel(Application application){
        super(application);
    }
    public LiveData<Map<String,Event>>getEvents(){
        if(eventLiveData == null){
            eventLiveData = new MutableLiveData<>();
            EventsModel events = EventsModelImpl.getSingletonInstance(getApplication());
            //load data
            eventLiveData.setValue(events.getEventsList());
        }
        return eventLiveData;
    }
}