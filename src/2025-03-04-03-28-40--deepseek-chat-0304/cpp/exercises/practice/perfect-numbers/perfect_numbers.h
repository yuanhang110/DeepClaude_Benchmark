#if !defined(PERFECT_NUMBERS_H)
#define PERFECT_NUMBERS_H

namespace perfect_numbers {

enum class classification {
    perfect,
    abundant,
    deficient
};

classification classify(int number);

}  // namespace perfect_numbers

#endif  // PERFECT_NUMBERS_H
