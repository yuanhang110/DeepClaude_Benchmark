#pragma once

#include <vector>

namespace sublist {

enum class List_comparison {
    equal,
    unequal,
    sublist,
    superlist
};

List_comparison sublist(const std::vector<int>& list_a, const std::vector<int>& list_b);

}  // namespace sublist
