import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class WordProblemSolver {
    int solve(final String wordProblem) {
        if (!wordProblem.startsWith("What is ") || !wordProblem.endsWith("?")) {
            throw new IllegalArgumentException("Unknown operation");
        }

        String content = wordProblem.substring(8, wordProblem.length() - 1).trim();
        if (content.isEmpty()) {
            throw new IllegalArgumentException("Invalid question");
        }

        String[] words = content.split("\\s+");
        List<String> tokens = new ArrayList<>();
        
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            try {
                // Try parsing as number
                Integer.parseInt(word);
                tokens.add(word);
                continue;
            } catch (NumberFormatException e) {
                // Not a number, check if it's an operation
                if (word.equals("plus") || word.equals("minus")) {
                    tokens.add(word);
                } else if (word.equals("multiplied") || word.equals("divided")) {
                    if (i + 1 < words.length && words[i + 1].equals("by")) {
                        tokens.add(word + " by");
                        i++; // Skip the "by"
                    } else {
                        throw new IllegalArgumentException("Invalid syntax");
                    }
                } else {
                    throw new IllegalArgumentException("Unknown operation");
                }
            }
        }

        if (tokens.isEmpty() || tokens.size() % 2 == 0) {
            throw new IllegalArgumentException("Invalid expression");
        }

        int result;
        try {
            result = Integer.parseInt(tokens.get(0));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number", e);
        }

        for (int i = 1; i < tokens.size(); i += 2) {
            String op = tokens.get(i);
            int num;
            try {
                num = Integer.parseInt(tokens.get(i + 1));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid number", e);
            }

            switch (op) {
                case "plus":
                    result += num;
                    break;
                case "minus":
                    result -= num;
                    break;
                case "multiplied by":
                    result *= num;
                    break;
                case "divided by":
                    result /= num;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown operation: " + op);
            }
        }

        return result;
    }
}
