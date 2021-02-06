package com.kwd;

import java.io.*;
import java.time.format.DateTimeFormatter;

public class TT {
    static float currentErrorRate = 0;
    static float currentWPM = 0;
    static float bestErrorRate = Float.MAX_VALUE;
    static float bestWPM = Float.MAX_VALUE;
    static String bestTimeString = "If you can read this.... you are too close to error. Hint: This should not happen";
    static float averageErrorRateAllTime = -1f;
    static float averageErrorRateLast7Days = -1f;
    static float averageWPMAllTime = -1;
    static float averageWPMLast7Days = -1;
    static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy>MM>dd>HH:mm:ss");

    public static void main(String[] args) throws IOException {
        File recordsFile = FileOperations.getRecordFile();

        if (args.length < 2) {
            System.out.println("To add record provide parameters: \n  error Rate (float), wpm (float), date yyyy>MM>dd>HH:mm:ss [optional]");
            FileOperations.parseRecordsFile(recordsFile);
            Print.printStatistics();
        } else if (args.length == 2) {
            currentErrorRate = Float.parseFloat(args[0]);
            currentWPM = Float.parseFloat(args[1]);
            FileOperations.parseRecordsFile(recordsFile);
            FileOperations.writeRecordToFile(currentErrorRate, currentWPM, recordsFile, dateTimeFormatter);
            Print.printStatistics();
            evaluateCurrentAtempt();
        } else if (args.length >= 3) {
            System.err.println("3 or more input parameters not yet supported");
        }
    }

    private static void evaluateCurrentAtempt() {
        // test
        if (currentErrorRate > averageErrorRateAllTime) {
            Print.printEncouragement();
        }
        if (currentErrorRate < averageErrorRateAllTime ||
                currentErrorRate == averageErrorRateAllTime && currentWPM < averageWPMAllTime) {
            Print.printAboveAverageAllTime();
        }
        if (currentErrorRate < averageErrorRateLast7Days ||
                currentErrorRate == averageErrorRateLast7Days && currentWPM < averageWPMLast7Days) {
            Print.printAboveAveragePast7Days();
        }
        if (currentErrorRate == bestErrorRate && currentWPM == bestWPM) {
            Print.printTiedPersonalBest();
        }
        if (currentErrorRate < bestErrorRate || currentErrorRate == bestErrorRate && currentWPM < bestWPM) {
            Print.printNewPersonalBest();
        }
    }

}
