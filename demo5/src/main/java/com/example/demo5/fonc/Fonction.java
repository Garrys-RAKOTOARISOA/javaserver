package com.example.demo5.fonc;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

public class Fonction {
//    public static Timestamp getCurrentTimestamp() {
//        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.systemDefault());
//        System.out.println(Timestamp.from(zonedDateTime.toInstant()));
//        return Timestamp.from(zonedDateTime.toInstant());
//    }

    public static Timestamp getCurrentTimestamp() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.systemDefault());
        Timestamp roundedTimestamp = new Timestamp(zonedDateTime.toInstant().toEpochMilli() / 1000 * 1000);
        System.out.println(roundedTimestamp);
        return roundedTimestamp;
    }


    public static Date getCurrentDate() {
        return new Date();
    }

    public static Timestamp getTimestamp(Date date, int heure, int minute, int seconde) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, heure);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, seconde);
        return new Timestamp(calendar.getTimeInMillis());
    }

    public static Date generateDate(int jour, int mois, int annee) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(annee, mois , jour);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
    public static Date makeDate(String strdate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(strdate);
        return date;
    }

    public static int makeYear(Timestamp timestamp){
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        int annee = localDateTime.getYear();
        return annee;
    }

    public static LocalDate convertDateToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}