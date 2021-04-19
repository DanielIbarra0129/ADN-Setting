package com.example.adn_danielibarra.mvp.vista.dialogo;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.adn_danielibarra.R;
import com.example.adn_danielibarra.mvp.vista.MainActivity;
import com.example.adn_danielibarra.mvp.vista.ObtenerVehiculoIngresado;
import com.example.dominio.modelo.Carro;
import com.example.dominio.modelo.Moto;
import com.example.dominio.modelo.Vehiculo;

import java.util.Calendar;

public class DialogoIngresar extends DialogFragment {

    RadioButton radioCarro;
    RadioButton radioMoto;
    EditText etPlaca;
    Button btnCancelar;
    Button btnRegistrar;
    EditText etCilindraje;
    LinearLayout contenedorCilindraje;
    MainActivity mainActivity;
    ObtenerVehiculoIngresado obtenerVehiculoIngresado;
    Vehiculo vehiculo;

    public DialogoIngresar(ObtenerVehiculoIngresado obtenerVehiculoIngresado) {
        this.obtenerVehiculoIngresado = obtenerVehiculoIngresado;
    }

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mainActivity = (MainActivity) getActivity();
        View view = mainActivity.getLayoutInflater().inflate(R.layout.dialogo_registrar_vehiculo, null);
        inicializar(view);
        builder.setView(view);
        setCancelable(false);
        return builder.create();
    }

    private void inicializar(View vista) {
        radioCarro = vista.findViewById(R.id.tipoCarro);
        radioMoto = vista.findViewById(R.id.tipoMoto);
        contenedorCilindraje = vista.findViewById(R.id.contenedorCilindraje);
        etPlaca = vista.findViewById(R.id.ed_placa);
        etPlaca.setAllCaps(true);
        etCilindraje = vista.findViewById(R.id.et_cilindraje);
        btnCancelar = vista.findViewById(R.id.btnCancelar);
        btnRegistrar = vista.findViewById(R.id.btnRegistrar);
        btnCancelar.setOnClickListener(v -> {
            dismiss();
        });
        btnRegistrar.setOnClickListener(v -> {
            validarDatos();
            crearVehiculo(etPlaca.getText().toString(), etCilindraje.getText().toString());
            obtenerVehiculoIngresado.obtenerVehiculo(vehiculo);
            dismiss();
        });

        radioCarro.setOnClickListener(v -> {
            contenedorCilindraje.setVisibility(View.GONE);
        });
        radioMoto.setOnClickListener(v -> {
            contenedorCilindraje.setVisibility(View.VISIBLE);
        });
    }

    private void validarDatos() {
        if (etPlaca.getText().toString().isEmpty()) {
            mainActivity.mostrarDialogoAlerta(R.string.informacion, "Ingrese placa del vehiculo");
            return;
        }
        if (radioMoto.isChecked() && etCilindraje.getText().toString().isEmpty()) {
            mainActivity.mostrarDialogoAlerta(R.string.informacion, "Ingrese cilindraje de la moto");
        }
    }

    private void crearVehiculo(String placa, String cilindraje) {
        if (radioCarro.isChecked()) {
            vehiculo = new Carro(placa);
        } else {
            vehiculo = new Moto(placa, Integer.parseInt(cilindraje));
        }
        vehiculo.modificarFechaIngreso(Calendar.getInstance());
    }
}
