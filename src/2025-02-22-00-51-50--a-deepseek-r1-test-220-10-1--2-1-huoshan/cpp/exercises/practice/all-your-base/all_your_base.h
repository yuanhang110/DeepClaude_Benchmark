#if !defined(ALL_YOUR_BASE_H)
#define ALL_YOUR_BASE_H

#include <vector>

namespace all_your_base {

std::vector<unsigned int> convert(const std::vector<unsigned int>& digits,
                                unsigned int input_base,
                                unsigned int output_base);

}  // namespace all_your_base

#endif // ALL_YOUR_BASE_H
