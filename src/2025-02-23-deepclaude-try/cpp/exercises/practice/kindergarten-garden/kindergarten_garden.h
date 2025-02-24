#pragma once

#include <string>
#include <vector>

namespace kindergarten_garden {

enum class Plants {
    clover,
    grass,
    radishes,
    violets
};

std::vector<Plants> plants(const std::string& child, const std::string& diagram);

}  // namespace kindergarten_garden
