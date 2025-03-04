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

int maximum_value(const std::vector<Item>& items, int maxWeight);

} // namespace knapsack

#endif // KNAPSACK_H
