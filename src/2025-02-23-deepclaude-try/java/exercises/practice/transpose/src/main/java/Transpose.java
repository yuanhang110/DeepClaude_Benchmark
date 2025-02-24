public class Transpose {
    public String transpose(String toTranspose) {
        if (toTranspose.isEmpty()) {
            return "";
        }
        
        String[] lines = toTranspose.split("\n");
        
        // Find maximum line length
        int maxLen = 0;
        for (String line : lines) {
            maxLen = Math.max(maxLen, line.length());
        }
        
        StringBuilder result = new StringBuilder();
        
        // Process each column
        for (int col = 0; col < maxLen; col++) {
            if (col > 0) {
                result.append("\n");
            }
            
            StringBuilder row = new StringBuilder();
            // Process each row for current column
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                // If column exists in current row, add character
                // Otherwise add space
                row.append(col < line.length() ? line.charAt(col) : ' ');
            }
            // Remove trailing spaces but preserve leading spaces
            String rowStr = row.toString();
            int endIndex = rowStr.length() - 1;
            while (endIndex >= 0 && Character.isWhitespace(rowStr.charAt(endIndex))) {
                endIndex--;
            }
            result.append(endIndex < 0 ? "" : rowStr.substring(0, endIndex + 1));
        }
        
        return result.toString();
    }
}
