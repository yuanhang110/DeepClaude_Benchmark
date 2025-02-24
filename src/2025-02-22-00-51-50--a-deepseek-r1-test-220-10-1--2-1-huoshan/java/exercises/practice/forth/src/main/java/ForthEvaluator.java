import java.util.*;
import java.util.stream.Collectors;

class ForthEvaluator {
    private Deque<Integer> stack = new ArrayDeque<>();
    private Map<String, List<String>> customWords = new HashMap<>();

    List<Integer> evaluateProgram(List<String> input) {
        stack.clear();
        customWords.clear();

        for (String line : input) {
            processLine(line.toUpperCase().split("\\s+"));
        }

        return new ArrayList<>(stack);
    }

    private void processLine(String[] tokens) {
        int i = 0;
        while (i < tokens.length) {
            String token = tokens[i];
            
            if (":".equals(token)) {
                parseCustomWord(tokens, i + 1);
                i = tokens.length; // Skip rest of line after definition
            } else if (customWords.containsKey(token)) {
                processCustomWord(token);
                i++;
            } else {
                processToken(token);
                i++;
            }
        }
    }

    private void parseCustomWord(String[] tokens, int start) {
        if (start >= tokens.length - 1 || "".equals(tokens[start]) || ";".equals(tokens[start])) {
            throw new IllegalArgumentException("Invalid word definition");
        }

        String wordName = tokens[start];
        if (isNumber(wordName)) {
            throw new IllegalArgumentException("Cannot redefine numbers");
        }

        List<String> definition = new ArrayList<>();
        int i = start + 1;
        while (i < tokens.length && !";".equals(tokens[i])) {
            String token = tokens[i];
            if (customWords.containsKey(token)) {
                definition.addAll(customWords.get(token));
            } else {
                definition.add(token);
            }
            i++;
        }

        if (i >= tokens.length) {
            throw new IllegalArgumentException("Word definition not terminated");
        }

        customWords.put(wordName, definition);
    }

    private void processCustomWord(String word) {
        List<String> definition = customWords.get(word);
        for (String token : definition) {
            processToken(token);
        }
    }

    private void processToken(String token) {
        if (isNumber(token)) {
            stack.push(Integer.parseInt(token));
            return;
        }

        switch (token) {
            case "+":   applyOperator((a, b) -> b + a); break;
            case "-":   applyOperator((a, b) -> b - a); break;
            case "*":   applyOperator((a, b) -> b * a); break;
            case "/":   applyOperator((a, b) -> { 
                if (a == 0) throw new ArithmeticException("Division by zero");
                return b / a; 
            }); break;
            case "DUP": dup(); break;
            case "DROP": drop(); break;
            case "SWAP": swap(); break;
            case "OVER": over(); break;
            default: throw new IllegalArgumentException("No definition available for operator \"" + token + "\"");
        }
    }

    private boolean isNumber(String token) {
        return token.matches("-?\\d+");
    }

    private void applyOperator(Operator op) {
        verifyStackSize(2);
        int a = stack.pop();
        int b = stack.pop();
        stack.push(op.apply(a, b));
    }

    private void dup() {
        verifyStackSize(1);
        stack.push(stack.peek());
    }

    private void drop() {
        verifyStackSize(1);
        stack.pop();
    }

    private void swap() {
        verifyStackSize(2);
        int a = stack.pop();
        int b = stack.pop();
        stack.push(a);
        stack.push(b);
    }

    private void over() {
        verifyStackSize(2);
        int a = stack.pop();
        int b = stack.peek();
        stack.push(a);
        stack.push(b);
    }

    private void verifyStackSize(int requiredSize) {
        if (stack.size() < requiredSize) {
            throw new IllegalArgumentException(
                String.format("%s requires that the stack contain at least %d value(s)", 
                stack.isEmpty() ? "Operation" : "Insufficient number of elements in stack",
                requiredSize));
        }
    }

    @FunctionalInterface
    private interface Operator {
        int apply(int a, int b);
    }
}
