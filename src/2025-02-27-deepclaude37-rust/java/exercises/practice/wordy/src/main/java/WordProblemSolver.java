class WordProblemSolver {
    int solve(final String wordProblem) {
        // Check question format
        if (!wordProblem.startsWith("What is ") || !wordProblem.endsWith("?")) {
            throw new IllegalArgumentException("Invalid question format");
        }
        
        // Extract core question
        String question = wordProblem.substring(8, wordProblem.length() - 1).trim();
        if (question.isEmpty()) {
            throw new IllegalArgumentException("Empty question");
        }
        
        // Validate overall syntax with regex
        if (!question.matches("^-?\\d+(\\s+(plus|minus|multiplied by|divided by)\\s+-?\\d+)*$")) {
            throw new IllegalArgumentException("Invalid syntax");
        }
        
        // Tokenize question parts
        java.util.List<String> tokens = new java.util.ArrayList<>();
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("-?\\d+|plus|minus|multiplied\\s+by|divided\\s+by").matcher(question);
        while (matcher.find()) {
            tokens.add(matcher.group());
        }
        
        // Check token sequence validity
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("No valid tokens");
        }
        
        try {
            int result = Integer.parseInt(tokens.get(0));
            
            if ((tokens.size() - 1) % 2 != 0) {
                throw new IllegalArgumentException("Invalid operation sequence");
            }
            
            for (int i = 1; i < tokens.size(); i += 2) {
                String operation = tokens.get(i);
                int operand = Integer.parseInt(tokens.get(i + 1));
                
                switch(operation) {
                    case "plus":
                        result += operand;
                        break;
                    case "minus":
                        result -= operand;
                        break;
                    case "multiplied by":
                        result *= operand;
                        break;
                    case "divided by":
                        result /= operand;
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported operation: " + operation);
                }
            }
            
            return result;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format", e);
        }
    }
}
