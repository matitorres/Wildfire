package com.example.wildfire.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.wildfire.R;
import com.example.wildfire.models.Brigada;
import com.example.wildfire.models.Brigadista;
import com.example.wildfire.models.Integrante;
import com.example.wildfire.models.Recurso;
import com.example.wildfire.models.RecursoBrigada;
import com.example.wildfire.views.MainActivity;
import com.example.wildfire.views.ShowToast;

import java.util.ArrayList;
import java.util.List;

public class ListaBrigadasAdapter extends ArrayAdapter<Brigada> {

    private Context context;
    private List<Brigada> lista;
    private LayoutInflater li;

    public ListaBrigadasAdapter(@NonNull Context context, int resource, @NonNull List<Brigada> objects, LayoutInflater li) {
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
            itemView=li.inflate(R.layout.item_brigada,parent,false);
        }

        final Brigada brigada = lista.get(position);

        Button btnBrigada = itemView.findViewById(R.id.btnBrigada);
        btnBrigada.setText("#" + brigada.getBrigadaId() + " / " + brigada.getEstado());
        Drawable drwEstado = btnBrigada.getContext().getResources().getDrawable( R.drawable.circulo ,null);;
        switch (brigada.getEstado()) {
            case "en preparacion":
                drwEstado.mutate().setTint(context.getResources().getColor(R.color.colorAccent, null));
                break;
            case "en camino":
                drwEstado.mutate().setTint(context.getResources().getColor(R.color.colorVerde, null));
                break;
            case "en posicion":
                drwEstado.mutate().setTint(context.getResources().getColor(R.color.colorPrimaryDark, null));
                break;
            case "demorada":
                drwEstado.mutate().setTint(context.getResources().getColor(R.color.colorAccent2, null));
                break;
        }
        Drawable drwTipo;
        switch (brigada.getTipo()) {
            case "municipio":
                drwTipo = btnBrigada.getContext().getResources().getDrawable( R.drawable.icon_municipio ,null);
                break;
            case "policia":
                drwTipo = btnBrigada.getContext().getResources().getDrawable( R.drawable.icon_policia ,null);
                break;
            case "hospital":
                drwTipo = btnBrigada.getContext().getResources().getDrawable( R.drawable.icon_hospital ,null);
                break;
            default:
                drwTipo = btnBrigada.getContext().getResources().getDrawable( R.drawable.icon_bomberos ,null);
                break;
        }
        btnBrigada.setCompoundDrawablesWithIntrinsicBounds( drwTipo, null, drwEstado, null);
        btnBrigada.setTag(brigada);
        btnBrigada.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // Recuperando objetos del button
                Brigada brigada = (Brigada) view.getTag();
                List<Recurso> recursosAux = new ArrayList<>();
                final List<RecursoBrigada> recursosBrigada = brigada.getRecursosBrigada();
                for (RecursoBrigada rb : recursosBrigada) {
                    recursosAux.add(rb.getRecurso());
                }
                final List<Recurso> recursos = recursosAux;

                List<Brigadista> brigadistasAux = new ArrayList<>();
                final List<Integrante> integrantes = brigada.getIntegrantes();
                for (Integrante i : integrantes) {
                    brigadistasAux.add(i.getBrigadista());
                }
                final List<Brigadista> brigadistas = brigadistasAux;

                // AlertDialog BRIGADA
                View dialogBrigada = li.inflate(R.layout.dialog_brigada,null);
                ArrayAdapter<Brigadista> adapterBrigadistas = new ListaBrigadistasAdapter
                        (context, R.layout.item_brigadista, brigadistas, li);
                ListView lvBrigadista = dialogBrigada.findViewById(R.id.lvGeneral);
                lvBrigadista.setAdapter(adapterBrigadistas);
                StringBuffer listaRecursosString = new StringBuffer();
                for (Recurso recurso : recursos) {
                    listaRecursosString.append("- " + recurso.getNombre() + "\n");
                }
                TextView tvRecursos = dialogBrigada.findViewById(R.id.tvRecursos);
                tvRecursos.setText(listaRecursosString);
                TextView tvBrigadaId = dialogBrigada.findViewById(R.id.tvBrigadaId);
                tvBrigadaId.setText("Brigada #" + brigada.getBrigadaId());

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(dialogBrigada);
                final AlertDialog alertDialogBrigada = builder.create();

                // Button REASIGNAR FOCO
                Button btnReasignarFoco = dialogBrigada.findViewById(R.id.btnReasignarFoco);
                btnReasignarFoco.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new ShowToast(getContext(),"¿Nuevo foco asignado a brigada?");
                        alertDialogBrigada.dismiss();
                    }
                });
                alertDialogBrigada.getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);
                alertDialogBrigada.show();
            }
        });

        return itemView;
    }
}
