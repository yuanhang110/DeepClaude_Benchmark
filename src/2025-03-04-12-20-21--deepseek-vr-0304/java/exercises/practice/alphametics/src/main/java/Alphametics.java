import java.util.*;
import java.util.stream.Collectors;

class Alphametics {
    private final String equation;
    private final Set<Character> letters;
    private final List<String> leftTerms;
    private final String rightTerm;
    private final Set<Character> leadingLetters;

    Alphametics(String userInput) {
        this.equation = userInput;
        String[] parts = userInput.split("\\s*=\\s*");
        this.rightTerm = parts[1];
        this.leftTerms = Arrays.stream(parts[0].split("\\s*\\+\\s*"))
                              .collect(Collectors.toList());
        
        this.letters = new HashSet<>();
        this.leadingLetters = new HashSet<>();
        
        for (String term : leftTerms) {
            leadingLetters.add(term.charAt(0));
            for (char c : term.toCharArray()) {
                letters.add(c);
            }
        }
        leadingLetters.add(rightTerm.charAt(0));
        for (char c : rightTerm.toCharArray()) {
            letters.add(c);
        }
    }

    Map<Character, Integer> solve() throws UnsolvablePuzzleException {
        List<Character> letterList = new ArrayList<>(letters);
        int[] digits = new int[10];
        boolean[] used = new boolean[10];
        
        Map<Character, Integer> solution = new HashMap<>();
        if (solveRecursive(letterList, 0, digits, used, solution)) {
            return solution;
        }
        throw new UnsolvablePuzzleException();
    }

    private boolean solveRecursive(List<Character> letters, int index, int[] digits, 
                                  boolean[] used, Map<Character, Integer> solution) {
        if (index == letters.size()) {
            return isValidSolution(solution);
        }
        
        char currentChar = letters.get(index);
        for (int digit = 0; digit <= 9; digit++) {
            if (!used[digit] && (digit != 0 || !leadingLetters.contains(currentChar))) {
                used[digit] = true;
                solution.put(currentChar, digit);
                if (solveRecursive(letters, index + 1, digits, used, solution)) {
                    return true;
                }
                solution.remove(currentChar);
                used[digit] = false;
            }
        }
        return false;
    }

    private boolean isValidSolution(Map<Character, Integer> solution) {
        long leftSum = 0;
        for (String term : leftTerms) {
            leftSum += wordToNumber(term, solution);
        }
        long rightNumber = wordToNumber(rightTerm, solution);
        return leftSum == rightNumber;
    }

    private long wordToNumber(String word, Map<Character, Integer> solution) {
        long number = 0;
        for (char c : word.toCharArray()) {
            number = number * 10 + solution.get(c);
        }
        return number;
    }
}
