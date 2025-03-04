#if !defined(QUEEN_ATTACK_H)
#define QUEEN_ATTACK_H

#include <utility>
#include <stdexcept>

namespace queen_attack {

class chess_board {
public:
    chess_board(std::pair<int, int> white, std::pair<int, int> black) 
        : white_(white.first, white.second), black_(black.first, black.second) {
        if (white_ == black_) {
            throw std::domain_error("Queens cannot occupy same position");
        }
    }

    std::pair<int, int> white() const { return {white_.x, white_.y}; }
    std::pair<int, int> black() const { return {black_.x, black_.y}; }

    bool can_attack() const {
        return white_.x == black_.x ||  // Same column
               white_.y == black_.y ||  // Same row
               abs(white_.x - black_.x) == abs(white_.y - black_.y); // Same diagonal
    }

private:
    struct ChessPosition {
        int x;
        int y;
        
        ChessPosition(int x, int y) : x(x), y(y) {
            if (x < 0 || x >= 8 || y < 0 || y >= 8) {
                throw std::domain_error("Position out of bounds");
            }
        }
        
        bool operator==(const ChessPosition& other) const {
            return x == other.x && y == other.y;
        }
    };

    ChessPosition white_;
    ChessPosition black_;
};

}  // namespace queen_attack

#endif // QUEEN_ATTACK_H
