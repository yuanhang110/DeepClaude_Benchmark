import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class ChangeCalculator {
    private final List<Integer> coins;

    ChangeCalculator(List<Integer> currencyCoins) {
        if (currencyCoins.isEmpty()) {
            throw new IllegalArgumentException("At least one coin is required");
        }
        if (currencyCoins.stream().anyMatch(coin -> coin <= 0)) {
            throw new IllegalArgumentException("Coins must be positive");
        }
        this.coins = currencyCoins.stream()
            .distinct()
            .sorted(Collections.reverseOrder())
            .collect(Collectors.toList());
    }

    List<Integer> computeMostEfficientChange(int grandTotal) {
        if (grandTotal < 0) {
            throw new IllegalArgumentException("Negative amount is invalid");
        }
        if (grandTotal == 0) {
            return new ArrayList<>();
        }

        List<List<Integer>> dp = new ArrayList<>(grandTotal + 1);
        for (int i = 0; i <= grandTotal; i++) {
            dp.add(null);
        }
        dp.set(0, new ArrayList<>());

        for (int amount = 1; amount <= grandTotal; amount++) {
            for (int coin : coins) {
                if (coin <= amount) {
                    int remaining = amount - coin;
                    List<Integer> prevList = dp.get(remaining);
                    if (prevList != null) {
                        List<Integer> newList = new ArrayList<>(prevList);
                        newList.add(coin);
                        if (dp.get(amount) == null || newList.size() < dp.get(amount).size()) {
                            dp.set(amount, newList);
                        }
                    }
                }
            }
        }

        List<Integer> result = dp.get(grandTotal);
        if (result == null) {
            throw new IllegalArgumentException("No combination found for amount " + grandTotal);
        }

        Collections.sort(result);
        return result;
    }

}
