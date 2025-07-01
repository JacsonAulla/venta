package com.mycompany.proyecto.gui;

import com.mycompany.proyecto.modelo.Producto;
import com.mycompany.proyecto.util.RepositorioProductos;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminFrame extends JFrame {
    public AdminFrame(String nombre) {
        setTitle("Administrador - Bienvenido " + nombre);
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnVer = new JButton("Ver Productos");
        JButton btnAgregar = new JButton("Agregar Producto");
        JButton btnModificar = new JButton("Modificar Producto");
        JButton btnEliminar = new JButton("Eliminar Producto");
        JButton btnRoles = new JButton("Gestionar Roles");
        JButton btnUsuarios = new JButton("Gestionar Usuarios");
        JButton btnSalir = new JButton("Cerrar sesión");
        

        btnVer.addActionListener(e -> mostrarInventario());
        btnAgregar.addActionListener(e -> agregarProducto());
        btnModificar.addActionListener(e -> modificarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnUsuarios.addActionListener(e -> new UsuariosFrame().setVisible(true));
        btnRoles.addActionListener(e -> new RolesFrame().setVisible(true));

        btnSalir.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });

        panel.add(btnVer);
        panel.add(btnAgregar);
        panel.add(btnModificar);
        panel.add(btnEliminar);
        panel.add(btnUsuarios);
        panel.add(btnRoles);
        panel.add(btnSalir);

        add(panel);
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

    private void agregarProducto() {
        while (true) {
            try {
                String idStr = JOptionPane.showInputDialog(this, "ID:");
                if (idStr == null) break; // Cancelado
                int id = Integer.parseInt(idStr);

                // Validar que no exista un producto con el mismo ID
                boolean existe = RepositorioProductos.obtenerProductos()
                        .stream()
                        .anyMatch(p -> p.getId() == id);
                if (existe) {
                    JOptionPane.showMessageDialog(this, "⚠️ Ya existe un producto con ese ID.", "ID duplicado", JOptionPane.WARNING_MESSAGE);
                    continue;
                }

                String nombre = JOptionPane.showInputDialog(this, "Nombre:");
                if (nombre == null || nombre.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "❌ El nombre no puede estar vacío.");
                    continue;
                }

                String marca = JOptionPane.showInputDialog(this, "Marca:");
                if (marca == null || marca.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "❌ La marca no puede estar vacía.");
                    continue;
                }

                String modelo = JOptionPane.showInputDialog(this, "Modelo:");
                if (modelo == null || modelo.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "❌ El modelo no puede estar vacío.");
                    continue;
                }

                String precioStr = JOptionPane.showInputDialog(this, "Precio:");
                if (precioStr == null) break;
                float precio = Float.parseFloat(precioStr);

                String stockStr = JOptionPane.showInputDialog(this, "Stock:");
                if (stockStr == null) break;
                int stock = Integer.parseInt(stockStr);

                Producto nuevo = new Producto(id, nombre.trim(), marca.trim(), modelo.trim(), precio, stock);
                RepositorioProductos.agregarProducto(nuevo);

                JOptionPane.showMessageDialog(this, "✅ Producto agregado correctamente.");
                break; // Salir si fue exitoso
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "❌ Precio o stock no válido. Intenta nuevamente.", "Error de formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "❌ Error al agregar el producto. Intenta nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void modificarProducto() {
        List<Producto> productos = RepositorioProductos.obtenerProductos();
        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay productos para modificar.");
            return;
        }

        String[] nombres = productos.stream().map(Producto::getNombre).toArray(String[]::new);
        String seleccion = (String) JOptionPane.showInputDialog(this, "Selecciona producto:", "Modificar Producto", JOptionPane.PLAIN_MESSAGE, null, nombres, nombres[0]);

        if (seleccion != null) {
            Producto prod = productos.stream().filter(p -> p.getNombre().equals(seleccion)).findFirst().orElse(null);
            if (prod != null) {
                while (true) {
                    try {
                        String nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo nombre:", prod.getNombre());
                        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(this, "❌ El nombre no puede estar vacío.");
                            continue;
                        }

                        String nuevaMarca = JOptionPane.showInputDialog(this, "Nueva marca:", prod.getMarca());
                        if (nuevaMarca == null || nuevaMarca.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(this, "❌ La marca no puede estar vacía.");
                            continue;
                        }

                        String nuevoModelo = JOptionPane.showInputDialog(this, "Nuevo modelo:", prod.getModelo());
                        if (nuevoModelo == null || nuevoModelo.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(this, "❌ El modelo no puede estar vacío.");
                            continue;
                        }

                        String precioStr = JOptionPane.showInputDialog(this, "Nuevo precio:", prod.getPrecio());
                        if (precioStr == null) break;
                        float nuevoPrecio = Float.parseFloat(precioStr);

                        String stockStr = JOptionPane.showInputDialog(this, "Nuevo stock:", prod.getStock());
                        if (stockStr == null) break;
                        int nuevoStock = Integer.parseInt(stockStr);

                        prod.actualizarDatos(nuevoNombre.trim(), nuevaMarca.trim(), nuevoModelo.trim(), nuevoPrecio, nuevoStock);
                        JOptionPane.showMessageDialog(this, "✅ Producto modificado correctamente.");
                        break;

                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "❌ Precio o stock no válido. Intenta nuevamente.", "Error de formato", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "❌ Error modificando el producto. Intenta nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }



    private void eliminarProducto() {
        List<Producto> productos = RepositorioProductos.obtenerProductos();
        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay productos para eliminar.");
            return;
        }

        String[] nombres = productos.stream().map(Producto::getNombre).toArray(String[]::new);
        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Selecciona producto:",
                "Eliminar Producto",
                JOptionPane.PLAIN_MESSAGE,
                null,
                nombres,
                nombres[0]
        );

        if (seleccion != null) {
            Producto prod = productos.stream()
                    .filter(p -> p.getNombre().equals(seleccion))
                    .findFirst()
                    .orElse(null);

            if (prod != null) {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "¿Estás seguro de eliminar el producto \"" + prod.getNombre() + "\"?",
                        "Confirmar Eliminación",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    RepositorioProductos.eliminarProducto(prod);
                    JOptionPane.showMessageDialog(this, "✅ Producto eliminado correctamente.");
                }
            }
        }
    }

}
