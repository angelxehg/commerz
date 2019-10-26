/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author angel
 */
public final class Productos {
    public static boolean Agregar (String Nombre, float Precio, String FechaC, int Existencias) {
        String De = "Productos.Agregar";
        MySQLConexion M = new MySQLConexion();
        // Verificar si la conexión está activa
        if (M.getConexion() != null) {
            // Preparar sentencia
            String Sentencia = "{ CALL coz_productos_insertar(?, ?, ?, ?, ?) }";
            // Rellenar sentencia e intentar ejecutarla
            try {
                CallableStatement CST = M.getConexion().prepareCall(Sentencia);
                CST.setString(1, Nombre);
                CST.setFloat(2, Precio);
                CST.setString(3, FechaC);
                CST.setInt(4, Existencias);
                // Ejecutar sentencia
                CST.execute();
                // Dar resultado
                String R = CST.getString(5);
                Consola.Msg(De, "Se ejecutó SQL, Resultado: "+R);
                M.Desconectar();
                return true;
            } catch (SQLException e){
                // Devolver mensaje de error
                Consola.Msg(De, e+"");
                JOptionPane.showMessageDialog(null, "Error "+e);
                M.Desconectar();
                return false;
            }
        } else {
            Consola.Msg(De, "No hay conexión");
            return false;
        }
    }
    public static boolean Editar(int ID, String Nombre, float Precio, String FechaC, int Existencias){
        String De = "Productos.Editar";
        MySQLConexion M = new MySQLConexion();
        // Verificar si la conexión está activa
        if (M.getConexion() != null) {
            // Preparar sentencia
            String Sentencia = "{ CALL coz_productos_actualizar(?, ?, ?, ?, ?, ?) }";
            // Rellenar sentencia e intentar ejecutarla
            try {
                CallableStatement CST = M.getConexion().prepareCall(Sentencia);
                CST.setInt(1, ID);
                CST.setString(2, Nombre);
                CST.setFloat(3, Precio);
                CST.setString(4, FechaC);
                CST.setInt(5, Existencias);
                // Ejecutar sentencia
                CST.execute();
                // Dar resultado
                String R = CST.getString(6);
                Consola.Msg(De, "Se ejecutó SQL, Resultado: "+R);
                M.Desconectar();
                return true;
            } catch(SQLException e){
                // Devolver mensaje de error
                Consola.Msg(De, e+"");
                JOptionPane.showMessageDialog(null, "Error "+e);
                M.Desconectar();
                return false;
            }
        } else {
            Consola.Msg(De, "No hay conexión");
            return false;
        }
    }
    public static void Eliminar(int ID){
        String De = "Productos.Eliminar";
        MySQLConexion M = new MySQLConexion();
        // Verificar si la conexión está activa
        if (M.getConexion() != null) {
            // Preparar sentencia
            String Sentencia = "{ CALL coz_productos_eliminar(?, ?) }";
            // Rellenar sentencia e intentar ejecutarla
            try {
                CallableStatement CST = M.getConexion().prepareCall(Sentencia);
                CST.setInt(1, ID);
                // Ejecutar sentencia
                CST.execute();
                // Dar resultado
                String R = CST.getString(2);
                Consola.Msg(De, "Se ejecutó SQL, Resultado: "+R);
                JOptionPane.showMessageDialog(null, "Operación de borrado: "+R);
                M.Desconectar();
            } catch(SQLException e){
                // Devolver mensaje de error
                Consola.Msg(De, e+"");
                JOptionPane.showMessageDialog(null, "Error "+e);
                M.Desconectar();
            }
        } else {
            Consola.Msg(De, "No hay conexión");
        }
    }
    public static List<Object[]> Buscar(String Query, String Desde, String Hasta){
        String De = "Productos.Buscar";
        MySQLConexion M = new MySQLConexion();
        // Verificar si la conexión está activa
        if (M.getConexion() != null) {
            // Crear sentencia e intentar ejecutarla
            try {
                Statement ST = M.getConexion().createStatement();
                // Ejecutar sentencia
                ResultSet RS = ST.executeQuery("CALL coz_productos_buscar('" + Query +"', '" + Desde + "', '" + Hasta +"');");
                // Rellenar Lista
                List<Object[]> Lista = new ArrayList<Object[]>();
                if (RS != null) {
                    try {
                        while (RS.next()) {
                            // Crear fila
                            Object[] Fila = new Object[5];
                            Fila[0] = (int) RS.getInt("idproducto");
                            Fila[1] = (String) RS.getString("nombre");
                            Fila[2] = (float) RS.getFloat("precio");
                            Fila[3] = (String) RS.getString("fechac");
                            Fila[4] = (int) RS.getInt("existencias");
                            // Añadir a la lista
                            Lista.add(Fila);
                        }
                    } catch(SQLException e){
                        JOptionPane.showMessageDialog(null, e);
                    }
                }
                // Dar resultado
                Consola.Msg(De, "Busqueda correcta de "+Query);
                M.Desconectar();
                return Lista;
            } catch(SQLException e){
                // Devolver mensaje de error
                Consola.Msg(De, e+"");
                M.Desconectar();
                return null;
            }
        } else {
            Consola.Msg(De, "No hay conexión");
            return null;
        }
    }
    public static List<Object[]> BuscarID(int ID){
        String De = "Productos.BuscarID";
        MySQLConexion M = new MySQLConexion();
        // Verificar si la conexión está activa
        if (M.getConexion() != null) {
            // Crear sentencia e intentar ejecutarla
            try {
                Statement ST = M.getConexion().createStatement();
                // Ejecutar sentencia
                ResultSet RS = ST.executeQuery("CALL coz_productos_buscar_id('" + ID +"');");
                // Rellenar Lista
                List<Object[]> Lista = new ArrayList<Object[]>();
                if (RS != null) {
                    try {
                        if (RS.next()) {
                            // Crear fila
                            Object[] Fila = new Object[5];
                            Fila[0] = (int) RS.getInt("idproducto");
                            Fila[1] = (String) RS.getString("nombre");
                            Fila[2] = (float) RS.getFloat("precio");
                            Fila[3] = (String) RS.getString("fechac");
                            Fila[4] = (int) RS.getInt("existencias");
                            // Añadir a la lista
                            Lista.add(Fila);
                        }
                    } catch(SQLException e){
                        JOptionPane.showMessageDialog(null, e);
                    }
                }
                // Dar resultado
                Consola.Msg(De, "Busqueda correcta de "+ID);
                M.Desconectar();
                return Lista;
            } catch(SQLException e){
                // Devolver mensaje de error
                Consola.Msg(De, e+"");
                M.Desconectar();
                return null;
            }
        } else {
            Consola.Msg(De, "No hay conexión");
            return null;
        }
    }
}
