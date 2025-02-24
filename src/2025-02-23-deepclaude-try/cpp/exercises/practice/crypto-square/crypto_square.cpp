#include "crypto_square.h"
#include <string>
#include <cmath>
#include <vector>
#include <algorithm>
#include <cctype>

namespace crypto_square {

namespace {

std::string normalize(const std::string& input) {
    std::string result;
    for (char c : input) {
        if (std::isalnum(c)) {
            result += std::tolower(c);
        }
    }
    return result;
}

std::pair<int, int> calculate_dimensions(int len) {
    if (len == 0) return {0, 0};
    int r = static_cast<int>(std::floor(std::sqrt(len)));
    if (r == 0) r = 1;
    
    while (r * (r + 1) < len) {
        ++r;
    }
    
    int c = r + 1;
    if (r * r >= len) {
        c = r;
    }
    return {r, c};
}

std::vector<std::string> build_rectangle(const std::string& text, int r, int c) {
    std::string padded = text;
    padded.resize(r * c, ' ');
    std::vector<std::string> rows;
    for (int i = 0; i < r; ++i) {
        rows.push_back(padded.substr(i * c, c));
    }
    return rows;
}

std::string encode(const std::vector<std::string>& rectangle, int r, int c) {
    std::string encoded;
    for (int col = 0; col < c; ++col) {
        std::string chunk;
        for (int row = 0; row < r; ++row) {
            chunk += rectangle[row][col];
        }
        if (col > 0) encoded += " ";
        encoded += chunk;
    }
    return encoded;
}

} // anonymous namespace

std::string cipher(const std::string& plaintext) {
    std::string normalized = normalize(plaintext);
    auto [r, c] = calculate_dimensions(normalized.size());
    if (r == 0 || c == 0) return "";
    auto rectangle = build_rectangle(normalized, r, c);
    return encode(rectangle, r, c);
}

}  // namespace crypto_square
