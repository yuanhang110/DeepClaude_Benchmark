public class Transpose {
    public String transpose(String toTranspose) {
        if (toTranspose.isEmpty()) {
            return "";
        }

        String[] lines = toTranspose.split("\n");
        int maxLength = 0;
        
        // Find the maximum line length
        for (String line : lines) {
            if (line.length() > maxLength) {
                maxLength = line.length();
            }
        }

        // Build the transposed result
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < maxLength; i++) {
            StringBuilder row = new StringBuilder();
            for (String line : lines) {
                if (i < line.length()) {
                    row.append(line.charAt(i));
                } else if (i < maxLength) {
                    row.append(' ');
                }
            }
            // Remove trailing spaces from the row
            int lastChar = row.length() - 1;
            while (lastChar >= 0 && row.charAt(lastChar) == ' ') {
                lastChar--;
            }
            if (lastChar >= 0) {
                row.setLength(lastChar + 1);
            }
            if (i < maxLength - 1) {
                result.append(row).append("\n");
            } else {
                result.append(row);
            }
        }

        return result.toString();
    }
}
