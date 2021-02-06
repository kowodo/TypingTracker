package com.kwd;

import java.io.*;
import java.time.LocalDateTime;
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
        File recordsFile = TT.getRecordFile();

        if (args.length < 2) {
            System.out.println("To add record provide parameters: \n  error Rate (float), wpm (float), date yyyy>MM>dd>HH:mm:ss [optional]");
            parseRecordsFile(recordsFile);
        } else if (args.length == 2) {
            currentErrorRate = Float.parseFloat(args[0]);
            currentWPM = Float.parseFloat(args[1]);
            parseRecordsFile(recordsFile);
            writeRecordToFile(currentErrorRate, currentWPM, recordsFile, dateTimeFormatter);
        } else if (args.length >= 3) {
            System.err.println("3 or more input parameters not yet supported");
        }
        printAverage();
        printAverageLast7Days();
        printBest();
        evaluate();
    }

    private static void evaluate() {
        // test
        if (currentErrorRate > averageErrorRateAllTime) {
            printEncouragement();
        }
        if (currentErrorRate < averageErrorRateAllTime ||
                currentErrorRate == averageErrorRateAllTime && currentWPM < averageWPMAllTime) {
            printAboveAverageAllTime();
        }
        if (currentErrorRate < averageErrorRateLast7Days ||
                currentErrorRate == averageErrorRateLast7Days && currentWPM < averageWPMLast7Days) {
            printAboveAveragePast7Days();
        }
        if (currentErrorRate == bestErrorRate && currentWPM == bestWPM) {
            printTiedPersonalBest();
        }
        if (currentErrorRate < bestErrorRate || currentErrorRate == bestErrorRate && currentWPM < bestWPM) {
            printNewPersonalBest();
        }
    }

    private static void printAboveAverageAllTime() {
        System.out.println(Constants.ANSI_GREEN + "Better than all time average!" + Constants.ANSI_RESET);
    }

    private static void printAboveAveragePast7Days() {
        System.out.println(Constants.ANSI_GREEN + "Better than average for last 7 days! " + Constants.ANSI_RESET);
    }

    private static void printTiedPersonalBest() {
        System.out.println(Constants.ANSI_CYAN + "You have TIED your personal best!!!" + Constants.ANSI_RESET);
    }

    private static void printNewPersonalBest() {
        System.out.println(Constants.ANSI_CYAN + ">>>> New personal best! Celebration time! <<<<" + Constants.ANSI_RESET);
    }

    private static void printEncouragement() {
        System.out.println("Stick with it and you will get better!");
    }

    private static void printAverage() {
        String average = "Average";
        System.out.printf("%" + Constants.CATEGORY_STRING_MAX_LENGTH + "s:%" + Constants.ERROR_RATE_DIGITS + ".2f%% %" + Constants.WPM_DIGITS + ".2fwpm\n"
                , average, averageErrorRateAllTime, averageWPMAllTime);
    }

    private static void printBest() {
        String personalBest = "Personal best";
        System.out.printf("%" + Constants.CATEGORY_STRING_MAX_LENGTH + "s:%" + Constants.ERROR_RATE_DIGITS + ".2f%% %" + Constants.WPM_DIGITS + ".2fwpm  %s\n"
                , personalBest, bestErrorRate, bestWPM, bestTimeString);
    }

    private static void printAverageLast7Days() {
        String average7days = "Average last 7 days";
        System.out.printf("%" + Constants.CATEGORY_STRING_MAX_LENGTH + "s:%" + Constants.ERROR_RATE_DIGITS + ".2f%% %" + Constants.WPM_DIGITS + ".2fwpm\n"
                , average7days, averageErrorRateLast7Days, averageWPMLast7Days);
    }

    private static void parseRecordsFile(File recordsFile) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(recordsFile));
        float tmpErrorRate;
        float sumErrorRate = 0;
        float sumErrorRateLast7Days = 0;
        float tmpWPM;
        float sumWPM = 0;
        float sumWPMLast7Days = 0;
        int numberOfRecords = 0;
        int numberOfRecordsLast7Days = 0;
        String line;
        while (null != (line = bufferedReader.readLine())) {
            numberOfRecords++;
            String[] split = line.split(";");
            tmpErrorRate = Float.parseFloat(split[0]);
            tmpWPM = Float.parseFloat(split[1]);
            sumErrorRate += tmpErrorRate;
            sumWPM += tmpWPM;
            // calculating avg for last 7 days
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime parsedDate = LocalDateTime.from(dateTimeFormatter.parse(split[2]));
            if (parsedDate.isAfter(now.minusDays(7))) {
                numberOfRecordsLast7Days++;
                sumErrorRateLast7Days += tmpErrorRate;
                sumWPMLast7Days += tmpWPM;
            }
            // finding best result
            if (tmpErrorRate < bestErrorRate) {
                bestErrorRate = tmpErrorRate;
                bestWPM = tmpWPM;
                bestTimeString = split[2];
            }
            if (tmpErrorRate == bestErrorRate && tmpWPM < bestWPM) {
                bestWPM = tmpWPM;
                bestTimeString = split[2];
            }
        }
        averageErrorRateAllTime = sumErrorRate / numberOfRecords;
        averageWPMAllTime = sumWPM / numberOfRecords;
        averageErrorRateLast7Days = sumErrorRateLast7Days / numberOfRecordsLast7Days;
        averageWPMLast7Days = sumWPMLast7Days / numberOfRecordsLast7Days;
        bufferedReader.close();
    }

    private static void writeRecordToFile(float errorRate, float wpm, File recordsFile, DateTimeFormatter dateTimeFormatter) throws IOException {
        LocalDateTime localDateTime = LocalDateTime.now();
        String timeString;
        timeString = dateTimeFormatter.format(localDateTime);
        FileWriter fileWriter = new FileWriter(recordsFile, true);
        fileWriter.write(Float.toString(errorRate));
        fileWriter.write(";");
        fileWriter.write(Float.toString(wpm));
        fileWriter.write(";");
        fileWriter.write(timeString);
        fileWriter.write(System.lineSeparator());

        String saving = "saving   ------->";
        System.out.printf("%" + Constants.CATEGORY_STRING_MAX_LENGTH + "s:%" + Constants.ERROR_RATE_DIGITS + ".2f%% %" + Constants.WPM_DIGITS + ".2fwpm  %s\n"
                , saving, errorRate, wpm, timeString);
        fileWriter.close();
    }

    public static File getRecordFile() throws IOException {
        File file = new File("typing records.csv");
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

}
