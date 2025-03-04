import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

class OpticalCharacterReader {
    private static final Map<String, String> DIGIT_PATTERNS = new HashMap<>();
    
    static {
        DIGIT_PATTERNS.put(" _ | ||_|", "0");
        DIGIT_PATTERNS.put("     |  |", "1");
        DIGIT_PATTERNS.put(" _  _||_ ", "2");
        DIGIT_PATTERNS.put(" _  _| _|", "3");
        DIGIT_PATTERNS.put("   |_|  |", "4");
        DIGIT_PATTERNS.put(" _ |_  _|", "5");
        DIGIT_PATTERNS.put(" _ |_ |_|", "6");
        DIGIT_PATTERNS.put(" _   |  |", "7");
        DIGIT_PATTERNS.put(" _ |_||_|", "8");
        DIGIT_PATTERNS.put(" _ |_| _|", "9");
    }

    String parse(List<String> input) {
        if (input == null || input.size() % 4 != 0) {
            throw new IllegalArgumentException("Number of input lines must be a positive multiple of 4");
        }
        
        List<String> results = new ArrayList<>();
        for (int lineStart = 0; lineStart < input.size(); lineStart += 4) {
            String lineResult = processLine(input.subList(lineStart, lineStart + 4));
            results.add(lineResult);
        }
        return String.join(",", results);
    }

    private String processLine(List<String> line) {
        if (line.get(0).length() % 3 != 0) {
            throw new IllegalArgumentException("Number of input columns must be a positive multiple of 3");
        }
        
        StringBuilder result = new StringBuilder();
        int digitCount = line.get(0).length() / 3;
        
        for (int digit = 0; digit < digitCount; digit++) {
            String pattern = extractPattern(line, digit);
            result.append(DIGIT_PATTERNS.getOrDefault(pattern, "?"));
        }
        return result.toString();
    }

    private String extractPattern(List<String> line, int digit) {
        StringBuilder pattern = new StringBuilder();
        for (int row = 0; row < 3; row++) {
            String lineRow = line.get(row);
            pattern.append(lineRow.substring(digit * 3, (digit + 1) * 3));
        }
        return pattern.toString();
    }
}
