package com.example.wildfire.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wildfire.models.Coordinador;
import com.example.wildfire.models.LoginView;
import com.example.wildfire.request.ApiClient;
import com.example.wildfire.views.ShowToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoordinadorViewModel extends AndroidViewModel {

    private MutableLiveData<String> token;
    private MutableLiveData<Coordinador> coordinador;
    private SharedPreferences sp;
    private Context context;

    public CoordinadorViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        sp = context.getSharedPreferences("token",0);
    }

    public LiveData<String> getToken() {
        if (token == null) {
            token = new MutableLiveData<>();
        }
        return token;
    }

    public LiveData<Coordinador> getCoordinador() {
        if (coordinador == null) {
            coordinador = new MutableLiveData<>();
        }
        return coordinador;
    }


    public void login(final LoginView loginView) {
        Call<String> dato = ApiClient.getMyApiClient().login(loginView);
        dato.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    token.postValue(response.body());
                    Log.d("token",response.body());
                    SharedPreferences sp = context.getSharedPreferences("token",0);
                    SharedPreferences.Editor editor = sp.edit();
                    String t = "Bearer " + response.body();
                    editor.putString("token",t);
                    editor.putString("user",loginView.getMail());
                    editor.commit();
                } else {
                    new ShowToast(context, "Datos invalidos");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                new ShowToast(context, t.getMessage());
            }
        });
    }

    public void setCoordinador() {
        Call<Coordinador> dato = ApiClient.getMyApiClient()
                .getCoordinador(sp.getString("token", ""), sp.getString("user",""));
        dato.enqueue(new Callback<Coordinador>() {
            @Override
            public void onResponse(Call<Coordinador> call, Response<Coordinador> response) {
                if (response.isSuccessful()) {
                    coordinador.postValue(response.body());
                } else {
                    new ShowToast(context, "Error al cargar datos");
                }
            }
            @Override
            public void onFailure(Call<Coordinador> call, Throwable t) {
                new ShowToast(context, t.getMessage());
            }
        });
    }

    public void putCoordinador(String nombre, String telefono) {

        Coordinador coordinadorModificado = coordinador.getValue();
        coordinadorModificado.setNombre(nombre);
        coordinadorModificado.setTelefono(telefono);

        Call<Coordinador> dato = ApiClient.getMyApiClient().putCoordinador(
                sp.getString("token",""),
                coordinadorModificado.getCoordinadorId(),
                coordinadorModificado );
        dato.enqueue(new Callback<Coordinador>() {
            @Override
            public void onResponse(Call<Coordinador> call, Response<Coordinador> response) {
                if (response.isSuccessful()) {
                    coordinador.postValue(response.body());
                    new ShowToast(context, "Actualización exitosa");
                } else {
                    new ShowToast(context, response.message());
                }
            }
            @Override
            public void onFailure(Call<Coordinador> call, Throwable t) {
                new ShowToast(context, t.getMessage());
            }
        });

    }

    public void putClaveCoordinador(String claveNueva) {

        Coordinador coordinadorModificado = coordinador.getValue();
        coordinadorModificado.setClave(claveNueva);

        Call<Coordinador> dato = ApiClient.getMyApiClient().putClaveCoordinador(
                sp.getString("token",""),
                coordinadorModificado.getCoordinadorId(),
                coordinadorModificado );
        dato.enqueue(new Callback<Coordinador>() {
            @Override
            public void onResponse(Call<Coordinador> call, Response<Coordinador> response) {
                if (response.isSuccessful()) {
                    coordinador.postValue(response.body());
                    new ShowToast(context, "Actualización exitosa");
                } else {
                    new ShowToast(context, response.message());
                }
            }
            @Override
            public void onFailure(Call<Coordinador> call, Throwable t) {
                new ShowToast(context, t.getMessage());
            }
        });

    }
}
