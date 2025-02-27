#include "crypto_square.h"
#include <string>
#include <vector>
#include <cmath>
#include <cctype>

namespace crypto_square {

cipher::cipher(const std::string& input) {
    // Normalize: remove spaces and punctuation, convert to lowercase
    std::string normalized;
    for (char ch : input) {
        if (std::isalnum(ch)) {
            normalized += std::tolower(ch);
        }
    }
    
    int len = normalized.length();
    if (len == 0) {
        normalized_text_ = "";
        return;
    }
    
    // Calculate rectangle dimensions
    int c = static_cast<int>(std::ceil(std::sqrt(len)));
    int r = (len + c - 1) / c;
    
    // Pad normalized text to rectangle size
    normalized.resize(r * c, ' ');
    
    // Split into rows
    std::vector<std::string> rows;
    for (int i = 0; i < r; ++i) {
        rows.push_back(normalized.substr(i * c, c));
    }
    
    // Read down columns to create encoded text
    std::string encoded;
    for (int j = 0; j < c; ++j) {
        for (int i = 0; i < r; ++i) {
            encoded += rows[i][j];
        }
    }
    
    // Split into chunks
    std::vector<std::string> chunks;
    for (int i = 0; i < c; ++i) {
        chunks.push_back(encoded.substr(i * r, r));
    }
    
    // Join chunks with spaces
    std::string result;
    for (size_t i = 0; i < chunks.size(); ++i) {
        if (i > 0) result += " ";
        result += chunks[i];
    }
    
    normalized_text_ = result;
}

std::string cipher::normalized_cipher_text() const {
    return normalized_text_;
}

}  // namespace crypto_square
