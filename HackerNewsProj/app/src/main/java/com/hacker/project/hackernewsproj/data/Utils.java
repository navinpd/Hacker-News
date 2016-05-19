package com.hacker.project.hackernewsproj.data;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author Navin Prasad <navin.prasad@fastacash.com>
 * @since 19/May/2016
 */


public class Utils {

    // Converts the difference between two dates into a date
    public static String updateDate(String time) {

        Long ti = Long.decode(time);

        Date past = new Date(ti * 1000);
        Date now = new Date();

        if (TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) > 0) {
            return TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + "d";
        } else if (TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) > 0) {
            return TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) + "h";
        }
        if (TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) > 0) {
            return TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + "m";
        } else {
            return TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime()) + "s";
        }
    }

}
