package com.kwd;

import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;

import static com.kwd.Constants.*;

public class FileOperations {
    private static float tmpErrorRate;
    private static float sumErrorRate = 0;
    private static float sumErrorRateFirst7Days = 0;
    private static float sumErrorRateLast3Days = 0;
    private static float sumErrorRateLast7Days = 0;
    private static float tmpWPM;
    private static float sumWPM = 0;
    private static float sumWPMFirst7Days = 0;
    private static float sumWPMLast3Days = 0;
    private static float sumWPMLast7Days = 0;
    private static int numberOfRecords = 0;
    private static int numberOfRecordsFirst7Days = 0;
    private static int numberOfRecordsLast3Days = 0;
    private static int numberOfRecordsLast7Days = 0;
    private static LocalDateTime startDateAllTime;
    private static LocalDateTime startDateFirst7Days;
    private static LocalDateTime startDateLast3Days;
    private static LocalDateTime startDateLast7Days;
    private static LocalDateTime endDateAllTime;
    private static LocalDateTime endDateFirst7Days;
    private static LocalDateTime endDateLast3Days;
    private static LocalDateTime endDateLast7Days;

    private static float errorRateBest = Float.MAX_VALUE;
    private static float wpmBest = Float.MIN_VALUE;

    private FileOperations() {
    }

    static void parseRecordsFile(File recordsFile, HashMap<String, Statistic> mapToPopulate) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(recordsFile))) {
            String line;
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime firstEntryDateTime = null;
            while (null != (line = bufferedReader.readLine())) {
                numberOfRecords++;
                String[] split = line.split(";");
                tmpErrorRate = Float.parseFloat(split[0]);
                tmpWPM = Float.parseFloat(split[1]);
                sumErrorRate += tmpErrorRate;
                sumWPM += tmpWPM;
                // calculating avg for first 7 days
                LocalDateTime parsedDate = LocalDateTime.from(dateTimeFormatter.parse(split[2]));
                if (null == firstEntryDateTime) {
                    firstEntryDateTime = parsedDate;
                    startDateAllTime = firstEntryDateTime;
                    endDateAllTime = now;
                    startDateFirst7Days = firstEntryDateTime;
                    endDateFirst7Days = startDateFirst7Days.plusDays(7);
                    startDateLast3Days = now.minusDays(3);
                    endDateLast3Days = now;
                    startDateLast7Days = now.minusDays(7);
                    endDateLast7Days = now;

                }
                if (parsedDate.isBefore(firstEntryDateTime.plusDays(7))) {
                    numberOfRecordsFirst7Days++;
                    sumErrorRateFirst7Days += tmpErrorRate;
                    sumWPMFirst7Days += tmpWPM;
                }
                // calculating avg for last 7 days
                if (parsedDate.isAfter(now.minusDays(7))) {
                    numberOfRecordsLast7Days++;
                    sumErrorRateLast7Days += tmpErrorRate;
                    sumWPMLast7Days += tmpWPM;
                }
                // calculating avg for last 3 days
                if (parsedDate.isAfter(now.minusDays(3))) {
                    numberOfRecordsLast3Days++;
                    sumErrorRateLast3Days += tmpErrorRate;
                    sumWPMLast3Days += tmpWPM;
                }
                // finding best result
                if (tmpErrorRate < errorRateBest) {
                    errorRateBest = tmpErrorRate;
                    wpmBest = tmpWPM;
                }
                if (tmpErrorRate == errorRateBest && tmpWPM > wpmBest) {
                    wpmBest = tmpWPM;
                }
            }

            mapToPopulate.put(BEST, new Statistic(BEST, errorRateBest, wpmBest, now, now));

            mapToPopulate.put(ALL_TIME,
                    new Statistic(ALL_TIME,
                            sumErrorRate / numberOfRecords,
                            sumWPM / numberOfRecords, startDateAllTime, endDateAllTime
                    ));

            mapToPopulate.put(FIRST_7_DAYS,
                    new Statistic(FIRST_7_DAYS,
                            sumErrorRateFirst7Days / numberOfRecordsFirst7Days,
                            sumWPMFirst7Days / numberOfRecordsFirst7Days, startDateFirst7Days, endDateFirst7Days
                    ));

            mapToPopulate.put(LAST_7_DAYS,
                    new Statistic(LAST_7_DAYS,
                            sumErrorRateLast7Days / numberOfRecordsLast7Days,
                            sumWPMLast7Days / numberOfRecordsLast7Days, startDateLast7Days, endDateLast7Days
                    ));

            mapToPopulate.put(LAST_3_DAYS,
                    new Statistic(LAST_3_DAYS,
                            sumErrorRateLast3Days / numberOfRecordsLast3Days,
                            sumWPMLast3Days / numberOfRecordsLast3Days, startDateLast3Days, endDateLast3Days
                    ));
        }
    }

    static void writeRecordToFile(float errorRate, float wpm, File recordsFile) throws IOException {
        LocalDateTime localDateTime = LocalDateTime.now();
        String timeString;
        timeString = dateTimeFormatter.format(localDateTime);
        try (FileWriter fileWriter = new FileWriter(recordsFile, true)) {
            fileWriter.write(Float.toString(errorRate));
            fileWriter.write(";");
            fileWriter.write(Float.toString(wpm));
            fileWriter.write(";");
            fileWriter.write(timeString);
            fileWriter.write(System.lineSeparator());

            String saving = "saving   ------->";
            System.out.printf("%" + Constants.CATEGORY_STRING_MAX_LENGTH + "s:%" + Constants.ERROR_RATE_DIGITS + ".2f%% %" + Constants.WPM_DIGITS + ".2fwpm  %s\n"
                    , saving, errorRate, wpm, timeString);
        }
    }

    public static File getRecordFile() throws IOException {
        File file = new File("typing records.csv");
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }
}
