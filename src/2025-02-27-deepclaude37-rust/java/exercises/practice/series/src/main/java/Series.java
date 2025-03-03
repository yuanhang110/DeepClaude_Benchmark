import java.util.ArrayList;
import java.util.List;

class Series {
    private String str;

    Series(String string) {
        if (string == null) {
            throw new IllegalArgumentException("String cannot be null");
        }
        this.str = string;
    }

    List<String> slices(int num) {
        if (num <= 0) {
            throw new IllegalArgumentException("Slice length must be at least 1");
        }
        if (num > str.length()) {
            throw new IllegalArgumentException("Slice length is too long");
        }
        
        List<String> result = new ArrayList<>();
        for (int i = 0; i <= str.length() - num; i++) {
            result.add(str.substring(i, i + num));
        }
        return result;
    }
}
