package com.mycompany.proyecto.modelo;

public class Producto {
    private int id;
    private String nombre;
    private String marca;
    private String modelo;
    private float precio;
    private int stock;

    public void actualizarDatos(String nombre, String marca, String modelo, float precio, int stock) {
        this.nombre = nombre;
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
        this.stock = stock;
    }

    public Producto(int id, String nombre, String marca, String modelo, float precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
        this.stock = stock;
    }

    public void actualizarStock(int nuevoStock) {
        this.stock = nuevoStock;
    }

    public String getNombre() {
        return nombre;
    }

    public int getStock() {
        return stock;
    }

    public float getPrecio() {
        return precio;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public int getId() {
        return id;
    }
}
