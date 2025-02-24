#include "sublist.h"

namespace sublist {

namespace {

bool is_sublist(const std::vector<int>& a, const std::vector<int>& b) {
    if (a.empty()) return true;
    if (b.size() < a.size()) return false;

    for (size_t i = 0; i <= b.size() - a.size(); ++i) {
        if (std::equal(a.begin(), a.end(), b.begin() + i)) {
            return true;
        }
    }
    return false;
}

} // anonymous namespace

List_comparison sublist(const std::vector<int>& a, const std::vector<int>& b) {
    if (a == b) {
        return List_comparison::equal;
    }

    if (is_sublist(a, b)) {
        return List_comparison::sublist;
    }

    if (is_sublist(b, a)) {
        return List_comparison::superlist;
    }

    return List_comparison::unequal;
}

}  // namespace sublist
