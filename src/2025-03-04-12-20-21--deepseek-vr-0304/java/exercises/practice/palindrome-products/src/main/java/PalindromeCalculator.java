import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

class PalindromeCalculator {

    SortedMap<Long, List<List<Integer>>> getPalindromeProductsWithFactors(int minFactor, int maxFactor) {
        if (minFactor > maxFactor) {
            throw new IllegalArgumentException("Invalid range: min > max");
        }

        Long minProduct = null;
        List<List<Integer>> minFactors = new ArrayList<>();
        Long maxProduct = null;
        List<List<Integer>> maxFactors = new ArrayList<>();

        for (int i = minFactor; i <= maxFactor; i++) {
            for (int j = i; j <= maxFactor; j++) {
                long product = (long) i * j;
                if (isPalindrome(product)) {
                    List<Integer> factors = Arrays.asList(i, j);
                    // Update min
                    if (minProduct == null || product < minProduct) {
                        minProduct = product;
                        minFactors.clear();
                        minFactors.add(factors);
                    } else if (product == minProduct) {
                        minFactors.add(factors);
                    }
                    // Update max
                    if (maxProduct == null || product > maxProduct) {
                        maxProduct = product;
                        maxFactors.clear();
                        maxFactors.add(factors);
                    } else if (product == maxProduct) {
                        maxFactors.add(factors);
                    }
                }
            }
        }

        SortedMap<Long, List<List<Integer>>> result = new TreeMap<>();
        if (minProduct != null) {
            result.put(minProduct, minFactors);
            if (!maxProduct.equals(minProduct)) {
                result.put(maxProduct, maxFactors);
            }
        }
        return result;
    }

    private boolean isPalindrome(long number) {
        if (number < 0) return false;
        long reversed = 0;
        long original = number;
        while (original > 0) {
            reversed = reversed * 10 + original % 10;
            original /= 10;
        }
        return reversed == number;
    }

}
