package com.kwd;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileOperations {
    private static float tmpErrorRate;
    private static float sumErrorRate = 0;
    private static float sumErrorRateLast3Days = 0;
    private static float sumErrorRateLast7Days = 0;
    private static float tmpWPM;
    private static float sumWPM = 0;
    private static float sumWPMLast3Days = 0;
    private static float sumWPMLast7Days = 0;
    private static int numberOfRecords = 0;
    private static int numberOfRecordsLast3Days = 0;
    private static int numberOfRecordsLast7Days = 0;

    static void parseRecordsFile(File recordsFile) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(recordsFile));
        String line;
        LocalDateTime now = LocalDateTime.now();
        while (null != (line = bufferedReader.readLine())) {
            numberOfRecords++;
            String[] split = line.split(";");
            tmpErrorRate = Float.parseFloat(split[0]);
            tmpWPM = Float.parseFloat(split[1]);
            sumErrorRate += tmpErrorRate;
            sumWPM += tmpWPM;
            // calculating avg for last 7 days
            LocalDateTime parsedDate = LocalDateTime.from(TT.dateTimeFormatter.parse(split[2]));
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
            if (tmpErrorRate < TT.bestErrorRate) {
                TT.bestErrorRate = tmpErrorRate;
                TT.bestWPM = tmpWPM;
                TT.bestTimeString = split[2];
            }
            if (tmpErrorRate == TT.bestErrorRate && tmpWPM > TT.bestWPM) {
                TT.bestWPM = tmpWPM;
                TT.bestTimeString = split[2];
            }
        }
        TT.averageErrorRateAllTime = sumErrorRate / numberOfRecords;
        TT.averageWPMAllTime = sumWPM / numberOfRecords;

        TT.averageErrorRateLast7Days = sumErrorRateLast7Days / numberOfRecordsLast7Days;
        TT.averageWPMLast7Days = sumWPMLast7Days / numberOfRecordsLast7Days;

        TT.averageErrorRateLast3Days = sumErrorRateLast3Days / numberOfRecordsLast3Days;
        TT.averageWPMLast3Days = sumWPMLast3Days / numberOfRecordsLast3Days;
        bufferedReader.close();
    }

    static void writeRecordToFile(float errorRate, float wpm, File recordsFile, DateTimeFormatter dateTimeFormatter) throws IOException {
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
