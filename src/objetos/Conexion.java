/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objetos;
import java.sql.*;
import javax.swing.JOptionPane;
/**
 *
 * @author 52341
 */
public class Conexion {
    Connection sql;
    public boolean conectado;
    String localhost;
    String puerto;
    String password;

    public Connection conectar(){
        try {
            String connectionUrl = "jdbc:sqlserver://localhost:1433;database=ControlInventariosCelulares;user=sa; password = 12345;";
            sql = DriverManager.getConnection(connectionUrl);
            conectado = true;
        } catch (SQLException e) {
            conectado = false;
            JOptionPane.showMessageDialog(null, "No se pudo conectar\na la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return sql;
    }
}
