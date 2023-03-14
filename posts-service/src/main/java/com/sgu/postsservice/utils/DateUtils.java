package com.sgu.postsservice.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    public static String getNow() {
        return String.valueOf(new Timestamp(System.currentTimeMillis()).getTime());
    }

    public static String getNowWithFormat() {
        return new SimpleDateFormat("HH:mm:ss dd-MM-YYYY").format(new Date());
    }
}
