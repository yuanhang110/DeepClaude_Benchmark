#include "allergies.h"

namespace allergies {

allergy_test::allergy_test(unsigned int score) {
    if (score & 1) allergies_.insert("eggs");
    if (score & 2) allergies_.insert("peanuts");
    if (score & 4) allergies_.insert("shellfish");
    if (score & 8) allergies_.insert("strawberries");
    if (score & 16) allergies_.insert("tomatoes");
    if (score & 32) allergies_.insert("chocolate");
    if (score & 64) allergies_.insert("pollen");
    if (score & 128) allergies_.insert("cats");
}

bool allergy_test::is_allergic_to(const std::string& item) const {
    return allergies_.find(item) != allergies_.end();
}

std::unordered_set<std::string> allergy_test::get_allergies() const {
    return allergies_;
}

}  // namespace allergies
