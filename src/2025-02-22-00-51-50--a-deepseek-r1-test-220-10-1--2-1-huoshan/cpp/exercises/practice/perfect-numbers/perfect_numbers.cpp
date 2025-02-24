#include "perfect_numbers.h"
#include <cmath>
#include <stdexcept>

namespace perfect_numbers {

namespace {

int aliquot_sum(int n) {
    if (n == 1) {
        return 0;
    }

    int sum = 1; // 1 is a proper divisor for n > 1
    int sqrt_n = static_cast<int>(std::sqrt(n));

    for (int i = 2; i <= sqrt_n; ++i) {
        if (n % i == 0) {
            sum += i;
            int counterpart = n / i;
            if (counterpart != i && counterpart != n) {
                sum += counterpart;
            }
        }
    }
    return sum;
}

} // namespace

classification classify(int n) {
    if (n <= 0) {
        throw std::domain_error("Classification is only defined for positive integers.");
    }

    int sum = aliquot_sum(n);

    if (sum == n) {
        return classification::perfect;
    } else if (sum > n) {
        return classification::abundant;
    } else {
        return classification::deficient;
    }
}

}  // namespace perfect_numbers
