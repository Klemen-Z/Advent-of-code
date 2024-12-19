import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        final long start = System.currentTimeMillis();
        Main main = new Main();
        String input = main.readFile("input.txt");

        String[] mainArr = input.replace("\r", "").split("\n\n");
        ArrayList<RuleSet> rules = main.compileRules(mainArr[0]);
        ArrayList<ArrayList<Integer>> intArrays = main.compileArrays(mainArr[1]);

        ArrayList<ArrayList<Integer>> wrongIntArrs = main.getWronglyOrderedArrs(intArrays, rules);
        ArrayList<ArrayList<Integer>> correctIntArrs = main.fixOrder(wrongIntArrs, rules);

        System.out.println(main.addMiddleNumbers(correctIntArrs));

        final long end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start) + " ms");
    }

    private int addMiddleNumbers(ArrayList<ArrayList<Integer>> correctIntArrs) {
        int count = 0;

        for (ArrayList<Integer> arr : correctIntArrs) {
            count += arr.get((arr.size()/2));
        }

        return count;
    }

    private ArrayList<ArrayList<Integer>> fixOrder(ArrayList<ArrayList<Integer>> arrs, ArrayList<RuleSet> rules) {
        ArrayList<ArrayList<Integer>> correctedArrs = new ArrayList<>();

        for (ArrayList<Integer> arr : arrs) {
            while(!checkRules(arr, rules)){
                for(RuleSet rule : rules){
                    if (!arr.contains(rule.first) || !arr.contains(rule.after)){
                        continue;
                    }

                    if (arr.indexOf(rule.first) > arr.indexOf(rule.after)) {
                        int prevIndex = arr.indexOf(rule.after);
                        arr.set(arr.indexOf(rule.first), rule.after);
                        arr.set(prevIndex, rule.first);
                    }
                }
            }
            correctedArrs.add(arr);
        }

        return correctedArrs;
    }

    private ArrayList<ArrayList<Integer>> getWronglyOrderedArrs(ArrayList<ArrayList<Integer>> arrs, ArrayList<RuleSet> rules) {
        ArrayList<ArrayList<Integer>> intArrs = new ArrayList<>(arrs);

        ArrayList<ArrayList<Integer>> correctArrs = getCorrectlyOrderedArrs(intArrs, rules);
        intArrs.removeAll(correctArrs);

        return intArrs;
    }

    private boolean checkRules(ArrayList<Integer> arr, ArrayList<RuleSet> rules) {
        for (RuleSet rule : rules) {
            if (!arr.contains(rule.first) || !arr.contains(rule.after)){
                continue;
            }

            if (arr.indexOf(rule.first) > arr.indexOf(rule.after)) {
                return false;
            }
        }

        return true;
    }

    private ArrayList<ArrayList<Integer>> getCorrectlyOrderedArrs(ArrayList<ArrayList<Integer>> arrs, ArrayList<RuleSet> rules) {
        ArrayList<ArrayList<Integer>> intArrs = new ArrayList<>();

        for (ArrayList<Integer> arr : arrs) {
            boolean fulfillsRules = true;
            for (RuleSet rule : rules) {
                if (!arr.contains(rule.first) || !arr.contains(rule.after)){
                    continue;
                }

                if (arr.indexOf(rule.first) > arr.indexOf(rule.after)) {
                    fulfillsRules = false;
                }

                if (!fulfillsRules) {
                    break;
                }
            }
            if (fulfillsRules) {
                intArrs.add(arr);
            }
        }

        return intArrs;
    }

    private ArrayList<ArrayList<Integer>> compileArrays(String str) {
        ArrayList<ArrayList<Integer>> intArraySet = new ArrayList<>();

        String[] strArray = str.split("\n");

        for (String string : strArray) {
            ArrayList<Integer> tempIntList = new ArrayList<>();
            String[] tempStrArr = string.split(",");
            for (String tempInt : tempStrArr) {
                tempIntList.add(Integer.parseInt(tempInt));
            }
            intArraySet.add(tempIntList);
        }

        return intArraySet;
    }

    private ArrayList<RuleSet> compileRules(String str){
        ArrayList<RuleSet> ruleSets = new ArrayList<>();
        String[] rules = str.replace("|", "\n").split("\n");
        for (int i = 0; i < rules.length; i++) {
            if (i%2 == 0){
                ruleSets.add(new RuleSet(Integer.parseInt(rules[i]), Integer.parseInt(rules[(i+1)])));
            }
        }
        return ruleSets;
    }

    private int parseForWord(String word, String[] input) {
        char[][] grid = new char[input.length][];
        for (int i = 0; i < input.length; i++) {
            grid[i] = input[i].toCharArray();
        }

        int count = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] != word.charAt(word.length()/2)) continue;

                int nums = 0;
                StringBuilder tempDiagonal1 = new StringBuilder();
                StringBuilder tempDiagonal2 = new StringBuilder();

                while(nums <= word.length()/2){
                    try{
                        tempDiagonal1.append(grid[i+nums][j+nums]);
                    } catch (IndexOutOfBoundsException ignored) {}

                    try {
                        tempDiagonal2.append(grid[i+nums][j-nums]);
                    } catch (IndexOutOfBoundsException ignored) {}

                    try{
                        if (nums != 0){
                            tempDiagonal1.insert(0, (grid[i-nums][j-nums]));
                        }
                    } catch (IndexOutOfBoundsException ignored) {}

                    try {
                        if (nums != 0){
                            tempDiagonal2.insert(0, (grid[i-nums][j+nums]));
                        }
                    } catch (IndexOutOfBoundsException ignored) {}

                    nums++;
                }

                if (tempDiagonal1.length() != word.length() || tempDiagonal2.length() != word.length()){
                    continue;
                }

                String tempStringDiagonal1 = tempDiagonal1.toString();
                String tempStringDiagonal2 = tempDiagonal2.toString();

                String tempStringDiagonal1Reverse = tempDiagonal1.reverse().toString();
                String tempStringDiagonal2Reverse = tempDiagonal2.reverse().toString();

                if (tempStringDiagonal1.equals(word) && tempStringDiagonal2.equals(word)
                        || tempStringDiagonal1Reverse.equals(word) && tempStringDiagonal2Reverse.equals(word)
                        || tempStringDiagonal1Reverse.equals(word) && tempStringDiagonal2.equals(word)
                        || tempStringDiagonal1.equals(word) && tempStringDiagonal2Reverse.equals(word)) {
                    count++;
                }
            }
        }
        return count;
    }

    private int multiplyAllThenAdd(ArrayList<String[]> nums){
        int finalCount = 0;

        for (String[] num : nums) {
            for (int i = 0; i < num.length-1; i++) {
                if (i%2 == 1){
                    int num1 = Integer.parseInt(num[i]);
                    int num2 = Integer.parseInt(num[i+1]);

                    finalCount += (num1*num2);
                }
            }
        }
        return finalCount;
    }

    private String regexMatch(String input) {
        StringBuilder returnVal = new StringBuilder();

        Pattern pattern = Pattern.compile("(mul\\([0-9]{1,3},[0-9]{1,3}\\))|(do\\(\\))|(don't\\(\\))");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            returnVal.append(matcher.group());
        }

        return returnVal.toString();
    }

    private ArrayList<String[]> ignoreDoDont(String input){
        String formattedInput = input.replaceAll("[dont']", "");
        ArrayList<String[]> nums = new ArrayList<>();

        String[] inputArr = convertMulInstructions(formattedInput);

        for (int i = 0; i < inputArr.length; i++) {
            if (i%2 == 1){
                nums.add(new String[]{"",inputArr[i], inputArr[i+1]});
            }
        }

        return nums;
    }

    private ArrayList<String[]> handleDoDont(String input){
        ArrayList<String> donts = new ArrayList<>(Arrays.asList(input.split("(don't\\(\\))")));
        ArrayList<String> vals = new ArrayList<>();
        vals.add(donts.getFirst().replace("do", ""));

        for(int i = 1; i < donts.size(); i++){
            String tempStr = donts.get(i);
            if (tempStr.contains("do()")){
                String[] tempArr = tempStr.split("do\\(\\)");
                vals.addAll(Arrays.asList(tempArr).subList(1, tempArr.length));
            }
        }

        ArrayList<String[]> returnVal = new ArrayList<>();

        for (String val : vals) {
            returnVal.add(convertMulInstructions(val));
        }

        return returnVal;
    }

    private String[] convertMulInstructions(String input) {
        return input.replace("m", " ").replace("u", "").replace("l", "").replace("(", "").replace(")", "").replace(",", " ").split(" ");
    }

    private int getSafetyCount(ArrayList<ArrayList<Integer>> list) {
        int finalCount = 0;

        for (ArrayList<Integer> list1 : list){
            boolean safe = false;
            int removeInd = -1;

            while(!safe){
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

    private boolean isSafe(int difference, int prevOp) {
        return  difference > 0 && prevOp < 0 || difference < 0 && prevOp > 0 || difference > 3 || difference < -3 || difference == 0;
    }

    private int getSimilarity(ArrayList<Integer> list1, ArrayList<Integer> list2) {
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

    private int getTotalDifference(ArrayList<Integer> list1, ArrayList<Integer> list2) {
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

    private String readFile(String filename) {
        File f = new File(filename);
        assert f.exists();

        FileReader fr;

        try {
            fr = new FileReader(f);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        StringBuilder sb = new StringBuilder();

        while (true) {
            int res;
            try {
                res = fr.read();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (res == -1) {
                break;
            }

            sb.append((char) res);
        }

        return sb.toString();
    }

    private class RuleSet{
        int first;
        int after;

        RuleSet(int first, int after){
            this.first = first;
            this.after = after;
        }
    }
}