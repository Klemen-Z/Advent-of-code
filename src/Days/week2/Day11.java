package Days.week2;

import java.util.*;

public class Day11 {
    public void solve(String input,int iterations){
        System.out.println("There are now " + calculateTotalElements(arrangementParse(input), iterations) + " stones");
    }

    public long calculateTotalElements(ArrayList<Long> numbers, int blinks) {
        Map<Long, Long> stones = new HashMap<>();
        for (Long val : new HashSet<>(numbers)) {
            stones.put(val, numbers.stream().filter((e -> e.equals(val))).count());
        }

        for (int i = 0; i < blinks; i++) {
            Map<Long, Long> newStones = new HashMap<>();

            for (Map.Entry<Long, Long> entry : stones.entrySet()) {
                long key = entry.getKey();
                if (key == 0L) {
                    newStones.merge(1L, entry.getValue(), Long::sum);
                } else if (String.valueOf(key).length() % 2 == 0) {
                    long length = 1;

                    for (int j = 0; j < String.valueOf(key).length() / 2; j++) {
                        length *= 10;
                    }

                    newStones.merge(key / length, entry.getValue(), Long::sum);
                    newStones.merge(key % length, entry.getValue(), Long::sum);

                } else {
                    newStones.merge(key * 2024, entry.getValue(), Long::sum);
                }
                stones = newStones;
            }
        }

        return getSize(stones);
    }

    public long getSize(Map<Long, Long> stoneCount) {
        return stoneCount.values().stream().reduce(0L, Long::sum);
    }

    public ArrayList<Long> arrangementParse(String input) {
        ArrayList<Long> arrangement = new ArrayList<>();
        String[] inputStrings = input.replace("\r\n", "").split(" ");
        for (String inputString : inputStrings) {
            arrangement.add(Long.parseLong(inputString));
        }
        return arrangement;
    }
}
