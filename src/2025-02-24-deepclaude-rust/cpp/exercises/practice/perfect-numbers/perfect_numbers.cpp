#include "perfect_numbers.h"

#include <stdexcept>

namespace perfect_numbers {

classification classify(int n) {
    if (n < 1) {
        throw std::domain_error("Number must be positive");
    }

    int aliquot_sum = 0;
    for (int i = 1; i <= n / 2; ++i) {
        if (n % i == 0) {
            aliquot_sum += i;
        }
    }

    if (aliquot_sum == n) {
        return classification::perfect;
    } else if (aliquot_sum > n) {
        return classification::abundant;
    } else {
        return classification::deficient;
    }
}

}  // namespace perfect_numbers
