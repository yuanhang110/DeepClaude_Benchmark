#include "allergies.h"
#include <vector>
#include <utility>

namespace allergies {

allergy_test::allergy_test(unsigned int score) {
    const std::vector<std::pair<std::string, unsigned int>> allergens = {
        {"eggs", 1},
        {"peanuts", 2},
        {"shellfish", 4},
        {"strawberries", 8},
        {"tomatoes", 16},
        {"chocolate", 32},
        {"pollen", 64},
        {"cats", 128}
    };

    for (const auto& allergen : allergens) {
        if (score & allergen.second) {
            allergies_.emplace(allergen.first);
        }
    }
}

bool allergy_test::is_allergic_to(const std::string& allergen) const {
    return allergies_.find(allergen) != allergies_.end();
}

const std::unordered_set<std::string>& allergy_test::get_allergies() const {
    return allergies_;
}

}  // namespace allergies
