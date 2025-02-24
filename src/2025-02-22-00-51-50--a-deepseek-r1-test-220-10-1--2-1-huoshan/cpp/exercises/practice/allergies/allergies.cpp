#include "allergies.h"

namespace allergies {

bool AllergyTest::is_allergic_to(const std::string& allergen) const {
    unsigned int mask = 0;
    if (allergen == "eggs") mask = 1;
    else if (allergen == "peanuts") mask = 2;
    else if (allergen == "shellfish") mask = 4;
    else if (allergen == "strawberries") mask = 8;
    else if (allergen == "tomatoes") mask = 16;
    else if (allergen == "chocolate") mask = 32;
    else if (allergen == "pollen") mask = 64;
    else if (allergen == "cats") mask = 128;
    else return false; // Unknown allergens are ignored

    // Mask score to only consider valid allergens (0-255)
    return (score & 0xFF & mask) != 0;
}

std::unordered_set<std::string> AllergyTest::get_allergies() const {
    std::unordered_set<std::string> result;
    
    if (masked_score & 1) result.emplace("eggs");
    if (masked_score & 2) result.emplace("peanuts");
    if (masked_score & 4) result.emplace("shellfish");
    if (masked_score & 8) result.emplace("strawberries");
    if (masked_score & 16) result.emplace("tomatoes");
    if (masked_score & 32) result.emplace("chocolate");
    if (masked_score & 64) result.emplace("pollen");
    if (masked_score & 128) result.emplace("cats");

    return result;
}

}  // namespace allergies
