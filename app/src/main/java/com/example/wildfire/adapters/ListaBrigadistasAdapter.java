package com.example.wildfire.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.wildfire.R;
import com.example.wildfire.models.Brigadista;

import java.util.List;

public class ListaBrigadistasAdapter extends ArrayAdapter<Brigadista> {

    private Context context;
    private List<Brigadista> lista;
    private LayoutInflater li;

    public ListaBrigadistasAdapter(@NonNull Context context, int resource, @NonNull List<Brigadista> objects, LayoutInflater li) {
        super(context, resource, objects);
        this.context = context;
        this.lista = objects;
        this.li = li;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        View itemView = convertView;

        if (itemView==null) {
            itemView=li.inflate(R.layout.item_brigadista,parent,false);
        }

        final Brigadista brigadista = lista.get(position);

        Button btnBrigadistaInstitucion = itemView.findViewById(R.id.btnBrigadistaInstitucion);
        btnBrigadistaInstitucion.setText(brigadista.getNombre() + " / " + brigadista.getFuncion());
        btnBrigadistaInstitucion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String telefono = brigadista.getTelefono();
                String dial = "tel:" + telefono;
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(dial));
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    context.startActivity(intent);
                } else {
                    ActivityCompat.requestPermissions((Activity)context,
                            new String[]{Manifest.permission.CALL_PHONE}, 1);
                }
            }
        });

        return itemView;
    }
}
