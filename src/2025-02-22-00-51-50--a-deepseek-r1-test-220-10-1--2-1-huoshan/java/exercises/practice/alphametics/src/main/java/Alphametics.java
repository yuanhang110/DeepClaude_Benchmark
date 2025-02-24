import java.util.*;
import java.math.BigInteger;

class Alphametics {

    private List<String> leftWords;
    private String rightWord;
    private List<Character> letters;
    private Set<Character> leadingLetters;

    Alphametics(String userInput) {
        String[] parts = userInput.split(" = ");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid equation format");
        }
        
        String leftPart = parts[0];
        rightWord = parts[1];
        leftWords = Arrays.asList(leftPart.split(" \\+ "));
        
        Set<Character> uniqueLetters = new HashSet<>();
        leadingLetters = new HashSet<>();
        
        // Process left words
        for (String word : leftWords) {
            if (!word.isEmpty()) {
                leadingLetters.add(word.charAt(0));
                for (char c : word.toCharArray()) {
                    uniqueLetters.add(c);
                }
            }
        }
        
        // Process right word
        if (!rightWord.isEmpty()) {
            leadingLetters.add(rightWord.charAt(0));
            for (char c : rightWord.toCharArray()) {
                uniqueLetters.add(c);
            }
        }
        
        letters = new ArrayList<>(uniqueLetters);
        Collections.sort(letters);
    }

    static class UnsolvablePuzzleException extends Exception {
        public UnsolvablePuzzleException() {
            super("No solution exists");
        }
    }

    Map<Character, Integer> solve() throws UnsolvablePuzzleException {
        Map<Character, Integer> solution = new HashMap<>();
        boolean[] usedDigits = new boolean[10];
        if (backtrack(0, solution, usedDigits)) {
            return solution;
        }
        throw new UnsolvablePuzzleException();
    }

    private boolean backtrack(int index, Map<Character, Integer> solution, boolean[] usedDigits) {
        if (index == letters.size()) {
            return validateSolution(solution);
        }
        
        char currentChar = letters.get(index);
        for (int digit = 0; digit <= 9; digit++) {
            if (usedDigits[digit]) continue;
            if (digit == 0 && leadingLetters.contains(currentChar)) continue;
            
            usedDigits[digit] = true;
            solution.put(currentChar, digit);
            
            if (backtrack(index + 1, solution, usedDigits)) {
                return true;
            }
            
            usedDigits[digit] = false;
            solution.remove(currentChar);
        }
        return false;
    }

    private boolean validateSolution(Map<Character, Integer> solution) {
        BigInteger leftSum = BigInteger.ZERO;
        BigInteger rightSum = convertToNumber(rightWord, solution);
        
        for (String word : leftWords) {
            leftSum = leftSum.add(convertToNumber(word, solution));
        }
        
        return leftSum.equals(rightSum);
    }

    private BigInteger convertToNumber(String word, Map<Character, Integer> solution) {
        BigInteger num = BigInteger.ZERO;
        for (char c : word.toCharArray()) {
            num = num.multiply(BigInteger.TEN).add(BigInteger.valueOf(solution.get(c)));
        }
        return num;
    }

}

