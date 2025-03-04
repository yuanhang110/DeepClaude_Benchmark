#include "crypto_square.h"

namespace crypto_square {

cipher::cipher(const std::string& text) {
    // Normalize the input text
    normalized_text.reserve(text.size());
    for (char ch : text) {
        if (std::isalnum(ch)) {
            normalized_text += std::tolower(ch);
        }
    }
    calculate_rectangle_dimensions();
}

std::string cipher::normalize_plain_text() const {
    return normalized_text;
}

void cipher::calculate_rectangle_dimensions() {
    size_t length = normalized_text.length();
    if (length == 0) {
        columns = rows = 0;
        return;
    }
    
    // Calculate the smallest c where c >= r and c - r <= 1
    columns = static_cast<size_t>(std::ceil(std::sqrt(length)));
    rows = static_cast<size_t>(std::floor(std::sqrt(length)));
    
    if (columns * rows < length) {
        rows++;
    }
}

std::vector<std::string> cipher::plain_text_segments() const {
    std::vector<std::string> segments;
    if (columns == 0) return segments;
    
    for (size_t i = 0; i < normalized_text.length(); i += columns) {
        segments.push_back(normalized_text.substr(i, columns));
    }
    
    return segments;
}

std::string cipher::cipher_text() const {
    std::string result;
    if (columns == 0) return result;
    
    for (size_t col = 0; col < columns; ++col) {
        for (size_t row = 0; row < rows; ++row) {
            size_t index = row * columns + col;
            if (index < normalized_text.length()) {
                result += normalized_text[index];
            }
        }
    }
    
    return result;
}

std::string cipher::normalized_cipher_text() const {
    std::string result;
    if (columns == 0) return result;
    
    std::string cipher = cipher_text();
    size_t total_length = columns * rows;
    size_t padding_needed = total_length - cipher.length();
    
    // Add padding spaces to the end of the cipher text
    cipher.append(padding_needed, ' ');
    
    // Split into columns chunks of rows length
    for (size_t i = 0; i < cipher.length(); i += rows) {
        std::string segment = cipher.substr(i, rows);
        result += segment + ' ';
    }
    
    // Remove the trailing space
    if (!result.empty()) {
        result.pop_back();
    }
    
    return result;
}

}  // namespace crypto_square
