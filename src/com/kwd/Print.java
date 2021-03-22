package com.kwd;

import java.util.HashMap;

import static com.kwd.Constants.*;

public class Print {
    static void printAboveAverageAllTime() {
        System.out.println(Constants.ANSI_GREEN + "Better than all time average!" + Constants.ANSI_RESET);
    }

    static void printAboveAveragePast7Days() {
        System.out.println(Constants.ANSI_GREEN + "Better than average for last 7 days! " + Constants.ANSI_RESET);
    }

    static void printAboveAveragePast3Days() {
        System.out.println(Constants.ANSI_GREEN + "Better than average for last 3 days! " + Constants.ANSI_RESET);
    }

    static void printTiedPersonalBest() {
        System.out.println(Constants.ANSI_CYAN + "You have TIED your personal best!!!" + Constants.ANSI_RESET);
    }

    static void printNewPersonalBest() {
        System.out.println(Constants.ANSI_CYAN + ">>>> New personal best! Celebration time! <<<<" + Constants.ANSI_RESET);
    }

    static void printEncouragement() {
        System.out.println("Stick with it and you will get better!");
    }

    static void printStatistics() {
        printAverage();
        printAverageLast7Days();
        printAverageLast3Days();
        printBest();
    }

    static void printMapStatistics(HashMap<String, Statistic> map) {
        printStatistic(map.get(FIRST_7_DAYS));
        printStatistic(map.get(ALL_TIME));
        printStatistic(map.get(LAST_7_DAYS));
        printStatistic(map.get(LAST_3_DAYS));
    }

    static private void printStatistic(Statistic statistic) {
        System.out.printf("%" + Constants.CATEGORY_STRING_MAX_LENGTH + "s:%"
                        + Constants.ERROR_RATE_DIGITS + ".2f%% %" + Constants.WPM_DIGITS + ".2fwpm\n"
                , statistic.getName(), statistic.getErrorRate(), statistic.getWPM());
    }


    static void printAverage() {
        String average = "Average";
        System.out.printf("%" + Constants.CATEGORY_STRING_MAX_LENGTH + "s:%" + Constants.ERROR_RATE_DIGITS + ".2f%% %" + Constants.WPM_DIGITS + ".2fwpm\n"
                , average, TT.averageErrorRateAllTime, TT.averageWPMAllTime);
    }

    static void printBest() {
        String personalBest = "Personal best";
        System.out.printf("%" + Constants.CATEGORY_STRING_MAX_LENGTH + "s:%" + Constants.ERROR_RATE_DIGITS + ".2f%% %" + Constants.WPM_DIGITS + ".2fwpm  %s\n"
                , personalBest, TT.bestErrorRate, TT.bestWPM, TT.bestTimeString);
    }

    static void printAverageLast7Days() {
        String average7days = "Average last 7 days";
        System.out.printf("%" + Constants.CATEGORY_STRING_MAX_LENGTH + "s:%" + Constants.ERROR_RATE_DIGITS + ".2f%% %" + Constants.WPM_DIGITS + ".2fwpm\n"
                , average7days, TT.averageErrorRateLast7Days, TT.averageWPMLast7Days);
    }

    static void printAverageLast3Days() {
        String average3days = "Average last 3 days";
        System.out.printf("%" + Constants.CATEGORY_STRING_MAX_LENGTH + "s:%" + Constants.ERROR_RATE_DIGITS + ".2f%% %" + Constants.WPM_DIGITS + ".2fwpm\n"
                , average3days, TT.averageErrorRateLast3Days, TT.averageWPMLast3Days);
    }
}
