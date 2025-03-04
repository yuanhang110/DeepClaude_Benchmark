#if !defined(ALLERGIES_H)
#define ALLERGIES_H

#include <unordered_set>
#include <string>

namespace allergies {

inline AllergyTest allergy_test(unsigned int score) {
    return AllergyTest(score);
}

class AllergyTest {
public:
    explicit AllergyTest(unsigned int score);
    
    bool is_allergic_to(const std::string& item) const;
    std::unordered_set<std::string> get_allergies() const;

private:
    unsigned int score_;
    
    const std::unordered_set<std::string> allergen_names = {
        "eggs", "peanuts", "shellfish", "strawberries",
        "tomatoes", "chocolate", "pollen", "cats"
    };
    
    const std::unordered_set<unsigned int> allergen_values = {
        1, 2, 4, 8, 16, 32, 64, 128
    };
};

}  // namespace allergies

#endif // ALLERGIES_H
