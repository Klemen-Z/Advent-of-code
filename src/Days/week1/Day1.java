package Days.week1;

import java.util.ArrayList;

public class Day1 {
    public void solve(String input, boolean part1){
        ArrayList<ArrayList<Integer>> parsedInput = parseInput(input);

        if (part1){
            System.out.println("Total Difference: " + getTotalDifference(parsedInput.getFirst(), parsedInput.getLast()));
            return;
        }
        System.out.println("Similarity: " + getSimilarity(parsedInput.getFirst(), parsedInput.getLast()));
    }

    public ArrayList<ArrayList<Integer>> parseInput(String input){
        String[] nums = input.replace("   ", "\n").replace("\r", "").split("\n");

        int finalResult = 0;

        ArrayList<Integer> list1 = new ArrayList<>();
        ArrayList<Integer> list2 = new ArrayList<>();

        for (int i = 0; i < nums.length; i++) {
            int num = Integer.parseInt(nums[i]);
            if (i % 2 == 0) {
                list1.add(num);
            } else {
                list2.add(num);
            }
        }

        ArrayList<ArrayList<Integer>> result = new ArrayList<>();

        result.add(list1);
        result.add(list2);
        return result;
    }

    public int getSimilarity(ArrayList<Integer> list1, ArrayList<Integer> list2) {
        int finalResult = 0;

        int count = 0;

        for (Integer integer1 : list1) {
            for (Integer integer2 : list2) {
                if (integer1.intValue() == integer2.intValue()) {
                    count++;
                }
            }
            finalResult += integer1*count;
            count = 0;
        }

        return finalResult;
    }

    public int getTotalDifference(ArrayList<Integer> list1, ArrayList<Integer> list2) {
        int finalResult = 0;

        list2.sort(Integer::compareTo);
        list1.sort(Integer::compareTo);

        for (int i = 0; i < list1.size(); i++) {
            int tempVal;

            int int1 = list1.get(i);
            int int2 = list2.get(i);

            tempVal = int1 - int2;
            if (tempVal < 0) {
                tempVal = int2 - int1;
            }
            finalResult += tempVal;
        }

        return finalResult;
    }
}
