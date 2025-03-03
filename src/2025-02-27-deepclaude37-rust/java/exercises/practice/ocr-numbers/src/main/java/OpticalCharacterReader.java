import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class OpticalCharacterReader {

    String parse(List<String> input) {
        // For empty input or invalid size
        if (input == null || input.size() % 4 != 0) {
            throw new IllegalArgumentException("Input has invalid size");
        }
        
        // Process groups of four lines
        List<String> results = new ArrayList<>();
        for (int i = 0; i < input.size(); i += 4) {
            List<String> group = input.subList(i, i + 4);
            if (!isValidGroup(group)) {
                throw new IllegalArgumentException("Input group has invalid format");
            }
            results.add(parseGroup(group));
        }
        
        // Join the results with commas
        return String.join(",", results);
    }
    
    private boolean isValidGroup(List<String> group) {
        // Group must have 4 lines
        if (group.size() != 4) {
            return false;
        }
        
        // First three lines must have the same length and be a multiple of 3
        int length = group.get(0).length();
        if (length % 3 != 0) {
            return false;
        }
        
        for (int i = 1; i < 3; i++) {
            if (group.get(i).length() != length) {
                return false;
            }
        }
        
        // Fourth line must be all spaces and same length as other lines
        String fourthLine = group.get(3);
        if (fourthLine.length() != length) {
            return false;
        }
        
        for (char c : fourthLine.toCharArray()) {
            if (c != ' ') {
                return false;
            }
        }
        
        return true;
    }
    
    private String parseGroup(List<String> group) {
        String line1 = group.get(0);
        String line2 = group.get(1);
        String line3 = group.get(2);
        int length = line1.length();
        int numDigits = length / 3;
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < numDigits; i++) {
            int start = i * 3;
            String segment1 = line1.substring(start, start + 3);
            String segment2 = line2.substring(start, start + 3);
            String segment3 = line3.substring(start, start + 3);
            
            // Combine the segments to form a key for lookup
            String key = segment1 + segment2 + segment3;
            result.append(getDigit(key));
        }
        
        return result.toString();
    }
    
    private char getDigit(String key) {
        Map<String, Character> digitMap = new HashMap<>();
        
        // Define patterns for digits 0-9
        digitMap.put(" _ | ||_|", '0');
        digitMap.put("     |  |", '1');
        digitMap.put(" _  _||_ ", '2');
        digitMap.put(" _  _| _|", '3');
        digitMap.put("   |_|  |", '4');
        digitMap.put(" _ |_  _|", '5');
        digitMap.put(" _ |_ |_|", '6');
        digitMap.put(" _   |  |", '7');
        digitMap.put(" _ |_||_|", '8');
        digitMap.put(" _ |_| _|", '9');
        
        // Return the mapped digit or '?' if not recognized
        return digitMap.getOrDefault(key, '?');
    }
}
