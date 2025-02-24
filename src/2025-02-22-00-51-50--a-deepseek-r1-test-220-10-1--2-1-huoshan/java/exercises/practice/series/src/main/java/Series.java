package series;

import java.util.ArrayList;
import java.util.List;

class Series {
    private final String string;

    Series(String string) {
        this.string = string;
    }

    List<String> slices(int num) {
        if (num <= 0) {
            throw new IllegalArgumentException("Slice size must be positive.");
        }
        if (num > string.length()) {
            throw new IllegalArgumentException("Slice size cannot be greater than string length.");
        }
        
        List<String> result = new ArrayList<>();
        for (int i = 0; i <= string.length() - num; i++) {
            result.add(string.substring(i, i + num));
        }
        return result;
    }
}
