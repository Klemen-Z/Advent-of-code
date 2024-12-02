import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        File f = new File("nums.txt");
        assert f.exists();

        FileReader fr;

        try {
            fr = new FileReader(f);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        StringBuilder sb = new StringBuilder();

        while (true) {
            int res = -1;
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

        String[] nums = sb.toString().replace("   ", "\n").replace("\r", "").split("\n");

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
        list2.sort(Integer::compareTo);
        list1.sort(Integer::compareTo);

        for (int i = 0; i < list1.size(); i++) {
            int tempVal = 0;
            tempVal = list1.get(i) - list2.get(i);
            if (tempVal < 0) {
                tempVal = list2.get(i) - list1.get(i);
            }
            finalResult += tempVal;
        }

        System.out.println(finalResult);
    }
}