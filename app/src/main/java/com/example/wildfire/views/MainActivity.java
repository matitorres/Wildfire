package com.example.wildfire.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.wildfire.R;
import com.example.wildfire.models.Foco;
import com.example.wildfire.viewmodels.CoordinadorViewModel;
import com.example.wildfire.viewmodels.FocoViewModel;
import com.example.wildfire.viewmodels.MainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

@SuppressLint("RestrictedApi")
public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;
    private String token;
    private SharedPreferences sp;
    private FloatingActionButton btnOptions;
    private Button btnFocos, btnDenuncias, btnInstituciones, btnPerfil, btnLogout;
    private Bundle savedInstanceState;
    private View container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;

        validarSesion();

        setContentView(R.layout.activity_main);

        initContainer();
        initComponents();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mainViewModel.setBtnOptionsVisible(View.VISIBLE);
    }

    private void validarSesion() {
        sp = getSharedPreferences("token",0);
        token = sp.getString("token", "");

        if (token.equals("")) {
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }
    }

    private void initContainer() {
        container = findViewById(R.id.container);

        // Verifica que exista el contenedor
        if(container != null)
        {
            if(savedInstanceState != null)
                return;

            // Cargar mapa (MapFragment)
            MapFragment mapFragment = new MapFragment();
            getSupportFragmentManager().beginTransaction().add(container.getId(), mapFragment).commit();
        }
    }

    private void initComponents() {

        // ViewModels
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getBtnOptionsVisible().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer e) {
                btnOptions.setVisibility(e);
            }
        });

        // Dialog menu
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        LayoutInflater inflater = getLayoutInflater();
        View dialogMenu = inflater.inflate(R.layout.dialog_menu,null);
        builder.setView(dialogMenu);
        final AlertDialog alertDialogMenu = builder.create();
        alertDialogMenu.getWindow()
                .setBackgroundDrawableResource(android.R.color.transparent);

        // Buttons
        btnOptions = findViewById(R.id.btnOptions);
        btnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogMenu.show();
            }
        });

        btnFocos = dialogMenu.findViewById(R.id.btnFocos);
        btnFocos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnDenuncias = dialogMenu.findViewById(R.id.btnDenuncias);
        btnDenuncias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnInstituciones = dialogMenu.findViewById(R.id.btnInstituciones);
        btnInstituciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnPerfil = dialogMenu.findViewById(R.id.btnPerfil);
        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(container != null)
                {
                    // Cargar perfil (PerfilFragment)
                    Fragment perfilFragment = new PerfilFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(container.getId(), perfilFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                alertDialogMenu.dismiss();
                mainViewModel.setBtnOptionsVisible(View.GONE);
            }
        });

        btnLogout = dialogMenu.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("token","");
                editor.commit();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }
}
