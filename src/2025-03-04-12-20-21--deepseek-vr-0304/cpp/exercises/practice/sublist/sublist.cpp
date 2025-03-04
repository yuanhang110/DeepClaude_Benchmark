#include "sublist.h"

#include <vector>

namespace sublist {

namespace {

bool is_sublist(const std::vector<int>& x, const std::vector<int>& y) {
    if (y.size() < x.size()) {
        return false;
    }

    const size_t max_start = y.size() - x.size();
    for (size_t i = 0; i <= max_start; ++i) {
        size_t j;
        for (j = 0; j < x.size(); ++j) {
            if (y[i + j] != x[j]) {
                break;
            }
        }
        if (j == x.size()) {
            return true;
        }
    }
    return false;
}

} // namespace

List_comparison sublist(const std::vector<int>& a, const std::vector<int>& b) {
    if (a.empty() && b.empty()) {
        return List_comparison::equal;
    } else if (a.empty()) {
        return List_comparison::sublist;
    } else if (b.empty()) {
        return List_comparison::superlist;
    }

    if (a == b) {
        return List_comparison::equal;
    }

    if (is_sublist(b, a)) {
        return List_comparison::superlist;
    }

    if (is_sublist(a, b)) {
        return List_comparison::sublist;
    }

    return List_comparison::unequal;
}

}  // namespace sublist
