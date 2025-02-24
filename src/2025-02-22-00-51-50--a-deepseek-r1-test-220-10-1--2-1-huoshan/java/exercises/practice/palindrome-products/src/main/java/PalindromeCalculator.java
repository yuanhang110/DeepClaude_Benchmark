import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

class PalindromeCalculator {

    SortedMap<Long, List<List<Integer>>> getPalindromeProductsWithFactors(int minFactor, int maxFactor) {
        SortedMap<Long, List<List<Integer>>> result = new TreeMap<>();
        if (minFactor > maxFactor) {
            return result;
        }

        Map<Long, List<List<Integer>>> palindromes = new HashMap<>();

        for (int a = minFactor; a <= maxFactor; a++) {
            for (int b = a; b <= maxFactor; b++) {
                long product = (long) a * b;
                if (isPalindrome(product)) {
                    List<Integer> factors = new ArrayList<>();
                    factors.add(a);
                    factors.add(b);
                    palindromes.computeIfAbsent(product, k -> new ArrayList<>()).add(factors);
                }
            }
        }

        if (palindromes.isEmpty()) {
            return result;
        }

        long minPalindrome = Collections.min(palindromes.keySet());
        long maxPalindrome = Collections.max(palindromes.keySet());

        result.put(minPalindrome, palindromes.get(minPalindrome));
        if (minPalindrome != maxPalindrome) {
            result.put(maxPalindrome, palindromes.get(maxPalindrome));
        }

        return result;
    }

    private boolean isPalindrome(long number) {
        String s = String.valueOf(number);
        return new StringBuilder(s).reverse().toString().equals(s);
    }

}
