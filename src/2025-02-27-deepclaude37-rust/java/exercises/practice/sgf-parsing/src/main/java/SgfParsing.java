import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SgfParsing {
    public static class SgfNode {
        private final Map<String, List<String>> properties;
        private final List<SgfNode> children;

        public SgfNode(Map<String, List<String>> properties, List<SgfNode> children) {
            this.properties = properties;
            this.children = children;
        }

        public Map<String, List<String>> getProperties() {
            return properties;
        }

        public List<SgfNode> getChildren() {
            return children;
        }
    }

    public static class SgfParsingException extends RuntimeException {
        public SgfParsingException(String message) {
            super(message);
        }
    }

    private int index;
    private String input;

    public SgfNode parse(String input) throws SgfParsingException {
        if (input == null || input.isEmpty()) {
            throw new SgfParsingException("Empty input");
        }
        this.input = input;
        this.index = 0;
        try {
            expect('(');
            SgfNode root = parseNode();
            expect(')');
            if (index != input.length()) {
                throw new SgfParsingException("Unexpected characters after root node");
            }
            return root;
        } catch (IndexOutOfBoundsException e) {
            throw new SgfParsingException("Unexpected end of input");
        }
    }

    private void expect(char expected) throws SgfParsingException {
        if (index >= input.length()) {
            throw new SgfParsingException("Expected '" + expected + "' but got end of input");
        }
        char actual = input.charAt(index);
        if (actual != expected) {
            throw new SgfParsingException("Expected '" + expected + "' but got '" + actual + "' at index " + index);
        }
        index++;
    }

    private SgfNode parseNode() throws SgfParsingException {
        expect(';');
        Map<String, List<String>> properties = new HashMap<>();
        while (index < input.length()) {
            char c = input.charAt(index);
            if (!Character.isUpperCase(c)) {
                break;
            }
            String key = parseKey();
            List<String> values = parseValues();
            if (properties.containsKey(key)) {
                throw new SgfParsingException("Duplicate key: " + key);
            }
            properties.put(key, values);
        }
        List<SgfNode> children = new ArrayList<>();
        while (index < input.length()) {
            char c = input.charAt(index);
            if (c == ';') {
                children.add(parseNode());
            } else if (c == '(') {
                index++;
                children.add(parseNode());
                expect(')');
            } else {
                break;
            }
        }
        return new SgfNode(properties, children);
    }

    private String parseKey() throws SgfParsingException {
        StringBuilder key = new StringBuilder();
        while (index < input.length()) {
            char c = input.charAt(index);
            if (Character.isUpperCase(c)) {
                key.append(c);
                index++;
            } else {
                break;
            }
        }
        if (key.length() == 0) {
            throw new SgfParsingException("Invalid key at index " + index);
        }
        return key.toString();
    }

    private List<String> parseValues() throws SgfParsingException {
        List<String> values = new ArrayList<>();
        while (index < input.length() && input.charAt(index) == '[') {
            index++;
            StringBuilder value = new StringBuilder();
            boolean escaped = false;
            while (index < input.length()) {
                char c = input.charAt(index);
                if (escaped) {
                    if (c == '\n') {
                        // Remove backslash and newline
                        index++;
                        escaped = false;
                        continue;
                    }
                    if (Character.isWhitespace(c)) {
                        value.append(' ');
                    } else {
                        value.append(c);
                    }
                    index++;
                    escaped = false;
                } else if (c == '\\') {
                    escaped = true;
                    index++;
                } else if (c == ']') {
                    index++;
                    break;
                } else {
                    if (Character.isWhitespace(c) && c != '\n') {
                        value.append(' ');
                    } else {
                        value.append(c);
                    }
                    index++;
                }
            }
            if (index > input.length()) {
                throw new SgfParsingException("Unclosed value at index " + (index - 1));
            }
            values.add(value.toString());
        }
        if (values.isEmpty()) {
            throw new SgfParsingException("No values found for key");
        }
        return values;
    }
}
