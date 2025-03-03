#if !defined(QUEEN_ATTACK_H)
#define QUEEN_ATTACK_H

#include <utility>
#include <stdexcept>

namespace queen_attack {

class chess_board {
public:
    chess_board(std::pair<int, int> white, std::pair<int, int> black);
    bool can_attack() const;
    std::pair<int, int> white() const { return white_; }
    std::pair<int, int> black() const { return black_; }
private:
    std::pair<int, int> white_;
    std::pair<int, int> black_;
};

}  // namespace queen_attack

#endif // QUEEN_ATTACK_H
