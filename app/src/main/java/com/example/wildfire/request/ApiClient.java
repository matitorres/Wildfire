package com.example.wildfire.request;

import android.util.Log;

import com.example.wildfire.models.Coordinador;
import com.example.wildfire.models.Denuncia;
import com.example.wildfire.models.Foco;
import com.example.wildfire.models.Institucion;
import com.example.wildfire.models.LoginView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class ApiClient {

    private static final String PATH="http://192.168.1.102:45455/api/";
    private static  MyApiInterface myApiInterface;

    public static MyApiInterface getMyApiClient(){

        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient client = new OkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PATH)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Log.d("mensaje", retrofit.baseUrl().toString());
        myApiInterface = retrofit.create(MyApiInterface.class);
        return myApiInterface;
    }

    public interface MyApiInterface {

        // Coordinadores
        @POST("coordinadores/login")
        Call<String> login(@Body LoginView loginView);

        @GET("coordinadores/bymail/{mail}")
        Call<Coordinador> getCoordinador(
                @Header("Authorization")String token,
                @Path("mail") String mail);

        @PUT("coordinadores/{id}")
        Call<Coordinador> putCoordinador(
                @Header("Authorization")String token,
                @Path("id") int id,
                @Body Coordinador coordinador);

        @PUT("coordinadores/cambioclave/{id}")
        Call<Coordinador> putClaveCoordinador(
                @Header("Authorization")String token,
                @Path("id") int id,
                @Body Coordinador coordinador);

        // Focos
        @GET("focos")
        Call<List<Foco>> getFocos(@Header("Authorization")String token);
        @POST("focos")
        Call<Foco> postFoco(@Header("Authorization")String token, @Body Foco foco);
        @PUT("focos/{id}")
        Call<Foco> putFoco(
                @Header("Authorization")String token,
                @Path("id") int id,
                @Body Foco foco);

        // Instituciones
        @GET("instituciones")
        Call<List<Institucion>> getInstituciones(@Header("Authorization")String token);

        // Denuncias
        @GET("denuncias")
        Call<List<Denuncia>> getDenuncias(@Header("Authorization")String token);
        @PUT("denuncias/{id}")
        Call<Denuncia> putDenuncia(
                @Header("Authorization")String token,
                @Path("id") int id,
                @Body Denuncia denuncia);
    }

}
