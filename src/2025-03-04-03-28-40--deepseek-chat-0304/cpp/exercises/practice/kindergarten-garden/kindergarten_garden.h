#pragma once
#include <string>
#include <array>
#include <vector>

namespace kindergarten_garden {

enum class Plants {
    clover,
    grass,
    radishes,
    violets
};

std::array<Plants, 4> plants(const std::string& diagram, const std::string& child);

}  // namespace kindergarten_garden
