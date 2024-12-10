import Days.week2.Day10;

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
        ArrayList<HikingTrail> trails = main.searchForTrailStarts(grid);
        main.findAllPaths(trails, grid);
        System.out.println(main.getAllScores(trails));

        final long end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start) + " ms");
    }

    //Workshop space below
    public int getAllScores(ArrayList<HikingTrail> trails) {
        int score = 0;

        for (HikingTrail trail : trails) {
            score += trail.getScore();
        }

        return score;
    }

    public void findAllPaths(ArrayList<HikingTrail> trails, char[][] grid){
        trails.parallelStream().forEach(trail -> createPaths(trail, grid));
    }

    public void createPaths(HikingTrail trail, char[][] grid) {
        int index1 = 0;
        while(index1 < trail.paths.size()) {
            int index2 = 0;
            while (index2 < trail.paths.get(index1).size()) {
                ArrayList<Coordinates> possiblePaths;
                if (index2 != 0){
                    possiblePaths = checkSurroundingSquares(trail.paths.get(index1).get(index2-1), trail.paths.get(index1).get(index2), grid);
                } else {
                    possiblePaths = checkSurroundingSquares(trail.paths.get(index1).get(index2), trail.paths.get(index1).get(index2), grid);
                }

                if (!possiblePaths.isEmpty()) {
                    for (int i = 0; i < possiblePaths.size(); i++) {
                        ArrayList<Coordinates> newPath = new ArrayList<>(trail.paths.get(index1));
                        if (i != 0){
                            newPath.add(possiblePaths.get(i));
                            trail.paths.add(new ArrayList<>(newPath));
                            newPath.clear();
                            continue;
                        }

                        trail.paths.get(index1).add(possiblePaths.getFirst());
                    }
                }
                index2++;
            }
            index1++;
        }

        int index3 = 0;
        while(index3 < trail.paths.size()) {
            if(trail.paths.get(index3).isEmpty() || trail.paths.get(index3).getLast().val != 9){
                trail.paths.remove(index3);
            }
            index3++;
        }

        System.out.println("paths generated for trail: " + trail.id);
    }

    public ArrayList<Coordinates> checkSurroundingSquares (Coordinates prevCoordinates, Coordinates coordinates,char[][] grid) {
        ArrayList<Coordinates> result = new ArrayList<>();
        int val;

        if (coordinates.y > 0 && grid[coordinates.y][coordinates.x]+1 ==  grid[coordinates.y-1][coordinates.x] && coordinates.y-1 != prevCoordinates.y) {
            val = Integer.parseInt(grid[coordinates.y-1][coordinates.x]+"");
            result.add(new Coordinates(coordinates.y-1, coordinates.x, val));
        }
        if (coordinates.y < grid.length-1 && grid[coordinates.y][coordinates.x]+1 == grid[coordinates.y+1][coordinates.x] && coordinates.y+1 != prevCoordinates.y) {
            val = Integer.parseInt(grid[coordinates.y+1][coordinates.x]+"");
            result.add(new Coordinates(coordinates.y+1, coordinates.x, val));
        }
        if (coordinates.x > 0 && grid[coordinates.y][coordinates.x]+1 == grid[coordinates.y][coordinates.x-1] && coordinates.x-1 != prevCoordinates.x) {
            val = Integer.parseInt(grid[coordinates.y][coordinates.x-1]+"");
            result.add(new Coordinates(coordinates.y, coordinates.x-1, val));
        }
        if (coordinates.x < grid.length-1 && grid[coordinates.y][coordinates.x]+1 == grid[coordinates.y][coordinates.x+1] && coordinates.x+1 != prevCoordinates.x) {
            val = Integer.parseInt(grid[coordinates.y][coordinates.x+1]+"");
            result.add(new Coordinates(coordinates.y, coordinates.x+1, val));
        }

        return result;
    }

    public ArrayList<HikingTrail> searchForTrailStarts(char[][] grid){
        ArrayList<HikingTrail> hikingTrails = new ArrayList<>();

        int id = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '0') {
                    Coordinates coords = new Coordinates(i, j, 0);
                    ArrayList<Coordinates> trails = new ArrayList<>();
                    trails.add(coords);

                    hikingTrails.add(new HikingTrail(id, trails));
                    id++;
                }
            }
        }

        return hikingTrails;
    }

    public class HikingTrail{
        int id;
        ArrayList<ArrayList<Coordinates>> paths = new ArrayList<>();

        HikingTrail(int id, ArrayList<Coordinates> coordinates){
            this.id = id;
            paths.add(coordinates);
        }

        public int getScore(){
            int score = 0;
            for (ArrayList<Coordinates> path : paths) {
                try {
                    if (path.getLast().val == 9) {
                        score++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return score;
        }
    }
    public class Coordinates{
        int x;
        int y;

        int val;

        Coordinates(int y, int x, int val){
            this.x = x;
            this.y = y;
            this.val = val;
        }
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