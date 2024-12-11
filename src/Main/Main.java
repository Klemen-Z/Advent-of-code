package Main;

import Days.week1.*;
import Days.week2.Day11;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        final long start = System.currentTimeMillis();
        Day11 day11 = new Day11();
        Main main = new Main();
        String input = readFile("input.txt");

        day11.solve(input, 75);

        final long end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start) + " ms");
    }

    //Workshop space below



    public ArrayList<Long> arrangementParse(String input) {
        ArrayList<Long> arrangement = new ArrayList<>();
        String[] inputStrings = input.replace("\r\n", "").split(" ");
        for (String inputString : inputStrings) {
            arrangement.add(Long.parseLong(inputString));
        }
        return arrangement;
    }

    private long getSize(Map<Long, Long> stoneCount) {
        return stoneCount.values().stream().reduce(0L, Long::sum);
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