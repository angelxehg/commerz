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
public final class Ventas {
    public static boolean Agregar (int IDCliente, int IDProducto, int Cantidad, float Subtotal, float IVA, float Total, String Estado){
        String De = "Ventas.Agregar";
        MySQLConexion M = new MySQLConexion();
        // Verificar si la conexión está activa
        if (M.getConexion() != null) {
            // Preparar sentencia
            String Sentencia = "{ CALL coz_ventas_insertar(?, ?, ?, ?, ?, ?, ?, ?) }";
            // Rellenar sentencia e intentar ejecutarla
            try {
                CallableStatement CST = M.getConexion().prepareCall(Sentencia);
                CST.setInt(1, IDCliente);
                CST.setInt(2, IDProducto);
                CST.setInt(3, Cantidad);
                CST.setFloat(4, Subtotal);
                CST.setFloat(5, IVA);
                CST.setFloat(6, Total);
                CST.setString(7, Estado);
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
    public static void Cancelar (int ID){
        String De = "Ventas.Cancelar";
        MySQLConexion M = new MySQLConexion();
        // Verificar si la conexión está activa
        if (M.getConexion() != null) {
            // Preparar sentencia
            String Sentencia = "{ CALL coz_ventas_cancelar(?, ?) }";
            // Rellenar sentencia e intentar ejecutarla
            try {
                CallableStatement CST = M.getConexion().prepareCall(Sentencia);
                CST.setInt(1, ID);
                // Ejecutar sentencia
                CST.execute();
                // Dar resultado
                String R = CST.getString(2);
                Consola.Msg(De, "Se ejecutó SQL, Resultado: "+R);
                JOptionPane.showMessageDialog(null, "Operación de cancelado: "+R);
                M.Desconectar();
            } catch (SQLException e){
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
        String De = "Ventas.Buscar";
        MySQLConexion M = new MySQLConexion();
        // Verificar si la conexión está activa
        if (M.getConexion() != null) {
            // Crear sentencia e intentar ejecutarla
            try {
                Statement ST = M.getConexion().createStatement();
                // Ejecutar sentencia
                ResultSet RS = ST.executeQuery("CALL coz_ventas_buscar('" + Query +"', '" + Desde + "', '" + Hasta +"');");
                // Rellenar Lista
                List<Object[]> Lista = new ArrayList<Object[]>();
                if (RS != null) {
                    try {
                        while (RS.next()) {
                            // Crear fila
                            Object[] Fila = new Object[9];
                            Fila[0] = (int) RS.getInt("idventa");
                            Fila[1] = (String) RS.getString("cliente");
                            Fila[2] = (String) RS.getString("producto");
                            Fila[3] = (int) RS.getInt("cantidad");
                            Fila[4] = (String) RS.getString("fechav");
                            Fila[5] = (float) RS.getFloat("subtotal");
                            Fila[6] = (float) RS.getFloat("iva");
                            Fila[7] = (float) RS.getFloat("total");
                            Fila[8] = (String) RS.getString("estado");
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
        String De = "Ventas.BuscarID";
        MySQLConexion M = new MySQLConexion();
        // Verificar si la conexión está activa
        if (M.getConexion() != null) {
            // Crear sentencia e intentar ejecutarla
            try {
                Statement ST = M.getConexion().createStatement();
                // Ejecutar sentencia
                ResultSet RS = ST.executeQuery("CALL coz_ventas_buscar_id('" + ID +"');");
                // Rellenar Lista
                List<Object[]> Lista = new ArrayList<Object[]>();
                if (RS != null) {
                    try {
                        if (RS.next()) {
                            // Crear fila
                            Object[] Fila = new Object[9];
                            Fila[0] = (int) RS.getInt("idventa");
                            Fila[1] = (String) RS.getString("cliente");
                            Fila[2] = (String) RS.getString("producto");
                            Fila[3] = (int) RS.getInt("cantidad");
                            Fila[4] = (String) RS.getString("fechav");
                            Fila[5] = (float) RS.getFloat("subtotal");
                            Fila[6] = (float) RS.getFloat("iva");
                            Fila[7] = (float) RS.getFloat("total");
                            Fila[8] = (String) RS.getString("estado");
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
