#include "sublist.h"
#include <algorithm>

namespace sublist {

bool is_sublist(const std::vector<int>& list1, const std::vector<int>& list2) {
    if (list1.empty()) return true;
    if (list2.empty()) return false;
    
    auto it = std::search(list2.begin(), list2.end(), list1.begin(), list1.end());
    return it != list2.end();
}

ListComparison sublist(const std::vector<int>& list1, const std::vector<int>& list2) {
    if (list1 == list2) {
        return ListComparison::equal;
    }
    
    if (is_sublist(list1, list2)) {
        return ListComparison::sublist;
    }
    
    if (is_sublist(list2, list1)) {
        return ListComparison::superlist;
    }
    
    return ListComparison::unequal;
}

}  // namespace sublist
