#if !defined(ALL_YOUR_BASE_H)
#define ALL_YOUR_BASE_H

#include <vector>
#include <stdexcept>

namespace all_your_base {

std::vector<unsigned int> convert(unsigned int input_base,
                                 const std::vector<unsigned int>& input_digits,
                                 unsigned int output_base);

}  // namespace all_your_base

#endif // ALL_YOUR_BASE_H
