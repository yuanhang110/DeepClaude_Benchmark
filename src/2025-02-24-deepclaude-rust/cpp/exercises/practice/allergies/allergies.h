#if !defined(ALLERGIES_H)
#define ALLERGIES_H

#include <unordered_set>
#include <string>

namespace allergies {

class allergy_test {
public:
    explicit allergy_test(unsigned int score);
    bool is_allergic_to(const std::string& allergen) const;
    const std::unordered_set<std::string>& get_allergies() const;

private:
    std::unordered_set<std::string> allergies_;
};

}  // namespace allergies

#endif // ALLERGIES_H
