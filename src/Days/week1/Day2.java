package Days.week1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day2 {
    public void solve(String input, boolean part1) {
        ArrayList<ArrayList<Integer>> parsedInput = parseInput(input);
        System.out.println("Safe reports: " + getSafetyCount(parsedInput, part1));
    }

    public ArrayList<ArrayList<Integer>> parseInput(String input) {
        String[] nums = input.replace("\r", "").split("\n");
        ArrayList<ArrayList<Integer>> list1 = new ArrayList<>();

        for (String string : nums) {
            String[] num = string.split(" ");
            ArrayList<Integer> list = new ArrayList<>();
            for (String s : num) {
                list.add(Integer.parseInt(s));
            }
            list1.add(list);
        }

        return list1;
    }

    public int getSafetyCount(ArrayList<ArrayList<Integer>> list, boolean part1) {
        int finalCount = 0;

        for (ArrayList<Integer> list1 : list){
            boolean safe = checkList(list1);
            int removeInd = -1;

            while(!safe && !part1){
                ArrayList<Integer> list2 = new ArrayList<>(list1);
                if (removeInd != -1){
                    list2.remove(removeInd);
                }
                safe = checkList(list2);
                removeInd++;
                if (removeInd > list1.size()-1){
                    break;
                }
            }

            if (safe) {
                finalCount++;
            }
        }

        return finalCount;
    }

    private boolean checkList(List<Integer> list) {
        int prevOp = 0;
        for (int i = 0; i < list.size()-1; i++) {
            int difference = list.get(i)-list.get(i+1);
            if (isSafe(difference, prevOp)) {
                return false;
            } else {
                if (prevOp == 0){
                    prevOp = difference;
                }
            }
        }

        return true;
    }

    public boolean isSafe(int difference, int prevOp) {
        return  difference > 0 && prevOp < 0 || difference < 0 && prevOp > 0 || difference > 3 || difference < -3 || difference == 0;
    }
}
