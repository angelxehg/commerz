/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author angel
 */
public final class MySQLConexion {
    private final String De = "MySQLConexion";
    // Variables privadas de la conexión
    private String servidor, basededatos, usuario, clave, url_conexion;
    private Connection conexion = null;

    // Constructor
    public MySQLConexion(){
        // Cargar propiedades conexión
        Consola.Msg(De, "Intentando iniciar conexión...");
        servidor = MySQLServer.getServidor();
        basededatos = MySQLServer.getBaseDeDatos();
        usuario = MySQLServer.getUsuario();
        clave = MySQLServer.getClave();
        // Poner URL
        url_conexion = "jdbc:mysql://" + servidor + "/" + basededatos;
        // Buscar el driver e intentar conectar
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexion = DriverManager.getConnection(url_conexion, usuario, clave);
            if (conexion != null) {
                Consola.Msg(De, "Conexión exitosa a " + basededatos + " desde " + servidor);
            }
        } catch(SQLException e) {
            // Mensaje de error en caso de fallo
            Consola.Msg(De, e+"");
        } catch(ClassNotFoundException e) { 
            Consola.Msg(De, e+"");
        } 
    }
    // Obtener conexión
    public Connection getConexion(){
        if (conexion != null) {
            return conexion;
        } else {
            Consola.Msg(De, "Conexión nula");
            JOptionPane.showMessageDialog(null, "Error No hay conexión");
            return null;
        }
    }
    // Desconectar
    public void Desconectar(){
        // Solo desconecta si la conexión existe
        if (conexion != null) {
            try {
                conexion.close();
                conexion = null;
                Consola.Msg(De, "Conexión a " + basededatos + " en " + servidor + " ha sido cerrada.");
            } catch(SQLException e) { 
                Consola.Msg(De, e+"");
            }
        } else {
            Consola.Msg(De, "Conexión nula");
        }
    }
}
