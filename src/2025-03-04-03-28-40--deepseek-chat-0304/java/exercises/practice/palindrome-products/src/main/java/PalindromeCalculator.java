import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.ArrayList;

class PalindromeCalculator {

    SortedMap<Long, List<List<Integer>>> getPalindromeProductsWithFactors(int minFactor, int maxFactor) {
        if (minFactor > maxFactor) {
            throw new IllegalArgumentException("Invalid input: min is greater than max");
        }
        if (minFactor < 0 || maxFactor < 0) {
            throw new IllegalArgumentException("Factors must be non-negative");
        }

        SortedMap<Long, List<List<Integer>>> palindromes = new TreeMap<>();
        long smallestProduct = Long.MAX_VALUE;
        long largestProduct = Long.MIN_VALUE;

        for (int i = minFactor; i <= maxFactor; i++) {
            // Skip if i * maxFactor is smaller than current smallest palindrome
            if ((long) i * maxFactor < smallestProduct) {
                continue;
            }
            
            for (int j = i; j <= maxFactor; j++) {
                long product = (long) i * j;
                // Skip if product is smaller than current smallest palindrome
                if (product < smallestProduct) {
                    continue;
                }
                
                if (isPalindrome(product)) {
                    List<Integer> factors = new ArrayList<>();
                    factors.add(i);
                    factors.add(j);
                    palindromes.computeIfAbsent(product, k -> new ArrayList<>()).add(factors);
                    
                    // Update smallest and largest palindromes
                    if (product < smallestProduct) {
                        smallestProduct = product;
                    }
                    if (product > largestProduct) {
                        largestProduct = product;
                    }
                }
            }
        }

        if (palindromes.isEmpty()) {
            throw new IllegalArgumentException("No palindromes found in the given range");
        }

        return palindromes;
    }

    private boolean isPalindrome(long number) {
        if (number < 0) {
            return false;
        }
        long reversed = 0;
        long original = number;
        
        while (original != 0) {
            long digit = original % 10;
            reversed = reversed * 10 + digit;
            original /= 10;
        }
        
        return number == reversed;
    }
}
