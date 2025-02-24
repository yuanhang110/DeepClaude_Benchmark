import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SgfParsing {
    public static class SgfNode {
        private final Map<String, List<String>> properties = new HashMap<>();
        private final List<SgfNode> children = new ArrayList<>();
        private final List<SgfNode> variations = new ArrayList<>();

        public Map<String, List<String>> getProperties() {
            return properties;
        }
    }

    public static class SgfParsingException extends Exception {
        public SgfParsingException(String message) {
            super(message);
        }
    }

    private int index;
    private String input;

    public SgfNode parse(String input) throws SgfParsingException {
        if (input == null || input.isEmpty()) {
            throw new SgfParsingException("Input cannot be empty");
        }

        this.input = input;
        this.index = 0;

        try {
            consume('(');
            SgfNode root = parseNode();
            if (root == null) {
                throw new SgfParsingException("No root node found");
            }
            
            if (index < input.length()) {
                throw new SgfParsingException("Unexpected content after tree");
            }
            
            return root;
        } catch (IndexOutOfBoundsException e) {
            throw new SgfParsingException("Unexpected end of input");
        }
    }

    private SgfNode parseNode() throws SgfParsingException {
        consume(';');
        SgfNode node = new SgfNode();
        parseProperties(node);

        // A node must have at least one property
        if (node.properties.isEmpty()) {
            throw new SgfParsingException("Node must contain at least one property");
        }

        // Parse children and variations
        while (index < input.length()) {
            char c = input.charAt(index);
            if (c == ';') {
                node.children.add(parseNode());
            } else if (c == '(') {
                index++;
                node.variations.add(parseNode());
            } else if (c == ')') {
                index++;
                break;
            } else {
                throw new SgfParsingException("Unexpected character: " + c);
            }
        }

        return node;
    }

    private void parseProperties(SgfNode node) throws SgfParsingException {
        while (index < input.length()) {
            char c = input.charAt(index);
            if (c == '[') {
                break;
            }

            if (Character.isUpperCase(c)) {
                String key = parseKey();
                List<String> values = parseValues();
                node.properties.put(key, values);
            } else {
                throw new SgfParsingException("Unexpected character in property key: " + c);
            }
        }
    }

    private String parseKey() {
        int start = index;
        while (index < input.length() && Character.isUpperCase(input.charAt(index))) {
            index++;
        }
        return input.substring(start, index);
    }

    private List<String> parseValues() throws SgfParsingException {
        List<String> values = new ArrayList<>();
        while (index < input.length() && input.charAt(index) == '[') {
            index++;
            int start = index;
            boolean escape = false;

            while (index < input.length()) {
                char c = input.charAt(index);
                
                if (escape) {
                    escape = false;
                    index++;
                    continue;
                }

                if (c == '\\') {
                    escape = true;
                    index++;
                    continue;
                }

                if (c == ']') {
                    String value = processText(input.substring(start, index));
                    values.add(value);
                    index++;
                    break;
                }

                index++;
            }
        }
        return values;
    }

    private String processText(String text) {
        StringBuilder sb = new StringBuilder();
        boolean escape = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (escape) {
                sb.append(c);
                escape = false;
                continue;
            }

            if (c == '\\') {
                escape = true;
                // Check if next character is newline
                if (i + 1 < text.length() && text.charAt(i + 1) == '\n') {
                    i++; // Skip the newline
                    escape = false;
                }
                continue;
            }

            if (c == '\n') {
                sb.append(c);
            } else if (Character.isWhitespace(c)) {
                sb.append(' ');
            } else {
                sb.append(c);
            }
        }

        if (escape) {
            sb.append('\\');
        }

        return sb.toString();
    }

    private void consume(char expected) throws SgfParsingException {
        if (index >= input.length() || input.charAt(index) != expected) {
            throw new SgfParsingException("Expected '" + expected + "' but found " + 
                (index >= input.length() ? "end of input" : "'" + input.charAt(index) + "'"));
        }
        index++;
    }
}
