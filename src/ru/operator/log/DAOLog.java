/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.operator.log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ATonevitskiy
 */
public class DAOLog {

    private static Logger log = Logger.getLogger(DAOLog.class.getCanonicalName());

    private static Connection connection;
    private PreparedStatement getStates;

    public void connect() {
        if (connection == null) {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

                String connectionUrl1 = "jdbc:sqlserver://10.58.50.6\\CRSSQL;"
                        + "databaseName=db_cra;user=SQLview;password=QwErFdSa1234;";

                connection = DriverManager.getConnection(connectionUrl1);
            } catch (ClassNotFoundException ex) {
                log.log(Level.WARNING, "Ошибка драйвера базы!", ex);
                System.exit(0);
            } catch (SQLException ex) {
                log.log(Level.WARNING, "Ошибка коннекта к базе!", ex);
            }
        }
        try {
            getStates = connection.prepareStatement("Select  a.eventType, a.eventDateTime from AgentStateDetail a "
                    + "inner join Resource r on r.resourceID=a.agentID "
                    + "where a.eventDateTime > ? and a.eventDateTime < ? "
                    + "and r.resourceName = ? and (a.eventType =1 or a.eventType=7 )  order by a.eventDateTime");
        } catch (Exception e) {
            log.log(Level.WARNING, "Ошибка коннекта к базе!", e);
        }
    }

    public ResultSet getOperatorLog(String name, Calendar date) throws SQLException {

        Timestamp tStart = new Timestamp(date.get(Calendar.YEAR) - 1900, date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH),
                0, 0, 0, 1);
        Timestamp end = new Timestamp(tStart.getTime());
        end.setHours(23);
        end.setMinutes(59);
        end.setSeconds(59);
        end.setNanos(999999);
        tStart.setTime(tStart.getTime() - 3600000L);
        getStates.setTimestamp(1, tStart);
        getStates.setTimestamp(2, end);
        getStates.setString(3, name);
        return getStates.executeQuery();
    }

}
