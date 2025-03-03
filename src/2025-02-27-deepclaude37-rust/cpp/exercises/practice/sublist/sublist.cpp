#include "sublist.h"

namespace sublist {

namespace {
    // Helper function to check if one list is a sublist of another
    bool is_contained(const std::vector<int>& sub, const std::vector<int>& super) {
        // An empty list is a sublist of any list
        if (sub.empty()) {
            return true;
        }
        
        // If the potential sublist is longer than the superlist, it can't be a sublist
        if (sub.size() > super.size()) {
            return false;
        }
        
        // Check all possible starting positions in the superlist
        for (size_t i = 0; i <= super.size() - sub.size(); ++i) {
            bool match = true;
            
            // Check if the subsequence starting at position i matches the sublist
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
}

List_comparison sublist(const std::vector<int>& list_a, const std::vector<int>& list_b) {
    // Check for equality first
    if (list_a == list_b) {
        return List_comparison::equal;
    }
    
    // Check if A is a sublist of B
    if (is_contained(list_a, list_b)) {
        return List_comparison::sublist;
    }
    
    // Check if B is a sublist of A (making A a superlist of B)
    if (is_contained(list_b, list_a)) {
        return List_comparison::superlist;
    }
    
    // If neither is a sublist of the other, they are unequal
    return List_comparison::unequal;
}

}  // namespace sublist
