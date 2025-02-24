#include "perfect_numbers.h"
#include <stdexcept>

namespace perfect_numbers {

classification classify(int n) {
    if (n <= 0) {
        throw std::domain_error("Number must be positive");
    }
    
    int sum = 0;
    for (int i = 1; i < n; ++i) {
        if (n % i == 0) {
            sum += i;
        }
    }
    
    if (sum == n) {
        return classification::perfect;
    } else if (sum > n) {
        return classification::abundant;
    } else {
        return classification::deficient;
    }
}

}  // namespace perfect_numbers
