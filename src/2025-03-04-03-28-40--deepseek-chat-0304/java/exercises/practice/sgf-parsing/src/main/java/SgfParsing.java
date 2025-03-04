import java.util.*;

class SgfParsingException extends Exception {
    public SgfParsingException(String message) {
        super(message);
    }
}

public class SgfParsing {
    
    public static class SgfNode {
        private Map<String, List<String>> properties = new HashMap<>();
        private List<SgfNode> children = new ArrayList<>();
        
        public Map<String, List<String>> getProperties() {
            return properties;
        }
        
        public List<SgfNode> getChildren() {
            return children;
        }
        
        public void addProperty(String key, List<String> values) {
            properties.put(key, values);
        }
        
        public void addChild(SgfNode child) {
            children.add(child);
        }
    }
    
    public SgfNode parse(String input) throws SgfParsingException {
        if (input == null || input.isEmpty()) {
            throw new SgfParsingException("Input cannot be empty");
        }
        
        // Remove outer parentheses
        if (input.charAt(0) != '(' || input.charAt(input.length()-1) != ')') {
            throw new SgfParsingException("Input must start with '(' and end with ')'");
        }
        input = input.substring(1, input.length()-1);
        
        // Parse the tree
        return parseTree(input);
    }
    
    private SgfNode parseTree(String input) throws SgfParsingException {
        SgfNode root = new SgfNode();
        int pos = 0;
        
        // Parse nodes
        while (pos < input.length()) {
            if (input.charAt(pos) != ';') {
                throw new SgfParsingException("Node must start with ';'");
            }
            pos++;
            
            // Parse properties
            while (pos < input.length() && input.charAt(pos) != ';' && input.charAt(pos) != '(') {
                String key = parseKey(input, pos);
                pos += key.length();
                
                List<String> values = new ArrayList<>();
                while (pos < input.length() && input.charAt(pos) == '[') {
                    String value = parseValue(input, pos);
                    values.add(value);
                    pos += value.length() + 2; // +2 for the brackets
                }
                
                if (values.isEmpty()) {
                    throw new SgfParsingException("Property must have at least one value");
                }
                
                root.addProperty(key, values);
            }
            
            // Parse children
            if (pos < input.length() && input.charAt(pos) == '(') {
                int end = findMatchingParenthesis(input, pos);
                String childInput = input.substring(pos, end+1);
                root.addChild(parseTree(childInput));
                pos = end + 1;
            }
        }
        
        return root;
    }
    
    private String parseKey(String input, int pos) throws SgfParsingException {
        int end = pos;
        while (end < input.length() && Character.isUpperCase(input.charAt(end))) {
            end++;
        }
        if (end == pos) {
            throw new SgfParsingException("Property key must be uppercase letters");
        }
        return input.substring(pos, end);
    }
    
    private String parseValue(String input, int pos) throws SgfParsingException {
        if (input.charAt(pos) != '[') {
            throw new SgfParsingException("Value must start with '['");
        }
        pos++;
        
        StringBuilder value = new StringBuilder();
        boolean escape = false;
        
        while (pos < input.length()) {
            char c = input.charAt(pos);
            
            if (escape) {
                value.append(c);
                escape = false;
                pos++;
                continue;
            }
            
            if (c == '\\') {
                escape = true;
                pos++;
                continue;
            }
            
            if (c == ']') {
                break;
            }
            
            if (c == '\n' && !escape) {
                value.append(c);
            } else if (Character.isWhitespace(c)) {
                value.append(' ');
            } else {
                value.append(c);
            }
            
            pos++;
        }
        
        if (pos >= input.length() || input.charAt(pos) != ']') {
            throw new SgfParsingException("Value must end with ']'");
        }
        
        return value.toString();
    }
    
    private int findMatchingParenthesis(String input, int pos) throws SgfParsingException {
        int depth = 1;
        pos++;
        
        while (pos < input.length() && depth > 0) {
            char c = input.charAt(pos);
            if (c == '(') {
                depth++;
            } else if (c == ')') {
                depth--;
            }
            pos++;
        }
        
        if (depth != 0) {
            throw new SgfParsingException("Unbalanced parentheses");
        }
        
        return pos - 1;
    }
}
