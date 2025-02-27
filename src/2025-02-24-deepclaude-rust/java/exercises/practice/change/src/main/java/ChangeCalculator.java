import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

class ChangeCalculator {
    private final List<Integer> coins;

    ChangeCalculator(List<Integer> currencyCoins) {
        if (currencyCoins == null || currencyCoins.isEmpty()) {
            throw new IllegalArgumentException("At least one coin must be provided.");
        }

        for (Integer coin : currencyCoins) {
            if (coin == null || coin <= 0) {
                throw new IllegalArgumentException("Coins must be positive integers.");
            }
        }

        this.coins = currencyCoins.stream()
                .distinct()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
    }

    List<Integer> computeMostEfficientChange(int grandTotal) {
        if (grandTotal < 0) {
            throw new IllegalArgumentException("Cannot compute negative change.");
        }

        if (grandTotal == 0) {
            return new ArrayList<>();
        }

        int minCoin = coins.get(0);
        if (minCoin > grandTotal) {
            throw new IllegalArgumentException("The smallest coin is larger than the amount.");
        }

        Queue<Integer> queue = new LinkedList<>();
        queue.add(grandTotal);
        Map<Integer, List<Integer>> coinsUsed = new HashMap<>();
        coinsUsed.put(grandTotal, new ArrayList<>());
        Set<Integer> visited = new HashSet<>();
        visited.add(grandTotal);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            for (int coin : coins) {
                int next = current - coin;
                if (next < 0) {
                    continue;
                }
                if (next == 0) {
                    List<Integer> result = new ArrayList<>(coinsUsed.get(current));
                    result.add(coin);
                    Collections.sort(result);
                    return result;
                }
                if (!visited.contains(next)) {
                    visited.add(next);
                    List<Integer> newCoins = new ArrayList<>(coinsUsed.get(current));
                    newCoins.add(coin);
                    coinsUsed.put(next, newCoins);
                    queue.add(next);
                }
            }
        }

        throw new IllegalArgumentException("No combination can form the amount.");
    }
}
