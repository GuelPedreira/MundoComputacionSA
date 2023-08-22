
package controlStock;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManejoProductos {

    public static void cargarStock() {
        boolean cargarMasStock = true;
    
        while (cargarMasStock) {
            String nombre = JOptionPane.showInputDialog("Ingrese el nombre del producto:");
            
            // Verificar si se seleccionó "cancelar"
            if (nombre == null) {
                return; // Volver al menú principal
            }
    
            double precio = 0.0;
            try {
                String precioStr = JOptionPane.showInputDialog("Ingrese el precio del producto:");
                
                // Verificar si se seleccionó "cancelar"
                if (precioStr == null) {
                    return; // Volver al menú principal
                }
                
                precio = Double.parseDouble(precioStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingrese un valor numérico válido para el precio");
                continue;
            }
            
            int stock = 0;
            try {
                String stockStr = JOptionPane.showInputDialog("Ingrese el stock del producto:");
                
                // Verificar si se seleccionó "cancelar"
                if (stockStr == null) {
                    return; // Volver al menú principal
                }
                
                stock = Integer.parseInt(stockStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingrese un valor numérico válido para el stock");
                continue;
            }
    
            // Convertir el nombre a mayúsculas
            nombre = nombre.toUpperCase();
    
            Connection connection = DataBaseManager.getConnection();
            PreparedStatement preparedStatement = null;
            ResultSet resultado = null;
    
            try {
                String query = "SELECT * FROM productos WHERE UPPER(nombre) = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, nombre);
    
                resultado = preparedStatement.executeQuery();
    
                if (resultado.next()) {
                    // El producto ya existe
                    JOptionPane.showMessageDialog(null, "El producto ya existe en la base de datos. Seleccione otra opción.");
    
                } else {
                    // El producto no existe, agregar un nuevo registro
                    if (stock > 100) {
                        JOptionPane.showMessageDialog(null, "El stock máximo es 100");
                    } else {
                        query = "INSERT INTO productos (nombre, precio, stock) VALUES (?, ?, ?)";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, nombre);
                        preparedStatement.setDouble(2, precio);
                        preparedStatement.setInt(3, stock);
    
                        int rowsAffected = preparedStatement.executeUpdate();
    
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "Producto agregado exitosamente");
                        } else {
                            JOptionPane.showMessageDialog(null, "No se pudo agregar el producto");
                        }
                    }
                }
    
                int opcion = JOptionPane.showOptionDialog(null, "¿Desea cargar más stock?", "Confirmación",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
    
                if (opcion == JOptionPane.NO_OPTION) {
                    cargarMasStock = false;
                }
    
            } catch (SQLException e) {
                System.out.println("Falló la conexión");
                e.printStackTrace();
            } finally {
                try {
                    if (resultado != null) {
                        resultado.close();
                    }
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void comprarStock() {
        boolean comprarMasStock = true;
    
        while (comprarMasStock) {
            String nombre = JOptionPane.showInputDialog("Ingrese el nombre del producto:");
            
            // Verificar si se seleccionó "cancelar"
            if (nombre == null) {
                return; // Volver al menú principal
            }
    
            double precio = 0.0;
            try {
                String precioStr = JOptionPane.showInputDialog("Ingrese el precio del producto:");
                
                // Verificar si se seleccionó "cancelar"
                if (precioStr == null) {
                    return; // Volver al menú principal
                }
                
                precio = Double.parseDouble(precioStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingrese un valor numérico válido para el precio");
                continue;
            }
            
            int cantidad = 0;
            try {
                String cantidadStr = JOptionPane.showInputDialog("Ingrese la cantidad a comprar:");
                
                // Verificar si se seleccionó "cancelar"
                if (cantidadStr == null) {
                    return; // Volver al menú principal
                }
                
                cantidad = Integer.parseInt(cantidadStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingrese un valor numérico válido para la cantidad");
                continue;
            }
    
            // Convertir el nombre a mayúsculas
            nombre = nombre.toUpperCase();
    
            Connection connection = DataBaseManager.getConnection();
            PreparedStatement preparedStatement = null;
            ResultSet resultado = null;
    
            try {
                String query = "SELECT * FROM productos WHERE UPPER(nombre) = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, nombre);
    
                resultado = preparedStatement.executeQuery();
    
                if (resultado.next()) {
                    // El producto ya existe
                    double precioExistente = resultado.getDouble("precio");
                    int stockExistente = resultado.getInt("stock");
    
                    if (precio == precioExistente) {
                        // Actualizar el stock existente
                        int nuevoStock = stockExistente + cantidad;
    
                        if (nuevoStock <= 100) {
                            query = "UPDATE productos SET stock = ? WHERE nombre = ?";
                            preparedStatement = connection.prepareStatement(query);
                            preparedStatement.setInt(1, nuevoStock);
                            preparedStatement.setString(2, nombre);
    
                            int rowsAffected = preparedStatement.executeUpdate();
    
                            if (rowsAffected > 0) {
                                JOptionPane.showMessageDialog(null, "Compra realizada exitosamente");
                            } else {
                                JOptionPane.showMessageDialog(null, "No se pudo realizar la compra");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "El stock máximo es 100. Stock actual: " + stockExistente);
                        }
                    } else {
                        // Modificar el precio existente y actualizar el stock
                        query = "UPDATE productos SET precio = ?, stock = ? WHERE nombre = ?";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setDouble(1, precio);
                        preparedStatement.setInt(2, cantidad);
                        preparedStatement.setString(3, nombre);
    
                        int rowsAffected = preparedStatement.executeUpdate();
    
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "Compra realizada exitosamente");
                        } else {
                            JOptionPane.showMessageDialog(null, "No se pudo realizar la compra");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "El producto no existe en la base de datos. Seleccione la opción 1 para cargar stock inicial");
                }
    
                int opcion = JOptionPane.showOptionDialog(null, "¿Desea comprar más stock?", "Confirmación",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
    
                if (opcion == JOptionPane.NO_OPTION) {
                    comprarMasStock = false;
                }
    
            } catch (SQLException e) {
                System.out.println("Falló la conexión");
                e.printStackTrace();
            } finally {
                try {
                    if (resultado != null) {
                        resultado.close();
                    }
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    

    public static void mostrarProductos() {
        Connection connection = DataBaseManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultado = null;

        try {
            String query = "SELECT id, nombre, precio FROM productos ORDER BY nombre ASC";
            preparedStatement = connection.prepareStatement(query);

            resultado = preparedStatement.executeQuery();

            StringBuilder sb = new StringBuilder();
            while (resultado.next()) {
                int id = resultado.getInt("id");
                String nombre = resultado.getString("nombre");
                double precio = resultado.getDouble("precio");

                sb.append("CÓD.-").append(id).append(" - ").append(nombre).append(" - Precio: $").append(precio).append("\n");
            }

            if (sb.length() > 0) {
                JOptionPane.showMessageDialog(null, "Lista de productos:\n" + sb.toString());
            } else {
                JOptionPane.showMessageDialog(null, "No hay productos disponibles.");
            }

        } catch (SQLException e) {
            System.out.println("Falló la conexión");
            e.printStackTrace();
        } finally {
            try {
                if (resultado != null) {
                    resultado.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void mostrarStock() {
        Connection connection = DataBaseManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultado = null;

        try {
            String query = "SELECT id, nombre, stock FROM productos";
            preparedStatement = connection.prepareStatement(query);

            resultado = preparedStatement.executeQuery();

            StringBuilder sb = new StringBuilder();
            while (resultado.next()) {
                int id = resultado.getInt("id");
                String nombre = resultado.getString("nombre");
                int stock = resultado.getInt("stock");

                sb.append(String.format("%-5d%-20s%5d\n", id, nombre, stock));
            }

            if (sb.length() > 0) {
                JOptionPane.showMessageDialog(null, "Stock de productos:\n\n" + sb.toString());
            } else {
                JOptionPane.showMessageDialog(null, "No hay productos disponibles.");
            }

        } catch (SQLException e) {
            System.out.println("Falló la conexión");
            e.printStackTrace();
        } finally {
            try {
                if (resultado != null) {
                    resultado.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static void mostrarCosto() {
        while (true) {
            String codigoStr = JOptionPane.showInputDialog("Ingrese el código del producto:");
            
            // Verificar si se seleccionó "cancelar"
            if (codigoStr == null) {
                return; // Volver al menú principal
            }
    
            int codigo = Integer.parseInt(codigoStr);
    
            Connection connection = DataBaseManager.getConnection();
            PreparedStatement preparedStatement = null;
            ResultSet resultado = null;
    
            try {
                String query = "SELECT id, nombre, stock, precio FROM productos WHERE id = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, codigo);
    
                resultado = preparedStatement.executeQuery();
    
                if (resultado.next()) {
                    int id = resultado.getInt("id");
                    String nombre = resultado.getString("nombre");
                    int stock = resultado.getInt("stock");
                    double precio = resultado.getDouble("precio");
    
                    double costoTotal = stock * precio;
    
                    JOptionPane.showMessageDialog(null, id + " - " + nombre + "\nSu costo total es: $" + costoTotal);
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró el producto con el código especificado");
                }
    
                break; // Salir del bucle
    
            } catch (SQLException e) {
                System.out.println("Falló la conexión");
                e.printStackTrace();
            } finally {
                try {
                    if (resultado != null) {
                        resultado.close();
                    }
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    
    public static void mostrarCostoTotal() {
        Connection connection = DataBaseManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultado = null;
        double costoTotal = 0;
    
        try {
            String query = "SELECT id, nombre, stock, precio FROM productos";
            preparedStatement = connection.prepareStatement(query);
    
            resultado = preparedStatement.executeQuery();
    
            StringBuilder sb = new StringBuilder();
            while (resultado.next()) {
                int id = resultado.getInt("id");
                String nombre = resultado.getString("nombre");
                int stock = resultado.getInt("stock");
                double precio = resultado.getDouble("precio");
    
                double costoProducto = stock * precio;
                costoTotal += costoProducto;
    
                sb.append("CÓD.-").append(id).append(" - ").append(nombre).append(" - Costo total: $").append(costoProducto).append("\n");
            }
    
            sb.append("\nCosto total de todos los productos: $").append(costoTotal);
    
            if (sb.length() > 0) {
                JOptionPane.showMessageDialog(null, "Costo total de los productos:\n\n" + sb.toString());
            } else {
                JOptionPane.showMessageDialog(null, "No hay productos disponibles.");
            }
    
        } catch (SQLException e) {
            System.out.println("Falló la conexión");
            e.printStackTrace();
        } finally {
            try {
                if (resultado != null) {
                    resultado.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void retirarStock() {
        while (true) {
            String codigoStr = JOptionPane.showInputDialog("Ingrese el código del producto:");
            
            // Verificar si se seleccionó "cancelar"
            if (codigoStr == null) {
                return; // Volver al menú principal
            }
    
            int codigo = Integer.parseInt(codigoStr);
    
            String cantidadStr = JOptionPane.showInputDialog("Ingrese la cantidad a retirar:");
            
            // Verificar si se seleccionó "cancelar"
            if (cantidadStr == null) {
                return; // Volver al menú principal
            }
    
            int cantidad = Integer.parseInt(cantidadStr);
    
            Connection connection = DataBaseManager.getConnection();
            PreparedStatement preparedStatement = null;
            ResultSet resultado = null;
    
            try {
                String query = "SELECT id, stock FROM productos WHERE id = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, codigo);
    
                resultado = preparedStatement.executeQuery();
    
                if (resultado.next()) {
                    int id = resultado.getInt("id");
                    int stock = resultado.getInt("stock");
    
                    if (stock >= cantidad) {
                        int nuevoStock = stock - cantidad;
    
                        query = "UPDATE productos SET stock = ? WHERE id = ?";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setInt(1, nuevoStock);
                        preparedStatement.setInt(2, codigo);
    
                        int rowsAffected = preparedStatement.executeUpdate();
    
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "Retiro realizado con éxito. Nuevo stock: " + nuevoStock);
                        } else {
                            JOptionPane.showMessageDialog(null, "No se pudo realizar el retiro");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "No es posible realizar el retiro. Stock actual: " + stock);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró el producto con el código especificado");
                }
    
                break; // Salir del bucle
    
            } catch (SQLException e) {
                System.out.println("Falló la conexión");
                e.printStackTrace();
            } finally {
                try {
                    if (resultado != null) {
                        resultado.close();
                    }
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
        

    public static void alterarProducto() {
        String codigoStr = JOptionPane.showInputDialog("Ingrese el código del producto:");
        
        // Verificar si se seleccionó "cancelar"
        if (codigoStr == null) {
            return; // Volver al menú principal
        }
    
        int codigo = Integer.parseInt(codigoStr);
    
        Connection connection = DataBaseManager.getConnection();
        PreparedStatement preparedStatement = null;
    
        try {
            String query = "SELECT * FROM productos WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, codigo);
    
            ResultSet resultado = preparedStatement.executeQuery();
    
            if (resultado.next()) {
                String nombre = resultado.getString("nombre").toUpperCase();
    
                String[] opciones = {"Eliminar producto", "Modificar producto", "Modificar precio"};
    
                int opcion = JOptionPane.showOptionDialog(null, "¿Qué desea hacer?", "Alterar Producto", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
    
                switch (opcion) {
                    case 0: // Eliminar producto
                        query = "DELETE FROM productos WHERE id = ?";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setInt(1, codigo);
    
                        int rowsAffectedDelete = preparedStatement.executeUpdate();
    
                        if (rowsAffectedDelete > 0) {
                            JOptionPane.showMessageDialog(null, "Producto eliminado exitosamente");
                        } else {
                            JOptionPane.showMessageDialog(null, "No se encontró el producto con el código especificado");
                        }
                        break;
                    case 1: // Modificar nombre del producto
                        String nuevoNombre = JOptionPane.showInputDialog("Ingrese el nuevo nombre del producto:");
                        if (nuevoNombre != null) {
                            nuevoNombre = nuevoNombre.toUpperCase();
    
                            query = "UPDATE productos SET nombre = ? WHERE id = ?";
                            preparedStatement = connection.prepareStatement(query);
                            preparedStatement.setString(1, nuevoNombre);
                            preparedStatement.setInt(2, codigo);
    
                            int rowsAffectedUpdateName = preparedStatement.executeUpdate();
    
                            if (rowsAffectedUpdateName > 0) {
                                JOptionPane.showMessageDialog(null, "Nombre del producto modificado exitosamente");
                            } else {
                                JOptionPane.showMessageDialog(null, "No se encontró el producto con el código especificado");
                            }
                        }
                        break;
                    case 2: // Modificar precio del producto
                        String nuevoPrecioStr = JOptionPane.showInputDialog("Ingrese el nuevo precio del producto:");
                        if (nuevoPrecioStr != null) {
                            double nuevoPrecio = Double.parseDouble(nuevoPrecioStr);
    
                            query = "UPDATE productos SET precio = ? WHERE id = ?";
                            preparedStatement = connection.prepareStatement(query);
                            preparedStatement.setDouble(1, nuevoPrecio);
                            preparedStatement.setInt(2, codigo);
    
                            int rowsAffectedUpdatePrice = preparedStatement.executeUpdate();
    
                            if (rowsAffectedUpdatePrice > 0) {
                                JOptionPane.showMessageDialog(null, "Precio del producto modificado exitosamente");
                            } else {
                                JOptionPane.showMessageDialog(null, "No se encontró el producto con el código especificado");
                            }
                        }
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Opción inválida");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el producto con el código especificado");
            }
        } catch (SQLException e) {
            System.out.println("Falló la conexión");
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
}
