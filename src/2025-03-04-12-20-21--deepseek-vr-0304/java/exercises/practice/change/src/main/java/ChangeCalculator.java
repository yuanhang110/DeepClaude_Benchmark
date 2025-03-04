import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

class ChangeCalculator {

    private final List<Integer> coins;

    ChangeCalculator(List<Integer> currencyCoins) {
        if (currencyCoins == null || currencyCoins.isEmpty()) {
            throw new IllegalArgumentException("Currency coins list cannot be empty.");
        }
        for (int coin : currencyCoins) {
            if (coin <= 0) {
                throw new IllegalArgumentException("Coins must be positive integers.");
            }
        }
        // Deduplicate and sort coins
        Set<Integer> coinSet = new TreeSet<>(currencyCoins);
        coins = new ArrayList<>(coinSet);
    }

    List<Integer> computeMostEfficientChange(int grandTotal) {
        if (grandTotal < 0) {
            throw new IllegalArgumentException("Negative grandTotal is not allowed.");
        }
        if (grandTotal == 0) {
            return new ArrayList<>();
        }
        if (coins.get(0) > grandTotal) {
            throw new IllegalArgumentException("Cannot make change smaller than smallest coin.");
        }

        int[] minCoins = new int[grandTotal + 1];
        Arrays.fill(minCoins, Integer.MAX_VALUE);
        minCoins[0] = 0;

        int[] prevCoin = new int[grandTotal + 1];
        Arrays.fill(prevCoin, -1);

        for (int i = 1; i <= grandTotal; i++) {
            for (int coin : coins) {
                if (coin > i) break;
                if (minCoins[i - coin] != Integer.MAX_VALUE && minCoins[i - coin] + 1 < minCoins[i]) {
                    minCoins[i] = minCoins[i - coin] + 1;
                    prevCoin[i] = coin;
                }
            }
        }

        if (minCoins[grandTotal] == Integer.MAX_VALUE) {
            throw new IllegalArgumentException("No combination can form the grandTotal.");
        }

        List<Integer> change = new ArrayList<>();
        int current = grandTotal;
        while (current > 0) {
            int coin = prevCoin[current];
            if (coin == -1) {
                throw new IllegalStateException("Backtracking failed for amount " + current);
            }
            change.add(coin);
            current -= coin;
        }

        // Sort the change list in ascending order
        Collections.sort(change);
        return change;
    }

}
