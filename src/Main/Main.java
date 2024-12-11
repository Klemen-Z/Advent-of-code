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

    public long calculateTotalElements(ArrayList<Long> numbers, int iterations) {
        AtomicLong totalElements = new AtomicLong(numbers.size());
        Map<Long, Long[]> knownOps = Collections.synchronizedMap(new HashMap<>());
        knownOps.put(0L, new Long[]{1L});

        numbers.forEach(number -> {
            final long start = System.currentTimeMillis();
            blink(number, iterations, totalElements, knownOps);
            final long end = System.currentTimeMillis();
            System.out.println("Iteration done, took: " + (end - start) + " ms");
        });

        return totalElements.get();
    }

    public void blink(Long number, int iterations, AtomicLong total, Map<Long, Long[]> knownOps) {
        if (iterations <= 0) {
            return;
        }

        if (knownOps.containsKey(number)) {
            Long[] nums = knownOps.get(number);
            if (nums.length == 1) {
               blink(nums[0], iterations-1, total, knownOps);
               return;
            }

            total.incrementAndGet();
            for (Long num : nums) {
                blink(num, iterations-1, total, knownOps);
            }
            return;
        }

        String numberStr = number+"";
        if (numberStr.length()%2 == 0) {
            total.incrementAndGet();
            long left = Long.parseLong(numberStr.substring(0, numberStr.length()/2));
            long right = Long.parseLong(numberStr.substring(numberStr.length()/2));
            Long[] vals = new Long[]{left, right};
            knownOps.put(number, vals);
            for (Long val : vals) {
                blink(val, iterations-1, total, knownOps);
            }
            return;
        }

        long newNum = number*2024;
        knownOps.put(number, new Long[]{newNum});
        blink(newNum, iterations-1, total, knownOps);
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