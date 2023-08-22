
package controlStock;

import javax.swing.JOptionPane;
import java.sql.*;

public class MenuPrincipal {
    
    private static Connection connection = DataBaseManager.getConnection();

    public static void main(String[] args) {
        boolean login = false;
        String legajo = null;

        try {
            Statement miStatement = connection.createStatement();
            ResultSet miResultSet = miStatement.executeQuery("SELECT * FROM usuarios");

            while (true) {
                if (login) {
                    int option = Integer.parseInt(JOptionPane.showInputDialog(

                        "1. Cargar stock inicial o producto nuevo\n" +
                        "2. Comprar stock\n" +
                        "3. Mostrar productos, sus códigos y precio unitario\n" +
                        "4. Mostrar el stock de cada producto\n" +
                        "5. Mostrar el costo total de un producto determinado\n" +
                        "6. Mostrar el costo total de todos los productos\n" +
                        "7. Retirar stock\n" +
                        "8. Eliminar o modificar producto\n" +
                        "9. Volver al menú principal"));

                    switch (option) {
                        case 1:
                            ManejoProductos.cargarStock();
                            break;
                        case 2:
                            ManejoProductos.comprarStock();
                            break;
                        case 3:
                            ManejoProductos.mostrarProductos();
                            break;
                        case 4:
                            ManejoProductos.mostrarStock();
                            break;
                        case 5:
                            ManejoProductos.mostrarCosto();
                            break;
                        case 6:
                            ManejoProductos.mostrarCostoTotal();
                            break;
                        case 7:
                            ManejoProductos.retirarStock();
                            break;
                        case 8:
                            ManejoProductos.alterarProducto();
                            break;
                        case 9:
                            login = false;
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Opción inválida");
                    }
                } else {

                    int option = Integer.parseInt(JOptionPane.showInputDialog(
                        
                        "BIENVENIDO A MUNDO COMPUTACIÓN\n\n" +
                        "1. Crear un usuario\n" +
                        "2. Iniciar sesión\n" +
                        "3. Cambiar contraseña\n" +
                        "4. Salir"));


                    switch (option) {
                        case 1:
                            ManejoClaves.crearUsuario();
                            break;
                        case 2:
                            legajo = ManejoClaves.login();
                            if (legajo != null) {
                                login = true;
                            }
                            break;
                        case 3:
                            ManejoClaves.cambiarClave();
                            break;
                        case 4:
                            JOptionPane.showMessageDialog(null, "Decidiste salir. Hasta pronto");
                            System.exit(0);
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Opción inválida");
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Falló la conexión");
            e.printStackTrace();

        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión");
                e.printStackTrace();
            }
        }
    }
}
