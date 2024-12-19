package Days.week2;

import java.util.ArrayList;

public class Day10 {
    public int getAllScores(ArrayList<HikingTrail> trails) {
        int score = 0;

        for (HikingTrail trail : trails) {
            score += trail.getScoreCalcPart2();
        }

        return score;
    }

    public void findAllPaths(ArrayList<HikingTrail> trails, char[][] grid){
        trails.parallelStream().forEach(trail -> createPaths(trail, grid));
    }

    public void createPaths(HikingTrail trail, char[][] grid) {
        char[][] grid1 = grid.clone();
        int index1 = 0;
        while(index1 < trail.paths.size()) {
            int index2 = 0;
            while (index2 < trail.paths.get(index1).size()) {
                ArrayList<Coordinates> possiblePaths;
                if (index2 != 0){
                    possiblePaths = checkSurroundingSquares(trail.paths.get(index1).get(index2-1), trail.paths.get(index1).get(index2), grid1);
                } else {
                    possiblePaths = checkSurroundingSquares(trail.paths.get(index1).get(index2), trail.paths.get(index1).get(index2), grid1);
                }

                if (!possiblePaths.isEmpty()) {
                    ArrayList<Coordinates> prevTrailPos = new ArrayList<>(trail.paths.get(index1));
                    for (int i = 0; i < possiblePaths.size(); i++) {
                        if (possiblePaths.get(i).val <= prevTrailPos.getLast().val){
                            continue;
                        }
                        ArrayList<Coordinates> newPath = new ArrayList<>(prevTrailPos);
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
            if (trail.paths.get(index1).getLast().val != 9){
                trail.paths.remove(index1);
            } else {
                index1++;
            }
        }

        System.out.println("paths generated for trail: " + trail.id);
    }

    public ArrayList<Coordinates> checkSurroundingSquares (Coordinates prevCoordinates, Coordinates coordinates,char[][] grid) {
        ArrayList<Coordinates> result = new ArrayList<>();
        int val;

        if (coordinates.y > 0 && ((int) (grid[coordinates.y][coordinates.x])+1) == ((int) grid[coordinates.y-1][coordinates.x]) && coordinates.y-1 != prevCoordinates.y) {
            val = Integer.parseInt(grid[coordinates.y-1][coordinates.x]+"");
            result.add(new Coordinates(coordinates.y-1, coordinates.x, val));
        }
        if (coordinates.y < grid.length-1 && (((int)grid[coordinates.y][coordinates.x])+1) == ((int) grid[coordinates.y+1][coordinates.x]) && coordinates.y+1 != prevCoordinates.y) {
            val = Integer.parseInt(grid[coordinates.y+1][coordinates.x]+"");
            result.add(new Coordinates(coordinates.y+1, coordinates.x, val));
        }
        if (coordinates.x > 0 && (((int) grid[coordinates.y][coordinates.x])+1) == ((int) grid[coordinates.y][coordinates.x-1]) && coordinates.x-1 != prevCoordinates.x) {
            val = Integer.parseInt(grid[coordinates.y][coordinates.x-1]+"");
            result.add(new Coordinates(coordinates.y, coordinates.x-1, val));
        }
        if (coordinates.x < grid.length-1 && (((int) grid[coordinates.y][coordinates.x])+1) == ((int)grid[coordinates.y][coordinates.x+1]) && coordinates.x+1 != prevCoordinates.x) {
            val = Integer.parseInt(grid[coordinates.y][coordinates.x+1]+"");
            result.add(new Coordinates(coordinates.y, coordinates.x+1, val));
        }

        return result;
    }

    public ArrayList<HikingTrail> searchForTrailStarts(char[][] grid){
        ArrayList<HikingTrail> hikingTrails = new ArrayList<>();

        int id = 1;
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

        public int getScoreCalcPart1(){
            int score = 0;
            ArrayList<Coordinates> appeared = new ArrayList<>();
            for (ArrayList<Coordinates> path : paths) {
                Coordinates last = path.getLast();
                boolean found = false;
                for (Coordinates coordinates : appeared) {
                    if (coordinates.x == last.x && coordinates.y == last.y) {
                        found = true;
                        break;
                    }
                }
                if (path.getLast().val == 9 && !found){
                    score++;
                    appeared.add(last);
                }
            }
            return score;
        }

        public int getScoreCalcPart2(){
            int score = 0;
            for (ArrayList<Coordinates> path : paths) {
                Coordinates last = path.getLast();
                if (path.getLast().val == 9){
                    score++;
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
}
