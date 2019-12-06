package com.example.wildfire.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wildfire.models.Denuncia;
import com.example.wildfire.models.Institucion;
import com.example.wildfire.request.ApiClient;
import com.example.wildfire.views.ShowToast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DenunciaViewModel extends AndroidViewModel {

    private Context context;
    private SharedPreferences sp;
    private MutableLiveData<List<Denuncia>> denuncias;
    private MutableLiveData<Denuncia> denuncia;

    public DenunciaViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        sp = context.getSharedPreferences("token",0);
    }

    public LiveData<List<Denuncia>> getDenuncias() {
        if (denuncias == null) {
            denuncias = new MutableLiveData<>();
        }
        return denuncias;
    }

    public LiveData<Denuncia> getDenuncia() {
        if (denuncia == null) {
            denuncia = new MutableLiveData<>();
        }
        return denuncia;
    }

    public void setDenuncias() {
        Call<List<Denuncia>> dato = ApiClient.getMyApiClient().getDenuncias(sp.getString("token",""));
        dato.enqueue(new Callback<List<Denuncia>>() {
            @Override
            public void onResponse(Call<List<Denuncia>> call, Response<List<Denuncia>> response) {
                if (response.isSuccessful()) {
                    denuncias.postValue(response.body());
                } else {
                    new ShowToast(context, "Error al cargar denuncias");
                }
            }

            @Override
            public void onFailure(Call<List<Denuncia>> call, Throwable t) {
                new ShowToast(context, t.getMessage());
            }
        });
    }
}
