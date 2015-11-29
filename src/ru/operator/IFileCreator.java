/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.operator;

import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author Andrey
 */
public interface IFileCreator {

    public void saveFile(String fileName);

    public Sheet createSheetWithHead(String sheetName);

}
