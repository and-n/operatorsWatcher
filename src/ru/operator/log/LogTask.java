package ru.operator.log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Andrey
 */
public class LogTask {

    private Calendar day;
    private Row row;
    private DAOLog daoLog;

    public LogTask(Calendar day, Row row, DAOLog dao) {
        this.day = day;
        this.row = row;
        daoLog = dao;
    }

//    @Override
    public Void call() throws Exception {
        String name = getInitials(row.getCell(0).getStringCellValue());
        ResultSet rs = daoLog.getOperatorLog(name, day);
        int[] res = parseResult(rs);
        writeToRow(res);
        return null;
    }

    private String getInitials(String name) {
        String[] splits = name.split("\\s+");
        String init = splits[1].substring(0, 1) + "." + splits[2].substring(0, 1) + ". "
                + splits[0].toUpperCase().substring(0, 1) + splits[0].toLowerCase().substring(1, splits[0].length());
        return init;
    }

    private int[] parseResult(ResultSet resultSet) throws SQLException {
        int[] fiveMinutesRes = new int[288];
        day.set(Calendar.HOUR, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MILLISECOND, 0);
        long end = day.getTimeInMillis() + 86400000L;
        long time = day.getTimeInMillis();
        int index = 0;
        boolean isWorked = false;
        long lastStateTime = time;
        long workTime = 0;
        if (resultSet.next()) {
            while (time < end) {
                Pair<Boolean, Long> state = getState(resultSet);
                if (state.getR() < time) {
                    isWorked = state.getL();
                    if (resultSet.next()) {
                        continue;
                    } else {
                        break;
                    }
                } else {
                    if (state.getR() > time + 300000) {
                        if (lastStateTime <= time) {
                            fiveMinutesRes[index] = isWorked ? 5 : 0;
                        } else {
                            if (state.getL()) {
//                                BigDecimal bd = new BigDecimal(state.getR())
//                                        .subtract(new BigDecimal(time)).add(new BigDecimal(workTime))
//                                        .divide(new BigDecimal(60000), 2, RoundingMode.UP);
//                                if (bd.intValue() > 5) {
//                                    System.out.println("MORE 5 " + workTime + " state " + state.getR() + " time " + time);
//                                }
                                BigDecimal bd = new BigDecimal(workTime)
                                        .divide(new BigDecimal(60000), 2, RoundingMode.UP);
                                fiveMinutesRes[index] = bd.intValue();
                                workTime = 0;
                            } else {
                                BigDecimal bd = new BigDecimal(workTime)
                                        .divide(new BigDecimal(60000), 2, RoundingMode.UP);
                                fiveMinutesRes[index] = bd.intValue();
                                workTime = 0;
                            }
                        }
                        time = time + 300000;
                        index++;
                    } else {
                        if (lastStateTime > time) {
                            workTime = state.getL() ? workTime : workTime + (state.getR() - lastStateTime);
//                            System.err.println("WT IF" + (state.getR() > time + 300000) + " " + workTime);
                        } else {
                            workTime = state.getL() ? 0 : state.getR() - time;
//                            System.err.println("WT else " + (state.getR() > time + 300000) + " " + workTime);
                        }
                        lastStateTime = state.getR();
                        isWorked = state.getL();
                        if (resultSet.next()) {
                            continue;
                        } else {
                            BigDecimal bd = new BigDecimal(workTime)
                                    .divide(new BigDecimal(60000), 2, RoundingMode.UP);
                            fiveMinutesRes[index] = bd.intValue();
                            workTime = 0;
                            index++;
                            break;
                        }
                    }
//                    time = time + 300000;
//                    index++;
                }
            }
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
                c = row.createCell(i + 1);
            }
            c.setCellValue(values[i]);
        }
    }

    private Pair<Boolean, Long> getState(ResultSet resultSet) throws SQLException {
        int state = resultSet.getInt(1);
        Timestamp ts = resultSet.getTimestamp(2);
        Boolean b = state == 1;
        Long l = new Long(ts.getTime());
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(l);
        return new Pair(b, l);
    }

}
