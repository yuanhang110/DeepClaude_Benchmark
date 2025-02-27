import java.util.*;

class ForthEvaluator {
    private final Deque<Integer> stack = new ArrayDeque<>();
    private final Map<String, List<String>> dictionary = new HashMap<>();

    List<Integer> evaluateProgram(List<String> input) {
        for (String line : input) {
            List<String> tokens = parseLine(line);
            if (tokens.isEmpty()) continue;

            if (tokens.get(0).equalsIgnoreCase(":")) {
                defineWord(tokens);
            } else {
                processTokens(tokens);
            }
        }
        return new ArrayList<>(stack);
    }

    private List<String> parseLine(String line) {
        List<String> tokens = new ArrayList<>();
        for (String token : line.split("\\s+")) {
            if (!token.isEmpty()) {
                tokens.add(token);
            }
        }
        return tokens;
    }

    private void defineWord(List<String> tokens) {
        if (tokens.size() < 4 || !tokens.get(tokens.size() - 1).equalsIgnoreCase(";")) {
            throw new IllegalArgumentException("Invalid word definition");
        }
        String wordName = tokens.get(1).toUpperCase();
        if (wordName.matches("-?\\d+")) {
            throw new IllegalArgumentException("Cannot redefine numbers");
        }
        List<String> definition = tokens.subList(2, tokens.size() - 1);
        dictionary.put(wordName, new ArrayList<>(definition));
    }

    private void processTokens(List<String> tokens) {
        Deque<String> tokenQueue = new ArrayDeque<>(tokens);
        while (!tokenQueue.isEmpty()) {
            String token = tokenQueue.pollFirst().toUpperCase();
            if (dictionary.containsKey(token)) {
                List<String> definition = dictionary.get(token);
                for (int i = definition.size() - 1; i >= 0; i--) {
                    tokenQueue.addFirst(definition.get(i).toUpperCase());
                }
            } else if (isNumber(token)) {
                stack.addLast(Integer.parseInt(token));
            } else {
                executeOperation(token);
            }
        }
    }

    private boolean isNumber(String token) {
        return token.matches("-?\\d+");
    }

    private void executeOperation(String op) {
        switch (op) {
            case "+":
                checkStackSize(2);
                int a = stack.removeLast();
                int b = stack.removeLast();
                stack.addLast(b + a);
                break;
            case "-":
                checkStackSize(2);
                a = stack.removeLast();
                b = stack.removeLast();
                stack.addLast(b - a);
                break;
            case "*":
                checkStackSize(2);
                a = stack.removeLast();
                b = stack.removeLast();
                stack.addLast(b * a);
                break;
            case "/":
                checkStackSize(2);
                a = stack.removeLast();
                if (a == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                b = stack.removeLast();
                stack.addLast(b / a);
                break;
            case "DUP":
                checkStackSize(1);
                int top = stack.getLast();
                stack.addLast(top);
                break;
            case "DROP":
                checkStackSize(1);
                stack.removeLast();
                break;
            case "SWAP":
                checkStackSize(2);
                a = stack.removeLast();
                b = stack.removeLast();
                stack.addLast(a);
                stack.addLast(b);
                break;
            case "OVER":
                checkStackSize(2);
                int second = stack.get(stack.size() - 2);
                stack.addLast(second);
                break;
            default:
                throw new IllegalArgumentException("Unknown word: " + op);
        }
    }

    private void checkStackSize(int required) {
        if (stack.size() < required) {
            throw new IllegalStateException("Stack underflow");
        }
    }
}
