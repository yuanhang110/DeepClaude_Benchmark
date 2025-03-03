import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class ChangeCalculator {

    private final List<Integer> coins;

    ChangeCalculator(List<Integer> currencyCoins) {
        if (currencyCoins == null || currencyCoins.isEmpty()) {
            throw new IllegalArgumentException("At least one coin denomination must be provided");
        }
        
        // Check for invalid coin values
        for (Integer coin : currencyCoins) {
            if (coin <= 0) {
                throw new IllegalArgumentException("All coins must have positive values");
            }
        }
        
        // Store a sorted copy of the coins (removing duplicates)
        this.coins = new ArrayList<>();
        for (Integer coin : currencyCoins) {
            if (!this.coins.contains(coin)) {
                this.coins.add(coin);
            }
        }
        Collections.sort(this.coins);
    }

    List<Integer> computeMostEfficientChange(int grandTotal) {
        // Handle edge cases
        if (grandTotal < 0) {
            throw new IllegalArgumentException("Cannot make change for negative amount");
        }
        
        if (grandTotal == 0) {
            return new ArrayList<>();
        }
        
        
        // Dynamic programming to find minimum number of coins
        int[] dp = new int[grandTotal + 1];
        int[] coinUsed = new int[grandTotal + 1];
        
        // Initialize with a value larger than any possible solution
        Arrays.fill(dp, Integer.MAX_VALUE - 1);
        dp[0] = 0;
        
        // Fill the dp array
        for (int i = 1; i <= grandTotal; i++) {
            for (int coin : coins) {
                if (coin <= i && dp[i - coin] + 1 < dp[i]) {
                    dp[i] = dp[i - coin] + 1;
                    coinUsed[i] = coin;
                }
            }
        }
        
        // Check if a solution exists
        if (dp[grandTotal] == Integer.MAX_VALUE - 1) {
            throw new IllegalArgumentException("The total " + grandTotal + " cannot be represented in the given currency");
        }
        
        // Reconstruct the solution
        List<Integer> change = new ArrayList<>();
        int remaining = grandTotal;
        
        while (remaining > 0) {
            int coin = coinUsed[remaining];
            if (coin == 0) {
                throw new IllegalArgumentException("The total " + grandTotal + " cannot be represented in the given currency");
            }
            change.add(coin);
            remaining -= coin;
        }
        return change;
    }

}
