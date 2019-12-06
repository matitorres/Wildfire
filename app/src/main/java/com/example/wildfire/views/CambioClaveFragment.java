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

public class CambioClaveFragment extends Fragment {

    private View root;
    private CoordinadorViewModel coordinadorViewModel;
    private Button btnUpdateClave, btnCancelarCambioClave;
    private EditText etClaveNueva, etClaveNuevaRepeat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_cambio_clave, container, false);
        initComponents();
        return root;
    }

    private void initComponents() {

        // EditTexts
        etClaveNueva = root.findViewById(R.id.etClaveNueva);
        etClaveNuevaRepeat = root.findViewById(R.id.etClaveNuevaRepeat);

        // CordinadorViewModel
        coordinadorViewModel = ViewModelProviders.of(this)
                .get(CoordinadorViewModel.class);
        coordinadorViewModel.getCoordinador().observe(this, new Observer<Coordinador>() {
            @Override
            public void onChanged(Coordinador c) {
            }
        });
        coordinadorViewModel.setCoordinador();

        //Buttons
        btnCancelarCambioClave = root.findViewById(R.id.btnCancelarCambioClave);
        btnCancelarCambioClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment perfilFragment = new PerfilFragment();
                FragmentTransaction transaction = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, perfilFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        btnUpdateClave = root.findViewById(R.id.btnUpdateClave);
        btnUpdateClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String claveNueva = etClaveNueva.getText().toString();
                if (claveNueva.equals(etClaveNuevaRepeat.getText().toString())) {
                    coordinadorViewModel.putClaveCoordinador(claveNueva);
                    Fragment perfilFragment = new PerfilFragment();
                    FragmentTransaction transaction = getActivity()
                            .getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, perfilFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    new ShowToast(getContext(),"Poné bien la repetida!");
                }

            }
        });
    }

}
