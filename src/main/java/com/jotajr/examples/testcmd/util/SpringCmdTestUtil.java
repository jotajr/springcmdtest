package com.jotajr.examples.testcmd.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class SpringCmdTestUtil {

    public static String getActualTimeStamp() {
        TimeZone tz = TimeZone.getTimeZone("America/Sao_Paulo");
        TimeZone.setDefault(tz);
        Calendar ca = GregorianCalendar.getInstance(tz);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss.SSS");
        return simpleDateFormat.format(ca.getTime());
    }

    public static String getFilename() {
        TimeZone tz = TimeZone.getTimeZone("America/Sao_Paulo");
        TimeZone.setDefault(tz);
        Calendar ca = GregorianCalendar.getInstance(tz);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return "Results_" + simpleDateFormat.format(ca.getTime()) + ".txt";
    }
}
