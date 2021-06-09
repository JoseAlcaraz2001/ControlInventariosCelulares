/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfazG;
import java.sql.Connection;
import objetos.Conexion;

/**
 *
 * @author 52341
 */

public class Paneles {
    public static Gestionar g = new Gestionar();
    public static Conexion enlace = new Conexion();
    public static Connection conect = enlace.conectar();
}
