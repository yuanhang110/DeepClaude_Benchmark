#pragma once
#include <vector>

namespace sublist {

enum class ListComparison {
    equal,
    unequal,
    sublist,
    superlist
};

ListComparison compare(const std::vector<int>& list1, const std::vector<int>& list2);

}  // namespace sublist
