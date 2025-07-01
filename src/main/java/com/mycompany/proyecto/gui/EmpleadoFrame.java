package com.mycompany.proyecto.gui;

import com.mycompany.proyecto.modelo.Producto;
import com.mycompany.proyecto.util.RepositorioProductos;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EmpleadoFrame extends JFrame {
    public EmpleadoFrame(String nombre) {
        setTitle("Empleado - Bienvenido " + nombre);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnPedidos = new JButton("Consultar Inventario");
        JButton btnEnvios = new JButton("Preparar Envíos");
        JButton btnSalir = new JButton("Cerrar sesión");

        btnPedidos.addActionListener(e -> mostrarInventario());

        btnEnvios.addActionListener(e -> prepararEnvios());

        JButton btnHistorial = new JButton("Ver historial de envíos");
        btnHistorial.addActionListener(e -> verHistorialEnvios());

        btnSalir.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });

        panel.add(btnPedidos);
        panel.add(btnEnvios);
        panel.add(btnHistorial);
        panel.add(btnSalir);

        add(panel);
    }

    private void prepararEnvios() {
        java.util.List<com.mycompany.proyecto.modelo.SolicitudCompra> confirmadas = com.mycompany.proyecto.util.RepositorioSolicitudes.obtenerSolicitudesConfirmadas();

        if (confirmadas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay solicitudes confirmadas para preparar.", "Sin envíos", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] opciones = confirmadas.stream()
                .map(s -> "Solicitud ID: " + s.getId() + " - Fecha: " + s.getFecha())
                .toArray(String[]::new);

        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Selecciona una solicitud para preparar:",
                "Preparar Envío",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (seleccion != null) {
            int idSeleccionado = Integer.parseInt(seleccion.split(":")[1].trim().split(" ")[0]);
            com.mycompany.proyecto.modelo.SolicitudCompra solicitudSeleccionada = confirmadas.stream()
                    .filter(s -> s.getId() == idSeleccionado)
                    .findFirst()
                    .orElse(null);

            if (solicitudSeleccionada != null) {
                com.mycompany.proyecto.util.RepositorioSolicitudes.marcarComoPreparada(solicitudSeleccionada);
                JOptionPane.showMessageDialog(this,
                        "Solicitud ID " + solicitudSeleccionada.getId() + " marcada como preparada.",
                        "Envío preparado",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void verHistorialEnvios() {
        java.util.List<com.mycompany.proyecto.modelo.SolicitudCompra> preparadas = com.mycompany.proyecto.util.RepositorioSolicitudes.obtenerSolicitudesPreparadas();

        if (preparadas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay envíos preparados todavía.", "Historial vacío", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("Solicitudes preparadas:\n");
        for (com.mycompany.proyecto.modelo.SolicitudCompra s : preparadas) {
            sb.append("ID: ").append(s.getId())
            .append(" - Fecha: ").append(s.getFecha())
            .append("\n");
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(350, 200));

        JOptionPane.showMessageDialog(this, scrollPane, "Historial de Envíos", JOptionPane.INFORMATION_MESSAGE);
    }


    private void mostrarInventario() {
        List<Producto> productos = RepositorioProductos.obtenerProductos();
        StringBuilder sb = new StringBuilder();
        for (Producto p : productos) {
            sb.append("ID: ").append(p.getId())
              .append(", Nombre: ").append(p.getNombre())
              .append(", Marca: ").append(p.getMarca())
              .append(", Modelo: ").append(p.getModelo())
              .append(", Precio: S/ ").append(p.getPrecio())
              .append(", Stock: ").append(p.getStock())
              .append("\n");
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(380, 200));

        JOptionPane.showMessageDialog(this, scroll, "Inventario de Productos", JOptionPane.INFORMATION_MESSAGE);
    }
}
