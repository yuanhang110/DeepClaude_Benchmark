#include "perfect_numbers.h"

#include <cmath>
#include <stdexcept>

namespace perfect_numbers {

classification classify(int num) {
    if (num <= 0) {
        throw std::domain_error("Number must be positive");
    }

    int aliquot_sum = 0;
    int sqrt_num = static_cast<int>(std::sqrt(num));

    for (int i = 1; i <= sqrt_num; ++i) {
        if (num % i == 0) {
            // Add i as a divisor
            if (i != num) {
                aliquot_sum += i;
            }
            
            // Add the paired divisor
            int other = num / i;
            if (other != i && other != num) {
                aliquot_sum += other;
            }
        }
    }

    if (aliquot_sum == num) {
        return classification::perfect;
    } else if (aliquot_sum > num) {
        return classification::abundant;
    } else {
        return classification::deficient;
    }
}

}  // namespace perfect_numbers
