import java.util.List;
import java.util.ArrayList;

public class OpticalCharacterReader {

    String parse(List<String> input) {
        if (input.size() % 4 != 0) {
            return "error";
        }
        
        List<String> results = new ArrayList<>();
        
        for (int i = 0; i < input.size(); i += 4) {
            List<String> group = input.subList(i, Math.min(i + 4, input.size()));
            if (group.size() < 4) {
                return "error";
            }
            
            String line1 = group.get(0);
            String line2 = group.get(1);
            String line3 = group.get(2);
            String line4 = group.get(3);
            
            if (!line4.trim().isEmpty()) {
                return "error";
            }
            
            int length = line1.length();
            if (length != line2.length() || length != line3.length() || length != line4.length() || length % 3 != 0) {
                return "error";
            }
            
            int digitCount = length / 3;
            StringBuilder number = new StringBuilder();
            
            for (int d = 0; d < digitCount; d++) {
                int start = d * 3;
                int end = start + 3;
                String l1 = line1.substring(start, end);
                String l2 = line2.substring(start, end);
                String l3 = line3.substring(start, end);
                number.append(parseDigit(l1, l2, l3));
            }
            
            results.add(number.toString());
        }
        
        return String.join(",", results);
    }
    
    private String parseDigit(String l1, String l2, String l3) {
        if (l1.equals(" _ ") && l2.equals("| |") && l3.equals("|_|")) return "0";
        if (l1.equals("   ") && l2.equals("  |") && l3.equals("  |")) return "1";
        if (l1.equals(" _ ") && l2.equals(" _|") && l3.equals("|_ ")) return "2";
        if (l1.equals(" _ ") && l2.equals(" _|") && l3.equals(" _|")) return "3";
        if (l1.equals("   ") && l2.equals("|_|") && l3.equals("  |")) return "4";
        if (l1.equals(" _ ") && l2.equals("|_ ") && l3.equals(" _|")) return "5";
        if (l1.equals(" _ ") && l2.equals("|_ ") && l3.equals("|_|")) return "6";
        if (l1.equals(" _ ") && l2.equals("  |") && l3.equals("  |")) return "7";
        if (l1.equals(" _ ") && l2.equals("|_|") && l3.equals("|_|")) return "8";
        if (l1.equals(" _ ") && l2.equals("|_|") && l3.equals(" _|")) return "9";
        return "?";
    }
}
