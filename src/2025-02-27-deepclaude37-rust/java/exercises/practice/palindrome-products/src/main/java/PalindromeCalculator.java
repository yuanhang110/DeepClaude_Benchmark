import java.util.List;
import java.util.SortedMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

class PalindromeCalculator {

    SortedMap<Long, List<List<Integer>>> getPalindromeProductsWithFactors(int minFactor, int maxFactor) {
        if (minFactor > maxFactor) {
            throw new IllegalArgumentException("Invalid input: min > max");
        }
        
        if (minFactor < 1) {
            throw new IllegalArgumentException("Input error: min must be >= 1");
        }
        
        TreeMap<Long, List<List<Integer>>> palindromes = new TreeMap<>();
        
        for (int i = minFactor; i <= maxFactor; i++) {
            for (int j = i; j <= maxFactor; j++) {
                long product = (long) i * j;
                if (isPalindromeNumber(product)) {
                    List<Integer> factors = new ArrayList<>(2);
                    factors.add(i);
                    factors.add(j);
                    
                    if (!palindromes.containsKey(product)) {
                        palindromes.put(product, new ArrayList<>());
                    }
                    palindromes.get(product).add(factors);
                }
            }
        }
        
        if (palindromes.isEmpty()) {
            throw new IllegalArgumentException("No palindrome found with factors in the given range.");
        }
        
        return palindromes;
    }
    
    private boolean isPalindromeNumber(long number) {
        // Handle single-digit numbers
        if (number >= 0 && number <= 9) {
            return true;
        }
        
        // Extract digits and compare
        long original = number;
        long reversed = 0;
        
        while (original > 0) {
            reversed = reversed * 10 + original % 10;
            original /= 10;
        }
        
        return reversed == number;
    }

}
