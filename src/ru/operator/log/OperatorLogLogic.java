/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.operator.log;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
    
    public void action(Calendar start, Calendar end) {
        try {
            fc = new FileCreator();
        } catch (IOException ex) {
            Logger.getLogger(OperatorLogLogic.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        
        fc.saveFile("asd");
        
    }
    
    private void createThreads(Calendar date1, Calendar date2) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Future<Void>> flist = new ArrayList<>();
        DAOLog dao = new DAOLog();
        dao.connect();
        Calendar d = Calendar.getInstance();
        d.setTimeInMillis(date1.getTimeInMillis());
        while (d.before(date2) || d.equals(date2)) {
            DateFormat df1 = new SimpleDateFormat("dd.MM.yyyy");
            Sheet sheet = fc.createSheetWithHead(df1.format(d.getTime()));
            
            int i = 2;
            while (true) {
                Row r = sheet.getRow(i);
                if (r != null) {
                    i++;
                    LogTask lt = new LogTask(d, r, dao);
                    flist.add(executor.submit(lt));
                } else {
                    break;
                }
                
            }
        }
        for (Future future : flist) {
            future.get();
        }
        executor.shutdown();
    }
    
}
