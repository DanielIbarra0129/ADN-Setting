package com.example.infraestructura;

import android.content.Context;

import com.example.dominio.modelo.Moto;
import com.example.dominio.modelo.Vehiculo;
import com.example.dominio.repositorio.VehiculoRepositorio;

import java.util.ArrayList;
import java.util.List;

public class VehiculoRepostorioMotoTemporal implements VehiculoRepositorio {

    List<Vehiculo> vehiculoList = new ArrayList<>();

    public VehiculoRepostorioMotoTemporal(Context context) {
    }

    @Override
    public List<Vehiculo> obtenerVehiculos() {
        vehiculoList.add(new Moto("zxc123", 600));
        vehiculoList.add(new Moto("zxc456", 500));
        vehiculoList.add(new Moto("zxc789", 200));
        vehiculoList.add(new Moto("zxc850", 1000));
        return vehiculoList;
    }

    @Override
    public void guardarVehiculo(Vehiculo vehiculo) {
        System.out.println("Moto guardado");
    }

    @Override
    public void eliminarVehiculo(Vehiculo vehiculo) {
        System.out.println("Moto eliminada");
    }

    @Override
    public int obtenerCantidadVehiculos() {
        return vehiculoList.size();
    }
}
