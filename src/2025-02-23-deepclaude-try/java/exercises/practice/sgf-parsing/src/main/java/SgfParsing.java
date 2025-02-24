import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SgfParsing {
    public SgfNode parse(String input) throws SgfParsingException {
        if (input == null || input.isEmpty()) {
            throw new SgfParsingException("Empty input");
        }
        
        if (!input.startsWith("(") || !input.endsWith(")")) {
            throw new SgfParsingException("Tree missing outer parentheses");
        }
        
        // Remove outer parentheses
        String content = input.substring(1, input.length() - 1);
        if (content.isEmpty()) {
            throw new SgfParsingException("Empty tree");
        }
        
        AtomicInteger pos = new AtomicInteger(0);
        SgfNode root = parseNode(content, pos);
        
        // Check for unexpected content after parsing
        if (pos.get() < content.length()) {
            throw new SgfParsingException("Unexpected content after root node");
        }
        
        return root;
    }

    private SgfNode parseNode(String content, AtomicInteger pos) throws SgfParsingException {
        // Each node must start with semicolon
        if (pos.get() >= content.length() || content.charAt(pos.get()) != ';') {
            throw new SgfParsingException("Node must start with semicolon");
        }
        pos.incrementAndGet(); // Skip semicolon
        
        Map<String, List<String>> properties = new HashMap<>();
        
        // Parse properties until we hit a variation, child node, or end
        while (pos.get() < content.length()) {
            char c = content.charAt(pos.get());
            if (c == '(' || c == ';' || c == ')') break;
            
            // Parse property key (must be uppercase letters)
            if (!Character.isUpperCase(c)) {
                throw new SgfParsingException("Property must start with uppercase letter");
            }
            
            String key = parseKey(content, pos);
            List<String> values = parseValues(content, pos);
            
            if (values.isEmpty()) {
                throw new SgfParsingException("Property must have at least one value");
            }
            
            properties.put(key, values);
        }
        
        if (properties.isEmpty()) {
            throw new SgfParsingException("Node must have at least one property");
        }
        
        SgfNode node = new SgfNode(properties);
        
        // Parse variations (branches in parentheses)
        while (pos[0] < content.length() && content.charAt(pos[0]) == '(') {
            pos[0]++; // Skip opening parenthesis
            SgfNode variation = parseNode(content, pos);
            if (pos[0] >= content.length() || content.charAt(pos[0]) != ')') {
                throw new SgfParsingException("Unclosed variation");
            }
            pos[0]++; // Skip closing parenthesis
            node.addVariation(variation);
        }
        
        // Parse child node if present
        if (pos[0] < content.length() && content.charAt(pos[0]) == ';') {
            node.addChild(parseNode(content, pos));
        }
        
        return node;
    }
    
    private String parseKey(String content, AtomicInteger pos) {
        int start = pos.get();
        while (pos.get() < content.length() && Character.isUpperCase(content.charAt(pos.get()))) {
            pos.incrementAndGet();
        }
        return content.substring(start, pos.get());
    }
    
    private List<String> parseValues(String content, AtomicInteger pos) throws SgfParsingException {
        List<String> values = new ArrayList<>();
        
        while (pos.get() < content.length() && content.charAt(pos.get()) == '[') {
            pos.incrementAndGet(); // Skip opening bracket
            StringBuilder value = new StringBuilder();
            boolean escaped = false;
            
            while (pos.get() < content.length()) {
                char c = content.charAt(pos.get());
                
                if (escaped) {
                    if (c == '\n') {
                        // Skip escaped newline
                    } else if (Character.isWhitespace(c)) {
                        value.append(' ');
                    } else {
                        value.append(c);
                    }
                    escaped = false;
                } else if (c == '\\') {
                    escaped = true;
                } else if (c == ']') {
                    pos.incrementAndGet();
                    break;
                } else {
                    if (Character.isWhitespace(c) && c != '\n') {
                        value.append(' ');
                    } else {
                        value.append(c);
                    }
                }
                pos.incrementAndGet();
            }
            
            values.add(value.toString());
        }
        
        return values;
    }
}
