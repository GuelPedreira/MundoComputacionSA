
package controlStock;

import javax.swing.JOptionPane;
import java.sql.*;

public class ManejoClaves {

    public static void crearUsuario() {
        Connection connection = DataBaseManager.getConnection();
        PreparedStatement selectStatement = null;
        PreparedStatement insertStatement = null;

        try {
            String legajo = JOptionPane.showInputDialog("Ingrese el legajo:");
            String clave = JOptionPane.showInputDialog("Ingrese la clave:");

            // Verificar si el legajo ya existe en la tabla
            String selectQuery = "SELECT * FROM usuarios WHERE legajo = ?";
            selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setString(1, legajo);
            ResultSet resultado = selectStatement.executeQuery();
            if (resultado.next()) {
                JOptionPane.showMessageDialog(null, "El usuario existe. No se puede agregar.");
            } else {
                // Insertar el nuevo usuario en la tabla
                String insertQuery = "INSERT INTO usuarios (legajo, clave) VALUES (?, ?)";
                insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, legajo);
                insertStatement.setString(2, clave);
                insertStatement.executeUpdate();

                JOptionPane.showMessageDialog(null, "Usuario creado exitosamente");
            }
        } catch (SQLException e) {
            System.out.println("Falló la conexión");
            e.printStackTrace();
        } finally {
            try {
                if (selectStatement != null) {
                    selectStatement.close();
                }
                if (insertStatement != null) {
                    insertStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static String login() {
        String legajo = JOptionPane.showInputDialog("Ingrese el legajo:");
        String clave = JOptionPane.showInputDialog("Ingrese la clave:");

        Connection connection = DataBaseManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultado = null;

        try {
            String query = "SELECT * FROM usuarios WHERE legajo = ? AND clave = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, legajo);
            preparedStatement.setString(2, clave);

            resultado = preparedStatement.executeQuery();

            if (resultado.next()) {
                JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso");
                return legajo;
            } else {
                JOptionPane.showMessageDialog(null, "El usuario no existe o la clave es errónea. Verifíquelo e intente nuevamente");
                return null; // Regresamos null para indicar que el inicio de sesión fue incorrecto
            }
        } catch (SQLException e) {
            System.out.println("Falló la conexión");
            e.printStackTrace();
            return null; // Regresamos null en caso de que ocurra una excepción
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

    public static void cambiarClave() {
        String legajo = JOptionPane.showInputDialog("Ingrese el legajo:");
        String claveVieja = JOptionPane.showInputDialog("Ingrese la clave actual:");

        Connection connection = DataBaseManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultado = null;

        try {
            String query = "SELECT * FROM usuarios WHERE legajo = ? AND clave = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, legajo);
            preparedStatement.setString(2, claveVieja);

            resultado = preparedStatement.executeQuery();

            if (resultado.next()) {
                String nuevaClave = JOptionPane.showInputDialog("Ingrese la nueva clave:");

                query = "UPDATE usuarios SET clave = ? WHERE legajo = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, nuevaClave);
                preparedStatement.setString(2, legajo);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Contraseña cambiada exitosamente");
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró el usuario con el legajo especificado");
                }
            } else {
                JOptionPane.showMessageDialog(null, "El usuario no existe o la clave actual es errónea. Verifíquelo e intente nuevamente");
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
