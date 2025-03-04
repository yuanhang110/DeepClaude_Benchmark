#include "allergies.h"

namespace allergies {

AllergyTest::AllergyTest(unsigned int score) : score_(score) {}

bool AllergyTest::is_allergic_to(const std::string& item) const {
    // Find the value for the given item
    unsigned int value = 0;
    if (item == "eggs") value = 1;
    else if (item == "peanuts") value = 2;
    else if (item == "shellfish") value = 4;
    else if (item == "strawberries") value = 8;
    else if (item == "tomatoes") value = 16;
    else if (item == "chocolate") value = 32;
    else if (item == "pollen") value = 64;
    else if (item == "cats") value = 128;
    
    // Check if the value is set in the score
    return (score_ & value) == value;
}

std::unordered_set<std::string> AllergyTest::get_allergies() const {
    std::unordered_set<std::string> allergies;
    
    if (is_allergic_to("eggs")) allergies.emplace("eggs");
    if (is_allergic_to("peanuts")) allergies.emplace("peanuts");
    if (is_allergic_to("shellfish")) allergies.emplace("shellfish");
    if (is_allergic_to("strawberries")) allergies.emplace("strawberries");
    if (is_allergic_to("tomatoes")) allergies.emplace("tomatoes");
    if (is_allergic_to("chocolate")) allergies.emplace("chocolate");
    if (is_allergic_to("pollen")) allergies.emplace("pollen");
    if (is_allergic_to("cats")) allergies.emplace("cats");
    
    return allergies;
}

}  // namespace allergies
