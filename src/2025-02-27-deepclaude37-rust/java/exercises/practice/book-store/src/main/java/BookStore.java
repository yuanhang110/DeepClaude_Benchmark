import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class BookStore {

    double calculateBasketCost(List<Integer> books) {
        // Return 0 for empty baskets
        if (books == null || books.isEmpty()) {
            return 0.0;
        }
        
        // Count occurrences of each book (1-5)
        int[] counts = new int[5];
        for (int book : books) {
            // Validate book numbers
            if (book < 1 || book > 5) {
                throw new IllegalArgumentException("Invalid book number: " + book);
            }
            counts[book - 1]++;
        }

        // Convert counts to list, filtering zeros, and sort descending
        List<Integer> countsList = new ArrayList<>();
        for (int count : counts) {
            if (count > 0) {
                countsList.add(count);
            }
        }
        countsList.sort(Collections.reverseOrder());

        // Track number of groups of each size
        int[] groupCounts = new int[6]; // index 0 unused, indices 1-5 for group sizes 1-5

        // Form groups by taking as many different books as possible each time
        while (!countsList.isEmpty()) {
            int currentGroupSize = countsList.size();
            groupCounts[currentGroupSize]++;

            // Subtract 1 from each of the first 'currentGroupSize' elements
            for (int i = 0; i < currentGroupSize; i++) {
                countsList.set(i, countsList.get(i) - 1);
            }

            // Remove zeros and re-sort
            countsList.removeIf(count -> count == 0);
            countsList.sort(Collections.reverseOrder());
        }

        // Calculate initial total cost based on discounts
        double total = 0.0;
        final double[] discounts = {0.0, 0.05, 0.10, 0.20, 0.25}; // discounts for group sizes 1-5
        for (int groupSize = 1; groupSize <= 5; groupSize++) {
            int count = groupCounts[groupSize];
            if (count > 0) {
                total += count * groupSize * 8 * (1 - discounts[groupSize - 1]);
            }
        }

        // Apply optimization: replace pairs of 5+3 groups with 4+4 groups
        // This saves $0.40 per replacement
        int adjustments = Math.min(groupCounts[5], groupCounts[3]);
        total -= adjustments * 0.40;

        return total;
    }
}
