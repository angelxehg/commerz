/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.List;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author angel
 */
public final class Clientes {
    public static boolean Agregar (String Nombre, String ApellidoP, String ApellidoM, String Email, String Telefono, int Edad, String Direccion) {
        String De = "Clientes.Agregar";
        MySQLConexion M = new MySQLConexion();
        // Verificar si la conexión está activa
        if (M.getConexion() != null) {
            // Preparar sentencia
            String Sentencia = "{ CALL coz_clientes_insertar(?, ?, ?, ?, ?, ?, ?, ?) }";
            // Rellenar sentencia e intentar ejecutarla
            try {
                CallableStatement CST = M.getConexion().prepareCall(Sentencia);
                CST.setString(1, Nombre);
                CST.setString(2, ApellidoP);
                CST.setString(3, ApellidoM);
                CST.setString(4, Email);
                CST.setString(5, Telefono);
                CST.setInt(6, Edad);
                CST.setString(7, Direccion);
                // Ejecutar sentencia
                CST.execute();
                // Dar resultado
                String R = CST.getString(8);
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
    public static boolean Editar(int ID, String Nombre, String ApellidoP, String ApellidoM, String Email, String Telefono, int Edad, String Direccion){
        String De = "Clientes.Editar";
        MySQLConexion M = new MySQLConexion();
        // Verificar si la conexión está activa
        if (M.getConexion() != null) {
            // Preparar sentencia
            String Sentencia = "{ CALL coz_clientes_actualizar(?, ?, ?, ?, ?, ?, ?, ?, ?) }";
            // Rellenar sentencia e intentar ejecutarla
            try {
                CallableStatement CST = M.getConexion().prepareCall(Sentencia);
                CST.setInt(1, ID);
                CST.setString(2, Nombre);
                CST.setString(3, ApellidoP);
                CST.setString(4, ApellidoM);
                CST.setString(5, Email);
                CST.setString(6, Telefono);
                CST.setInt(7, Edad);
                CST.setString(8, Direccion);
                // Ejecutar sentencia
                CST.execute();
                // Dar resultado
                String R = CST.getString(9);
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
        String De = "Clientes.Eliminar";
        MySQLConexion M = new MySQLConexion();
        // Verificar si la conexión está activa
        if (M.getConexion() != null) {
            // Preparar sentencia
            String Sentencia = "{ CALL coz_clientes_eliminar(?, ?) }";
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
    public static List<Object[]> Buscar(String Query){
        String De = "Productos.Buscar";
        MySQLConexion M = new MySQLConexion();
        // Verificar si la conexión está activa
        if (M.getConexion() != null) {
            // Crear sentencia e intentar ejecutarla
            try {
                Statement ST = M.getConexion().createStatement();
                // Ejecutar sentencia
                ResultSet RS = ST.executeQuery("CALL coz_clientes_buscar('" + Query +"');");
                // Rellenar Lista
                List<Object[]> Lista = new ArrayList<Object[]>();
                if (RS != null) {
                    try {
                        while (RS.next()) {
                            // Crear fila
                            Object[] Fila = new Object[8];
                            Fila[0] = (int) RS.getInt("idcliente");
                            Fila[1] = (String) RS.getString("nombre");
                            Fila[2] = (String) RS.getString("apellidop");
                            Fila[3] = (String) RS.getString("apellidom");
                            Fila[4] = (String) RS.getString("email");
                            Fila[5] = (String) RS.getString("telefono");
                            Fila[6] = (int) RS.getInt("edad");
                            Fila[7] = (String) RS.getString("direccion");
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
        String De = "Clientes.BuscarID";
        MySQLConexion M = new MySQLConexion();
        // Verificar si la conexión está activa
        if (M.getConexion() != null) {
            // Crear sentencia e intentar ejecutarla
            try {
                Statement ST = M.getConexion().createStatement();
                // Ejecutar sentencia
                ResultSet RS = ST.executeQuery("CALL coz_clientes_buscar_id('" + ID +"');");
                // Rellenar Lista
                List<Object[]> Lista = new ArrayList<Object[]>();
                if (RS != null) {
                    try {
                        if (RS.next()) {
                            // Crear fila
                            Object[] Fila = new Object[8];
                            Fila[0] = (int) RS.getInt("idcliente");
                            Fila[1] = (String) RS.getString("nombre");
                            Fila[2] = (String) RS.getString("apellidop");
                            Fila[3] = (String) RS.getString("apellidom");
                            Fila[4] = (String) RS.getString("email");
                            Fila[5] = (String) RS.getString("telefono");
                            Fila[6] = (int) RS.getInt("edad");
                            Fila[7] = (String) RS.getString("direccion");
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
