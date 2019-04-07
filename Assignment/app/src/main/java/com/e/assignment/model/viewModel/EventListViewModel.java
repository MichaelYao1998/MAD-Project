package com.e.assignment.model.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.e.assignment.model.Event;
import com.e.assignment.model.EventImpl;

import java.util.Map;

public class EventListViewModel extends AndroidViewModel {
    private MutableLiveData<Map<String, Event>>eventLiveData;
    public EventListViewModel(Application application){
        super(application);
    }
    public LiveData<Map<String,Event>>getEvent(){
        if(eventLiveData == null){
            eventLiveData = new MutableLiveData<>();
            Event event = EventImpl.getSingletonInstance(getApplication());
            //load data
            eventLiveData.setValue(event.getEventList());
        }
        return eventLiveData;
    }

}