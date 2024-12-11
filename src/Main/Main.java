package Main;

import Days.week1.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Main {
    public static void main(String[] args) {
        final long start = System.currentTimeMillis();
        Day4 day4 = new Day4();
        Main main = new Main();
        String input = readFile("input.txt");

        System.out.println("There are " + main.calculateTotalElements(main.arrangementParse(input), 75) + " stones");

        final long end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start) + " ms");
    }

    //Workshop space below

    //TODO convert so that there is a map that leads to the next value to avoid doing the same calculation multiple times.

    public long calculateTotalElements(ArrayList<Long> numbers, int iterations) {
        AtomicLong totalElements = new AtomicLong(numbers.size());

        numbers.parallelStream().forEach(number -> {
            long sum = blink(number, iterations, 0L);
            totalElements.addAndGet(sum);
            System.out.println("Blinked");
        });

        return totalElements.get();
    }

    public long blink(Long number, int iterations, long total) {
        long counter = total;
        if (iterations <= 0) {
            return counter;
        };

        if (number == 0){
            counter += blink(1L, iterations-1, total);
            return counter;
        }
        String numberStr = number+"";
        if (numberStr.length()%2 == 0) {
            counter++;
            long left = Long.parseLong(numberStr.substring(0, numberStr.length()/2));
            long right = Long.parseLong(numberStr.substring(numberStr.length()/2));
            Long[] vals = new Long[]{left, right};
            for (Long val : vals) {
                counter += blink(val, iterations-1, total);
            }
            return counter;
        }

        long newNum = number*2024;
        counter += blink(newNum, iterations-1, total);
        return counter;
    }

    public ArrayList<Long> arrangementParse(String input) {
        ArrayList<Long> arrangement = new ArrayList<>();
        String[] inputStrings = input.replace("\r\n", "").split(" ");
        for (String inputString : inputStrings) {
            arrangement.add(Long.parseLong(inputString));
        }
        return arrangement;
    }

    //util functions below
    private static String readFile(String filename) {
        File f = new File(filename);
        assert f.exists();

        FileReader fr;
        StringBuilder sb = new StringBuilder();

        try {
            fr = new FileReader(f);

            while (true) {
                int res = fr.read();
                if (res == -1) {
                    break;
                }
                sb.append((char) res);
            }

            fr.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return sb.toString();
    }

    public static char[][] createGrid(String input) {
        String[] strArr = input.replace("\r", "").split("\n");
        char[][] grid = new char[strArr.length][];
        for (int i = 0; i < strArr.length; i++) {
            grid[i] = strArr[i].toCharArray();
        }
        return grid.clone();
    }
}