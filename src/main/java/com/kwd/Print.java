package com.kwd;

import java.util.HashMap;
import java.util.Map;

import static com.kwd.Constants.*;

public class Print {

    private Print() {
    }

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

    static void printMapStatistics(HashMap<String, Statistic> map) {
        printStatistic(map.get(FIRST_7_DAYS));
        printStatistic(map.get(ALL_TIME));
        printStatistic(map.get(LAST_7_DAYS));
        printStatistic(map.get(LAST_3_DAYS));
    }

    static void printMapStatisticsComparison(HashMap<String, Statistic> before, HashMap<String, Statistic> after) {
        printStatisticComparison(before.get(FIRST_7_DAYS), after.get(FIRST_7_DAYS));
        printStatisticComparison(before.get(ALL_TIME), after.get(ALL_TIME));
        printStatisticComparison(before.get(LAST_7_DAYS), after.get(LAST_7_DAYS));
        printStatisticComparison(before.get(LAST_3_DAYS), after.get(LAST_3_DAYS));

    }

    private static void printStatisticComparison(Statistic before, Statistic after) {
        Float errorDelta = after.getErrorRate() - before.getErrorRate();
        Float wpmDelta = after.getWpm() - before.getWpm();
        System.out.printf("%" + CATEGORY_STRING_MAX_LENGTH + "s:%"
                        + ERROR_RATE_DIGITS + ".2f(%"
                        + (ERROR_RATE_DIGITS - 1) + ".2f)%%   "
                        + "%" + WPM_DIGITS + ".2f("
                        + "%" + (WPM_DIGITS - 2) + ".2f)wpm"
                        + "  %s --> %s\n"
                , after.getName(), after.getErrorRate(), errorDelta, after.getWpm(), wpmDelta,
                dateFormatter.format(after.getStartDate()), dateTimeFormatter.format(after.getEndDate())
        );
    }

    private static void printStatistic(Statistic statistic) {
        System.out.printf("%" + Constants.CATEGORY_STRING_MAX_LENGTH + "s:%"
                        + Constants.ERROR_RATE_DIGITS + ".2f%% %" + Constants.WPM_DIGITS + ".2fwpm  %s --> %s\n"
                , statistic.getName(), statistic.getErrorRate(), statistic.getWpm(),
                dateFormatter.format(statistic.getStartDate()), dateTimeFormatter.format(statistic.getEndDate())
        );
    }

    static void printBest(Map<String, Statistic> map) {
        String personalBest = "Personal best";
        System.out.printf("%" + Constants.CATEGORY_STRING_MAX_LENGTH + "s:%" + Constants.ERROR_RATE_DIGITS + ".2f%% %" + Constants.WPM_DIGITS + ".2fwpm  %s\n"
                , personalBest, map.get(BEST).getErrorRate(), map.get(BEST).getWpm(), dateTimeFormatter.format(map.get(BEST).getStartDate()));
    }

}
