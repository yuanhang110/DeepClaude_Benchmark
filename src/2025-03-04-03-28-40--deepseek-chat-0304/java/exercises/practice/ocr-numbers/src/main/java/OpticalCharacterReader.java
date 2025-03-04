import java.util.List;
import java.util.ArrayList;

class OpticalCharacterReader {
    
    private static final String[] DIGITS = {
        " _ | ||_|",  // 0
        "     |  |",  // 1
        " _  _||_ ",  // 2
        " _  _| _|",  // 3
        "   |_|  |",  // 4
        " _ |_  _|",  // 5
        " _ |_ |_|",  // 6
        " _   |  |",  // 7
        " _ |_||_|",  // 8
        " _ |_| _|"   // 9
    };
    
    String parse(List<String> input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        
        if (input.size() % 4 != 0) {
            throw new IllegalArgumentException("Number of input lines must be a multiple of four");
        }
        
        List<String> results = new ArrayList<>();
        for (int line = 0; line < input.size(); line += 4) {
            try {
                results.add(parseLine(input.subList(line, line + 4)));
            } catch (IllegalArgumentException e) {
                results.add("?");
            }
        }
        return String.join(",", results);
    }
    
    private String parseLine(List<String> input) {
        if (input == null || input.size() != 4) {
            throw new IllegalArgumentException("Each number must have exactly 4 lines");
        }
        
        // Handle empty lines
        for (String line : input) {
            if (line == null || line.length() % 3 != 0) {
                throw new IllegalArgumentException("Each line must have a length that is a multiple of 3");
            }
        }
        
        StringBuilder result = new StringBuilder();
        int digitCount = input.get(0).length() / 3;
        
        for (int digit = 0; digit < digitCount; digit++) {
            String pattern = getPattern(input, digit);
            result.append(recognizeDigit(pattern));
        }
        
        return result.toString();
    }
    
    private String getPattern(List<String> input, int digit) {
        StringBuilder pattern = new StringBuilder();
        for (int row = 0; row < 3; row++) {
            String line = input.get(row);
            pattern.append(line.substring(digit * 3, (digit + 1) * 3));
        }
        return pattern.toString();
    }
    
    private char recognizeDigit(String pattern) {
        for (int i = 0; i < DIGITS.length; i++) {
            if (pattern.equals(DIGITS[i])) {
                return (char)('0' + i);
            }
        }
        return '?';
    }
}
