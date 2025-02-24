public class WordProblemSolver {
    public int solve(final String wordProblem) {
        if (!wordProblem.startsWith("What is ") || !wordProblem.endsWith("?")) {
            throw new IllegalArgumentException("Invalid question format");
        }
        
        String content = wordProblem.substring(8, wordProblem.length() - 1).trim();
        if (content.isEmpty()) {
            throw new IllegalArgumentException("Empty question content");
        }
        
        String[] tokens = content.split("\\s+");
        if (tokens.length == 0) {
            throw new IllegalArgumentException("No content found");
        }
        
        try {
            int result = Integer.parseInt(tokens[0]);
            int i = 1;
            
            while (i < tokens.length) {
                String operation = null;
                int consumed = 0;
                
                if (tokens[i].equals("plus")) {
                    operation = "plus";
                    consumed = 1;
                } else if (tokens[i].equals("minus")) {
                    operation = "minus";
                    consumed = 1;
                } else if (i < tokens.length - 1 && tokens[i].equals("multiplied") && tokens[i+1].equals("by")) {
                    operation = "multiplied by";
                    consumed = 2;
                } else if (i < tokens.length - 1 && tokens[i].equals("divided") && tokens[i+1].equals("by")) {
                    operation = "divided by";
                    consumed = 2;
                } else {
                    throw new IllegalArgumentException("Unknown operation: " + tokens[i]);
                }
                
                i += consumed;
                if (i >= tokens.length) {
                    throw new IllegalArgumentException("Missing number after operation");
                }
                
                int nextNumber = Integer.parseInt(tokens[i]);
                i++;
                
                switch (operation) {
                    case "plus":
                        result += nextNumber;
                        break;
                    case "minus":
                        result -= nextNumber;
                        break;
                    case "multiplied by":
                        result *= nextNumber;
                        break;
                    case "divided by":
                        result /= nextNumber;
                        break;
                    default:
                        throw new IllegalArgumentException("Unhandled operation: " + operation);
                }
            }
            
            return result;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format", e);
        }
    }
}
