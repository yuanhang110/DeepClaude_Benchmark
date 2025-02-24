import java.util.*;

class Alphametics {
    private List<String> leftParts;
    private String rightPart;
    private List<Character> letters;
    private Set<Character> leadingLetters;

    Alphametics(String userInput) {
        String[] parts = userInput.split(" = ");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid equation format");
        }
        rightPart = parts[1];
        leftParts = Arrays.asList(parts[0].split(" \\+ "));
        
        Set<Character> uniqueLetters = new LinkedHashSet<>();
        leadingLetters = new HashSet<>();
        
        for (String word : leftParts) {
            if (!word.isEmpty()) {
                leadingLetters.add(word.charAt(0));
                for (char c : word.toCharArray()) {
                    uniqueLetters.add(c);
                }
            }
        }
        
        if (!rightPart.isEmpty()) {
            leadingLetters.add(rightPart.charAt(0));
            for (char c : rightPart.toCharArray()) {
                uniqueLetters.add(c);
            }
        }
        
        letters = new ArrayList<>(uniqueLetters);
    }

    Map<Character, Integer> solve() throws UnsolvablePuzzleException {
        if (letters.size() > 10) {
            throw new UnsolvablePuzzleException();
        }
        
        Map<Character, Integer> solution = new HashMap<>();
        boolean[] used = new boolean[10];
        
        if (backtrack(solution, used, 0)) {
            return solution;
        }
        throw new UnsolvablePuzzleException();
    }
    
    private boolean backtrack(Map<Character, Integer> solution, boolean[] used, int index) {
        if (index == letters.size()) {
            return isValid(solution);
        }
        
        char c = letters.get(index);
        for (int d = 0; d < 10; d++) {
            if (!used[d]) {
                if (d == 0 && leadingLetters.contains(c)) {
                    continue;
                }
                solution.put(c, d);
                used[d] = true;
                if (backtrack(solution, used, index + 1)) {
                    return true;
                }
                used[d] = false;
                solution.remove(c);
            }
        }
        return false;
    }
    
    private boolean isValid(Map<Character, Integer> assignment) {
        long leftSum = 0;
        for (String word : leftParts) {
            long num = toNumber(word, assignment);
            if (num == -1) return false;
            leftSum += num;
        }
        long rightNum = toNumber(rightPart, assignment);
        return rightNum != -1 && leftSum == rightNum;
    }
    
    private long toNumber(String word, Map<Character, Integer> assignment) {
        long num = 0;
        for (char c : word.toCharArray()) {
            Integer digit = assignment.get(c);
            if (digit == null) return -1;
            num = num * 10 + digit;
        }
        return num;
    }

    static class UnsolvablePuzzleException extends Exception {
        public UnsolvablePuzzleException() {
            super("No solution exists");
        }
    }
}
