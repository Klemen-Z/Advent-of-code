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
        String input = readFile("input.txt");
        String word = "xmas";
        System.out.println("Found '" + word + "' " + parseForWord(word, input.toLowerCase().replaceAll("\r", "").split("\n")));
        final long end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start) + " ms");
    }

    private static int parseForWord(String word, String[] input) {
        char[][] grid = new char[input.length][];
        for (int i = 0; i < input.length; i++) {
            grid[i] = input[i].toCharArray();
        }

        int count = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                int nums = 0;
                int forwardNum = 0;
                int reverseNum = 0;

                //regular
                while(nums < word.length()){
                    int[] prevVals = new int[]{forwardNum, reverseNum};

                    try{
                        if (grid[i][j+nums] == word.charAt(nums)) forwardNum++;
                    } catch (IndexOutOfBoundsException ignored){}

                    try{
                        if (grid[i][j-nums] == word.charAt(nums)) reverseNum++;
                    } catch (IndexOutOfBoundsException ignored){}

                    if (forwardNum == word.length()) count++;
                    if (reverseNum == word.length()) count++;
                    if (reverseNum == prevVals[1] && prevVals[0] == forwardNum) break;
                    nums++;
                }

                nums = 0;
                forwardNum = 0;
                reverseNum = 0;

                //vertical
                while(nums < word.length()){
                    int[] prevVals = new int[]{forwardNum, reverseNum};

                    try{
                        if (grid[i+nums][j] == word.charAt(nums)) forwardNum++;
                    } catch (IndexOutOfBoundsException ignored){}

                    try{
                        if (grid[i-nums][j] == word.charAt(nums)) reverseNum++;
                    } catch (IndexOutOfBoundsException ignored){}

                    if (forwardNum == word.length()) count++;
                    if (reverseNum == word.length()) count++;
                    if (reverseNum == prevVals[1] && prevVals[0] == forwardNum) break;
                    nums++;
                }

                nums = 0;
                forwardNum = 0;
                reverseNum = 0;

                //diagonal /
                while(nums < word.length()){
                    int[] prevVals = new int[]{forwardNum, reverseNum};

                    try{
                        if (grid[i+nums][j+nums] == word.charAt(nums)) forwardNum++;
                    } catch (IndexOutOfBoundsException ignored){}

                    try{
                        if (grid[i+nums][j-nums] == word.charAt(nums)) reverseNum++;
                    } catch (IndexOutOfBoundsException ignored){}

                    if (forwardNum >= word.length()) count++;
                    if (reverseNum >= word.length()) count++;
                    if (reverseNum == prevVals[1] && prevVals[0] == forwardNum) break;
                    nums++;
                }

                nums = 0;
                forwardNum = 0;
                reverseNum = 0;

                //diagonal \
                while(nums < word.length()){
                    int[] prevVals = new int[]{forwardNum, reverseNum};

                    try{
                        if (grid[i-nums][j+nums] == word.charAt(nums)) forwardNum++;
                    } catch (IndexOutOfBoundsException ignored){}

                    try{
                        if (grid[i-nums][j-nums] == word.charAt(nums)) reverseNum++;
                    } catch (IndexOutOfBoundsException ignored){}

                    if (forwardNum == word.length()) count++;
                    if (reverseNum == word.length()) count++;
                    if (reverseNum == prevVals[1] && prevVals[0] == forwardNum) break;
                    nums++;
                }
            }
        }
        return count;
    }

    private static int multiplyAllThenAdd(ArrayList<String[]> nums){
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

    private static String regexMatch(String input) {
        StringBuilder returnVal = new StringBuilder();

        Pattern pattern = Pattern.compile("(mul\\([0-9]{1,3},[0-9]{1,3}\\))|(do\\(\\))|(don't\\(\\))");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            returnVal.append(matcher.group());
        }

        return returnVal.toString();
    }

    private static ArrayList<String[]> ignoreDoDont(String input){
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

    private static ArrayList<String[]> handleDoDont(String input){
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

    private static String[] convertMulInstructions(String input) {
        return input.replace("m", " ").replace("u", "").replace("l", "").replace("(", "").replace(")", "").replace(",", " ").split(" ");
    }

    private static int getSafetyCount(ArrayList<ArrayList<Integer>> list) {
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

    private static boolean checkList(List<Integer> list) {
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

    private static boolean isSafe(int difference, int prevOp) {
        return  difference > 0 && prevOp < 0 || difference < 0 && prevOp > 0 || difference > 3 || difference < -3 || difference == 0;
    }

    private static int getSimilarity(ArrayList<Integer> list1, ArrayList<Integer> list2) {
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

    private static int getTotalDifference(ArrayList<Integer> list1, ArrayList<Integer> list2) {
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

    private static String readFile(String filename) {
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
}