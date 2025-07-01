package com.mycompany.proyecto.gui;

import com.mycompany.proyecto.usuarios.*;
import com.mycompany.proyecto.util.RepositorioUsuarios;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LoginFrame extends JFrame {
    private JTextField correoField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registrarButton;

    private List<Usuario> usuarios = RepositorioUsuarios.obtenerUsuarios();

    public LoginFrame() {
        setTitle("Sistema de Inventario - Login");
        setSize(400, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal con BoxLayout vertical
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Panel de campos (correo y contraseña)
        JPanel fieldsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        fieldsPanel.add(new JLabel("Correo:"));
        correoField = new JTextField();
        fieldsPanel.add(correoField);

        fieldsPanel.add(new JLabel("Contraseña:"));
        passwordField = new JPasswordField();
        fieldsPanel.add(passwordField);

        // Panel de botones en horizontal
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        registrarButton = new JButton("Registrarse");
        loginButton = new JButton("Iniciar sesión");

        buttonPanel.add(registrarButton);
        buttonPanel.add(loginButton);

        // Acciones
        loginButton.addActionListener(e -> login());
        
        registrarButton.addActionListener(e -> {
            this.setVisible(false);
            new RegistroClienteFrame(this).setVisible(true);
        });


        // Agregar todo al main panel
        mainPanel.add(fieldsPanel);
        mainPanel.add(buttonPanel);
        add(mainPanel);
    }

    private void login() {
        String correo = correoField.getText();
        String password = new String(passwordField.getPassword());

        for (Usuario usuario : usuarios) {
            if (usuario.validarCredenciales(correo, password)) {
                JOptionPane.showMessageDialog(this, "Bienvenido, " + usuario.getNombre() + " (" + usuario.getRol() + ")");
                abrirVentanaSegunRol(usuario);
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void abrirVentanaSegunRol(Usuario usuario) {
        this.dispose();

        switch (usuario.getRol()) {
            case "cliente" -> new ClienteFrame((Cliente) usuario).setVisible(true);
            case "empleado" -> new EmpleadoFrame(usuario.getNombre()).setVisible(true);
            case "admin" -> new AdminFrame(usuario.getNombre()).setVisible(true);
        }
    }
}
