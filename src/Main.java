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
        String input = readFile("input.txt");

        String word = "xmas";

        final long start = System.currentTimeMillis();
        System.out.println("Found '" + word + "' " + parseForWord(word, input.toLowerCase().replaceAll("\r", "").split("\n")));
        final long end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start) + " ms");
    }

    private static int parseForWord(String word, String[] input) {
        char[][] grid = new char[input.length][];
        for(int i = 0; i < input.length; i++) {
            grid[i] = input[i].toCharArray();
        }

        ArrayList<String> strings = new ArrayList<>();

        strings.addAll(straightBuild(grid));
        strings.addAll(verticalBuild(grid));
        strings.addAll(diagonalBuild(grid));

        return SearchStringsForWord(word, strings);
    }

    private static int SearchStringsForWord(String word, ArrayList<String> input) {
        int finalCount = 0;
        for (String s : input) {
            if (s.length() < word.length()) continue;
            String tempStr = s.toLowerCase();

            finalCount += regexCount(tempStr, word);
        }

        return finalCount;
    }

    private static ArrayList<String> straightBuild(char[][] grid) {
        char[][] tempGrid = grid.clone();
        ArrayList<String> strings = new ArrayList<>();

        for (char[] s : tempGrid) {
            String tempString = String.valueOf(s);
            strings.add(tempString);
            strings.add(reverseWord(tempString));
        }
        return strings;
    }

    private static ArrayList<String> verticalBuild(char[][] grid) {
        char[][] tempGrid = grid.clone();
        ArrayList<String> strings = new ArrayList<>();

        for (int i = 0; i < (tempGrid[0].length); i++) {
            StringBuilder tempString = new StringBuilder();
            for (char[] chars : tempGrid) {
                tempString.append(chars[i]);
            }
            strings.add(tempString.toString());
            strings.add(reverseWord(tempString.toString()));
        }

        return strings;
    }

    private static ArrayList<String> diagonalBuild(char[][] grid) {
        char[][] tempGrid = grid.clone();
        ArrayList<String> strings = new ArrayList<>();

        for (int i = 0; i < (tempGrid[0].length); i++) {
            StringBuilder tempString = new StringBuilder();
            int tempNum = 0;
            for (int j = tempGrid.length-1; j >= 0; j--) {
                if (i+tempNum >= tempGrid[j].length) {break;}
                tempString.append(tempGrid[j][i + tempNum]);
                tempNum++;
            }
            strings.add(tempString.toString());
            strings.add(reverseWord(tempString.toString()));
        }

        for (int i = (tempGrid[tempGrid.length-1].length-1); i >= 0; i--) {
            StringBuilder tempString = new StringBuilder();
            int tempNum = 0;

            for (int j = tempGrid.length-1; j >= 0; j--) {
                if (i+tempNum >= tempGrid[j].length) {break;}
                tempString.append(tempGrid[j][i + tempNum]);
                tempNum++;
            }
            strings.add(tempString.toString());
            strings.add(reverseWord(tempString.toString()));
        }

        return strings;
    }

    private static String reverseWord(String word) {
        return new StringBuilder(word).reverse().toString();
    }

    private static int regexCount(String str, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str.toLowerCase());
        int count = 0;

        while (matcher.find()) {
            count++;
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