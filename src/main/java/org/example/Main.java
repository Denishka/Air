package org.example;

import java.io.*;
import java.util.*;

import static org.example.Constants.*;

public class Main {

    public static void main(String[] args) throws IOException {

        int columnNum = parseColumnNum(args);


        Map<Integer, Set<Integer>> airs = initMap(FILE_PATH, columnNum);
        Scanner scanner = new Scanner(System.in);

        String userInput = "";

        while (true) {

            System.out.print("Input: ");
            userInput = scanner.next();

            if (QUIT_COMMAND.equals(userInput))
                System.exit(SUCCESS_CODE);

            if (EMPTY_USER_INPUT.equals(userInput))
                System.out.println("Please input something...");

            long before = System.currentTimeMillis();

            Set<Integer> positions = airs.get(userInput.toLowerCase().hashCode());

            printLines(FILE_PATH, columnNum, positions);
            long after = System.currentTimeMillis();
            System.out.println("Search time: " + (after - before));
        }


    }

    public static int parseColumnNum(String[] args) {
        if (args.length < 1) {
            System.out.println("Column num not provided. Please use '.exe {column num}' ");
            System.exit(NOT_ENOUGH_PARAMETERS_SPECIFIED_ERROR_CODE);
        }


        try {
            int columnNum = Integer.parseInt(args[0]);

            if(columnNum <= 0){
                System.out.println("Incorrect parameter specified. Column name must be above 0");
                System.exit(INCORRECT_COLUMN_NUM_SPECIFIED_ERROR_CODE);
            }
            return columnNum;

        } catch (NumberFormatException e) {
            System.out.println("Unable to parse column num. Please make sure that provided value is number: " + args[0]);
            System.exit(INCORRECT_COLUMN_NUM_FORMAT_ERROR_CODE);
        }

        return 0;
    }


    public static void printLines(String path, int columnNum, Set<Integer> positions) throws IOException {
        if (Objects.isNull(positions) || positions.isEmpty()) {
            System.out.println("Nothing found!");
            return;
        }
        RandomAccessFile raf = new RandomAccessFile(path, "r");
        Map<String, String> sortedResult = new TreeMap<>();

        for (Integer pos : positions) {
            raf.seek(pos);
            String line = raf.readLine();
            String column = line.split(CSV_DELIMITER_SYMBOL)[columnNum-1];
            sortedResult.put(column, line);
        }

        for (Map.Entry<String,String> entry: sortedResult.entrySet()) {
            String lineOutput = String.format("%s[%s]", entry.getKey(), entry.getValue());
            System.out.println(lineOutput);
        }

        raf.close();

    }

    public static Map<Integer, Set<Integer>> initMap(String filePath, int columnNum) {

        Map<Integer, Set<Integer>> map = new TreeMap<>();
        try {
            File file = new File(filePath);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);

            int lineStartPos = 0;

            while (true) {
                String line = reader.readLine();

                if (Objects.isNull(line))
                    break;

                String[] lineParth = line.split(CSV_DELIMITER_SYMBOL);

                if(columnNum > lineParth.length){
                    System.out.println("Unable to init program. File contains fewer columns than specified column num");
                    System.exit(INCORRECT_COLUMN_NUM_SPECIFIED_ERROR_CODE);
                }

                String columnValue = lineParth[columnNum - 1];
                columnValue = StringUtils.unquote(columnValue);

                for (int i = 0; i < columnValue.length(); i++) {
                    int hashCode = columnValue.substring(0, i + 1).toLowerCase().hashCode();

                    MapHelper.addValue(map, hashCode, lineStartPos);
                }

                lineStartPos += line.getBytes().length + 1;

            }

            fr.close();
            reader.close();

            return map;
        } catch (IOException e) {
            System.out.println("Unable to read file '" + FILE_PATH + "'. File is corrupted or is using by another process.");
            e.printStackTrace();
            System.exit(UNABLE_TO_READ_FILE_ERROR_CODE);
        }
        return map;
    }
}