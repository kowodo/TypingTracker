package com.kwd;

public class Print {
    static void printAboveAverageAllTime() {
        System.out.println(Constants.ANSI_GREEN + "Better than all time average!" + Constants.ANSI_RESET);
    }

    static void printAboveAveragePast7Days() {
        System.out.println(Constants.ANSI_GREEN + "Better than average for last 7 days! " + Constants.ANSI_RESET);
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

    static void printStatistics(){
        printAverage();
        printAverageLast7Days();
        printBest();
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
}