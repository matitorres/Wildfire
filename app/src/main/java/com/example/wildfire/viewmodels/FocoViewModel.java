package com.example.wildfire.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wildfire.models.Foco;
import com.example.wildfire.request.ApiClient;
import com.example.wildfire.views.ShowToast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FocoViewModel extends AndroidViewModel {

    private Context context;
    private SharedPreferences sp;
    private MutableLiveData<List<Foco>> focos;
    private MutableLiveData<Foco> foco;

    public FocoViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        sp = context.getSharedPreferences("token",0);
    }

    public LiveData<List<Foco>> getFocos() {
        if (focos == null) {
            focos = new MutableLiveData<>();
        }
        return focos;
    }

    public LiveData<Foco> getFoco() {
        if (foco == null) {
            foco = new MutableLiveData<>();
        }
        return foco;
    }public void setFocos() {
        Call<List<Foco>> dato = ApiClient.getMyApiClient().getFocos(sp.getString("token",""));
        dato.enqueue(new Callback<List<Foco>>() {
            @Override
            public void onResponse(Call<List<Foco>> call, Response<List<Foco>> response) {
                if (response.isSuccessful()) {
                    focos.postValue(response.body());
                } else {
                    new ShowToast(context, "Error al cargar focos");
                }
            }

            @Override
            public void onFailure(Call<List<Foco>> call, Throwable t) {
                new ShowToast(context, t.getMessage());
            }
        });
    }


}
