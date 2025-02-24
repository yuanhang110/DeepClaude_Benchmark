#include "knapsack.h"
#include <algorithm>

namespace knapsack
{

int maximum_value(int max_weight, const std::vector<Item>& items) {
    std::vector<int> dp(max_weight + 1, 0);
    
    for (const auto& item : items) {
        for (int w = max_weight; w >= item.weight; --w) {
            dp[w] = std::max(dp[w], dp[w - item.weight] + item.value);
        }
    }
    
    return dp[max_weight];
}

} // namespace knapsack

