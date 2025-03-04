#include "knapsack.h"

namespace knapsack
{

int maximum_value(const std::vector<Item>& items, int maxWeight) {
    if (maxWeight <= 0 || items.empty()) {
        return 0;
    }
    
    std::vector<int> dp(maxWeight + 1, 0);
    
    for (const auto& item : items) {
        for (int w = maxWeight; w >= item.weight; --w) {
            if (dp[w - item.weight] + item.value > dp[w]) {
                dp[w] = dp[w - item.weight] + item.value;
            }
        }
    }
    
    return dp[maxWeight];
}

} // namespace knapsack

