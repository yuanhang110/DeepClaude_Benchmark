#if !defined(ALLERGIES_H)
#define ALLERGIES_H

#include <string>
#include <unordered_set>

namespace allergies {

class AllergyTest {
public:
    explicit AllergyTest(unsigned int score) : score(score & 0xFF) {}
    
    bool is_allergic_to(const std::string& allergen) const;
    std::unordered_set<std::string> get_allergies() const;

private:
    unsigned int score;
};

inline AllergyTest allergy_test(unsigned int score) {
    return AllergyTest(score);
}

}  // namespace allergies

#endif // ALLERGIES_H
