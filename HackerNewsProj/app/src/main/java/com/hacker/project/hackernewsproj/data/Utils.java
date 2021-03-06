package com.hacker.project.hackernewsproj.data;

import android.content.res.Resources;
import android.util.TypedValue;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author Navin Prasad <navin.prasad@fastacash.com>
 * @since 19/May/2016
 */


public class Utils {

    // Converts the difference between two dates into a date
    public static String updateDate(String time) {
        if (time == null)
            return "";

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

    /**
     * Convert Dp to Pixel
     */
    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }


}
