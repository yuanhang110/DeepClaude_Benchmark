#include "sublist.h"

namespace sublist {

namespace {

bool is_sublist(const std::vector<int>& sub, const std::vector<int>& super) {
    if (sub.size() > super.size()) {
        return false;
    }
    if (sub.empty()) {
        return true;
    }
    const size_t max_start = super.size() - sub.size();
    for (size_t i = 0; i <= max_start; ++i) {
        bool match = true;
        for (size_t j = 0; j < sub.size(); ++j) {
            if (super[i + j] != sub[j]) {
                match = false;
                break;
            }
        }
        if (match) {
            return true;
        }
    }
    return false;
}

} // namespace

List_comparison sublist(const std::vector<int>& a, const std::vector<int>& b) {
    if (a == b) {
        return List_comparison::equal;
    }
    if (a.empty()) {
        return List_comparison::sublist;
    }
    if (b.empty()) {
        return List_comparison::superlist;
    }
    bool a_is_sublist = is_sublist(a, b);
    bool b_is_sublist = is_sublist(b, a);
    if (a_is_sublist) {
        return List_comparison::sublist;
    }
    if (b_is_sublist) {
        return List_comparison::superlist;
    }
    return List_comparison::unequal;
}

}  // namespace sublist
