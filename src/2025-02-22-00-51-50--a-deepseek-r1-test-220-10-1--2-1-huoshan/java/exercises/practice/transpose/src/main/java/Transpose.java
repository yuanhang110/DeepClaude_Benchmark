public class Transpose {
    public String transpose(String toTranspose) {
        if (toTranspose.isEmpty()) {
            return "";
        }
        
        String[] lines = toTranspose.split("\n");
        int maxLen = 0;
        for (String line : lines) {
            if (line.length() > maxLen) {
                maxLen = line.length();
            }
        }
        
        StringBuilder result = new StringBuilder();
        for (int col = 0; col < maxLen; col++) {
            StringBuilder transposedRow = new StringBuilder();
            int lastNonPaddingIndex = -1;
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                if (col < line.length()) {
                    char c = line.charAt(col);
                    transposedRow.append(c);
                    // Track last original character (even spaces) that's not padding
                    lastNonPaddingIndex = transposedRow.length() - 1;
                } else {
                    transposedRow.append(' ');
                }
            }
            // Trim only trailing padding spaces
            String row = lastNonPaddingIndex >= 0 
                ? transposedRow.substring(0, lastNonPaddingIndex + 1)
                : "";
            if (result.length() > 0) {
                result.append('\n');
            }
            result.append(row);
        }
        
        return result.toString();
    }
}
