import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

class ChangeCalculator {

    private final List<Integer> coins;

    ChangeCalculator(List<Integer> currencyCoins) {
        if (currencyCoins == null || currencyCoins.isEmpty()) {
            throw new IllegalArgumentException("Coin list must not be empty.");
        }
        for (Integer coin : currencyCoins) {
            if (coin == null || coin <= 0) {
                throw new IllegalArgumentException("Coins must be positive integers.");
            }
        }
        Set<Integer> uniqueCoins = new TreeSet<>(Collections.reverseOrder());
        uniqueCoins.addAll(currencyCoins);
        this.coins = new ArrayList<>(uniqueCoins);
    }

    List<Integer> computeMostEfficientChange(int grandTotal) {
        if (grandTotal < 0) {
            throw new IllegalArgumentException("Negative total is not allowed.");
        }
        if (grandTotal == 0) {
            return Collections.emptyList();
        }
        int smallestCoin = coins.get(coins.size() - 1);
        if (smallestCoin > grandTotal) {
            throw new IllegalArgumentException("Total is smaller than the smallest coin.");
        }
        
        List<List<Integer>> dp = new ArrayList<>(grandTotal + 1);
        for (int i = 0; i <= grandTotal; i++) {
            dp.add(null);
        }
        dp.set(0, new ArrayList<>());
        
        for (int i = 1; i <= grandTotal; i++) {
            for (int coin : coins) {
                if (coin > i) continue;
                List<Integer> prev = dp.get(i - coin);
                if (prev != null) {
                    List<Integer> newCombination = new ArrayList<>(prev);
                    newCombination.add(coin);
                    List<Integer> current = dp.get(i);
                    if (current == null || newCombination.size() < current.size()) {
                        dp.set(i, newCombination);
                    }
                }
            }
        }
        
        if (dp.get(grandTotal) == null) {
            throw new IllegalArgumentException("No combination found.");
        }
        
        List<Integer> result = dp.get(grandTotal);
        Collections.sort(result);
        return result;
    }
}
