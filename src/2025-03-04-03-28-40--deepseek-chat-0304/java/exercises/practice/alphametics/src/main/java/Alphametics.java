import java.util.*;

class Alphametics {
    private final String equation;
    private final List<Character> letters;
    private final Set<Character> leadingLetters;
    private Map<Character, Integer> solution;

    Alphametics(String userInput) {
        this.equation = userInput.replaceAll("\\s", "");
        this.letters = extractLetters(this.equation);
        this.leadingLetters = extractLeadingLetters(this.equation);
    }

    Map<Character, Integer> solve() throws UnsolvablePuzzleException {
        solution = new HashMap<>();
        if (solve(0, new boolean[10])) {
            return solution;
        }
        throw new UnsolvablePuzzleException();
    }

    private boolean solve(int index, boolean[] usedDigits) {
        if (index == letters.size()) {
            return isValidSolution();
        }

        char currentChar = letters.get(index);
        for (int digit = 0; digit <= 9; digit++) {
            if (digit == 0 && leadingLetters.contains(currentChar)) {
                continue;
            }
            if (!usedDigits[digit]) {
                solution.put(currentChar, digit);
                usedDigits[digit] = true;
                
                if (solve(index + 1, usedDigits)) {
                    return true;
                }
                
                usedDigits[digit] = false;
                solution.remove(currentChar);
            }
        }
        return false;
    }

    private List<Character> extractLetters(String equation) {
        Set<Character> uniqueLetters = new HashSet<>();
        for (char c : equation.toCharArray()) {
            if (Character.isLetter(c)) {
                uniqueLetters.add(c);
            }
        }
        return new ArrayList<>(uniqueLetters);
    }

    private Set<Character> extractLeadingLetters(String equation) {
        Set<Character> leading = new HashSet<>();
        String[] parts = equation.split("[^A-Za-z]+");
        for (String part : parts) {
            if (!part.isEmpty()) {
                leading.add(part.charAt(0));
            }
        }
        return leading;
    }

    private boolean isValidSolution() {
        String[] parts = equation.split("==");
        if (parts.length != 2) {
            parts = equation.split("=");
        }
        
        long leftSum = Arrays.stream(parts[0].split("\\+"))
            .mapToLong(this::wordToNumber)
            .sum();
            
        long rightSum = wordToNumber(parts[1]);
        
        return leftSum == rightSum;
    }

    private long wordToNumber(String word) {
        long number = 0;
        for (char c : word.toCharArray()) {
            if (Character.isLetter(c)) {
                number = number * 10 + solution.get(c);
            }
        }
        return number;
    }
}

class UnsolvablePuzzleException extends Exception {
    public UnsolvablePuzzleException() {
        super("No solution exists for this puzzle");
    }
}
