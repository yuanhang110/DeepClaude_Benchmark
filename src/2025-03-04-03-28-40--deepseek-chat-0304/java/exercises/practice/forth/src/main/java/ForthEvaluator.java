import java.util.*;
import java.util.regex.*;
import java.util.concurrent.ConcurrentHashMap;

class ForthEvaluator {
    private final Deque<Integer> stack = new ArrayDeque<>();
    private final Map<String, List<String>> customWords = new ConcurrentHashMap<>();

    List<Integer> evaluateProgram(List<String> input) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        synchronized (this) {
            stack.clear();
            customWords.clear();

            for (String line : input) {
                if (line != null) {
                    evaluateLine(line);
                }
            }

            return new ArrayList<>(stack);
        }
    }

    private void evaluateLine(String line) {
        if (line.trim().isEmpty()) {
            return;
        }

        String[] tokens = line.split("\\s+");
        int i = 0;

        while (i < tokens.length) {
            String token = tokens[i].toUpperCase(Locale.ROOT);

            if (isNumber(token)) {
                try {
                    stack.push(Integer.parseInt(token));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid number: " + token);
                }
            } else if (":".equals(token)) {
                i = processWordDefinition(tokens, i);
            } else {
                evaluateToken(token);
            }
            i++;
        }
    }

    private boolean isNumber(String token) {
        return token.matches("-?\\d+");
    }

    private int processWordDefinition(String[] tokens, int i) {
        if (i + 1 >= tokens.length) {
            throw new IllegalArgumentException("Invalid word definition");
        }

        String wordName = tokens[i + 1].toUpperCase();
        if (isNumber(wordName)) {
            throw new IllegalArgumentException("Cannot redefine numbers");
        }

        List<String> definition = new ArrayList<>();
        i += 2;

        while (i < tokens.length && !";".equals(tokens[i])) {
            definition.add(tokens[i].toUpperCase());
            i++;
        }

        if (i >= tokens.length) {
            throw new IllegalArgumentException("Word definition not terminated");
        }

        customWords.put(wordName, definition);
        return i;
    }

    private void evaluateToken(String token) {
        // Check if it's a custom word first
        if (customWords.containsKey(token)) {
            for (String word : customWords.get(token)) {
                evaluateToken(word);
            }
            return;
        }

        // Built-in operations
        switch (token) {
            case "+":
                arithmeticOperation((a, b) -> a + b);
                break;
            case "-":
                arithmeticOperation((a, b) -> a - b);
                break;
            case "*":
                arithmeticOperation((a, b) -> a * b);
                break;
            case "/":
                arithmeticOperation((a, b) -> {
                    if (b == 0) throw new IllegalArgumentException("Division by zero");
                    return a / b;
                });
                break;
            case "DUP":
                if (stack.isEmpty()) {
                    throw new IllegalArgumentException("DUP requires at least one value");
                }
                stack.push(stack.peek());
                break;
            case "DROP":
                if (stack.isEmpty()) {
                    throw new IllegalArgumentException("DROP requires at least one value");
                }
                stack.pop();
                break;
            case "SWAP":
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("SWAP requires at least two values");
                }
                int a = stack.pop();
                int b = stack.pop();
                stack.push(a);
                stack.push(b);
                break;
            case "OVER":
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("OVER requires at least two values");
                }
                int top = stack.pop();
                int second = stack.peek();
                stack.push(top);
                stack.push(second);
                break;
            default:
                throw new IllegalArgumentException("Unknown word: " + token);
        }
    }

    private void arithmeticOperation(ArithmeticOperation operation) {
        if (stack.size() < 2) {
            throw new IllegalArgumentException("Arithmetic operation requires at least two values");
        }
        int b = stack.pop();
        int a = stack.pop();
        stack.push(operation.apply(a, b));
    }

    @FunctionalInterface
    private interface ArithmeticOperation {
        int apply(int a, int b);
    }
}
