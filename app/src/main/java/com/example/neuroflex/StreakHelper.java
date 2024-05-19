package com.example.neuroflex;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StreakHelper {

    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static boolean isConsecutiveDay(String prevDate, String currentDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date prev = sdf.parse(prevDate);
            Date curr = sdf.parse(currentDate);

            // Check if the current date is one day after the previous date
            return (curr.getTime() - prev.getTime()) == (24 * 60 * 60 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int calculateStreak(List<Map<String, Object>> dailyScores) {
        if (dailyScores == null || dailyScores.isEmpty()) {
            return 0;
        }

        // Sort the list by date
        Collections.sort(dailyScores, new Comparator<Map<String, Object>>() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                try {
                    Date date1 = sdf.parse(o1.get("date").toString());
                    Date date2 = sdf.parse(o2.get("date").toString());
                    return date1.compareTo(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        int streak = 1; // Initial streak is 1 if there's at least one score
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String prevDateStr = sdf.format(new Date());

        for (int i = dailyScores.size() - 1; i >= 0; i--) {
            String currentDateStr = dailyScores.get(i).get("date").toString();
            if (i == dailyScores.size() - 1) {
                prevDateStr = currentDateStr;
                continue;
            }

            if (isConsecutiveDay(currentDateStr, prevDateStr)) {
                streak++;
            } else {
                break;
            }
            prevDateStr = currentDateStr;
        }

        return streak;
    }
}