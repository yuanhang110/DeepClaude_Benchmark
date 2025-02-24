#if !defined(QUEEN_ATTACK_H)
#define QUEEN_ATTACK_H

#include <string>
#include <stdexcept>

namespace queen_attack {
    class chess_board {
    public:
        chess_board(const std::string& white, const std::string& black);
        std::string white() const { return white_pos; }
        std::string black() const { return black_pos; }
        bool can_attack() const;
        
    private:
        std::string white_pos;
        std::string black_pos;
        int white_col;
        int white_row;
        int black_col;
        int black_row;
    };
} // namespace queen_attack

#endif // QUEEN_ATTACK_H
