#include "queen_attack.h"
#include <stdexcept>
#include <cctype>
#include <string>

using namespace std;

namespace queen_attack {

    chess_board::chess_board(const string& white, const string& black) 
        : white_pos(white), black_pos(black) {
        
        if (white.empty() || black.empty()) {
            throw invalid_argument("Invalid queen position");
        }
        
        // Parse and validate white position
        char w_col = tolower(white[0]);
        if (w_col < 'a' || w_col > 'h') throw invalid_argument("Invalid column");
        white_col = w_col - 'a';
        
        try {
            white_row = 8 - stoi(white.substr(1));
        } catch (...) {
            throw invalid_argument("Invalid row");
        }
        if (white_row < 0 || white_row > 7) throw invalid_argument("Row out of bounds");

        // Parse and validate black position
        char b_col = tolower(black[0]);
        if (b_col < 'a' || b_col > 'h') throw invalid_argument("Invalid column");
        black_col = b_col - 'a';
        
        try {
            black_row = 8 - stoi(black.substr(1));
        } catch (...) {
            throw invalid_argument("Invalid row");
        }
        if (black_row < 0 || black_row > 7) throw invalid_argument("Row out of bounds");

        // Check if queens occupy same position
        if (white == black) {
            throw domain_error("Queens cannot occupy same position");
        }
    }

    bool chess_board::can_attack() const {
        return (white_col == black_col) ||          // Same column
               (white_row == black_row) ||          // Same row
               (abs(white_col - black_col) == abs(white_row - black_row)); // Diagonal
    }

} // namespace queen_attack
