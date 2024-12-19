package Days.week2;

import java.util.ArrayList;

public class Day9 {
    public void solve(String input, boolean part1){
        ArrayList<FileBlock> inputWSpacing = calculateSpacing(input);
        long checksum;

        if (part1){
            ArrayList<FileBlock> fixedInput = moveBlocks(inputWSpacing);
            checksum = calculateChecksumSingles(fixedInput);
        } else {
            ArrayList<FileBlockGroup> fileBlockGroups = calculateFileBlockGroups(inputWSpacing);
            ArrayList<FileBlockGroup> fixedInput = moveWholeBlocks(fileBlockGroups);
            checksum = calculateChecksumGroups(fixedInput);
        }

        System.out.println("The checksum is: " + checksum);
    }

    public long calculateChecksumSingles(ArrayList<FileBlock> input) {
        long sum = 0;

        for (int i = 0; i < input.size(); i++) {
            if (input.get(i).id == -1){
                continue;
            }
            sum += ((long)input.get(i).id*i);
        }

        return sum;
    }

    public long calculateChecksumGroups(ArrayList<FileBlockGroup> input) {
        long sum = 0;

        int position = 0;

        for (FileBlockGroup fileBlockGroup : input) {
            if (fileBlockGroup.id == -1) {
                position += fileBlockGroup.size;
                continue;
            }
            for (int j = 0; j < fileBlockGroup.size; j++) {
                sum += ((long) fileBlockGroup.id * position);
                position++;
            }
        }

        return sum;
    }

    public ArrayList<FileBlockGroup> moveWholeBlocks(ArrayList<FileBlockGroup> input) {
        int currentID;
        int index = input.size()-1;

        do {
            currentID = input.get(index).id;
            index--;
        } while (currentID == -1);

        while(true){
            if (currentID == 0) {
                return input;
            }

            int index1 = input.size()-1;
            int index2 = 0;

            while (true) {
                if (index1 < 0 || index2 >= index1) {
                    break;
                }

                FileBlockGroup fileBlockGroup1 = input.get(index1);

                if (fileBlockGroup1.id != currentID) {
                    index1--;
                    continue;
                }

                if (index2 > input.size()-1) {
                    break;
                }

                FileBlockGroup fileBlockGroup2 = input.get(index2);

                if (fileBlockGroup2.id == -1 && fileBlockGroup2.size >= fileBlockGroup1.size) {
                    input.set(index1, new FileBlockGroup(-1, fileBlockGroup1.size));
                    input.add(index2, fileBlockGroup1);
                    fileBlockGroup2.size -= fileBlockGroup1.size;
                    break;
                }

                index2++;
            }
            currentID--;
        }
    }

    public ArrayList<FileBlockGroup> calculateFileBlockGroups(ArrayList<FileBlock> input) {
        ArrayList<FileBlockGroup> result = new ArrayList<>();

        int count = 0;
        int prevID = -1;

        for (FileBlock fileBlock : input) {
            if (prevID != fileBlock.id) {
                result.add(new FileBlockGroup(prevID, count));
                prevID = fileBlock.id;
                count = 0;
            }
            count++;
        }
        result.add(new FileBlockGroup(prevID, count));
        result.removeFirst();

        return result;
    }

    public ArrayList<FileBlock> moveBlocks(ArrayList<FileBlock> input) {
        ArrayList<FileBlock> returnVal = new ArrayList<>();
        int index1 = input.size()-1;
        int index2 = 0;

        while (index2 < input.size() && index1 >= index2) {
            if (input.get(index2).id != -1) {
                returnVal.add(input.get(index2));
                index2++;
            } else {
                while (input.get(index1).id == -1) {
                    index1--;
                }
                returnVal.add(input.get(index1));
                index1--;
                index2++;
            }
        }

        return returnVal;
    }

    public ArrayList<FileBlock> calculateSpacing(String input) {
        int id = 0;
        ArrayList<FileBlock> returnVal = new ArrayList<>();

        for (int i = 0; i < input.length(); i++) {
            if (i%2 == 0){
                for (int j = 0; j < Integer.parseInt(input.substring(i, i+1)); j++) {
                    returnVal.add(new FileBlock(id));
                }
                id++;
            } else {
                for (int j = 0; j < Integer.parseInt(input.substring(i, i+1)); j++) {
                    returnVal.add(new FileBlock(-1));
                }
            }
        }
        return returnVal;
    }

    public class FileBlock{
        int id;

        FileBlock(int id){
            this.id = id;
        }
    }

    public class FileBlockGroup{
        int id;
        int size;

        FileBlockGroup(int id, int size){
            this.id = id;
            this.size = size;
        }
    }
}
