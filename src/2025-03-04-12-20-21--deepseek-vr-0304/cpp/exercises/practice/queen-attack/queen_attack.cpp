#include "queen_attack.h"
#include <cmath>

namespace queen_attack {

chess_board::chess_board(const std::pair<int, int>& white, const std::pair<int, int>& black)
    : white_pos(white), black_pos(black) {
    if (white_pos.first < 0 || white_pos.first >= 8 ||
        white_pos.second < 0 || white_pos.second >= 8 ||
        black_pos.first < 0 || black_pos.first >= 8 ||
        black_pos.second < 0 || black_pos.second >= 8) {
        throw std::domain_error("Position out of bounds");
    }
    if (white_pos == black_pos) {
        throw std::domain_error("Queens cannot be on the same square");
    }
}

std::pair<int, int> chess_board::white() const {
    return white_pos;
}

std::pair<int, int> chess_board::black() const {
    return black_pos;
}

bool chess_board::can_attack() const {
    if (white_pos.first == black_pos.first || white_pos.second == black_pos.second) {
        return true;
    }
    return std::abs(white_pos.first - black_pos.first) == 
           std::abs(white_pos.second - black_pos.second);
}

}  // namespace queen_attack
