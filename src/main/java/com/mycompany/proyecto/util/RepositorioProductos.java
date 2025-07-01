package com.mycompany.proyecto.util;

import com.mycompany.proyecto.modelo.Producto;
import java.util.ArrayList;
import java.util.List;

public class RepositorioProductos {

    // Lista estática que persiste durante la ejecución
    private static List<Producto> productos = new ArrayList<>();

    static {
        // Inicialización
        productos.add(new Producto(1, "Laptop HP", "HP", "ProBook", 2500.0f, 10));
        productos.add(new Producto(2, "Impresora Canon", "Canon", "Pixma G2110", 700.0f, 5));
        productos.add(new Producto(3, "Switch TP-Link", "TP-Link", "TL-SG1008D", 120.0f, 8));
        productos.add(new Producto(4, "Servidor Dell", "Dell", "PowerEdge", 6500.0f, 2));
    }

    // Obtener todos los productos
    public static List<Producto> obtenerProductos() {
        return productos;
    }

    // Agregar un nuevo producto
    public static void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    // Eliminar un producto
    public static void eliminarProducto(Producto producto) {
        productos.remove(producto);
    }

    // (Opcional) Buscar producto por nombre
    public static Producto buscarPorNombre(String nombre) {
        for (Producto p : productos) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }
}
