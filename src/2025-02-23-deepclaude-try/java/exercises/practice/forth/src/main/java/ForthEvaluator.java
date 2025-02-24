import java.util.*;

class ForthEvaluator {
    private final Deque<Integer> stack;
    private final Map<String, List<String>> definitions;

    public ForthEvaluator() {
        stack = new ArrayDeque<>();
        definitions = new HashMap<>();
    }

    List<Integer> evaluateProgram(List<String> input) {
        for (String line : input) {
            processLine(line.trim());
        }
        return new ArrayList<>(stack);
    }

    private void processLine(String line) {
        if (line.isEmpty()) return;

        String[] tokens = line.split("\\s+");
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i].toUpperCase();

            // Handle word definition
            if (token.equals(":")) {
                if (i + 2 >= tokens.length) throw new IllegalArgumentException("Invalid word definition");
                String name = tokens[++i].toUpperCase();
                List<String> definition = new ArrayList<>();
                while (i + 1 < tokens.length && !tokens[i + 1].equals(";")) {
                    definition.add(tokens[++i].toUpperCase());
                }
                if (i + 1 >= tokens.length || !tokens[i + 1].equals(";")) {
                    throw new IllegalArgumentException("Unterminated word definition");
                }
                i++; // Skip the semicolon
                definitions.put(name, definition);
                continue;
            }

            processToken(token);
        }
    }

    private void processToken(String token) {
        // Try to parse as number
        try {
            stack.push(Integer.parseInt(token));
            return;
        } catch (NumberFormatException e) {
            // Not a number, continue with word processing
        }

        // Check for built-in words
        switch (token) {
            case "+":
                ensureStackSize(2);
                stack.push(stack.pop() + stack.pop());
                break;
            case "-":
                ensureStackSize(2);
                int b = stack.pop();
                int a = stack.pop();
                stack.push(a - b);
                break;
            case "*":
                ensureStackSize(2);
                stack.push(stack.pop() * stack.pop());
                break;
            case "/":
                ensureStackSize(2);
                int divisor = stack.pop();
                if (divisor == 0) throw new IllegalArgumentException("Division by zero");
                int dividend = stack.pop();
                stack.push(dividend / divisor);
                break;
            case "DUP":
                ensureStackSize(1);
                stack.push(stack.peek());
                break;
            case "DROP":
                ensureStackSize(1);
                stack.pop();
                break;
            case "SWAP":
                ensureStackSize(2);
                int first = stack.pop();
                int second = stack.pop();
                stack.push(first);
                stack.push(second);
                break;
            case "OVER":
                ensureStackSize(2);
                int top = stack.pop();
                int second = stack.pop();
                stack.push(second);
                stack.push(top);
                stack.push(second);
                break;
            default:
                // Check for custom word
                if (definitions.containsKey(token)) {
                    for (String defToken : definitions.get(token)) {
                        processToken(defToken);
                    }
                } else {
                    throw new IllegalArgumentException("Undefined word: " + token);
                }
        }
    }

    private void ensureStackSize(int size) {
        if (stack.size() < size) {
            throw new IllegalArgumentException("Stack underflow");
        }
    }
}
