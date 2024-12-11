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
        AtomicLong totalElements = new AtomicLong();
        Map<Long, Long> vals = Collections.synchronizedMap(new HashMap<>());

        numbers.forEach(number -> {
           blink(number, iterations, vals);
           System.out.println("Blinked");
        });

        vals.keySet().parallelStream().forEach(number -> {
           totalElements.addAndGet(vals.get(number));
        });

        return totalElements.get();
    }

    public static boolean hasEvenNumberOfDigits(long number) {
        int digitCount = String.valueOf(Math.abs(number)).length();
        return digitCount % 2 == 0;
    }

    public static Long[] splitNumber(long number) {
        String numberStr = number+"";;

        long left = Long.parseLong(numberStr.substring(0, numberStr.length()/2));
        long right = Long.parseLong(numberStr.substring(numberStr.length()/2));

        return new Long[]{left, right};
    }

    public void blink(Long number, int iterations, Map<Long, Long> map) {
        if (!map.containsKey(number)) {
            map.put(number, 1L);
        } else {
            map.replace(number, map.get(number) + 1);
        }

        if (number == 0){
            blink(1L, iterations-1, map);
        } else if (hasEvenNumberOfDigits(number)) {
            Long[] vals = splitNumber(number);
            for (Long val : vals) {
                blink(val, iterations-1, map);
            }
        } else {
            long newNum = number*2024;
            blink(newNum, iterations-1, map);
        }
        map.replace(number, map.get(number)-1);
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