public class SgfParsing {
    public static class SgfParsingException extends Exception {
        public SgfParsingException(String message) {
            super(message);
        }
    }

    public static class SgfNode {
        private final Map<String, List<String>> properties;
        private final List<SgfNode> children;

        public SgfNode(Map<String, List<String>> properties, List<SgfNode> children) {
            this.properties = Collections.unmodifiableMap(new HashMap<>(properties));
            this.children = Collections.unmodifiableList(new ArrayList<>(children));
        }

        public Map<String, List<String>> getProperties() {
            return properties;
        }

        public List<SgfNode> getChildren() {
            return children;
        }
    }

    private static class Parser {
        private final String input;
        private int pos;

        public Parser(String input) {
            this.input = input;
            this.pos = 0;
        }

        public SgfNode parse() throws SgfParsingException {
            if (input == null || input.isEmpty()) {
                throw new SgfParsingException("Empty input");
            }

            if (input.charAt(pos) != '(') {
                throw new SgfParsingException("Expected '('");
            }
            pos++;

            SgfNode root = parseNodeSequence();

            if (pos >= input.length() || input.charAt(pos) != ')') {
                throw new SgfParsingException("Expected ')'");
            }
            pos++;

            if (pos != input.length()) {
                throw new SgfParsingException("Extra characters after root node");
            }

            return root;
        }

        private SgfNode parseNodeSequence() throws SgfParsingException {
            SgfNode root = null;
            SgfNode current = null;
            Deque<SgfNode> stack = new ArrayDeque<>();
            List<SgfNode> variations = new ArrayList<>();
            
            while (pos < input.length()) {
                char c = input.charAt(pos);
                
                if (c == ';') {
                    // Parse a node
                    pos++; // Skip semicolon
                    Map<String, List<String>> properties = new HashMap<>();
                    
                    // Parse properties
                    while (pos < input.length() && isUpperCase(input.charAt(pos))) {
                        String key = parseKey();
                        List<String> values = parseValues();
                        properties.put(key, values);
                    }
                    
                    SgfNode node = new SgfNode(properties, new ArrayList<>());
                    
                    // Link the node into the tree
                    if (current != null) {
                        List<SgfNode> newChildren = new ArrayList<>();
                        newChildren.add(node);
                        newChildren.addAll(current.getChildren());
                        current = new SgfNode(current.getProperties(), newChildren);
                    } else {
                        root = node;
                        current = node;
                    }
                } else if (c == '(') {
                    // Start a variation
                    pos++;
                    stack.push(current);
                    current = null;
                    variations = new ArrayList<>();
                } else if (c == ')') {
                    // End a variation
                    pos++;
                    if (!stack.isEmpty()) {
                        SgfNode parent = stack.pop();
                        if (parent != null && current != null) {
                            List<SgfNode> newChildren = new ArrayList<>(parent.getChildren());
                            newChildren.add(current);
                            parent = new SgfNode(parent.getProperties(), newChildren);
                        }
                        current = parent;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            
            if (root == null) {
                throw new SgfParsingException("Empty node sequence");
            }
            
            return root;
        }

        private String parseKey() throws SgfParsingException {
            StringBuilder key = new StringBuilder();
            while (pos < input.length() && isUpperCase(input.charAt(pos))) {
                key.append(input.charAt(pos++));
            }
            if (key.length() == 0) {
                throw new SgfParsingException("Empty property key");
            }
            return key.toString();
        }

        private List<String> parseValues() throws SgfParsingException {
            List<String> values = new ArrayList<>();
            while (pos < input.length() && input.charAt(pos) == '[') {
                pos++; // Skip opening bracket
                StringBuilder value = new StringBuilder();
                boolean escaped = false;

                while (pos < input.length()) {
                    char c = input.charAt(pos++);
                    if (escaped) {
                        if (c != '\n') {
                            value.append(c);
                        }
                        escaped = false;
                    } else if (c == '\\') {
                        escaped = true;
                    } else if (c == ']') {
                        break;
                    } else if (c == '\n') {
                        value.append(c);
                    } else if (Character.isWhitespace(c)) {
                        value.append(' ');
                    } else {
                        value.append(c);
                    }
                }

                if (pos > input.length()) {
                    throw new SgfParsingException("Unterminated value");
                }

                values.add(value.toString());
            }

            if (values.isEmpty()) {
                throw new SgfParsingException("Property must have at least one value");
            }

            return values;
        }

        private boolean isUpperCase(char c) {
            return c >= 'A' && c <= 'Z';
        }
    }

    public SgfNode parse(String input) throws SgfParsingException {
        return new Parser(input).parse();
    }
}
