import Days.week2.Day10;
import Days.week2.Day10.*;

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

        char[][] grid = createGrid(input);
        ArrayList<HikingTrail> trails = day10.searchForTrailStarts(grid);
        day10.findAllPaths(trails, grid);
        System.out.println(day10.getAllScores(trails));

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

    private static char[][] createGrid(String input) {
        String[] strArr = input.replace("\r", "").split("\n");
        char[][] grid = new char[strArr.length][];
        for (int i = 0; i < strArr.length; i++) {
            grid[i] = strArr[i].toCharArray();
        }
        return grid;
    }

    private static void printGrid(char[][] grid) {
        System.out.println("Grid: ");
        for(char[] line : grid){
            System.out.println(Arrays.toString(line));
        }
        System.out.println();
    }
}