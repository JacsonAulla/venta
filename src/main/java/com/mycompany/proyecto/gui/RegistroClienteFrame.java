package com.mycompany.proyecto.gui;

import com.mycompany.proyecto.usuarios.Cliente;
import com.mycompany.proyecto.usuarios.Usuario;
import com.mycompany.proyecto.seguridad.Autenticacion;
import com.mycompany.proyecto.util.RepositorioUsuarios;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RegistroClienteFrame extends JFrame {
    private JTextField nombreField;
    private JTextField correoField;
    private JPasswordField passwordField;
    private LoginFrame loginFrame;

    public RegistroClienteFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;

        setTitle("Registro de Cliente");
        setSize(400, 230);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Nombre:"));
        nombreField = new JTextField();
        panel.add(nombreField);

        panel.add(new JLabel("Correo:"));
        correoField = new JTextField();
        panel.add(correoField);

        panel.add(new JLabel("Contraseña:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnCancelar = new JButton("Cancelar");

        btnRegistrar.addActionListener(e -> registrarCliente());
        btnCancelar.addActionListener(e -> cancelarRegistro());

        panel.add(btnCancelar);
        panel.add(btnRegistrar);

        add(panel);
    }

    private void registrarCliente() {
        String nombre = nombreField.getText().trim();
        String correo = correoField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        // Validaciones
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (correo.isEmpty() || !correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            JOptionPane.showMessageDialog(this, "Ingrese un correo válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (RepositorioUsuarios.buscarPorCorreo(correo) != null) {
            JOptionPane.showMessageDialog(this, "Este correo ya está registrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() < 4) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 4 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = generarNuevoId();
        String passCifrada = Autenticacion.cifrarPassword(password);
        Cliente nuevo = new Cliente(id, nombre, correo, passCifrada);
        RepositorioUsuarios.agregarUsuario(nuevo);

        JOptionPane.showMessageDialog(this, "Registro exitoso. Ahora puede iniciar sesión.");
        this.dispose(); // cerrar ventana de registro

        if (loginFrame != null) {
            loginFrame.setVisible(true); // volver al login
        }
    }

    private void cancelarRegistro() {
        this.dispose(); // cerrar ventana de registro
        if (loginFrame != null) {
            loginFrame.setVisible(true); // volver al login si estaba oculto
        }
    }

    private int generarNuevoId() {
        List<Usuario> usuarios = RepositorioUsuarios.obtenerUsuarios();
        int maxId = usuarios.stream().mapToInt(Usuario::getId).max().orElse(0);
        return maxId + 1;
    }
}
