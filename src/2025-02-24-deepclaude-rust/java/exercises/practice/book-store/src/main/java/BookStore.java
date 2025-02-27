import java.util.*;
import java.util.stream.Collectors;

class BookStore {

    private static final double[] DISCOUNTS = {0, 0, 0.05, 0.10, 0.20, 0.25};
    private Map<String, Double> memo;

    double calculateBasketCost(List<Integer> books) {
        // Count occurrences of each book (1-5)
        int[] countsArray = new int[5];
        for (int book : books) {
            countsArray[book - 1]++;
        }

        // Convert to sorted list in descending order, removing zeros
        List<Integer> counts = Arrays.stream(countsArray)
                .boxed()
                .sorted(Collections.reverseOrder())
                .filter(c -> c > 0)
                .collect(Collectors.toList());

        memo = new HashMap<>();
        return calculateCost(counts);
    }

    private double calculateCost(List<Integer> counts) {
        if (counts.isEmpty()) {
            return 0.0;
        }

        String key = String.join(",", counts.stream().map(String::valueOf).collect(Collectors.toList()));
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        double minTotal = Double.MAX_VALUE;

        int maxSetSize = counts.size();
        for (int k = 1; k <= maxSetSize; k++) {
            List<Integer> nextCounts = new ArrayList<>(counts);
            // Subtract 1 from first k elements
            for (int i = 0; i < k; i++) {
                nextCounts.set(i, nextCounts.get(i) - 1);
            }

            // Filter zeros, sort descending, and recurse
            List<Integer> newCounts = nextCounts.stream()
                    .filter(c -> c > 0)
                    .sorted(Collections.reverseOrder())
                    .collect(Collectors.toList());

            double setCost = k * 8 * (1 - DISCOUNTS[k]);
            double remainingCost = calculateCost(newCounts);
            double total = setCost + remainingCost;

            if (total < minTotal) {
                minTotal = total;
            }
        }

        memo.put(key, minTotal);
        return minTotal;
    }
}
