#include "knapsack.h"
#include <vector>
#include <algorithm>

namespace knapsack
{

int maximum_value(int capacity, const std::vector<Item>& items)
{
    // Create a DP table with capacity+1 columns and items.size()+1 rows
    std::vector<std::vector<int>> dp(items.size() + 1, std::vector<int>(capacity + 1, 0));

    // Build table in bottom up manner
    for (size_t i = 1; i <= items.size(); i++) {
        for (int w = 0; w <= capacity; w++) {
            if (items[i-1].weight <= w) {
                // Max of including or excluding the item
                dp[i][w] = std::max(dp[i-1][w], 
                                  dp[i-1][w-items[i-1].weight] + items[i-1].value);
            } else {
                // Can't include this item
                dp[i][w] = dp[i-1][w];
            }
        }
    }

    // The last cell contains the maximum value
    return dp[items.size()][capacity];
}

} // namespace knapsack

