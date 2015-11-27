/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.operator.log;

/**
 *
 * @author ATonevitskiy
 */
public class Operator {

    /**
     * Фамилия оператора.
     */
    private final String surname;
    /**
     * Имя отчество оператора.
     */
    private final String name;

    public Operator(String name, String surname) {
        this.name = name.trim();
        this.surname = surname.trim();
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getInitials() {
        String[] splits = name.split("\\s+");
        String init = splits[0].substring(0, 1) + "." + splits[1].substring(0, 1) + ".";
        return init;
    }
    
    
    
}
