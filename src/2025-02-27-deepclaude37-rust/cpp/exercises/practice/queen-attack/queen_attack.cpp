#include "queen_attack.h"
#include <stdexcept>
#include <cstdlib>

namespace queen_attack {

chess_board::chess_board(std::pair<int, int> white, std::pair<int, int> black) 
    : white_(white), black_(black) {
    if (white.first < 0 || white.first >= 8 || white.second < 0 || white.second >= 8 ||
        black.first < 0 || black.first >= 8 || black.second < 0 || black.second >= 8) {
        throw std::domain_error("Invalid queen position: position must be between (0,0) and (7,7).");
    }
    if (white == black) {
        throw std::domain_error("Invalid queen position: both queens cannot be placed on the same square.");
    }
}

bool chess_board::can_attack() const {
    return white_.first == black_.first || 
           white_.second == black_.second || 
           std::abs(white_.first - black_.first) == std::abs(white_.second - black_.second);
}

}  // namespace queen_attack
