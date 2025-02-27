import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

class OpticalCharacterReader {

    private static final Map<String, Character> digitMap = new HashMap<>();
    
    static {
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
    }

    String parse(List<String> input) {
        if (input.size() % 4 != 0) {
            return "error";
        }
        
        List<String> results = new ArrayList<>();
        int groupCount = input.size() / 4;
        
        for (int g = 0; g < groupCount; g++) {
            List<String> group = input.subList(g * 4, (g + 1) * 4);
            StringBuilder sb = new StringBuilder();
            
            if (!validateGroup(group)) {
                int chars = group.get(0).length() / 3;
                for (int i = 0; i < chars; i++) {
                    sb.append('?');
                }
            } else {
                int chars = group.get(0).length() / 3;
                for (int i = 0; i < chars; i++) {
                    String part1 = safeSubstring(group.get(0), i * 3, 3);
                    String part2 = safeSubstring(group.get(1), i * 3, 3);
                    String part3 = safeSubstring(group.get(2), i * 3, 3);
                    String key = part1 + part2 + part3;
                    sb.append(digitMap.getOrDefault(key, '?'));
                }
            }
            
            results.add(sb.toString());
        }
        
        return String.join(",", results);
    }
    
    private boolean validateGroup(List<String> group) {
        if (group.size() != 4) return false;
        
        int length = group.get(0).length();
        if (length % 3 != 0) return false;
        if (!group.get(3).trim().isEmpty()) return false;
        
        for (String line : group) {
            if (line.length() != length) return false;
        }
        return true;
    }
    
    private String safeSubstring(String str, int start, int length) {
        if (start >= str.length()) return "   ";
        int end = Math.min(start + length, str.length());
        String substr = str.substring(start, end);
        return String.format("%-" + length + "s", substr).replace('\0', ' ');
    }
}
