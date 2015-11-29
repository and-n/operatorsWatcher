package ru.operator.log;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author ATonevitskiy
 */
public class OperatorLog {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DateFormat df1 = new SimpleDateFormat("dd.MM.yyyy");
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        try {
            if (args.length == 1) {
                String s = args[0].trim();
                start.setTime(df1.parse(s));
                end = start;
            } else if (args.length == 2) {
                start.setTime(df1.parse(args[0].trim()));
                end.setTime(df1.parse(args[1].trim()));
            } else {
                System.exit(1);
            }
        } catch (ParseException p) {

        }
        OperatorLogLogic logic = new OperatorLogLogic();
        logic.action(start, end);
    }

}
