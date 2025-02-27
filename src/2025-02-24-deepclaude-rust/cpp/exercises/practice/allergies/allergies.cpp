#include "allergies.h"
#include <vector>
#include <utility>

namespace allergies {

allergy_test::allergy_test(unsigned int score) {
    const std::vector<std::pair<unsigned int, std::string>> allergens = {
        {1, "eggs"},
        {2, "peanuts"},
        {4, "shellfish"},
        {8, "strawberries"},
        {16, "tomatoes"},
        {32, "chocolate"},
        {64, "pollen"},
        {128, "cats"}
    };

    for (const auto& allergen : allergens) {
        if (score & allergen.first) {
            allergies_.emplace(allergen.second);
        }
    }
}

bool allergy_test::is_allergic_to(const std::string& allergen) const {
    return allergies_.count(allergen) > 0;
}

const std::unordered_set<std::string>& allergy_test::get_allergies() const {
    return allergies_;
}

}  // namespace allergies
