package com.example.wildfire.views;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.wildfire.R;
import com.example.wildfire.adapters.ListaBrigadasAdapter;
import com.example.wildfire.adapters.ListaBrigadistasAdapter;
import com.example.wildfire.models.Brigada;
import com.example.wildfire.models.Brigadista;
import com.example.wildfire.models.Coordinador;
import com.example.wildfire.models.Denuncia;
import com.example.wildfire.models.Foco;
import com.example.wildfire.models.Institucion;
import com.example.wildfire.models.Integrante;
import com.example.wildfire.models.Recurso;
import com.example.wildfire.models.RecursoBrigada;
import com.example.wildfire.request.ApiClient;
import com.example.wildfire.viewmodels.CoordinadorViewModel;
import com.example.wildfire.viewmodels.DenunciaViewModel;
import com.example.wildfire.viewmodels.FocoViewModel;
import com.example.wildfire.viewmodels.InstitucionViewModel;
import com.example.wildfire.viewmodels.MainViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapLongClickListener {

    private FocoViewModel focoViewModel;
    private InstitucionViewModel institucionViewModel;
    private DenunciaViewModel denunciaViewModel;
    private CoordinadorViewModel coordinadorViewModel;
    private SharedPreferences sp;
    private final LatLng SAN_LUIS = new LatLng(-33.2762202,-65.9515546);
    public static GoogleMap googleMap;
    private int coordinadorId;
    private EditText etDescripcionFoco;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        sp = getContext().getSharedPreferences("token",0);

        getMapAsync(this);

        return rootView;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.mapstyle));

            if (!success) {
                Log.e("MapFragment", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapFragment", "Can't find style. Error: ", e);
        }

        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMapLongClickListener(this);

        this.googleMap = googleMap;

        // ViewModels
        focoViewModel = ViewModelProviders.of(this).get(FocoViewModel.class);
        focoViewModel.getFocos().observe(this, new Observer<List<Foco>>() {
            @Override
            public void onChanged(List<Foco> focos) {
                for (Foco foco:focos) {
                    LatLng latLng = new LatLng(
                            Double.parseDouble(foco.getLatitud()),
                            Double.parseDouble(foco.getLongitud()));
                    Marker marker = MapFragment.googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title("Foco de incendio #" + foco.getFocoId()));
                    marker.setTag(foco);
                    if (foco.getCoordinador().getMail().equals(sp.getString("user",""))) {
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.fire));
                        marker.setSnippet("foco");
                    } else {
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.fire_otro));
                        marker.setSnippet("foco_otro");
                    }
                }
            }
        });
        focoViewModel.setFocos();

        institucionViewModel = ViewModelProviders.of(this)
                .get(InstitucionViewModel.class);
        institucionViewModel.getInstituciones()
                .observe(this, new Observer<List<Institucion>>() {
            @Override
            public void onChanged(List<Institucion> instituciones) {
                for (Institucion institucion:instituciones) {
                    LatLng latLng = new LatLng(
                            Double.parseDouble(institucion.getLatitud()),
                            Double.parseDouble(institucion.getLongitud()));
                    Marker marker = MapFragment.googleMap.addMarker(
                            new MarkerOptions().position(latLng)
                            .title(institucion.getNombre())
                            .snippet("institucion"));
                    marker.setTag(institucion);
                    switch (institucion.getDescripcion()) {
                        case "bomberos":
                            marker.setIcon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.bomberos));
                            break;
                        case "policia":
                            marker.setIcon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.policia));
                            break;
                        case "municipio":
                            marker.setIcon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.municipio));
                            break;
                        case "hospital":
                            marker.setIcon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.hospital));
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        institucionViewModel.setInstituciones();

        denunciaViewModel = ViewModelProviders.of(this).get(DenunciaViewModel.class);
        denunciaViewModel.getDenuncias().observe(this, new Observer<List<Denuncia>>() {
            @Override
            public void onChanged(List<Denuncia> denuncias) {
                for (Denuncia denuncia : denuncias) {
                    LatLng latLng = new LatLng(
                            Double.parseDouble(denuncia.getLatitud()),
                            Double.parseDouble(denuncia.getLongitud()));
                    Marker marker = MapFragment.googleMap.addMarker(
                            new MarkerOptions()
                            .position(latLng)
                            .title("Denuncia #" + denuncia.getDenunciaId())
                            .snippet("denuncia")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.denuncia))
                    );
                    marker.setTag(denuncia);
                }
            }
        });
        denunciaViewModel.setDenuncias();

        coordinadorViewModel = ViewModelProviders.of(this).get(CoordinadorViewModel.class);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SAN_LUIS, 10));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        LayoutInflater inflater = getLayoutInflater();

        switch (marker.getSnippet()) {
            case "denuncia":

                // AlertDialog DENUNCIA
                View dialogDenuncia = inflater.inflate(R.layout.dialog_denuncia,null);
                builder.setView(dialogDenuncia);
                final AlertDialog alertDialogDenuncia = builder.create();

                // ImageButton LLAMAR
                ImageButton btnLlamarDenuncia = dialogDenuncia.findViewById(R.id.btnLlamarDenuncia);
                btnLlamarDenuncia.setTag(marker);
                btnLlamarDenuncia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Marker marker = (Marker) view.getTag();
                        Denuncia denuncia = (Denuncia) marker.getTag();
                        String telefono = denuncia.getTelefono();
                        String dial = "tel:" + telefono;
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(dial));
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            startActivity(intent);
                        } else {
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
                        }
                    }
                });

                // Button RECHAZAR
                Button btnRechazarDenuncia = dialogDenuncia.findViewById(R.id.btnRechazarDenuncia);
                btnRechazarDenuncia.setTag(marker);
                btnRechazarDenuncia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Marker marker = (Marker) view.getTag();
                        Denuncia denuncia = (Denuncia) marker.getTag();
                        denuncia.setEstado("rechazada");
                        denuncia.setResponsable(sp.getString("user",""));
                        Call<Denuncia> dato = ApiClient.getMyApiClient().putDenuncia(
                                        sp.getString("token",""),
                                        denuncia.getDenunciaId(),
                                        denuncia
                        );
                        dato.enqueue(new Callback<Denuncia>() {
                            @Override
                            public void onResponse(Call<Denuncia> call, Response<Denuncia> response) {
                                if (response.isSuccessful()) {
                                    new ShowToast(getContext(), "Denuncia rechazada");
                                } else {
                                    new ShowToast(getContext(), "Error al actualizar denuncia");
                                }
                            }

                            @Override
                            public void onFailure(Call<Denuncia> call, Throwable t) {
                                new ShowToast(getContext(), t.getMessage());
                            }
                        });
                        alertDialogDenuncia.dismiss();
                        denunciaViewModel.setDenuncias();
                    }
                });

                // Button VALIDAR
                Button btnValidarDenuncia = dialogDenuncia.findViewById(R.id.btnValidarDenuncia);
                btnValidarDenuncia.setTag(marker);
                btnValidarDenuncia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Marker marker = (Marker) view.getTag();
                        Denuncia denuncia = (Denuncia) marker.getTag();
                        denuncia.setEstado("aceptada");
                        denuncia.setResponsable(sp.getString("user",""));
                        Call<Denuncia> dato = ApiClient.getMyApiClient().putDenuncia(
                                sp.getString("token",""),
                                denuncia.getDenunciaId(),
                                denuncia
                        );
                        dato.enqueue(new Callback<Denuncia>() {
                            @Override
                            public void onResponse(Call<Denuncia> call, Response<Denuncia> response) {
                                if (response.isSuccessful()) {
                                    new ShowToast(getContext(), "Denuncia validada" );
                                } else {
                                    new ShowToast(getContext(), "Error al actualizar denuncia");
                                }
                            }

                            @Override
                            public void onFailure(Call<Denuncia> call, Throwable t) {
                                new ShowToast(getContext(), t.getMessage());
                            }
                        });
                        alertDialogDenuncia.dismiss();
                        denunciaViewModel.setDenuncias();
                    }
                });

                alertDialogDenuncia.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                alertDialogDenuncia.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                alertDialogDenuncia.show();
                break;

            case "institucion":

                final Institucion institucion = (Institucion) marker.getTag();
                final List<Recurso> recursosInstitucion = institucion.getRecursos();
                final List<Brigadista> brigadistasInstitucion = institucion.getBrigadistas();

                // AlertDialog INSTITUCIÓN
                View dialogInstitucion = inflater.inflate(R.layout.dialog_institucion,null);
                ArrayAdapter<Brigadista> adapterBrigadistas = new ListaBrigadistasAdapter
                        (getActivity(), R.layout.item_brigadista, brigadistasInstitucion,
                                getLayoutInflater());
                ListView lvBrigadista = dialogInstitucion.findViewById(R.id.lvGeneral);
                lvBrigadista.setAdapter(adapterBrigadistas);
                StringBuffer listaRecursosString = new StringBuffer();
                for (Recurso recurso : recursosInstitucion) {
                    listaRecursosString.append("- " + recurso.getNombre() + "\n");
                }
                TextView tvRecursosInstitucion = dialogInstitucion.findViewById(R.id.tvRecursos);
                tvRecursosInstitucion.setText(listaRecursosString);
                TextView tvNombreInstitucion = dialogInstitucion.findViewById(R.id.tvNombreInstitucion);
                tvNombreInstitucion.setText(institucion.getNombre());
                builder.setView(dialogInstitucion);
                final AlertDialog alertDialogInstitucion = builder.create();

                // Button ENVIAR FOCO
                Button btnEnviarFoco = dialogInstitucion.findViewById(R.id.btnEnviarFoco);
                btnEnviarFoco.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new ShowToast(getContext(),"¿Foco enviado?");
                        alertDialogInstitucion.dismiss();
                    }
                });

                // Button LLAMAR INSTITUCION
                Button btnLlamarInstitucion = dialogInstitucion.findViewById(R.id.btnLlamarInstitucion);
                btnLlamarInstitucion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String dial = "tel:3512434300";
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(dial));
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            startActivity(intent);
                        } else {
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
                        }
                    }
                });
                alertDialogInstitucion.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                alertDialogInstitucion.show();
                break;

            case "foco":

                final Foco foco = (Foco) marker.getTag();
                final List<Brigada> brigadas = foco.getBrigadas();

                // AlertDialog FOCO
                View dialogFoco = inflater.inflate(R.layout.dialog_foco,null);
                ArrayAdapter<Brigada> adapterBrigadas = new ListaBrigadasAdapter
                        (getActivity(), R.layout.item_brigada, brigadas,
                                getLayoutInflater());
                ListView lvBrigadas = dialogFoco.findViewById(R.id.lvGeneral);
                lvBrigadas.setAdapter(adapterBrigadas);
                TextView tvEstadoFoco = dialogFoco.findViewById(R.id.tvEstadoFoco);
                tvEstadoFoco.setText(foco.getEstado());
                switch (foco.getEstado()) {
                    case "controlado":
                        tvEstadoFoco.setBackgroundColor(getContext().getResources().getColor(R.color.colorVerde, null));
                        break;
                    default:
                        tvEstadoFoco.setBackgroundColor(getContext().getResources().getColor(R.color.colorAccent2, null));
                        break;
                }
                TextView tvFocoId = dialogFoco.findViewById(R.id.tvFocoId);
                tvFocoId.setText("Foco de incendio #" + foco.getFocoId());
                TextView tvDescripcionFoco = dialogFoco.findViewById(R.id.tvDescripcionFoco);
                tvDescripcionFoco.setText(foco.getDescripcion());
                lvBrigadas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
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
                        for (Integrante integrante : integrantes) {
                            brigadistasAux.add(integrante.getBrigadista());
                        }
                        final List<Brigadista> brigadistas = brigadistasAux;

                        // AlertDialog BRIGADA
                        View dialogBrigada = getLayoutInflater().inflate(R.layout.dialog_institucion,null);
                        ArrayAdapter<Brigadista> adapterBrigadistas = new ListaBrigadistasAdapter
                                (getActivity(), R.layout.item_brigadista, brigadistas,
                                        getLayoutInflater());
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                        alertDialogBrigada.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        alertDialogBrigada.show();
                    }
                });
                builder.setView(dialogFoco);
                final AlertDialog alertDialogFoco = builder.create();

                ImageButton btnDescripcionFoco = dialogFoco.findViewById(R.id.btnDescripcionFoco);
                btnDescripcionFoco.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View dialogNuevoFoco = getLayoutInflater().inflate(R.layout.dialog_nuevo_foco,null);

                        etDescripcionFoco = dialogNuevoFoco.findViewById(R.id.etDescripcionFoco);
                        etDescripcionFoco.setText(foco.getDescripcion());

                        Button btnGuardarFoco = dialogNuevoFoco.findViewById(R.id.btnGuardarFoco);

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setView(dialogNuevoFoco);
                        final AlertDialog alertDialogNuevoFoco = builder.create();
                        btnGuardarFoco.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Foco focoModificado = foco;
                                focoModificado.setDescripcion(etDescripcionFoco.getText().toString());

                                /*new ShowToast(getContext(), focoNuevo.getLatitud()
                                        + " // " + focoNuevo.getLongitud()
                                        + " // " + focoNuevo.getDescripcion()
                                        + " // " + focoNuevo.getCoordinadorId());*/

                                Call<Foco> dato = ApiClient.getMyApiClient().putFoco(
                                        sp.getString("token",""),
                                        foco.getFocoId(),
                                        focoModificado
                                );
                                dato.enqueue(new Callback<Foco>() {
                                    @Override
                                    public void onResponse(Call<Foco> call, Response<Foco> response) {
                                        if (response.isSuccessful()) {
                                            new ShowToast(getContext(), "Foco actualizado" );
                                        } else {
                                            new ShowToast(getContext(), "Error al actualizar foco");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Foco> call, Throwable t) {
                                        new ShowToast(getContext(), t.getMessage());
                                    }
                                });
                                focoViewModel.setFocos();
                                alertDialogNuevoFoco.dismiss();
                            }
                        });
                        alertDialogNuevoFoco.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        alertDialogNuevoFoco.show();
                        alertDialogFoco.dismiss();
                    }
                });

                alertDialogFoco.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                alertDialogFoco.show();
                break;

            case "foco_otro":

                final Foco focoOtro = (Foco) marker.getTag();

                // AlertDialog FOCO OTRO
                View dialogFocoOtro = inflater.inflate(R.layout.dialog_foco_otro,null);
                TextView tvFocoOtro = dialogFocoOtro.findViewById(R.id.tvFocoOtro);
                tvFocoOtro.setText("Coordinador de foco: " + focoOtro.getCoordinador().getNombre());
                Button btnLlamarCoordinador = dialogFocoOtro.findViewById(R.id.btnLlamarCoordinador);
                btnLlamarCoordinador.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String telefono = focoOtro.getCoordinador().getTelefono();
                        String dial = "tel:" + telefono;
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(dial));
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            startActivity(intent);
                        } else {
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
                        }
                    }
                });
                builder.setView(dialogFocoOtro);
                final AlertDialog alertDialogFocoOtro = builder.create();
                alertDialogFocoOtro.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                alertDialogFocoOtro.show();
                break;

            default:
                break;
        }
        return true;
    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        final String latitud = latLng.latitude+"";
        final String longitud = latLng.longitude+"";

        // AlertDialog NUEVO FOCO
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        LayoutInflater inflater = getLayoutInflater();

        View dialogNuevoFoco = inflater.inflate(R.layout.dialog_nuevo_foco,null);

        etDescripcionFoco = dialogNuevoFoco.findViewById(R.id.etDescripcionFoco);

        coordinadorViewModel.getCoordinador().observe(this, new Observer<Coordinador>() {
            @Override
            public void onChanged(Coordinador coordinador) {
                coordinadorId = coordinador.getCoordinadorId();
            }
        });

        builder.setView(dialogNuevoFoco);
        final AlertDialog alertDialogNuevoFoco = builder.create();

        Button btnGuardarFoco = dialogNuevoFoco.findViewById(R.id.btnGuardarFoco);
        btnGuardarFoco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Foco focoNuevo = new Foco();
                focoNuevo.setLatitud(latitud);
                focoNuevo.setLongitud(longitud);
                focoNuevo.setDescripcion(etDescripcionFoco.getText().toString());
                focoNuevo.setCoordinadorId(coordinadorId);

                /*new ShowToast(getContext(), focoNuevo.getLatitud()
                        + " // " + focoNuevo.getLongitud()
                        + " // " + focoNuevo.getDescripcion()
                        + " // " + focoNuevo.getCoordinadorId());*/

                Call<Foco> dato = ApiClient.getMyApiClient().postFoco(
                        sp.getString("token",""),
                        focoNuevo
                );
                dato.enqueue(new Callback<Foco>() {
                    @Override
                    public void onResponse(Call<Foco> call, Response<Foco> response) {
                        if (response.isSuccessful()) {
                            new ShowToast(getContext(), "Foco creado" );
                        } else {
                            new ShowToast(getContext(), "Error al crear foco");
                        }
                    }

                    @Override
                    public void onFailure(Call<Foco> call, Throwable t) {
                        new ShowToast(getContext(), t.getMessage());
                    }
                });
                focoViewModel.setFocos();
                alertDialogNuevoFoco.dismiss();
            }
        });

        alertDialogNuevoFoco.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialogNuevoFoco.show();
        coordinadorViewModel.setCoordinador();

    }
}