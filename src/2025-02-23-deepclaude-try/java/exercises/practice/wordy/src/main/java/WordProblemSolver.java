public class WordProblemSolver {
    public int solve(final String wordProblem) {
        if (wordProblem == null || !wordProblem.startsWith("What is ") || !wordProblem.endsWith("?")) {
            throw new IllegalArgumentException("Invalid question format");
        }

        // Extract the mathematical expression
        String expression = wordProblem.substring(8, wordProblem.length() - 1).trim();
        if (expression.isEmpty()) {
            throw new IllegalArgumentException("Empty expression");
        }

        // Split the expression into parts
        String[] parts = expression.split(" ");
        
        // Handle single number case
        if (parts.length == 1) {
            try {
                return Integer.parseInt(parts[0]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Not a valid number");
            }
        }

        int result;
        try {
            result = Integer.parseInt(parts[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid first number");
        }

        // Process operations left to right
        for (int i = 1; i < parts.length; i++) {
            if (parts[i].equals("plus")) {
                if (i + 1 >= parts.length) throw new IllegalArgumentException("Number expected after 'plus'");
                try {
                    result += Integer.parseInt(parts[++i]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid number after 'plus'");
                }
            } else if (parts[i].equals("minus")) {
                if (i + 1 >= parts.length) throw new IllegalArgumentException("Number expected after 'minus'");
                try {
                    result -= Integer.parseInt(parts[++i]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid number after 'minus'");
                }
            } else if (parts[i].equals("multiplied") && i + 2 < parts.length && parts[i + 1].equals("by")) {
                try {
                    result *= Integer.parseInt(parts[i + 2]);
                    i += 2;
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid number after 'multiplied by'");
                }
            } else if (parts[i].equals("divided") && i + 2 < parts.length && parts[i + 1].equals("by")) {
                try {
                    int divisor = Integer.parseInt(parts[i + 2]);
                    if (divisor == 0) throw new IllegalArgumentException("Division by zero");
                    result /= divisor;
                    i += 2;
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid number after 'divided by'");
                }
            } else {
                throw new IllegalArgumentException("Unsupported operation");
            }
        }

        return result;
    }
}
