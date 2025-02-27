#include "knapsack.h"
#include <algorithm>

namespace knapsack
{

int maximum_value(int maximumWeight, const std::vector<Item>& items) {
    int sumZeroWeights = 0;
    std::vector<Item> validItems;
    
    // Separate zero-weight items and sum their values
    for (const auto& item : items) {
        if (item.weight == 0) {
            sumZeroWeights += item.value;
        } else if (item.weight > 0) {
            validItems.push_back(item);
        }
    }
    
    // If only zero-weight items or no capacity, return their sum
    if (validItems.empty() || maximumWeight <= 0) {
        return sumZeroWeights;
    }
    
    std::vector<int> dp(maximumWeight + 1, 0);
    
    // Classic 0-1 knapsack DP for items with positive weight
    for (const auto& item : validItems) {
        for (int w = maximumWeight; w >= item.weight; --w) {
            dp[w] = std::max(dp[w], dp[w - item.weight] + item.value);
        }
    }
    
    return sumZeroWeights + dp[maximumWeight];
}

} // namespace knapsack

