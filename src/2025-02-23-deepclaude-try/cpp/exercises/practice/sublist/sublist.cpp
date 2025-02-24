#include "sublist.h"

namespace sublist {

namespace {
    bool isSublist(const std::vector<int>& sub, const std::vector<int>& super) {
        if (sub.empty()) {
            return true;
        }
        if (super.size() < sub.size()) {
            return false;
        }
        for (size_t i = 0; i <= super.size() - sub.size(); ++i) {
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
}  // namespace

List_comparison sublist(const std::vector<int>& a, const std::vector<int>& b) {
    if (a == b) {
        return List_comparison::equal;
    }
    if (isSublist(a, b)) {
        return List_comparison::sublist;
    }
    if (isSublist(b, a)) {
        return List_comparison::superlist;
    }
    return List_comparison::unequal;
}

}  // namespace sublist
