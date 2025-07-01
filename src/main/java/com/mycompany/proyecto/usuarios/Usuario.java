package com.mycompany.proyecto.usuarios;

public abstract class Usuario {
    protected int id;
    protected String nombre;
    protected String correo;
    protected String password;
    protected String rol;

    public Usuario(int id, String nombre, String correo, String password, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.rol = rol;
    }

    public abstract boolean validarCredenciales(String correo, String password);

    public String getRol() {
        return rol;
    }
    
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
    
    public String getCorreo() {
        return correo;
    }

    public String getPassword() {
        return password;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

}
