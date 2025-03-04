import java.util.regex.*;
import java.util.*;

class WordProblemSolver {
    private static final Pattern PROBLEM_PATTERN = Pattern.compile(
        "^What is (-?\\d+)((?: (?:plus|minus|multiplied by|divided by) -?\\d+)*)\\?$");
    private static final Pattern OPERATION_PATTERN = Pattern.compile(
        "(plus|minus|multiplied by|divided by) (-?\\d+)");

    int solve(final String wordProblem) {
        if (wordProblem == null) {
            throw new IllegalArgumentException("Question cannot be null");
        }

        // Validate basic problem structure
        Matcher problemMatcher = PROBLEM_PATTERN.matcher(wordProblem);
        if (!problemMatcher.matches()) {
            throw new IllegalArgumentException("I don't understand the question");
        }

        // Extract initial number
        int result = Integer.parseInt(problemMatcher.group(1));
        String operations = problemMatcher.group(2);

        // Process each operation left-to-right
        if (!operations.isEmpty()) {
            Matcher operationMatcher = OPERATION_PATTERN.matcher(operations);
            while (operationMatcher.find()) {
                String operation = operationMatcher.group(1);
                int number = Integer.parseInt(operationMatcher.group(2));
                
                try {
                    switch (operation) {
                        case "plus":
                            result = Math.addExact(result, number);
                            break;
                        case "minus":
                            result = Math.subtractExact(result, number);
                            break;
                        case "multiplied by":
                            result = Math.multiplyExact(result, number);
                            break;
                        case "divided by":
                            if (number == 0) {
                                throw new ArithmeticException("Division by zero");
                            }
                            result = result / number;
                            break;
                    }
                } catch (ArithmeticException e) {
                    throw new IllegalArgumentException("Arithmetic error: " + e.getMessage());
                }
            }
        }

        return result;
    }
}
