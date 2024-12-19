package Days.week1;

public class Day4 {
    private int parseForXOfWord(String word, char[][] grid) {

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

    public int parseForWord(String word, char[][] grid) {
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
}
