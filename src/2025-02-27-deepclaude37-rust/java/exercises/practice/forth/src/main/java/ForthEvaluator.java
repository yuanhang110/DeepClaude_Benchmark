import java.util.List;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

class ForthEvaluator {
    List<Integer> evaluateProgram(List<String> input) {
        Deque<Integer> stack = new ArrayDeque<>();
        Map<String, List<String>> customWords = new HashMap<>();

        for (String line : input) {
            processLine(line, stack, customWords);
        }

        List<Integer> result = new ArrayList<>();
        Iterator<Integer> it = stack.descendingIterator();
        while (it.hasNext()) {
            result.add(it.next());
        }
        return result;
    }

    private void processLine(String line, Deque<Integer> stack, Map<String, List<String>> customWords) {
        List<String> tokens = splitIntoTokens(line);
        if (tokens.isEmpty()) {
            return;
        }

        if (tokens.get(0).equalsIgnoreCase(":")) {
            defineCustomWord(tokens, customWords);
        } else {
            evaluateTokens(tokens, stack, customWords);
        }
    }

    private List<String> splitIntoTokens(String line) {
        List<String> tokens = new ArrayList<>();
        for (String token : line.split("\\s+")) {
            if (!token.isEmpty()) {
                tokens.add(token);
            }
        }
        return tokens;
    }

    private void defineCustomWord(List<String> tokens, Map<String, List<String>> customWords) {
        if (tokens.size() < 3 || !tokens.get(tokens.size() - 1).equalsIgnoreCase(";")) {
            throw new IllegalArgumentException("Invalid word definition");
        }
        String wordName = tokens.get(1).toLowerCase();
        if (isNumber(wordName)) {
            throw new IllegalArgumentException("Cannot redefine numbers");
        }
        List<String> definition = tokens.subList(2, tokens.size() - 1);
        customWords.put(wordName, new ArrayList<>(definition));
    }

    private boolean isNumber(String token) {
        try {
            Integer.parseInt(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void evaluateTokens(List<String> tokens, Deque<Integer> stack, Map<String, List<String>> customWords) {
        for (String token : tokens) {
            processToken(token, stack, customWords);
        }
    }

    private void processToken(String token, Deque<Integer> stack, Map<String, List<String>> customWords) {
        try {
            int num = Integer.parseInt(token);
            stack.push(num);
        } catch (NumberFormatException e) {
            String word = token.toLowerCase();
            if (customWords.containsKey(word)) {
                List<String> definition = customWords.get(word);
                for (String defToken : definition) {
                    processToken(defToken, stack, customWords);
                }
            } else {
                executeBuiltIn(word, stack);
            }
        }
    }

    private void executeBuiltIn(String word, Deque<Integer> stack) {
        switch (word) {
            case "+":
                add(stack);
                break;
            case "-":
                subtract(stack);
                break;
            case "*":
                multiply(stack);
                break;
            case "/":
                divide(stack);
                break;
            case "dup":
                dup(stack);
                break;
            case "drop":
                drop(stack);
                break;
            case "swap":
                swap(stack);
                break;
            case "over":
                over(stack);
                break;
            default:
                throw new IllegalArgumentException("Undefined word: " + word);
        }
    }

    private void add(Deque<Integer> stack) {
        checkStackSize(stack, 2, "+");
        int b = stack.pop();
        int a = stack.pop();
        stack.push(a + b);
    }

    private void subtract(Deque<Integer> stack) {
        checkStackSize(stack, 2, "-");
        int b = stack.pop();
        int a = stack.pop();
        stack.push(a - b);
    }

    private void multiply(Deque<Integer> stack) {
        checkStackSize(stack, 2, "*");
        int b = stack.pop();
        int a = stack.pop();
        stack.push(a * b);
    }

    private void divide(Deque<Integer> stack) {
        checkStackSize(stack, 2, "/");
        int b = stack.pop();
        int a = stack.pop();
        if (b == 0) {
            throw new ArithmeticException("Division by zero");
        }
        // Implement Forth-style floored division
        int quotient = a / b;
        // Adjust for negative numbers to match Forth's floored division
        if ((a < 0 && b > 0) || (a > 0 && b < 0)) {
            // If there's a remainder and operands have different signs
            if (a % b != 0) {
                quotient--; // Floor instead of truncate
            }
        }
        stack.push(quotient);
    }

    private void dup(Deque<Integer> stack) {
        checkStackSize(stack, 1, "DUP");
        stack.push(stack.peek());
    }

    private void drop(Deque<Integer> stack) {
        checkStackSize(stack, 1, "DROP");
        stack.pop();
    }

    private void swap(Deque<Integer> stack) {
        checkStackSize(stack, 2, "SWAP");
        int a = stack.pop();
        int b = stack.pop();
        stack.push(a);
        stack.push(b);
    }

    private void over(Deque<Integer> stack) {
        checkStackSize(stack, 2, "OVER");
        int a = stack.pop();
        int b = stack.pop();
        stack.push(b);
        stack.push(a);
        stack.push(b);
    }

    private void checkStackSize(Deque<Integer> stack, int requiredSize, String operation) {
        if (stack.size() < requiredSize) {
            throw new IllegalArgumentException(
                operation + " requires at least " + requiredSize + " value" + (requiredSize != 1 ? "s" : "") + " on the stack"
            );
        }
    }
}
