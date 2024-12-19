package Days;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Day7 {
    public Long sumAllResults(Set<Long> results){
        AtomicLong sum = new AtomicLong(0);

        results.parallelStream().forEach(sum::addAndGet);

        return sum.get();
    }

    public Set<Long> findTrueEquations(HashMap<Long, ArrayList<Long>> possibleEquations) {
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

    public HashSet<List<Character>> generatePossibleOperands(int len){
        HashSet<List<Character>> sequenceSet = new HashSet<>();

        //for part 1 of day seven remove the '|' from the char array being passed to the recursive function.
        generateCharacterCombinationsRecursive(new char[]{'+', '*', '|'}, len-1, new ArrayList<>(), sequenceSet);

        return sequenceSet;
    }

    public void generateCharacterCombinationsRecursive(char[] array, int length, List<Character> current, HashSet<List<Character>> results) {
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

    public Long doOperation(Long num1, Long num2, char operand){
        return switch (operand) {
            case '+' -> num1 + num2;
            case '*' -> num1 * num2;
            case '|' -> Long.parseLong((num1+""+num2));
            default -> throw new RuntimeException("Unknown operation: " + operand);
        };
    }

    public HashMap<Long, ArrayList<Long>> getPossibleEquations(String input) {
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
}
