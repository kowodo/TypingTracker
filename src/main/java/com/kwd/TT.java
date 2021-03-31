package com.kwd;

import java.io.*;
import java.util.HashMap;

import static com.kwd.Constants.*;

public class TT {
    static float errorRateCurrent = 0;
    static float wpmCurrent = 0;
    static HashMap<String, Statistic> statisticBeforeSavingMap = new HashMap<>();
    static HashMap<String, Statistic> statisticAfterSavingMap = new HashMap<>();

    public static void main(String[] args) throws IOException {
        File recordsFile = FileOperations.getRecordFile();

        if (args.length < 2) {
            System.out.println("To add record provide parameters: \n  error Rate (float), wpm (float), date yyyy>MM>dd>HH:mm:ss [optional]\n");
            FileOperations.parseRecordsFile(recordsFile, statisticBeforeSavingMap);
            Print.printMapStatistics(statisticBeforeSavingMap);
            System.out.println();
            Print.printBest(statisticBeforeSavingMap);
        } else if (args.length == 2) {
            System.out.println();
            errorRateCurrent = Float.parseFloat(args[0]);
            wpmCurrent = Float.parseFloat(args[1]);
            FileOperations.parseRecordsFile(recordsFile, statisticBeforeSavingMap);
            FileOperations.writeRecordToFile(errorRateCurrent, wpmCurrent, recordsFile);
            FileOperations.parseRecordsFile(recordsFile, statisticAfterSavingMap);
            System.out.println();
            Print.printMapStatisticsComparison(statisticBeforeSavingMap, statisticAfterSavingMap);
            System.out.println();
            Print.printBest(statisticAfterSavingMap);
            System.out.println();
            evaluateCurrentAttempt(statisticBeforeSavingMap, statisticAfterSavingMap);
        } else if (args.length >= 3) {
            System.err.println("3 or more input parameters not yet supported");
        }
    }

    private static void evaluateCurrentAttempt(HashMap<String, Statistic> before, HashMap<String, Statistic> after) {
        float errorRateAllTime = after.get(ALL_TIME).getErrorRate();
        if (errorRateCurrent > errorRateAllTime) {
            Print.printEncouragement();
        }
        float wpmAllTime = after.get(ALL_TIME).getWpm();
        if (errorRateCurrent < errorRateAllTime ||
                errorRateCurrent == errorRateAllTime
                        && wpmCurrent > wpmAllTime) {
            Print.printAboveAverageAllTime();
        }
        float errorRateLast7Days = after.get(LAST_7_DAYS).getErrorRate();
        float wpmLast7Days = after.get(LAST_7_DAYS).getWpm();
        if (errorRateCurrent < errorRateLast7Days ||
                errorRateCurrent == errorRateLast7Days
                        && wpmCurrent > wpmLast7Days) {
            Print.printAboveAveragePast7Days();
        }
        float errorRateLast3Days = after.get(LAST_3_DAYS).getErrorRate();
        float wpmLast3Days = after.get(LAST_3_DAYS).getWpm();
        if (errorRateCurrent < errorRateLast3Days ||
                errorRateCurrent == errorRateLast3Days
                        && wpmCurrent > wpmLast3Days) {
            Print.printAboveAveragePast3Days();
        }
        if (before.get(BEST).getErrorRate() == errorRateCurrent
                && before.get(BEST).getWpm() == wpmCurrent) {
            Print.printTiedPersonalBest();
        }
        if (before.get(BEST).getErrorRate() > errorRateCurrent
                || before.get(BEST).getErrorRate() == errorRateCurrent
                && before.get(BEST).getWpm() < wpmCurrent) {
            Print.printNewPersonalBest();
        }
    }

}
