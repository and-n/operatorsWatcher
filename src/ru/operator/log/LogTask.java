/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.operator.log;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.concurrent.Callable;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Andrey
 */
public class LogTask implements Callable<Void> {

    private Calendar day;
    private Row row;
    private DAOLog daoLog;

    public LogTask(Calendar day, Row row, DAOLog dao) {
        this.day = day;
        this.row = row;
        daoLog = dao;
    }

    @Override
    public Void call() throws Exception {
        String name = getInitials(row.getCell(0).getStringCellValue());
        ResultSet rs = daoLog.getOperatorLog(name, day);
        while (rs.next()){
            
        }
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String getInitials(String name) {
        String[] splits = name.split("\\s+");
        String init = splits[0].toUpperCase().substring(0, 1) + splits[0].toLowerCase().substring(1, splits[0].length())
                + " " + splits[1].substring(0, 1) + "." + splits[2].substring(0, 1) + ".";
        return init;
    }
}
