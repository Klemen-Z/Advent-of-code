import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        final long start = System.currentTimeMillis();
        Main main = new Main();
        String input = main.readFile("input.txt");

        ArrayList<FileBlock> inputWSpacing = main.calculateSpacing(input);
        ArrayList<FileBlock> fixedInput = main.moveBlocks(inputWSpacing);
        System.out.println(main.calculateChecksum(fixedInput));

        final long end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start) + " ms");
    }

    //day 9 methods below
    private long calculateChecksum(ArrayList<FileBlock> input) {
        long sum = 0;

        for (int i = 0; i < input.size(); i++) {
            sum += ((long)input.get(i).id*i);
        }

        return sum;
    }

    private ArrayList<FileBlock> moveBlocks(ArrayList<FileBlock> input) {
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

    private ArrayList<FileBlock> calculateSpacing(String input) {
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

    //day 8 methods below
    private Set<Coordinates> locateAllAntinodes(Map<Character, ArrayList<Coordinates>> antennaLocations, int gridMax, boolean part1) {
        Set<Coordinates> allAntinodes = Collections.synchronizedSet(new HashSet<>());

        antennaLocations.keySet().forEach(key -> {
            ArrayList<Coordinates> antennas = antennaLocations.get(key);
            antennas.parallelStream().forEach(c1 ->{
                antennas.parallelStream().forEach(c2 -> {
                    if (c1.equals(c2)){
                        return;
                    }

                    int xDistance = c1.x-c2.x;
                    int yDistance = c1.y-c2.y;

                    int tempXDist = 0;
                    int tempYDist = 0;

                    if (part1){
                        Coordinates combo = new Coordinates(c1.y+yDistance, c1.x+xDistance);
                        if (combo.x < gridMax && combo.y < gridMax && combo.x >= 0 && combo.y >= 0){
                            allAntinodes.add(combo);
                        }
                        return;
                    }

                    while(true){
                        Coordinates combo = new Coordinates(c1.y+tempYDist, c1.x+tempXDist);
                        if (combo.x < gridMax && combo.y < gridMax && combo.x >= 0 && combo.y >= 0){
                            allAntinodes.add(combo);
                        } else {
                            break;
                        }
                        tempXDist += xDistance;
                        tempYDist += yDistance;
                    }
                });
            });
        });

        return filterOverlaps(allAntinodes);
    }

    private HashSet<Coordinates> filterOverlaps(Set<Coordinates> set) {
        HashSet<Coordinates> returnValue = new HashSet<>();

        for (Coordinates c1 : set) {
            boolean tbA = true;
            for (Coordinates c2 : returnValue) {
                if (c2.x == c1.x && c2.y == c1.y) {
                    tbA = false;
                    break;
                }
            }
            if (tbA) {
                returnValue.add(c1);
            }
        }

        return returnValue;
    }

    private Map<Character, ArrayList<Coordinates>> locateAllAntennas(char[][] grid, HashSet<Character> frequencies){
        Map<Character, ArrayList<Coordinates>> map = Collections.synchronizedMap(new HashMap<>());

        frequencies.parallelStream().forEach(frequency -> {
            ArrayList<Coordinates> coordinates = new ArrayList<>();
            for(int i = 0; i < grid.length; i++){
                for(int j = 0; j < grid[0].length; j++){
                    if(grid[i][j] == frequency){
                        coordinates.add(new Coordinates(i, j));
                    }
                }
            }
            map.put(frequency, coordinates);
        });

        return map;
    }

    private HashSet<Character> getFrequencies(String input) {
        String tempStr = input;
        String[] tempArr = tempStr.replace("\r", "").replace("\n", "")
                .replace(".", "").split("");

        HashSet<Character> frequencies = new HashSet<>();

        for (String s : tempArr) {
            frequencies.add(s.charAt(0));
        }

        return frequencies;
    }

    //day 7 methods below
    private Long sumAllResults(Set<Long> results){
        AtomicLong sum = new AtomicLong(0);

        results.parallelStream().forEach(sum::addAndGet);

        return sum.get();
    }

    private Set<Long> findTrueEquations(HashMap<Long, ArrayList<Long>> possibleEquations) {
        Set<Long> results = Collections.synchronizedSet(new HashSet<>());

        possibleEquations.keySet().parallelStream().forEach(key -> {
            ArrayList<Long> value = possibleEquations.get(key);

            HashSet<List<Character>> operationsList = generatePossibleOperands(value.size());

            operationsList.parallelStream().forEach(operationSequence -> {
                long result = value.getFirst();
                for (int i = 1; i < value.size(); i++) {
                    result = doOperation(result, value.get(i), operationSequence.get(i-1));
                }

                if (result == key) {
                    results.add(key);
                }
            });
        });

        return results;
    }

    private HashSet<List<Character>> generatePossibleOperands(int len){
        HashSet<List<Character>> sequenceSet = new HashSet<>();

        //for part 1 of day seven remove the '|' from the char array being passed to the recursive function.
        generateCharacterCombinationsRecursive(new char[]{'+', '*', '|'}, len-1, new ArrayList<>(), sequenceSet);

        return sequenceSet;
    }

    private void generateCharacterCombinationsRecursive(char[] array, int length, List<Character> current, HashSet<List<Character>> results) {
        if (current.size() >= length) {
            results.add(new ArrayList<>(current));
            return;
        }

        for (char c : array) {
            current.add(c);
            generateCharacterCombinationsRecursive(array, length, current, results);
            current.removeLast(); // Backtrack
        }
    }

    private Long doOperation(Long num1, Long num2, char operand){
        return switch (operand) {
            case '+' -> num1 + num2;
            case '*' -> num1 * num2;
            case '|' -> Long.parseLong((num1+""+num2));
            default -> throw new RuntimeException("Unknown operation: " + operand);
        };
    }

    private HashMap<Long, ArrayList<Long>> getPossibleEquations(String input) {
        HashMap<Long, ArrayList<Long>> possibleEquations = new HashMap<>();

        String[] arr = input.replace("\r", "").split("\n");

        for (String s : arr) {
            String[] tempArr = s.split(": ");
            Long key = Long.parseLong(tempArr[0]);
            ArrayList<Long> value = new ArrayList<>();

            tempArr = tempArr[1].split(" ");
            for (String str : tempArr) {
                value.add(Long.parseLong(str));
            }
            possibleEquations.put(key, value);
        }

        return possibleEquations;
    }

    //Day 6 methods below
    private int obstacleLoopPositionCount(char[][] grid) {
        List<ArrayList<Integer>> possiblePositions = Collections.synchronizedList(new ArrayList<>());

        int[] guardPosition = getGuardPosition(grid);
        Set<ArrayList<Integer>> guardCoordinates = Collections.synchronizedSet(new HashSet<>());
        guardCoordinates.addAll(getGuardUniquePositions(grid, guardPosition));

        guardCoordinates.forEach(guardCoordinate ->{
            char[][] tempGrid = grid.clone();
            int[] tempGuardPos = guardPosition.clone();

            int x = guardCoordinate.get(0);
            int y = guardCoordinate.get(1);

            if (x == tempGuardPos[0] && y == tempGuardPos[1]) {
                return;
            }

            tempGrid[y][x] = '#';
            if (uniqueGuardMoveCount(tempGrid, tempGuardPos) == -1) possiblePositions.add(guardCoordinate);
            tempGrid[y][x] = '.';
        });

        return possiblePositions.size();
    }

    private int uniqueGuardMoveCount(char[][] grid) {
        return uniqueGuardMoveCount(grid, getGuardPosition(grid));
    }

    private HashSet<ArrayList<Integer>> getGuardUniquePositions(char[][] grid, int[] guardPosition) {
        HashSet<ArrayList<Integer>> uniquePositions = new HashSet<>();
        boolean isOnMap = true;
        int guardX = guardPosition[0], guardY = guardPosition[1];
        String direction = "^";
        int ignoredCount = 0;

        if (guardX < 0) {
            return uniquePositions;
        }

        while (true) {
            switch (direction) {
                case "^" -> {
                    String symbol = "";
                    try {
                        symbol = grid[guardY-1][guardX]+"";
                    } catch (IndexOutOfBoundsException e) {
                        isOnMap = false;
                    }

                    if (!isOnMap){
                        break;
                    }

                    if (symbol.equals("#")) {
                        direction = ">";
                    } else {
                        guardY--;
                    }
                }
                case ">" -> {
                    String symbol = "";
                    try {
                        symbol = grid[guardY][guardX+1]+"";
                    } catch (IndexOutOfBoundsException e) {
                        isOnMap = false;
                    }

                    if (!isOnMap){
                        break;
                    }

                    if (symbol.equals("#")) {
                        direction = "v";
                    } else {
                        guardX++;
                    }
                }
                case "v" -> {
                    String symbol = "";
                    try {
                        symbol = grid[guardY+1][guardX]+"";
                    } catch (IndexOutOfBoundsException e) {
                        isOnMap = false;
                    }

                    if (!isOnMap){
                        break;
                    }

                    if (symbol.equals("#")) {
                        direction = "<";
                    } else {
                        guardY++;
                    }
                }
                case "<" -> {
                    String symbol = "";
                    try {
                        symbol = grid[guardY][guardX-1]+"";
                    } catch (IndexOutOfBoundsException e) {
                        isOnMap = false;
                    }

                    if (!isOnMap){
                        break;
                    }

                    if (symbol.equals("#")) {
                        direction = "^";
                    } else {
                        guardX--;
                    }
                }

                default -> throw new RuntimeException("unexpected direction: " + direction);
            }

            if (!isOnMap) {
                break;
            }

            ArrayList<Integer> temp = new ArrayList<>();
            temp.add(guardX);
            temp.add(guardY);

            if(!uniquePositions.add(temp)) ignoredCount++;
            if (ignoredCount == (uniquePositions.size()*2)) break;
        }

        return uniquePositions;
    }

    private int[] getGuardPosition(char[][] grid) {
        int[] guardPosition = new int[2];

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == '^'||grid[i][j] == 'v'||grid[i][j] == '>'||grid[i][j] == '<') {
                    guardPosition[0] = j;
                    guardPosition[1] = i;
                }
            }
        }

        return guardPosition;
    }

    private int uniqueGuardMoveCount(char[][] grid, int[] guardPosition){
        char[][] tempGrid = grid.clone();
        boolean isOnMap = true;
        int guardX = guardPosition[0], guardY = guardPosition[1];
        HashSet<ArrayList<Integer>> previousPositions = new HashSet<>();
        String direction = "^";
        int ignoredCount = 0;

        if (guardX < 0) {
            return 0;
        }

        while (true) {
            switch(direction){
                case "^" -> {
                    String symbol = "";
                    try {
                        symbol = tempGrid[guardY-1][guardX]+"";
                    } catch (IndexOutOfBoundsException e) {
                        isOnMap = false;
                    }

                    if (!isOnMap){
                        break;
                    }

                    if (symbol.equals("#")) {
                        direction = ">";
                    } else {
                        guardY--;
                    }
                }
                case ">" -> {
                    String symbol = "";
                    try {
                        symbol = tempGrid[guardY][guardX+1]+"";
                    } catch (IndexOutOfBoundsException e) {
                        isOnMap = false;
                    }

                    if (!isOnMap){
                        break;
                    }

                    if (symbol.equals("#")) {
                        direction = "v";
                    } else {
                        guardX++;
                    }
                }
                case "v" -> {
                    String symbol = "";
                    try {
                        symbol = tempGrid[guardY+1][guardX]+"";
                    } catch (IndexOutOfBoundsException e) {
                        isOnMap = false;
                    }

                    if (!isOnMap){
                        break;
                    }

                    if (symbol.equals("#")) {
                        direction = "<";
                    } else {
                        guardY++;
                    }
                }
                case "<" -> {
                    String symbol = "";
                    try {
                        symbol = tempGrid[guardY][guardX-1]+"";
                    } catch (IndexOutOfBoundsException e) {
                        isOnMap = false;
                    }

                    if (!isOnMap){
                        break;
                    }

                    if (symbol.equals("#")) {
                        direction = "^";
                    } else {
                        guardX--;
                    }
                }

                default -> throw new RuntimeException("unexpected direction: " + direction);
            }

            if (!isOnMap) {
                break;
            }

            ArrayList<Integer> temp = new ArrayList<>();
            temp.add(guardX);
            temp.add(guardY);

            if(!previousPositions.add(temp)) ignoredCount++;
            if (ignoredCount == (previousPositions.size()*2)) return -1;
        }

        return previousPositions.size();
    }

    //Day 5 methods below
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

    //Day 4 methods below
    private int parseForXOfWord(String word,String input) {
        char[][] grid = createGrid(input);

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

    private int parseForWord(String word, String input) {
        char[][] grid = createGrid(input);

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

    //Day 3 methods below
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

    //Day 2 methods below
    private int getSafetyCount(ArrayList<ArrayList<Integer>> list, boolean part1) {
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

    private boolean isSafe(int difference, int prevOp) {
        return  difference > 0 && prevOp < 0 || difference < 0 && prevOp > 0 || difference > 3 || difference < -3 || difference == 0;
    }

    //Day 1 methods below
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

    //util functions below
    private String readFile(String filename) {
        File f = new File(filename);
        assert f.exists();

        FileReader fr;
        StringBuilder sb = new StringBuilder();

        try {
            fr = new FileReader(f);

            while (true) {
                int res = fr.read();
                if (res == -1) {
                    break;
                }
                sb.append((char) res);
            }

            fr.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return sb.toString();
    }

    private char[][] createGrid(String input) {
        String[] strArr = input.replace("\r", "").split("\n");
        char[][] grid = new char[strArr.length][];
        for (int i = 0; i < strArr.length; i++) {
            grid[i] = strArr[i].toCharArray();
        }
        return grid;
    }

    private void printGrid(char[][] grid) {
        System.out.println("Grid: ");
        for(char[] line : grid){
            System.out.println(Arrays.toString(line));
        }
        System.out.println();
    }

    private class RuleSet{
        int first;
        int after;

        RuleSet(int first, int after){
            this.first = first;
            this.after = after;
        }
    }

    private class Coordinates{
        int x;
        int y;

        Coordinates(int y, int x){
            this.x = x;
            this.y = y;
        }
    }

    private class FileBlock{
        int id;

        FileBlock(int id){
            this.id = id;
        }
    }
}