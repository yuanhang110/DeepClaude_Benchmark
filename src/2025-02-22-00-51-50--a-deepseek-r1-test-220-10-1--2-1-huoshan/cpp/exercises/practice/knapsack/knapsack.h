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

int maximum_value(int maximumWeight, const std::vector<Item>& items);

} // namespace knapsack

#endif // KNAPSACK_H
