package Days.week2;

import java.util.*;

public class Day8 {
    public Set<Coordinates> locateAllAntinodes(Map<Character, ArrayList<Coordinates>> antennaLocations, int gridMax, boolean part1) {
        Set<Coordinates> allAntinodes = Collections.synchronizedSet(new HashSet<>());

        antennaLocations.keySet().forEach(key -> {
            ArrayList<Coordinates> antennas = antennaLocations.get(key);
            antennas.parallelStream().forEach(c1 ->{
                antennas.parallelStream().forEach(c2 -> {
                    if (c1.equals(c2)){
                        return;
                    }

                    int xDistance = c1.x-c2.x;
                    int yDistance = c1.y-c2.y;

                    int tempXDist = 0;
                    int tempYDist = 0;

                    if (part1){
                        Coordinates combo = new Coordinates(c1.y+yDistance, c1.x+xDistance);
                        if (combo.x < gridMax && combo.y < gridMax && combo.x >= 0 && combo.y >= 0){
                            allAntinodes.add(combo);
                        }
                        return;
                    }

                    while(true){
                        Coordinates combo = new Coordinates(c1.y+tempYDist, c1.x+tempXDist);
                        if (combo.x < gridMax && combo.y < gridMax && combo.x >= 0 && combo.y >= 0){
                            allAntinodes.add(combo);
                        } else {
                            break;
                        }
                        tempXDist += xDistance;
                        tempYDist += yDistance;
                    }
                });
            });
        });

        return filterOverlaps(allAntinodes);
    }

    public HashSet<Coordinates> filterOverlaps(Set<Coordinates> set) {
        HashSet<Coordinates> returnValue = new HashSet<>();

        for (Coordinates c1 : set) {
            boolean tbA = true;
            for (Coordinates c2 : returnValue) {
                if (c2.x == c1.x && c2.y == c1.y) {
                    tbA = false;
                    break;
                }
            }
            if (tbA) {
                returnValue.add(c1);
            }
        }

        return returnValue;
    }

    public Map<Character, ArrayList<Coordinates>> locateAllAntennas(char[][] grid, HashSet<Character> frequencies){
        Map<Character, ArrayList<Coordinates>> map = Collections.synchronizedMap(new HashMap<>());

        frequencies.parallelStream().forEach(frequency -> {
            ArrayList<Coordinates> coordinates = new ArrayList<>();
            for(int i = 0; i < grid.length; i++){
                for(int j = 0; j < grid[0].length; j++){
                    if(grid[i][j] == frequency){
                        coordinates.add(new Coordinates(i, j));
                    }
                }
            }
            map.put(frequency, coordinates);
        });

        return map;
    }

    public HashSet<Character> getFrequencies(String input) {
        String tempStr = input;
        String[] tempArr = tempStr.replace("\r", "").replace("\n", "")
                .replace(".", "").split("");

        HashSet<Character> frequencies = new HashSet<>();

        for (String s : tempArr) {
            frequencies.add(s.charAt(0));
        }

        return frequencies;
    }

    public class Coordinates{
        int x;
        int y;

        Coordinates(int y, int x){
            this.x = x;
            this.y = y;
        }
    }
}
