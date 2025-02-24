#include "crypto_square.h"
#include <cmath>
#include <vector>
#include <cctype>
#include <algorithm>

namespace crypto_square {

cipher::cipher(const std::string& input) {
    normalize(input);
    calculate_dimensions();
}

std::string cipher::normalized_cipher_text() const {
    if (normalized.empty()) return "";
    
    std::string padded = build_padded();
    return read_columns(padded);
}

void cipher::normalize(const std::string& input) {
    normalized.reserve(input.size());
    for (char ch : input) {
        if (std::isalnum(ch)) {
            normalized.push_back(std::tolower(ch));
        }
    }
}

void cipher::calculate_dimensions() {
    int len = normalized.size();
    if (len == 0) {
        columns = rows = 0;
        return;
    }
    
    columns = static_cast<int>(std::ceil(std::sqrt(len)));
    rows = (len + columns - 1) / columns;
    
    // Ensure columns >= rows and columns - rows <= 1
    if (rows > columns || columns - rows > 1) {
        rows = columns;
        columns = (len + rows - 1) / rows;
    }
}

std::string cipher::build_padded() const {
    if (normalized.empty()) return "";
    
    int total = rows * columns;
    std::string padded = normalized;
    padded.resize(total, ' ');
    return padded;
}

std::string cipher::read_columns(const std::string& padded) const {
    std::vector<std::string> chunks;
    chunks.reserve(columns);
    
    for (int c = 0; c < columns; ++c) {
        std::string chunk;
        chunk.reserve(rows);
        for (int r = 0; r < rows; ++r) {
            chunk += padded[r * columns + c];
        }
        chunks.push_back(chunk);
    }
    
    std::string result;
    for (size_t i = 0; i < chunks.size(); ++i) {
        if (i != 0) result += ' ';
        result += chunks[i];
    }
    
    return result;
}

}  // namespace crypto_square
