import java.util.List;

class OpticalCharacterReader {

    String parse(List<String> input) {
        if (input.size() % 4 != 0) {
            throw new IllegalArgumentException("Number of input lines must be a multiple of four");
        }
        
        int expectedLength = -1;
        for (String line : input) {
            if (line.length() % 3 != 0) {
                throw new IllegalArgumentException("Each line's length must be a multiple of three");
            }
            if (expectedLength == -1) {
                expectedLength = line.length();
            } else if (line.length() != expectedLength) {
                throw new IllegalArgumentException("All lines must have the same length");
            }
        }
        
        StringBuilder result = new StringBuilder();
        int groupCount = input.size() / 4;
        for (int group = 0; group < groupCount; group++) {
            List<String> groupLines = input.subList(group * 4, (group + 1) * 4);
            if (!groupLines.get(3).trim().isEmpty()) {
                if (result.length() > 0) {
                    result.append(",");
                }
                result.append("?");
                continue;
            }
            
            String line1 = groupLines.get(0);
            String line2 = groupLines.get(1);
            String line3 = groupLines.get(2);
            int digitCount = line1.length() / 3;
            StringBuilder lineResult = new StringBuilder();
            
            for (int digit = 0; digit < digitCount; digit++) {
                int pos = digit * 3;
                String l1 = line1.substring(pos, pos + 3);
                String l2 = line2.substring(pos, pos + 3);
                String l3 = line3.substring(pos, pos + 3);
                lineResult.append(recognizeDigit(l1, l2, l3));
            }
            
            if (group > 0) {
                result.append(",");
            }
            result.append(lineResult);
        }
        
        return result.toString();
    }
    
    private char recognizeDigit(String line1, String line2, String line3) {
        String pattern = line1 + line2 + line3;
        switch (pattern) {
            case " _ | ||_|": return '0';
            case "     |  |": return '1';
            case " _  _||_ ": return '2';
            case " _  _| _|": return '3';
            case "   |_|  |": return '4';
            case " _ |_  _|": return '5';
            case " _ |_ |_|": return '6';
            case " _   |  |": return '7';
            case " _ |_||_|": return '8';
            case " _ |_| _|": return '9';
            default: return '?';
        }
    }
}
