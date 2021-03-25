package com.kwd;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import static com.kwd.Constants.*;

public class TT {
    static float errorRateCurrent = 0;
    static float wpmCurrent = 0;
    static float errorRateBest = Float.MAX_VALUE;
    static float wpmBest = Float.MAX_VALUE;
    static String bestTimeString = "If you can read this.... you are too close to error. Hint: This should not happen";
    static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy>MM>dd>HH:mm:ss");
    static HashMap<String, Statistic> statisticBeforeSavingMap = new HashMap<>();
    static HashMap<String, Statistic> statisticAfterSavingMap = new HashMap<>();

    public static void main(String[] args) throws IOException {
        File recordsFile = FileOperations.getRecordFile();

        if (args.length < 2) {
            System.out.println("To add record provide parameters: \n  error Rate (float), wpm (float), date yyyy>MM>dd>HH:mm:ss [optional]\n");
            FileOperations.parseRecordsFile(recordsFile, statisticAfterSavingMap);
            Print.printMapStatistics(statisticAfterSavingMap);
            System.out.println();
            Print.printBest();
        } else if (args.length == 2) {
            errorRateCurrent = Float.parseFloat(args[0]);
            wpmCurrent = Float.parseFloat(args[1]);
            FileOperations.parseRecordsFile(recordsFile, statisticBeforeSavingMap);
            System.out.println("before saving new record");
            Print.printMapStatistics(statisticBeforeSavingMap);
            System.out.println();
            FileOperations.writeRecordToFile(errorRateCurrent, wpmCurrent, recordsFile, dateTimeFormatter);
            FileOperations.parseRecordsFile(recordsFile, statisticAfterSavingMap);
            Print.printMapStatistics(statisticAfterSavingMap);
            Print.printBest();
            System.out.println();
            evaluateCurrentAttempt(statisticAfterSavingMap);
        } else if (args.length >= 3) {
            System.err.println("3 or more input parameters not yet supported");
        }
    }

    private static void evaluateCurrentAttempt(HashMap<String, Statistic> map) {
        float errorRateAllTime = map.get(ALL_TIME).getErrorRate();
        if (errorRateCurrent > errorRateAllTime) {
            Print.printEncouragement();
        }
        float wpmAllTime = map.get(ALL_TIME).getWpm();
        if (errorRateCurrent < errorRateAllTime ||
                errorRateCurrent == errorRateAllTime
                        && wpmCurrent > wpmAllTime) {
            Print.printAboveAverageAllTime();
        }
        float errorRateLast7Days = map.get(LAST_7_DAYS).getErrorRate();
        float wpmLast7Days = map.get(LAST_7_DAYS).getWpm();
        if (errorRateCurrent < errorRateLast7Days ||
                errorRateCurrent == errorRateLast7Days
                        && wpmCurrent > wpmLast7Days) {
            Print.printAboveAveragePast7Days();
        }
        float errorRateLast3Days = map.get(LAST_3_DAYS).getErrorRate();
        float wpmLast3Days = map.get(LAST_3_DAYS).getWpm();
        if (errorRateCurrent < errorRateLast3Days ||
                errorRateCurrent == errorRateLast3Days
                        && wpmCurrent > wpmLast3Days) {
            Print.printAboveAveragePast3Days();
        }
        if (errorRateCurrent == errorRateBest && wpmCurrent == wpmBest) {
            Print.printTiedPersonalBest();
        }
        if (errorRateCurrent < errorRateBest || errorRateCurrent == errorRateBest && wpmCurrent > wpmBest) {
            Print.printNewPersonalBest();
        }
    }

}
