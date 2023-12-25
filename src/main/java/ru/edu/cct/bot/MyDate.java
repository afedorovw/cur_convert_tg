package ru.edu.cct.bot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class MyDate {

    protected String myToString(LocalDate localDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return localDate.format(formatter);
    }
}
