package com.e.assignment.model.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.e.assignment.model.Event;
import com.e.assignment.model.EventsModel;
import com.e.assignment.model.EventsModelImpl;

import java.util.Date;
import java.util.Map;

public class EventListViewModel extends AndroidViewModel {
    //event list
    private MutableLiveData<Map<Date, Event>>eventLiveData;
    //event list view model
    public EventListViewModel(Application application){
        super(application);
    }
    public LiveData<Map<Date,Event>>getEvents(boolean isReverse){
        if(eventLiveData == null){
            eventLiveData = new MutableLiveData<>();
            EventsModel events = EventsModelImpl.getSingletonInstance(getApplication());
            //load data
            eventLiveData.setValue(events.sortTheEventList(isReverse));
        }
        return eventLiveData;
    }
}