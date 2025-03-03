#pragma once

#include <vector>
#include <string>
#include <array>

namespace kindergarten_garden {

    enum class Plants {
        grass,
        clover,
        radishes,
        violets
    };

    std::array<Plants, 4> plants(const std::string &child, const std::string &diagram);

}  // namespace kindergarten_garden
