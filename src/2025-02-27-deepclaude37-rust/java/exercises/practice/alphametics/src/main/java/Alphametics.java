import java.util.*;

class Alphametics {

    private final String[] leftTerms;
    private final String rightTerm;
    private final Set<Character> leadingChars;
    private final char[][] columns;
    private final int maxLength;

    Alphametics(String userInput) {
        String[] parts = userInput.split(" = ");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid equation");
        }

        this.leftTerms = parts[0].split(" \\+ ");
        this.rightTerm = parts[1];
        this.leadingChars = new HashSet<>();

        // Find max length and collect leading characters
        maxLength = Math.max(
            Arrays.stream(leftTerms).mapToInt(String::length).max().orElse(0),
            rightTerm.length()
        );

        // Record leading characters
        for (String term : leftTerms) {
            if (!term.isEmpty()) {
                leadingChars.add(term.charAt(0));
            }
        }
        if (!rightTerm.isEmpty()) {
            leadingChars.add(rightTerm.charAt(0));
        }

        // Create columns array (right-aligned)
        columns = new char[maxLength][];
        for (int col = 0; col < maxLength; col++) {
            List<Character> colChars = new ArrayList<>();
            int pos = maxLength - 1 - col;
            
            // Add characters from left terms
            for (String term : leftTerms) {
                if (term.length() > pos) {
                    colChars.add(term.charAt(term.length() - 1 - col));
                }
            }
            
            // Add character from right term
            if (rightTerm.length() > pos) {
                colChars.add(rightTerm.charAt(rightTerm.length() - 1 - col));
            }
            
            columns[col] = new char[colChars.size()];
            for (int i = 0; i < colChars.size(); i++) {
                columns[col][i] = colChars.get(i);
            }
        }
    }

    Map<Character, Integer> solve() throws UnsolvablePuzzleException {
        Map<Character, Integer> solution = new HashMap<>();
        boolean[] used = new boolean[10];
        
        if (backtrack(maxLength - 1, 0, solution, used)) {
            return solution;
        }
        throw new UnsolvablePuzzleException();
    }

    private boolean backtrack(int col, int carry, Map<Character, Integer> solution, boolean[] used) {
        if (col < 0) {
            return carry == 0;
        }

        // Get unique characters in this column that haven't been assigned yet
        Set<Character> unassigned = new HashSet<>();
        for (char c : columns[col]) {
            if (!solution.containsKey(c)) {
                unassigned.add(c);
            }
        }

        // If all characters in this column are assigned, verify the sum
        if (unassigned.isEmpty()) {
            int sum = carry;
            for (int i = 0; i < columns[col].length - 1; i++) {
                sum += solution.get(columns[col][i]);
            }
            char resultChar = columns[col][columns[col].length - 1];
            int result = solution.get(resultChar);
            
            return (sum % 10 == result) && backtrack(col - 1, sum / 10, solution, used);
        }

        // Try assigning digits to the unassigned characters
        char current = unassigned.iterator().next();
        for (int digit = 0; digit <= 9; digit++) {
            if (used[digit]) continue;
            if (digit == 0 && leadingChars.contains(current)) continue;

            used[digit] = true;
            solution.put(current, digit);
            
            if (backtrack(col, carry, solution, used)) {
                return true;
            }
            
            solution.remove(current);
            used[digit] = false;
        }

        return false;
    }

}
