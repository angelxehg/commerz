/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

/**
 *
 * @author angel
 */
public final class MySQLServer {
    private static String servidor = "localhost";
    private static String basededatos = "commerz";
    private static String usuario = "commerz_user";
    private static String clave = "commerz";
    
    public static void CambiarPropiedades(String Servidor, String BaseDeDatos, String Usuario, String Clave) {
        servidor = Servidor;
        basededatos = BaseDeDatos;
        usuario = Usuario;
        clave = Clave;
    }
    public static void Defecto(){
        servidor = "localhost";
        basededatos = "commerz";
        usuario = "commerz_user";
        clave = "commerz";
    }
    public static String getServidor(){
        return servidor;
    }
    public static String getBaseDeDatos(){
        return basededatos;
    }
    public static String getUsuario(){
        return usuario;
    }
    public static String getClave(){
        return clave;
    }
}
