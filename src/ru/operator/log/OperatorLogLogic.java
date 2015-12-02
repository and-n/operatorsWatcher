/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.operator.log;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author Andrey
 */
public class OperatorLogLogic {

    public OperatorLogLogic() {
    }

    private FileCreator fc;
    private DateFormat df1 = new SimpleDateFormat("dd.MM.yyyy");

    public void action(Calendar start, Calendar end) {
        try {
            fc = new FileCreator();
            createThreads(start, end);
        } catch (IOException | ExecutionException | InterruptedException ex) {
            Logger.getLogger(OperatorLogLogic.class
                    .getName()).log(Level.SEVERE, null, ex);
            System.exit(2);
        } catch (Exception ex) {
            Logger.getLogger(OperatorLogLogic.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(2);
        }
        String fileName = "LogIN " + df1.format(start.getTime());
        fileName = start.equals(end) ? fileName : fileName + "-" + df1.format(end.getTime());
        fc.saveFile(fileName);

    }

    private void createThreads(Calendar date1, Calendar date2) throws ExecutionException, InterruptedException, Exception {
//        ExecutorService executor = Executors.newFixedThreadPool(4);
//        List<Future<Void>> flist = new ArrayList<>();
        Calendar d = Calendar.getInstance();
        d.setTimeInMillis(date1.getTimeInMillis());
        for (; d.before(date2) || d.equals(date2);) {

            Sheet sheet = fc.createSheetWithHead(df1.format(d.getTime()));

            int i = 2;
            while (true) {
                Row r = sheet.getRow(i);
                if (r != null) {
                    i++;
                    DAOLog dao = new DAOLog();
                    dao.connect();
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(d.getTimeInMillis());
                    LogTask lt = new LogTask(cal, r, dao);
                    lt.call();
//                    flist.add(executor.submit(lt));
                } else {
                    break;
                }

            }
            d.setTimeInMillis(d.getTimeInMillis() + 86400000L);

        }
//        for (Future future : flist) {
//            future.get();
//        }
//        executor.shutdown();

    }

}
