package com.example.demo5.fonc;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Fonction {

    public static Timestamp getCurrentTimestamp() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.systemDefault());
        Timestamp roundedTimestamp = new Timestamp(zonedDateTime.toInstant().toEpochMilli() / 1000 * 1000);
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

    public static LocalDate timeStampToLocalDate(Timestamp timestamp){
        return timestamp.toLocalDateTime().toLocalDate();
    }

    public static List<LocalDate> getAllDatesInMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startMonth = yearMonth.atDay(1);
        LocalDate endMonth = yearMonth.atEndOfMonth();
        List<LocalDate> dates = new ArrayList<>();
        for (LocalDate date = startMonth; !date.isAfter(endMonth); date = date.plusDays(1)) {
            dates.add(date);
        }
        return dates;
    }

    public static LocalDate todayDate() {
        return LocalDate.now();
    }

    public static String castToFrDate(String date){
        String toreturn = "";
        if(date.equals("MONDAY")){
            toreturn = "LUNDI";
        }
        if(date.equals("MONDAY")){
            toreturn = "LUNDI";
        }
        if(date.equals("TUESDAY")){
            toreturn = "MARDI";
        }
        if(date.equals("WEDNESDAY")){
            toreturn = "MERCREDI";
        }
        if(date.equals("THURSDAY")){
            toreturn = "JEUDI";
        }
        if(date.equals("FRIDAY")){
            toreturn = "VENDREDI";
        }
        if(date.equals("SATURDAY")){
            toreturn = "SAMEDI";
        }
        if(date.equals("SUNDAY")){
            toreturn = "DIMANCHE";
        }
        return toreturn;
    }
}