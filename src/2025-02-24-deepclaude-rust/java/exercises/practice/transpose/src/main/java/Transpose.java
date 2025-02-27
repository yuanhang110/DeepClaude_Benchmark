public class Transpose {
    public String transpose(String toTranspose) {
        if (toTranspose.isEmpty()) {
            return "";
        }
        
        String[] rows = toTranspose.split("\n");
        int maxLength = 0;
        for (String row : rows) {
            if (row.length() > maxLength) {
                maxLength = row.length();
            }
        }
        
        StringBuilder result = new StringBuilder();
        for (int c = 0; c < maxLength; c++) {
            StringBuilder transposedRow = new StringBuilder();
            for (String row : rows) {
                char ch = (c < row.length()) ? row.charAt(c) : ' ';
                transposedRow.append(ch);
            }
            // Trim trailing spaces
            int endIndex = transposedRow.length();
            while (endIndex > 0 && transposedRow.charAt(endIndex - 1) == ' ') {
                endIndex--;
            }
            String trimmedRow = transposedRow.substring(0, endIndex);
            result.append(trimmedRow);
            if (c < maxLength - 1) {
                result.append('\n');
            }
        }
        return result.toString();
    }
}
