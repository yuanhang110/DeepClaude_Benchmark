#include "knapsack.h"
#include <algorithm>

namespace knapsack
{

int maximum_value(int maximumWeight, const std::vector<Item>& items)
{
    if (items.empty() || maximumWeight <= 0)
        return 0;
    
    // Initialize DP array with zeros (all weights have value 0 initially)
    std::vector<int> dp(maximumWeight + 1, 0);
    
    // Process each item
    for (const auto& item : items) {
        // For each weight from maximum down to the item's weight
        for (int w = maximumWeight; w >= item.weight; --w) {
            // Compare current value with value after taking this item
            dp[w] = std::max(dp[w], dp[w - item.weight] + item.value);
        }
    }
    
    // Maximum value is stored in dp[maximumWeight]
    return dp[maximumWeight];
}

} // namespace knapsack

