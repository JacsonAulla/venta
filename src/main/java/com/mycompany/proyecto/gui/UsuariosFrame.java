package com.mycompany.proyecto.gui;

import com.mycompany.proyecto.usuarios.*;
import com.mycompany.proyecto.seguridad.Autenticacion;
import com.mycompany.proyecto.util.RepositorioUsuarios;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UsuariosFrame extends JFrame {

    public UsuariosFrame() {
        setTitle("Gestión de Usuarios");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnVer = new JButton("Ver Usuarios");
        JButton btnAgregar = new JButton("Agregar Usuario");
        JButton btnModificar = new JButton("Modificar Usuario");
        JButton btnEliminar = new JButton("Eliminar Usuario");
        JButton btnCerrar = new JButton("Cerrar");

        btnVer.addActionListener(e -> mostrarUsuarios());
        btnAgregar.addActionListener(e -> agregarUsuario());
        btnModificar.addActionListener(e -> modificarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());
        btnCerrar.addActionListener(e -> dispose());

        panel.add(btnVer);
        panel.add(btnAgregar);
        panel.add(btnModificar);
        panel.add(btnEliminar);
        panel.add(btnCerrar);

        add(panel);
    }

    private void mostrarUsuarios() {
        List<Usuario> usuarios = RepositorioUsuarios.obtenerUsuarios();
        StringBuilder sb = new StringBuilder();
        for (Usuario u : usuarios) {
            sb.append("ID: ").append(u.getId())
              .append(", Nombre: ").append(u.getNombre())
              .append(", Correo: ").append(u.getCorreo())
              .append(", Rol: ").append(u.getRol())
              .append("\n");
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(450, 200));

        JOptionPane.showMessageDialog(this, scroll, "Lista de Usuarios", JOptionPane.INFORMATION_MESSAGE);
    }

    private void agregarUsuario() {
        try {
            // ID con validación
            int id = -1;
            while (id < 0) {
                String input = JOptionPane.showInputDialog(this, "ID:");
                if (input == null) return; // Cancelado
                try {
                    id = Integer.parseInt(input);
                    if (id < 0) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "ID inválido. Debe ser un número positivo.", "Error", JOptionPane.ERROR_MESSAGE);
                    id = -1;
                }
            }

            // Nombre
            String nombre;
            do {
                nombre = JOptionPane.showInputDialog(this, "Nombre:");
                if (nombre == null) return;
                if (nombre.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } while (nombre.trim().isEmpty());

            // Correo
            String correo;
            do {
                correo = JOptionPane.showInputDialog(this, "Correo:");
                if (correo == null) return;
                if (!correo.contains("@") || !correo.contains(".")) {
                    JOptionPane.showMessageDialog(this, "Correo no válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    correo = "";
                }
            } while (correo.trim().isEmpty());

            // Contraseña
            String password;
            do {
                password = JOptionPane.showInputDialog(this, "Contraseña:");
                if (password == null) return;
                if (password.length() < 4) {
                    JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 4 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
                    password = "";
                }
            } while (password.trim().isEmpty());

            // Rol
            String rol;
            do {
                rol = JOptionPane.showInputDialog(this, "Rol (cliente, empleado, admin):");
                if (rol == null) return;
                if (!rol.equalsIgnoreCase("cliente") && !rol.equalsIgnoreCase("empleado") && !rol.equalsIgnoreCase("admin")) {
                    JOptionPane.showMessageDialog(this, "Rol no válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    rol = "";
                }
            } while (rol.trim().isEmpty());

            Usuario nuevo = switch (rol.toLowerCase()) {
                case "cliente" -> new Cliente(id, nombre, correo, Autenticacion.cifrarPassword(password));
                case "empleado" -> new Empleado(id, nombre, correo, Autenticacion.cifrarPassword(password));
                case "admin" -> new Administrador(id, nombre, correo, Autenticacion.cifrarPassword(password));
                default -> null;
            };

            if (nuevo != null) {
                RepositorioUsuarios.agregarUsuario(nuevo);
                JOptionPane.showMessageDialog(this, "Usuario agregado correctamente.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    private void modificarUsuario() {
        List<Usuario> usuarios = RepositorioUsuarios.obtenerUsuarios();
        if (usuarios.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay usuarios para modificar.");
            return;
        }

        String[] nombres = usuarios.stream().map(Usuario::getNombre).toArray(String[]::new);
        String seleccion = (String) JOptionPane.showInputDialog(this, "Selecciona un usuario:", "Modificar Usuario",
                JOptionPane.PLAIN_MESSAGE, null, nombres, nombres[0]);

        if (seleccion != null) {
            Usuario u = usuarios.stream()
                    .filter(user -> user.getNombre().equals(seleccion))
                    .findFirst()
                    .orElse(null);

            if (u != null) {
                try {
                    // Validar nuevo nombre
                    String nuevoNombre;
                    do {
                        nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo nombre:", u.getNombre());
                        if (nuevoNombre == null) return;
                        if (nuevoNombre.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } while (nuevoNombre.trim().isEmpty());

                    // Validar nuevo correo
                    String nuevoCorreo;
                    do {
                        nuevoCorreo = JOptionPane.showInputDialog(this, "Nuevo correo:", u.getCorreo());
                        if (nuevoCorreo == null) return;
                        if (!nuevoCorreo.contains("@") || !nuevoCorreo.contains(".")) {
                            JOptionPane.showMessageDialog(this, "Correo no válido.", "Error", JOptionPane.ERROR_MESSAGE);
                            nuevoCorreo = "";
                        }
                    } while (nuevoCorreo.trim().isEmpty());

                    // Aplicar cambios
                    u.setNombre(nuevoNombre);
                    u.setCorreo(nuevoCorreo);

                    JOptionPane.showMessageDialog(this, "Usuario modificado correctamente.");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error modificando al usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }


    private void eliminarUsuario() {
        List<Usuario> usuarios = RepositorioUsuarios.obtenerUsuarios();
        if (usuarios.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay usuarios para eliminar.");
            return;
        }

        String[] nombres = usuarios.stream().map(Usuario::getNombre).toArray(String[]::new);
        String seleccion = (String) JOptionPane.showInputDialog(
                this, "Selecciona un usuario:", "Eliminar Usuario",
                JOptionPane.PLAIN_MESSAGE, null, nombres, nombres[0]);

        if (seleccion != null) {
            Usuario u = usuarios.stream()
                    .filter(user -> user.getNombre().equals(seleccion))
                    .findFirst().orElse(null);

            if (u != null) {
                int confirmar = JOptionPane.showConfirmDialog(
                        this, "¿Estás seguro de eliminar a " + u.getNombre() + "?",
                        "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

                if (confirmar == JOptionPane.YES_OPTION) {
                    RepositorioUsuarios.eliminarUsuario(u);
                    JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.");
                }
            }
        }
    }

}
