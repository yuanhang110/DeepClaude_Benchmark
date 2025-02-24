import java.util.List;
import java.util.ArrayList;

class Series {
    private final String digits;

    Series(String string) {
        if (string == null) {
            throw new IllegalArgumentException("Series string cannot be null");
        }
        this.digits = string;
    }

    List<String> slices(int num) {
        if (num <= 0) {
            throw new IllegalArgumentException("Slice length cannot be non-positive");
        }
        if (num > digits.length()) {
            throw new IllegalArgumentException("Slice length cannot be greater than series length");
        }

        List<String> result = new ArrayList<>();
        for (int i = 0; i <= digits.length() - num; i++) {
            result.add(digits.substring(i, i + num));
        }
        return result;
    }
}
