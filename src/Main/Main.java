package Main;

import Days.week1.*;
import Days.week2.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        final long start = System.currentTimeMillis();
        Day10 day10 = new Day10();
        Main main = new Main();
        String input = readFile("input.txt");

        day10.solve(input,true);

        final long end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start) + " ms");
    }

    //Workshop space below


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