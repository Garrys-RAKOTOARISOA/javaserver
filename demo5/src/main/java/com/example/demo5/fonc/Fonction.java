package com.example.demo5.fonc;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

public class Fonction {
    public static Timestamp getCurrentTimestamp() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.systemDefault());
        System.out.println(Timestamp.from(zonedDateTime.toInstant()));
        return Timestamp.from(zonedDateTime.toInstant());
    }

    public static Date getCurrentDate() {
        return new Date();
    }

    public static Timestamp getTimestamp(Date date, int heure, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, heure);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    public static Date generateDate(int jour, int mois, int annee) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(annee, mois - 1, jour);

        return calendar.getTime();
    }
}