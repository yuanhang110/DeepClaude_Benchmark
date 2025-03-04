import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

class ChangeCalculator {
    private final List<Integer> coins;

    ChangeCalculator(List<Integer> currencyCoins) {
        if (currencyCoins == null || currencyCoins.isEmpty()) {
            throw new IllegalArgumentException("Coin list cannot be null or empty");
        }
        // Create a new sorted list to avoid modifying the input
        this.coins = new ArrayList<>(currencyCoins);
        Collections.sort(this.coins, Collections.reverseOrder());
    }

    List<Integer> computeMostEfficientChange(int grandTotal) {
        if (grandTotal < 0) {
            throw new IllegalArgumentException("Negative amount is not allowed");
        }
        
        if (grandTotal == 0) {
            return new ArrayList<>();
        }

        int minCoin = Collections.min(coins);
        if (grandTotal > 0 && grandTotal < minCoin) {
            throw new IllegalArgumentException("Amount " + grandTotal + " is smaller than the smallest coin " + minCoin);
        }

        List<Integer> change = new ArrayList<>();
        int remaining = grandTotal;
        
        for (int coin : coins) {
            if (coin <= 0) {
                throw new IllegalStateException("Coin values must be positive");
            }
            while (remaining >= coin) {
                change.add(coin);
                remaining -= coin;
            }
        }

        if (remaining != 0) {
            throw new IllegalArgumentException("Cannot make exact change for " + grandTotal + " with given coins");
        }

        return change;
    }
}
