#include "knapsack.h"
#include <algorithm>
#include <vector>

namespace knapsack
{

int maximum_value(int maximumWeight, const std::vector<Item>& items) {
    int n = items.size();
    if (n == 0 || maximumWeight <= 0) {
        return {};
    }

    std::vector<std::vector<int>> dp(n + 1, std::vector<int>(maximumWeight + 1, 0));

    for (int i = 1; i <= n; ++i) {
        int current_weight = items[i-1].weight;
        int current_value = items[i-1].value;

        for (int w = 0; w <= maximumWeight; ++w) {
            if (current_weight > w) {
                dp[i][w] = dp[i-1][w];
            } else {
                dp[i][w] = std::max(dp[i-1][w], dp[i-1][w - current_weight] + current_value);
            }
        }
    }

    return dp[n][maximumWeight];
}

} // namespace knapsack

