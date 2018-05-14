package de.dis2011;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Kleine Helferklasse zum Einlesen von Formulardaten
 */
public class FormUtil {
    public static Date readDate(String label){
        String date;
        Date ret = null;
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.print(label+" (Format yyyy-MM-dd): ");
            date = stdin.readLine();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            ret = format.parse(date);
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException ex) {
            Logger.getLogger(FormUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ret;
    }



    /**
     * Liest einen String vom standard input ein
     * @param label Zeile, die vor der Eingabe gezeigt wird
     * @return eingelesene Zeile
     */
    public static String readString(String label) {
        String ret = null;
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.print(label+": ");
            ret = stdin.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    /**
     * Liest einen Integer vom standard input ein
     * @param label Zeile, die vor der Eingabe gezeigt wird
     * @return eingelesener Integer
     */
    public static int readInt(String label) {
        int ret = 0;
        boolean finished = false;

        while(!finished) {
            String line = readString(label);

            try {
                ret = Integer.parseInt(line);
                finished = true;
            } catch (NumberFormatException e) {
                System.err.println("Ungültige Eingabe: Bitte geben Sie eine Zahl an!");
            }
        }

        return ret;
    }
}
