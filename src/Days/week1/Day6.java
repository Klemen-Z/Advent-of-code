package Days.week1;

import Main.Main;

import java.util.*;

public class Day6 {
    public void solve(String input, boolean part1) {
        char[][] grid = Main.createGrid(input.toLowerCase().replaceAll("\r", ""));

        if (part1){
            System.out.println("Guard visits " + uniqueGuardMoveCount(grid) + " unique locations");
        } else {
            System.out.println("An obstacle can be placed in " + obstacleLoopPositionCount(grid) + " different locations to cause a loop");
        }
    }

    public int obstacleLoopPositionCount(char[][] grid) {
        List<ArrayList<Integer>> possiblePositions = Collections.synchronizedList(new ArrayList<>());

        int[] guardPosition = getGuardPosition(grid);
        Set<ArrayList<Integer>> guardCoordinates = Collections.synchronizedSet(new HashSet<>());
        guardCoordinates.addAll(getGuardUniquePositions(grid, guardPosition));

        guardCoordinates.forEach(guardCoordinate ->{
            char[][] tempGrid = grid.clone();
            int[] tempGuardPos = guardPosition.clone();

            int x = guardCoordinate.get(0);
            int y = guardCoordinate.get(1);

            if (x == tempGuardPos[0] && y == tempGuardPos[1]) {
                return;
            }

            tempGrid[y][x] = '#';
            if (uniqueGuardMoveCount(tempGrid, tempGuardPos) == -1) possiblePositions.add(guardCoordinate);
            tempGrid[y][x] = '.';
        });

        return possiblePositions.size();
    }

    public int uniqueGuardMoveCount(char[][] grid) {
        return uniqueGuardMoveCount(grid, getGuardPosition(grid));
    }

    public HashSet<ArrayList<Integer>> getGuardUniquePositions(char[][] grid, int[] guardPosition) {
        HashSet<ArrayList<Integer>> uniquePositions = new HashSet<>();
        boolean isOnMap = true;
        int guardX = guardPosition[0], guardY = guardPosition[1];
        String direction = "^";
        int ignoredCount = 0;

        if (guardX < 0) {
            return uniquePositions;
        }

        while (true) {
            switch (direction) {
                case "^" -> {
                    String symbol = "";
                    try {
                        symbol = grid[guardY-1][guardX]+"";
                    } catch (IndexOutOfBoundsException e) {
                        isOnMap = false;
                    }

                    if (!isOnMap){
                        break;
                    }

                    if (symbol.equals("#")) {
                        direction = ">";
                    } else {
                        guardY--;
                    }
                }
                case ">" -> {
                    String symbol = "";
                    try {
                        symbol = grid[guardY][guardX+1]+"";
                    } catch (IndexOutOfBoundsException e) {
                        isOnMap = false;
                    }

                    if (!isOnMap){
                        break;
                    }

                    if (symbol.equals("#")) {
                        direction = "v";
                    } else {
                        guardX++;
                    }
                }
                case "v" -> {
                    String symbol = "";
                    try {
                        symbol = grid[guardY+1][guardX]+"";
                    } catch (IndexOutOfBoundsException e) {
                        isOnMap = false;
                    }

                    if (!isOnMap){
                        break;
                    }

                    if (symbol.equals("#")) {
                        direction = "<";
                    } else {
                        guardY++;
                    }
                }
                case "<" -> {
                    String symbol = "";
                    try {
                        symbol = grid[guardY][guardX-1]+"";
                    } catch (IndexOutOfBoundsException e) {
                        isOnMap = false;
                    }

                    if (!isOnMap){
                        break;
                    }

                    if (symbol.equals("#")) {
                        direction = "^";
                    } else {
                        guardX--;
                    }
                }

                default -> throw new RuntimeException("unexpected direction: " + direction);
            }

            if (!isOnMap) {
                break;
            }

            ArrayList<Integer> temp = new ArrayList<>();
            temp.add(guardX);
            temp.add(guardY);

            if(!uniquePositions.add(temp)) ignoredCount++;
            if (ignoredCount == (uniquePositions.size()*2)) break;
        }

        return uniquePositions;
    }

    public int[] getGuardPosition(char[][] grid) {
        int[] guardPosition = new int[2];

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == '^'||grid[i][j] == 'v'||grid[i][j] == '>'||grid[i][j] == '<') {
                    guardPosition[0] = j;
                    guardPosition[1] = i;
                }
            }
        }

        return guardPosition;
    }

    public int uniqueGuardMoveCount(char[][] grid, int[] guardPosition){
        char[][] tempGrid = grid.clone();
        boolean isOnMap = true;
        int guardX = guardPosition[0], guardY = guardPosition[1];
        HashSet<ArrayList<Integer>> previousPositions = new HashSet<>();
        String direction = "^";
        int ignoredCount = 0;

        if (guardX < 0) {
            return 0;
        }

        while (true) {
            switch(direction){
                case "^" -> {
                    String symbol = "";
                    try {
                        symbol = tempGrid[guardY-1][guardX]+"";
                    } catch (IndexOutOfBoundsException e) {
                        isOnMap = false;
                    }

                    if (!isOnMap){
                        break;
                    }

                    if (symbol.equals("#")) {
                        direction = ">";
                    } else {
                        guardY--;
                    }
                }
                case ">" -> {
                    String symbol = "";
                    try {
                        symbol = tempGrid[guardY][guardX+1]+"";
                    } catch (IndexOutOfBoundsException e) {
                        isOnMap = false;
                    }

                    if (!isOnMap){
                        break;
                    }

                    if (symbol.equals("#")) {
                        direction = "v";
                    } else {
                        guardX++;
                    }
                }
                case "v" -> {
                    String symbol = "";
                    try {
                        symbol = tempGrid[guardY+1][guardX]+"";
                    } catch (IndexOutOfBoundsException e) {
                        isOnMap = false;
                    }

                    if (!isOnMap){
                        break;
                    }

                    if (symbol.equals("#")) {
                        direction = "<";
                    } else {
                        guardY++;
                    }
                }
                case "<" -> {
                    String symbol = "";
                    try {
                        symbol = tempGrid[guardY][guardX-1]+"";
                    } catch (IndexOutOfBoundsException e) {
                        isOnMap = false;
                    }

                    if (!isOnMap){
                        break;
                    }

                    if (symbol.equals("#")) {
                        direction = "^";
                    } else {
                        guardX--;
                    }
                }

                default -> throw new RuntimeException("unexpected direction: " + direction);
            }

            if (!isOnMap) {
                break;
            }

            ArrayList<Integer> temp = new ArrayList<>();
            temp.add(guardX);
            temp.add(guardY);

            if(!previousPositions.add(temp)) ignoredCount++;
            if (ignoredCount == (previousPositions.size()*2)) return -1;
        }

        return previousPositions.size();
    }
}
