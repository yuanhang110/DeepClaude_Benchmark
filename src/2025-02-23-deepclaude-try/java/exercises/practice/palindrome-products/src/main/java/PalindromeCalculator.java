import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

class PalindromeCalculator {

    SortedMap<Long, List<List<Integer>>> getPalindromeProductsWithFactors(int minFactor, int maxFactor) {
        if (minFactor > maxFactor) {
            throw new IllegalArgumentException("Invalid range");
        }

        Map<Long, List<List<Integer>>> palindromes = new HashMap<>();

        for (int i = minFactor; i <= maxFactor; i++) {
            for (int j = i; j <= maxFactor; j++) {
                long product = (long) i * j;
                if (isPalindrome(product)) {
                    List<Integer> factors = Arrays.asList(i, j);
                    palindromes.computeIfAbsent(product, k -> new ArrayList<>()).add(factors);
                }
            }
        }

        if (palindromes.isEmpty()) {
            throw new UnsupportedOperationException("No palindromes found");
        }

        long min = Collections.min(palindromes.keySet());
        long max = Collections.max(palindromes.keySet());

        SortedMap<Long, List<List<Integer>>> result = new TreeMap<>();
        result.put(min, palindromes.get(min));
        result.put(max, palindromes.get(max));

        return result;
    }

    private boolean isPalindrome(long number) {
        String str = Long.toString(number);
        int left = 0;
        int right = str.length() - 1;
        while (left < right) {
            if (str.charAt(left++) != str.charAt(right--)) {
                return false;
            }
        }
        return true;
    }
}
