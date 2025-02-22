package com.example.adn_danielibarra.mvp.vista;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adn_danielibarra.R;
import com.example.adn_danielibarra.mvp.presentador.PresentadorParqueaderoImpl;
import com.example.adn_danielibarra.mvp.presentador.contratos.PresentadorParqueadero;
import com.example.adn_danielibarra.mvp.vista.contratos.VistaParqueadero;
import com.example.adn_danielibarra.mvp.vista.dialogo.DialogoIngresar;
import com.example.dominio.modelo.Vehiculo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements VistaParqueadero {

    private ProgressDialog progressDialog;
    RecyclerView vistaReciclada;
    ImageView imagenListaVacia;
    TextView tvCantidadCarros;
    TextView tvCantidadMotos;
    AdaptadorVehiculo adaptadorVehiculo;
    PresentadorParqueadero presentador;
    Button btnIngresar;
    DialogoIngresar dialogoIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializar();
        obtenerVehiculos();
        obtenerCantidadCarros();
        obtenerCantidadMotos();

    }

    private void inicializar() {
        btnIngresar = findViewById(R.id.btn_ingresar);
        vistaReciclada = findViewById(R.id.recyclerView);
        imagenListaVacia = findViewById(R.id.img_lista_vacia);
        tvCantidadCarros = findViewById(R.id.tv_cant_carros);
        tvCantidadMotos = findViewById(R.id.tv_cant_motos);
        ObtenerVehiculoIngresado obtenerVehiculoAdaptador = this::eliminarVehiculo;
        adaptadorVehiculo = new AdaptadorVehiculo(new ArrayList<>(), obtenerVehiculoAdaptador);
        vistaReciclada.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        vistaReciclada.setAdapter(adaptadorVehiculo);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        ObtenerVehiculoIngresado obtenerVehiculoIngresado = vehiculo -> ingresarVehiculo(vehiculo);
        VerificarVehiculo verificarVehiculo = placa -> presentador.obtenerVehiculo(placa);
        dialogoIngresar = new DialogoIngresar(obtenerVehiculoIngresado, verificarVehiculo);
        presentador = new PresentadorParqueaderoImpl(this, getApplicationContext());
        btnIngresar.setOnClickListener(v -> {
            dialogoIngresar.show(getSupportFragmentManager(), "");
        });
    }

    @Override
    public void obtenerVehiculos() {
        mostrarDialogoCargando(R.string.informacion, R.string.obtener_vehiculos);
        presentador.obtenerVehiculos();
    }

    @Override
    public void mostrarVehiculos(List<Vehiculo> vehiculoLista) {
        System.out.println("tamaño de vehiculo ---" + vehiculoLista.size());
        cancelarDialogoCargando();
        vistaReciclada.setVisibility(View.VISIBLE);
        imagenListaVacia.setVisibility(View.GONE);
        adaptadorVehiculo.modificarVehiculoLista(vehiculoLista);
    }

    @Override
    public void mostrarSinVehiculos() {
        cancelarDialogoCargando();
        vistaReciclada.setVisibility(View.GONE);
        imagenListaVacia.setVisibility(View.VISIBLE);
    }

    @Override
    public void eliminarVehiculo(Vehiculo vehiculo) {
        cobrarDialogo(vehiculo);
    }

    @Override
    public void obtenerCantidadCarros() {
        tvCantidadCarros.setText(String.valueOf(presentador.obtenerCantidadCarros()));
    }

    @Override
    public void obtenerCantidadMotos() {
        tvCantidadMotos.setText(String.valueOf(presentador.obtenerCantidadMotos()));
    }

    @Override
    public void ingresarVehiculo(Vehiculo vehiculo) {
        mostrarDialogoCargando(R.string.informacion, R.string.ingresando_vehiculo);
        presentador.ingresarVehiculo(vehiculo);
        cancelarDialogoCargando();
        new Handler().postDelayed(() -> {
            obtenerVehiculos();
            obtenerCantidadMotos();
            obtenerCantidadCarros();
            cancelarDialogoCargando();
        }, 500);
    }

    @Override
    public void mostrarDialogoAlerta(int titulo, int mensaje) {
        cancelarDialogoCargando();
        Toast.makeText(getApplicationContext(), getString(titulo) + " : " + getString(mensaje), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mostrarDialogoAlerta(int titulo, String mensaje) {
        cancelarDialogoCargando();
        Toast.makeText(getApplicationContext(), getString(titulo) + " : " + mensaje, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mostrarDialogoCargando(int titulo, int mensaje) {
        cancelarDialogoCargando();
        progressDialog.setTitle(getString(titulo));
        progressDialog.setMessage(getString(mensaje));
        progressDialog.show();
    }

    @Override
    public void cancelarDialogoCargando() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void cobrarDialogo(Vehiculo vehiculo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("El valor a cobrar es: " + presentador.calcularTotal(vehiculo))
                .setPositiveButton(R.string.cobrar, (dialog, which) -> {
                    finalizacionCobro(vehiculo);
                });
        builder.setNegativeButton(R.string.cancelar, (dialog, which) -> {

        });
        builder.create();
        Dialog dialog = builder.create();
        dialog.show();

    }

    private void finalizacionCobro(Vehiculo vehiculo) {
        mostrarDialogoCargando(R.string.informacion, R.string.cobrando_vehiculo);
        cancelarDialogoCargando();
        presentador.eliminarVehiculo(vehiculo);

        new Handler().postDelayed(() -> {
            obtenerVehiculos();
            obtenerCantidadMotos();
            obtenerCantidadCarros();
            cancelarDialogoCargando();
        }, 500);
    }
}