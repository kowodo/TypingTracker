package com.kwd;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TT {
    static float bestErrorRate = Float.MAX_VALUE;
    static float bestWPM = Float.MAX_VALUE;
    static String bestTimeString = "If you can read this.... you are too close to error. Hint: This should not happen";
    static float averageErrorRate = -1f;
    static float averageErrorRateLast7Days = -1f;
    static float averageWPM = -1;
    static float averageWPMLast7Days = -1;
    static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy>MM>dd>HH:mm:ss");
    static final int CATEGORY_STRING_MAX_LENGTH = -20;
    static final int ERROR_RATE_DIGITS = 6;
    static final int WPM_DIGITS = 7;

    public static void main(String[] args) throws IOException {
        float errorRate = 0;
        float wpm = 0;
        File recordsFile = TT.getRecordFile();

        if (args.length < 2) {
            System.out.println("To add record provide parameters: \n  error Rate (float), wpm (float), date yyyy>MM>dd>HH:mm:ss [optional]");
            parseRecordsFile(recordsFile);
        } else if (args.length == 2) {
            errorRate = Float.parseFloat(args[0]);
            wpm = Float.parseFloat(args[1]);
            writeRecordToFile(errorRate, wpm, recordsFile, dateTimeFormatter);
            parseRecordsFile(recordsFile);
        } else if (args.length >= 3) {
            System.err.println("3 or more input parameters not yet supported");
        }
        printAverage();
        printAverageLast7Days();
        printBest();
    }

    private static void printAverage() {
        String average = "Average";
        System.out.printf("%" + CATEGORY_STRING_MAX_LENGTH + "s:%"+ERROR_RATE_DIGITS+".2f%% %"+WPM_DIGITS+".2fwpm\n"
                , average, averageErrorRate, averageWPM);
    }

    private static void printBest() {
        String personalBest = "Personal best";
        System.out.printf("%" + CATEGORY_STRING_MAX_LENGTH + "s:%"+ERROR_RATE_DIGITS+".2f%% %"+WPM_DIGITS+".2fwpm  %s\n"
                , personalBest, bestErrorRate, bestWPM, bestTimeString);
    }

    private static void printAverageLast7Days() {
        String average7days = "Average last 7 days";
        System.out.printf("%" + CATEGORY_STRING_MAX_LENGTH + "s:%"+ERROR_RATE_DIGITS+".2f%% %"+WPM_DIGITS+".2fwpm\n"
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
        averageErrorRate = sumErrorRate / numberOfRecords;
        averageWPM = sumWPM / numberOfRecords;
        averageErrorRateLast7Days = sumErrorRateLast7Days / numberOfRecordsLast7Days;
        averageWPMLast7Days = sumWPMLast7Days / numberOfRecordsLast7Days;
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
        System.out.printf("%" + CATEGORY_STRING_MAX_LENGTH + "s:%"+ERROR_RATE_DIGITS+".2f%% %"+WPM_DIGITS+".2fwpm  %s\n"
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
