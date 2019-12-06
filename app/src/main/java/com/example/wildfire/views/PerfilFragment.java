package com.example.wildfire.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.wildfire.R;
import com.example.wildfire.models.Coordinador;
import com.example.wildfire.viewmodels.CoordinadorViewModel;
import com.example.wildfire.viewmodels.MainViewModel;

public class PerfilFragment extends Fragment {

    private View root;
    private CoordinadorViewModel coordinadorViewModel;
    private MainViewModel mainViewModel;
    private Button btnUpdatePerfil, btnCancelarPerfil, btnFragmentClave;
    private EditText etNombre, etTelefono;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_perfil, container,false);
        initComponents();
        return root;
    }

    private void initComponents() {

        // EditTexts
        etNombre = root.findViewById(R.id.etNombre);
        etTelefono = root.findViewById(R.id.etTelefono);

        // CordinadorViewModel
        coordinadorViewModel = ViewModelProviders.of(this)
                .get(CoordinadorViewModel.class);
        coordinadorViewModel.getCoordinador().observe(this, new Observer<Coordinador>() {
            @Override
            public void onChanged(Coordinador c) {
                etNombre.setText(c.getNombre());
                etTelefono.setText(c.getTelefono());
            }
        });
        coordinadorViewModel.setCoordinador();

        //MainViewModel
        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        //Buttons
        btnCancelarPerfil = root.findViewById(R.id.btnCancelarPerfil);
        btnCancelarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment mapFragment = new MapFragment();
                FragmentTransaction transaction = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, mapFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                mainViewModel.setBtnOptionsVisible(View.VISIBLE);
            }
        });
        btnUpdatePerfil = root.findViewById(R.id.btnUpdatePerfil);
        btnUpdatePerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coordinadorViewModel.putCoordinador(
                        etNombre.getText().toString(),
                        etTelefono.getText().toString());
                Fragment mapFragment = new MapFragment();
                FragmentTransaction transaction = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, mapFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                mainViewModel.setBtnOptionsVisible(View.VISIBLE);
            }
        });
        btnFragmentClave = root.findViewById(R.id.btnFragmentClave);
        btnFragmentClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment cambioClaveFragment = new CambioClaveFragment();
                FragmentTransaction transaction = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, cambioClaveFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}
