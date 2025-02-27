import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

class Alphametics {

    public static class UnsolvablePuzzleException extends Exception {
        public UnsolvablePuzzleException() {
            super("No solution exists.");
        }
    }

    private final List<String> leftOperands;
    private final String rightOperand;
    private final Set<Character> leadingLetters;
    private final List<Character> uniqueLetters;

    Alphametics(String userInput) {
        // Split input into left and right parts
        String[] parts = userInput.split("=");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Equation must have exactly one '='");
        }

        String[] leftParts = parts[0].split("\\+");
        leftOperands = Arrays.stream(leftParts)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        rightOperand = parts[1].trim();

        if (leftOperands.isEmpty()) {
            throw new IllegalArgumentException("Must have at least one operand on left side");
        }

        // Collect all unique letters and leading letters
        leadingLetters = new HashSet<>();
        Set<Character> allLetters = new HashSet<>();

        for (String operand : leftOperands) {
            if (operand.isEmpty()) {
                throw new IllegalArgumentException("Operand cannot be empty");
            }
            leadingLetters.add(operand.charAt(0));
            for (char c : operand.toCharArray()) {
                allLetters.add(c);
            }
        }

        if (rightOperand.isEmpty()) {
            throw new IllegalArgumentException("Right operand cannot be empty");
        }
        leadingLetters.add(rightOperand.charAt(0));
        for (char c : rightOperand.toCharArray()) {
            allLetters.add(c);
        }

        uniqueLetters = new ArrayList<>(allLetters);
        if (uniqueLetters.size() > 10) {
            throw new IllegalArgumentException("Too many unique letters");
        }
        // Sort unique letters so that leading letters come first
        Collections.sort(uniqueLetters, (a, b) -> {
            boolean aLeading = leadingLetters.contains(a);
            boolean bLeading = leadingLetters.contains(b);
            if (aLeading == bLeading) return Character.compare(a, b);
            return aLeading ? -1 : 1;
        });
    }

    Map<Character, Integer> solve() throws UnsolvablePuzzleException {
        Map<Character, Integer> solution = new HashMap<>();
        boolean[] usedDigits = new boolean[10];
        
        if (backtrack(solution, usedDigits, 0)) {
            return Collections.unmodifiableMap(solution);
        } else {
            throw new UnsolvablePuzzleException();
        }
    }

    private boolean backtrack(Map<Character, Integer> solution, boolean[] usedDigits, int index) {
        if (index == uniqueLetters.size()) {
            return isValid(solution);
        }

        char currentChar = uniqueLetters.get(index);
        for (int digit = 0; digit < 10; digit++) {
            if (usedDigits[digit]) continue;
            if (digit == 0 && leadingLetters.contains(currentChar)) continue;

            usedDigits[digit] = true;
            solution.put(currentChar, digit);
            if (backtrack(solution, usedDigits, index + 1)) {
                return true;
            }
            solution.remove(currentChar);
            usedDigits[digit] = false;
        }
        return false;
    }

    private boolean isValid(Map<Character, Integer> solution) {
        long leftSum = 0;
        for (String operand : leftOperands) {
            leftSum += toNumber(operand, solution);
            if (leftSum < 0) { // Check for overflow
                return false;
            }
        }
        long rightNum = toNumber(rightOperand, solution);
        return leftSum == rightNum;
    }

    private long toNumber(String word, Map<Character, Integer> solution) {
        long num = 0;
        for (char c : word.toCharArray()) {
            num = num * 10 + solution.get(c);
            if (num < 0) { // Check for overflow
                return -1;
            }
        }
        return num;
    }
}
