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
        for (int i = 0; i < maxLength; i++) {
            StringBuilder transposedRow = new StringBuilder();
            for (String row : rows) {
                char c = (i < row.length()) ? row.charAt(i) : ' ';
                transposedRow.append(c);
            }
            String temp = transposedRow.toString();
            int end = temp.length();
            while (end > 0 && temp.charAt(end - 1) == ' ') {
                end--;
            }
            String trimmed = temp.substring(0, end);
            if (result.length() > 0) {
                result.append('\n');
            }
            result.append(trimmed);
        }
        return result.toString();
    }
}
