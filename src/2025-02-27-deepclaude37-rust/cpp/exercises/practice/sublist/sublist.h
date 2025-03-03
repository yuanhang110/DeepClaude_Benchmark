#pragma once
#include <vector>

namespace sublist {

enum class List_comparison {
    equal,
    sublist,
    superlist,
    unequal
};

List_comparison sublist(const std::vector<int>& list_a, const std::vector<int>& list_b);

}  // namespace sublist
