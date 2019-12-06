package com.example.wildfire.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wildfire.models.Institucion;
import com.example.wildfire.request.ApiClient;
import com.example.wildfire.views.ShowToast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstitucionViewModel extends AndroidViewModel {

    private Context context;
    private SharedPreferences sp;
    private MutableLiveData<List<Institucion>> instituciones;
    private MutableLiveData<Institucion> institucion;

    public InstitucionViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        sp = context.getSharedPreferences("token",0);
    }

    public LiveData<List<Institucion>> getInstituciones() {
        if (instituciones == null) {
            instituciones = new MutableLiveData<>();
        }
        return instituciones;
    }

    public LiveData<Institucion> getInstitucion() {
        if (institucion == null) {
            institucion = new MutableLiveData<>();
        }
        return institucion;
    }

    public void setInstituciones() {
        Call<List<Institucion>> dato = ApiClient.getMyApiClient().getInstituciones(sp.getString("token",""));
        dato.enqueue(new Callback<List<Institucion>>() {
            @Override
            public void onResponse(Call<List<Institucion>> call, Response<List<Institucion>> response) {
                if (response.isSuccessful()) {
                    instituciones.postValue(response.body());
                } else {
                    new ShowToast(context, "Error al cargar instituciones");
                }
            }

            @Override
            public void onFailure(Call<List<Institucion>> call, Throwable t) {
                new ShowToast(context, t.getMessage());
            }
        });
    }

}
