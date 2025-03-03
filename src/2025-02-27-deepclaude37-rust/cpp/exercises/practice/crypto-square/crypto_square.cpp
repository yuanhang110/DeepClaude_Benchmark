#include "crypto_square.h"
#include <cmath>
#include <cctype>
#include <string>

namespace crypto_square {

cipher::cipher(const std::string& plaintext) {
    // Normalize text: remove spaces and punctuation, convert to lowercase
    for (char ch : plaintext) {
        if (std::isalnum(static_cast<unsigned char>(ch))) {
            normalized_ += std::tolower(static_cast<unsigned char>(ch));
        }
    }
    
    size_t len = normalized_.length();
    if (len == 0) {
        columns_ = 0;
        rows_ = 0;
        return;
    }
    
    // Calculate rectangle dimensions
    columns_ = static_cast<size_t>(std::ceil(std::sqrt(len)));
    rows_ = (len + columns_ - 1) / columns_; // ceiling division
}

std::string cipher::normalized_cipher_text() const {
    if (normalized_.empty()) {
        return "";
    }
    
    size_t len = normalized_.length();
    
    // Pad normalized text to fill rectangle
    std::string paddedText = normalized_;
    paddedText.resize(rows_ * columns_, ' ');
    
    // Build encoded string by reading columns
    std::string encoded;
    for (size_t col = 0; col < columns_; ++col) {
        for (size_t row = 0; row < rows_; ++row) {
            size_t index = row * columns_ + col;
            encoded += paddedText[index];
        }
    }
    
    // Split into chunks and join with spaces
    std::string result;
    for (size_t i = 0; i < columns_; ++i) {
        if (i > 0) {
            result += ' ';
        }
        result += encoded.substr(i * rows_, rows_);
    }
    
    return result;
}

}  // namespace crypto_square
