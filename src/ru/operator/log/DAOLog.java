/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.operator.log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ATonevitskiy
 */
public class DAOLog {

    private static Logger log = Logger.getLogger(DAOLog.class.getCanonicalName());

    public DAOLog() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException ex) {
            log.log(Level.WARNING, "Ошибка драйвера базы!", ex);
            System.exit(0);
        }
    }

    public void connect() {
        String connectionUrl1 = "jdbc:sqlserver://10.58.50.6\\CRSSQL;"
                + "databaseName=db_cra;user=SQLview;password=QwErFdSa1234;";
        try {
            Connection connection = DriverManager.getConnection(connectionUrl1);
            PreparedStatement getStates = connection.prepareStatement("Select  a.eventType, a.eventDateTime from AgentStateDetail a "
                    + "inner join Resource r on r.resourceID=a.agentID "
                    + "where a.eventDateTime > ? and a.eventDateTime < ? "
                    + "and r.resourceName = ? and (a.eventType =1 or a.eventType=7 )  order by a.eventDateTime");
        } catch (Exception e) {
            log.log(Level.WARNING, "Ошибка коннекта к базе!", e);
        }
    }

    public Object getOperatorLog(String name, Calendar date) {
        Timestamp tStart = new Timestamp(date.get(Calendar.YEAR) - 1900, date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH),
                19, 0, 0, 1);
        Timestamp end = new Timestamp(tStart.getTime());
        end.setHours(23);
        end.setMinutes(59);
        end.setSeconds(59);
        end.setNanos(999999);
        return null;
    }

}
