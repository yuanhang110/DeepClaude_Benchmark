import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.TreeMap;

class PalindromeCalculator {

    SortedMap<Long, List<List<Integer>>> getPalindromeProductsWithFactors(int minFactor, int maxFactor) {
        if (minFactor > maxFactor) {
            throw new IllegalArgumentException("Invalid range");
        }

        Long minProduct = null;
        Long maxProduct = null;
        Map<Long, List<List<Integer>>> factorMap = new HashMap<>();

        for (int a = minFactor; a <= maxFactor; a++) {
            for (int b = a; b <= maxFactor; b++) {
                long product = (long) a * b;
                if (isPalindrome(product)) {
                    List<Integer> factors = Arrays.asList(a, b);
                    factorMap.computeIfAbsent(product, k -> new ArrayList<>()).add(factors);
                    
                    if (minProduct == null || product < minProduct) {
                        minProduct = product;
                    }
                    if (maxProduct == null || product > maxProduct) {
                        maxProduct = product;
                    }
                }
            }
        }

        if (minProduct == null) {
            throw new NoSuchElementException("No palindromes found");
        }

        TreeMap<Long, List<List<Integer>>> result = new TreeMap<>();
        result.put(minProduct, factorMap.get(minProduct));
        if (!minProduct.equals(maxProduct)) {
            result.put(maxProduct, factorMap.get(maxProduct));
        }

        return result;
    }

    private static boolean isPalindrome(long number) {
        String s = Long.toString(number);
        int left = 0;
        int right = s.length() - 1;
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

}
