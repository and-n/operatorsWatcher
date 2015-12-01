/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.operator.log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
        int[] res = parseResult(rs);
        writeToRow(res);
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String getInitials(String name) {
        String[] splits = name.split("\\s+");
        String init = splits[0].toUpperCase().substring(0, 1) + splits[0].toLowerCase().substring(1, splits[0].length())
                + " " + splits[1].substring(0, 1) + "." + splits[2].substring(0, 1) + ".";
        return init;
    }

    private int[] parseResult(ResultSet resultSet) throws SQLException {
        int[] fiveMinutesRes = new int[288];
        day.set(Calendar.HOUR, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MILLISECOND, 0);
        long time = day.getTimeInMillis();
        int index = 0;
        boolean isWorked = false;
        int lastIndex = 0;
        while (resultSet.next()) {
            int state = resultSet.getInt(1);
            Timestamp ts = resultSet.getTimestamp(2);
            ts.getTime();
        }
        for (; index < 288; index++) {
            fiveMinutesRes[index] = isWorked ? 5 : 0;
        }

        return fiveMinutesRes;
    }

    private void writeToRow(int[] values) {
        for (int i = 0; i < values.length; i++) {
            Cell c = row.getCell(i + 1);
            if (c == null) {
                c = row.createCell(i++);
            }
            c.setCellValue(values[i]);
        }
    }

}
