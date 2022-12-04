package com.example.deadlinenoteapp3122022.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtils {
    public static String getCurrentDateTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EE M dd, HH:mm a");
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }

    public static String getTimeInLong(){
        return String.valueOf(Calendar.getInstance().getTimeInMillis());
    }
}
