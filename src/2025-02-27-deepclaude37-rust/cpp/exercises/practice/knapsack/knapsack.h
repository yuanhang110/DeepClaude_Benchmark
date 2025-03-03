#ifndef KNAPSACK_H
#define KNAPSACK_H

#include <vector>

namespace knapsack
{

struct Item
{
    int weight;
    int value;
};

/**
 * Calculate the maximum value that can be obtained with a given weight limit.
 * 
 * @param maximumWeight The maximum weight the knapsack can hold.
 * @param items A vector of items, each with a weight and value.
 * @return The maximum possible value.
 */
int maximum_value(int maximumWeight, const std::vector<Item>& items);

} // namespace knapsack

#endif // KNAPSACK_H
