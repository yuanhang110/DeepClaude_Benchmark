#include "perfect_numbers.h"
#include <stdexcept>

namespace perfect_numbers {

namespace {
    int aliquot_sum(int number) {
        if (number <= 0) {
            throw std::domain_error("Classification is only possible for positive integers.");
        }
        
        if (number == 1) {
            return 0;
        }
        
        int sum = 1;
        for (int i = 2; i * i <= number; ++i) {
            if (number % i == 0) {
                sum += i;
                if (i != number / i) {
                    sum += number / i;
                }
            }
        }
        return sum;
    }
}

classification classify(int number) {
    if (number <= 0) {
        throw std::domain_error("Classification is only possible for positive integers.");
    }
    
    int sum = aliquot_sum(number);
    
    if (sum == number) {
        return classification::perfect;
    } else if (sum > number) {
        return classification::abundant;
    } else {
        return classification::deficient;
    }
}

}  // namespace perfect_numbers
