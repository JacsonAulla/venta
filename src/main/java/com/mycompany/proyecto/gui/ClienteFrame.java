package com.mycompany.proyecto.gui;

import com.mycompany.proyecto.modelo.Producto;
import com.mycompany.proyecto.modelo.SolicitudCompra;
import com.mycompany.proyecto.usuarios.Cliente;
import com.mycompany.proyecto.util.RepositorioProductos;
import com.mycompany.proyecto.util.RepositorioSolicitudes;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class ClienteFrame extends JFrame {

    private int solicitudId = 1; // contador local
    private Cliente cliente;

    public ClienteFrame(Cliente cliente) {
        this.cliente = cliente;

        setTitle("Cliente - Bienvenido " + cliente.getNombre());
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnConsultar = new JButton("Consultar Inventario");
        JButton btnSolicitar = new JButton("Registrar Solicitud de Compra");
        JButton btnHistorial = new JButton("Ver mis solicitudes");
        JButton btnSalir = new JButton("Cerrar sesión");

        btnConsultar.addActionListener(e -> mostrarInventario());
        btnSolicitar.addActionListener(e -> registrarSolicitud());
        btnHistorial.addActionListener(e -> mostrarHistorialSolicitudes());
        btnSalir.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });

        panel.add(btnConsultar);
        panel.add(btnSolicitar);
        panel.add(btnHistorial);
        panel.add(btnSalir);

        add(panel);
    }

    private void mostrarInventario() {
        List<Producto> productos = RepositorioProductos.obtenerProductos();

        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay productos en el inventario.", "Inventario", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] columnas = {"ID", "Nombre", "Marca", "Modelo", "Precio (S/)", "Stock"};
        Object[][] datos = new Object[productos.size()][6];

        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            datos[i][0] = p.getId();
            datos[i][1] = p.getNombre();
            datos[i][2] = p.getMarca();
            datos[i][3] = p.getModelo();
            datos[i][4] = String.format("%.2f", p.getPrecio());
            datos[i][5] = p.getStock();
        }

        JTable tabla = new JTable(datos, columnas);
        tabla.setEnabled(false);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabla.setRowHeight(24);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Centrar encabezados
        ((DefaultTableCellRenderer) tabla.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

        // Centrar columnas numéricas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
        tabla.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Precio
        tabla.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // Stock

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(800, 500));

        JOptionPane.showMessageDialog(this, scroll, "Inventario de Productos", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarHistorialSolicitudes() {
        List<SolicitudCompra> todas = RepositorioSolicitudes.obtenerTodas();
        List<SolicitudCompra> mias = todas.stream()
            .filter(s -> s.getCliente().getId() == cliente.getId())
            .toList();

        if (mias.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tienes solicitudes registradas.", "Historial", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] columnas = {"ID", "Producto", "Fecha", "Estado"};
        Object[][] datos = new Object[mias.size()][4];

        for (int i = 0; i < mias.size(); i++) {
            SolicitudCompra s = mias.get(i);
            datos[i][0] = s.getId();
            datos[i][1] = s.getProducto().getNombre();
            datos[i][2] = s.getFecha().toString();
            datos[i][3] = s.getEstado();
        }

        JTable tabla = new JTable(datos, columnas);
        tabla.setEnabled(false);
        tabla.setRowHeight(22);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(600, 300));

        JOptionPane.showMessageDialog(this, scroll, "Historial de Solicitudes", JOptionPane.INFORMATION_MESSAGE);
    }


    private void registrarSolicitud() {
        List<Producto> productos = RepositorioProductos.obtenerProductos();

        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay productos disponibles", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] nombres = productos.stream().map(Producto::getNombre).toArray(String[]::new);

        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione un producto:",
                "Registrar Solicitud",
                JOptionPane.PLAIN_MESSAGE,
                null,
                nombres,
                nombres[0]
        );

        if (seleccion != null) {
            Producto productoElegido = productos.stream()
                    .filter(p -> p.getNombre().equals(seleccion))
                    .findFirst()
                    .orElse(null);

            if (productoElegido != null) {
                if (productoElegido.getStock() <= 0) {
                    JOptionPane.showMessageDialog(this, "El producto no tiene stock disponible.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Reducir stock
                productoElegido.actualizarStock(productoElegido.getStock() - 1);

                // Crear y confirmar solicitud
                SolicitudCompra solicitud = new SolicitudCompra(solicitudId++, new Date(), productoElegido, cliente);
                solicitud.confirmar();
                RepositorioSolicitudes.agregarSolicitud(solicitud);

                JOptionPane.showMessageDialog(this,
                        "Solicitud registrada con éxito:\n" +
                                "Producto: " + productoElegido.getNombre() + "\n" +
                                "Cliente: " + cliente.getNombre() + "\n" +
                                "Fecha: " + solicitud.getFecha() + "\n" +
                                "Estado: " + solicitud.getEstado(),
                        "Solicitud Confirmada",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
