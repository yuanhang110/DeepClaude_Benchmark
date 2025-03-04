import java.util.ArrayList;
import java.util.List;

/**
 * The Series class provides functionality to extract contiguous substrings
 * of a specified length from a given string.
 */
class Series {
    private final String string;

    /**
     * Constructs a Series object with the given input string.
     * @param string The input string to process (must not be null or empty)
     * @throws IllegalArgumentException if the input string is null or empty
     */
    Series(String string) {
        if (string == null) {
            throw new IllegalArgumentException("String cannot be null");
        }
        if (string.isEmpty()) {
            throw new IllegalArgumentException("String cannot be empty");
        }
        this.string = string;
    }

    /**
     * Returns a list of all contiguous substrings of the specified length.
     * @param num The length of the substrings to extract
     * @return List of substrings
     * @throws IllegalArgumentException if num is not positive or exceeds string length
     */
    List<String> slices(int num) {
        if (num <= 0) {
            throw new IllegalArgumentException("Slice length must be positive");
        }
        if (num > string.length()) {
            throw new IllegalArgumentException("Slice length cannot be greater than string length");
        }

        List<String> result = new ArrayList<>();
        for (int i = 0; i <= string.length() - num; i++) {
            result.add(string.substring(i, i + num));
        }
        return result;
    }
}
