import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

class BookStore {
    private static final double BOOK_PRICE = 8.0;
    private static final double[] DISCOUNTS = {0, 0, 0.05, 0.10, 0.20, 0.25};

    double calculateBasketCost(List<Integer> books) {
        if (books == null || books.isEmpty()) {
            return 0.0;
        }

        // Validate book numbers are between 1 and 5
        for (int book : books) {
            if (book < 1 || book > 5) {
                throw new IllegalArgumentException("Book numbers must be between 1 and 5");
            }
        }

        // Count how many of each book we have
        int[] counts = new int[5];
        for (int book : books) {
            counts[book - 1]++;
        }

        // Try different grouping strategies to find the best discount
        double total1 = calculateTotal(counts.clone(), 5);
        double total2 = calculateTotal(counts.clone(), 4);
        
        return Math.min(total1, total2);
    }

    private double calculateTotal(int[] counts, int maxGroupSize) {
        double total = 0.0;
        int[] tempCounts = counts.clone();
        
        while (true) {
            int groupSize = 0;
            int uniqueBooks = 0;
            
            // Try to form the largest possible group
            for (int i = 0; i < tempCounts.length; i++) {
                if (tempCounts[i] > 0) {
                    groupSize++;
                    uniqueBooks++;
                    tempCounts[i]--;
                }
                
                if (groupSize == maxGroupSize) {
                    break;
                }
            }
            
            if (groupSize == 0) {
                break;
            }
            
            // Calculate price for this group
            total += groupSize * BOOK_PRICE * (1 - DISCOUNTS[uniqueBooks]);
            
            // Try to form smaller groups if we couldn't make a full group
            if (groupSize < maxGroupSize) {
                maxGroupSize = groupSize;
            }
        }
        
        return total;
    }
}
