public class Transpose {
    public String transpose(String toTranspose) {
        if (toTranspose.isEmpty()) {
            return "";
        }
        
        String[] lines = toTranspose.split("\n");
        int maxLength = 0;
        for (String line : lines) {
            if (line.length() > maxLength) {
                maxLength = line.length();
            }
        }
        
        List<String> transposed = new ArrayList<>();
        for (int i = 0; i < maxLength; i++) {
            StringBuilder sb = new StringBuilder();
            for (String line : lines) {
                if (i < line.length()) {
                    sb.append(line.charAt(i));
                } else {
                    sb.append(' ');
                }
            }
            String row = sb.toString();
            // Trim trailing spaces
            int end = row.length();
            while (end > 0 && row.charAt(end - 1) == ' ') {
                end--;
            }
            row = row.substring(0, end);
            transposed.add(row);
        }
        return String.join("\n", transposed);
    }
}
