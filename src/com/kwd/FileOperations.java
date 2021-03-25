package com.kwd;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                LocalDateTime parsedDate = LocalDateTime.from(TT.dateTimeFormatter.parse(split[2]));
                if (null == firstEntryDateTime) {
                    firstEntryDateTime = parsedDate;
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
                if (tmpErrorRate < TT.errorRateBest) {
                    TT.errorRateBest = tmpErrorRate;
                    TT.wpmBest = tmpWPM;
                    TT.bestTimeString = split[2];
                }
                if (tmpErrorRate == TT.errorRateBest && tmpWPM > TT.wpmBest) {
                    TT.wpmBest = tmpWPM;
                    TT.bestTimeString = split[2];
                }
            }

            mapToPopulate.put(ALL_TIME,
                    new Statistic(ALL_TIME,
                            sumErrorRate / numberOfRecords,
                            sumWPM / numberOfRecords
                    ));

            mapToPopulate.put(FIRST_7_DAYS,
                    new Statistic(FIRST_7_DAYS,
                            sumErrorRateFirst7Days / numberOfRecordsFirst7Days,
                            sumWPMFirst7Days / numberOfRecordsFirst7Days));

            mapToPopulate.put(LAST_7_DAYS,
                    new Statistic(LAST_7_DAYS,
                            sumErrorRateLast7Days / numberOfRecordsLast7Days,
                            sumWPMLast7Days / numberOfRecordsLast7Days)
            );

            mapToPopulate.put(LAST_3_DAYS,
                    new Statistic(LAST_3_DAYS,
                            sumErrorRateLast3Days / numberOfRecordsLast3Days,
                            sumWPMLast3Days / numberOfRecordsLast3Days
                    ));
        }
    }

    static void writeRecordToFile(float errorRate, float wpm, File recordsFile, DateTimeFormatter dateTimeFormatter) throws IOException {
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
