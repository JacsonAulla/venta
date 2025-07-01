package com.mycompany.proyecto.util;

import com.mycompany.proyecto.modelo.SolicitudCompra;

import java.util.ArrayList;
import java.util.List;

public class RepositorioSolicitudes {
    private static List<SolicitudCompra> solicitudes = new ArrayList<>();

    public static void agregarSolicitud(SolicitudCompra solicitud) {
        solicitudes.add(solicitud);
    }

    public static List<SolicitudCompra> obtenerSolicitudesConfirmadas() {
        List<SolicitudCompra> confirmadas = new ArrayList<>();
        for (SolicitudCompra s : solicitudes) {
            if ("confirmado".equalsIgnoreCase(s.getEstado())) {
                confirmadas.add(s);
            }
        }
        return confirmadas;
    }

    public static List<SolicitudCompra> obtenerSolicitudesPreparadas() {
        List<SolicitudCompra> preparadas = new ArrayList<>();
        for (SolicitudCompra s : solicitudes) {
            if ("preparado".equalsIgnoreCase(s.getEstado())) {
                preparadas.add(s);
            }
        }
        return preparadas;
    }


    public static void marcarComoPreparada(SolicitudCompra solicitud) {
        solicitud.setEstado("preparado");
    }

    public static List<SolicitudCompra> obtenerTodas() {
        return new ArrayList<>(solicitudes);
    }
}
