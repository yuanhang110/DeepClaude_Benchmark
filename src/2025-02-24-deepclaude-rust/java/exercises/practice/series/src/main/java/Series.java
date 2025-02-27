import java.util.ArrayList;
import java.util.List;

class Series {
    private final String str;

    Series(String string) {
        this.str = string;
    }

    List<String> slices(int num) {
        if (num <= 0) {
            throw new IllegalArgumentException("Slice size must be positive.");
        }
        List<String> result = new ArrayList<>();
        for (int i = 0; i <= str.length() - num; i++) {
            result.add(str.substring(i, i + num));
        }
        return result;
    }
}
