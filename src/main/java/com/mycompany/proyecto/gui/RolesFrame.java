package com.mycompany.proyecto.gui;

import com.mycompany.proyecto.usuarios.*;
import com.mycompany.proyecto.util.RepositorioUsuarios;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RolesFrame extends JFrame {

    public RolesFrame() {
        setTitle("Gestión de Roles de Usuario");
        setSize(450, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnVerRoles = new JButton("Ver Roles de Usuarios");
        JButton btnCambiarRol = new JButton("Cambiar Rol");
        JButton btnCerrar = new JButton("Cerrar");

        btnVerRoles.addActionListener(e -> mostrarRoles());
        btnCambiarRol.addActionListener(e -> cambiarRol());
        btnCerrar.addActionListener(e -> dispose());

        panel.add(btnVerRoles);
        panel.add(btnCambiarRol);
        panel.add(btnCerrar);

        add(panel);
    }

    private void mostrarRoles() {
        List<Usuario> usuarios = RepositorioUsuarios.obtenerUsuarios();
        StringBuilder sb = new StringBuilder();

        for (Usuario u : usuarios) {
            sb.append("ID: ").append(u.getId())
              .append(", Nombre: ").append(u.getNombre())
              .append(", Rol actual: ").append(u.getRol())
              .append("\n");
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(400, 150));

        JOptionPane.showMessageDialog(this, scroll, "Roles de Usuarios", JOptionPane.INFORMATION_MESSAGE);
    }

    private void cambiarRol() {
        List<Usuario> usuarios = RepositorioUsuarios.obtenerUsuarios();
        if (usuarios.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay usuarios registrados.");
            return;
        }

        String[] nombres = usuarios.stream().map(Usuario::getNombre).toArray(String[]::new);
        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Selecciona un usuario:",
                "Cambiar Rol",
                JOptionPane.PLAIN_MESSAGE,
                null,
                nombres,
                nombres[0]
        );

        if (seleccion != null) {
            Usuario usuario = usuarios.stream()
                    .filter(u -> u.getNombre().equals(seleccion))
                    .findFirst()
                    .orElse(null);

            if (usuario != null) {
                String[] roles = {"cliente", "empleado", "admin"};
                JComboBox<String> combo = new JComboBox<>(roles);
                combo.setSelectedItem(usuario.getRol().toLowerCase());

                int opcion = JOptionPane.showConfirmDialog(
                        this,
                        combo,
                        "Selecciona nuevo rol",
                        JOptionPane.OK_CANCEL_OPTION
                );

                if (opcion == JOptionPane.OK_OPTION) {
                    String nuevoRol = (String) combo.getSelectedItem();

                    Usuario nuevoUsuario = switch (nuevoRol) {
                        case "cliente" -> new Cliente(usuario.getId(), usuario.getNombre(), usuario.getCorreo(), usuario.getPassword());
                        case "empleado" -> new Empleado(usuario.getId(), usuario.getNombre(), usuario.getCorreo(), usuario.getPassword());
                        case "admin" -> new Administrador(usuario.getId(), usuario.getNombre(), usuario.getCorreo(), usuario.getPassword());
                        default -> null;
                    };

                    if (nuevoUsuario != null) {
                        RepositorioUsuarios.eliminarUsuario(usuario);
                        RepositorioUsuarios.agregarUsuario(nuevoUsuario);
                        JOptionPane.showMessageDialog(this, "Rol actualizado correctamente.");
                    }
                }
            }
        }
    }

}
