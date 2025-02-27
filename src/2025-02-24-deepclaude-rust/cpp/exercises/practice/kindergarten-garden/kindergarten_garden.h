#pragma once
#include <string>
#include <array>

namespace kindergarten_garden {

enum class Plants {
    violets,
    radishes,
    clover,
    grass
};

std::array<Plants, 4> plants(const std::string& diagram, const std::string& student);

}  // namespace kindergarten_garden
