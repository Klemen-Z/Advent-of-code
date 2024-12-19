import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String[] nums = readFile("nums.txt").replace("\r", "").split("\n");

        ArrayList<ArrayList<Integer>> list1 = new ArrayList<>();

        for (String string : nums) {
            String[] num = string.split(" ");
            ArrayList<Integer> list = new ArrayList<>();
            for (String s : num) {
                list.add(Integer.parseInt(s));
            }
            list1.add(list);
        }

        System.out.println(getSafetyCount(list1));
    }

    private static int getSafetyCount(ArrayList<ArrayList<Integer>> list) {
        int finalCount = 0;

        for (ArrayList<Integer> list1 : list){
            boolean safe = true;
            int prevOp = 0;
            int firstBad = -1;

            for (int i = 0; i < list1.size()-1; i++) {
                int difference = list1.get(i)-list1.get(i+1);
                if (isSafe(difference, prevOp)) {
                    safe = false;
                    firstBad = i+1;
                    break;
                } else {
                    if (prevOp == 0){
                        prevOp = difference;
                    }
                }
            }

            if (firstBad != -1) {
                list1.remove(firstBad);
                safe = true;

                for (int i = 0; i < list1.size()-1; i++) {
                    int difference = list1.get(i)-list1.get(i+1);
                    if (isSafe(difference, prevOp)) {
                        safe = false;
                        break;
                    }
                }
            }

            if (safe) {
                finalCount++;
            }
        }

        return finalCount;
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