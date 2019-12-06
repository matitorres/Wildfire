package com.example.wildfire.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MainViewModel extends AndroidViewModel {

    private Context context;
    private MutableLiveData<Integer> btnOptionsVisible;

    public MainViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<Integer> getBtnOptionsVisible(){
        if(btnOptionsVisible==null){
            btnOptionsVisible=new MutableLiveData<>();
        }
        return btnOptionsVisible;
    }

    public void setBtnOptionsVisible(int estado) {
        btnOptionsVisible.setValue(estado);
    }
}
