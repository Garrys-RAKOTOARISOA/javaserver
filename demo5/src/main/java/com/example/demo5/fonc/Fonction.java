package com.example.demo5.fonc;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Fonction {
    public static Timestamp getCurrentTimestamp() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.systemDefault());
        return Timestamp.from(zonedDateTime.toInstant());
    }
}
