/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author angel
 */
public final class ConvertirFecha {
    public static String NormalASQL(String FechaNormal) {
        Date DFechaNormal = new Date();
        try {
            DFechaNormal = new SimpleDateFormat("dd/MM/yyyy").parse(FechaNormal);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Error "+ex);
        }
        // Convertir al formato de SQL
        DateFormat SQLFormat = new SimpleDateFormat("yyyy-MM-dd");
        return SQLFormat.format(DFechaNormal);
    }
    public static String SQLANormal(String FechaSQL) {
        Date DFechaSQL = new Date();
        try {
            DFechaSQL = new SimpleDateFormat("yyyy-MM-dd").parse(FechaSQL);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Error "+ex);
        }
        // Convertir al formato de SQL
        DateFormat NormalFormat = new SimpleDateFormat("d/M/yyyy");
        return NormalFormat.format(DFechaSQL);
    }
}
