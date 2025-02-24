#pragma once

#include <string>
#include <vector>

namespace kindergarten_garden {

enum class Plants {
    grass,
    clover,
    radishes,
    violets
};

class Garden {
public:
    explicit Garden(const std::string& diagram);
    std::vector<Plants> plants(const std::string& child) const;

private:
    std::vector<std::vector<char>> cups_;
    static const std::vector<std::string> children_;
    
    Plants convert(char c) const;
};

}  // namespace kindergarten_garden
