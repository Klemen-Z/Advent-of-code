package Days.week1;

import java.util.ArrayList;

public class Day5 {
    public int addMiddleNumbers(ArrayList<ArrayList<Integer>> correctIntArrs) {
        int count = 0;

        for (ArrayList<Integer> arr : correctIntArrs) {
            count += arr.get((arr.size()/2));
        }

        return count;
    }

    public ArrayList<ArrayList<Integer>> fixOrder(ArrayList<ArrayList<Integer>> arrs, ArrayList<RuleSet> rules) {
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

    public ArrayList<ArrayList<Integer>> getWronglyOrderedArrs(ArrayList<ArrayList<Integer>> arrs, ArrayList<RuleSet> rules) {
        ArrayList<ArrayList<Integer>> intArrs = new ArrayList<>(arrs);

        ArrayList<ArrayList<Integer>> correctArrs = getCorrectlyOrderedArrs(intArrs, rules);
        intArrs.removeAll(correctArrs);

        return intArrs;
    }

    public boolean checkRules(ArrayList<Integer> arr, ArrayList<RuleSet> rules) {
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

    public ArrayList<ArrayList<Integer>> getCorrectlyOrderedArrs(ArrayList<ArrayList<Integer>> arrs, ArrayList<RuleSet> rules) {
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

    public ArrayList<ArrayList<Integer>> compileArrays(String str) {
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

    public ArrayList<RuleSet> compileRules(String str){
        ArrayList<RuleSet> ruleSets = new ArrayList<>();
        String[] rules = str.replace("|", "\n").split("\n");
        for (int i = 0; i < rules.length; i++) {
            if (i%2 == 0){
                ruleSets.add(new RuleSet(Integer.parseInt(rules[i]), Integer.parseInt(rules[(i+1)])));
            }
        }
        return ruleSets;
    }

    public class RuleSet{
        int first;
        int after;

        RuleSet(int first, int after){
            this.first = first;
            this.after = after;
        }
    }
}
