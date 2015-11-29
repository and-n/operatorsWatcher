/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.operator.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.operator.IFileCreator;

/**
 *
 * @author Andrey
 */
public class FileCreator implements IFileCreator {

    private XSSFWorkbook wb;

    public FileCreator() throws IOException {
        File f = new File("temp" + File.separator + "Сетка.xlsx");
        XSSFWorkbook template
                = new XSSFWorkbook(new FileInputStream(f));
        wb = new XSSFWorkbook(new FileInputStream(f));
    }

    @Override
    public void saveFile(String fileName) {
        File f = new File("result");
        f.mkdir();
        try (FileOutputStream fos = new FileOutputStream(f + File.separator + fileName + ".xlsx", false)) {
            wb.removeSheetAt(0);
            wb.write(fos);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Sheet createSheetWithHead(String sheetName) {
        Sheet s = wb.cloneSheet(0);
        wb.setSheetName(wb.getSheetIndex(s.getSheetName()), sheetName);
        System.out.println("Created " + s.getSheetName());
        return s;
    }

}
