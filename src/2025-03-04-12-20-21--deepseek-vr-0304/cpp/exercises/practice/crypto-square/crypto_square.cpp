#include "crypto_square.h"
#include <cmath>
#include <algorithm>

namespace crypto_square {

cipher::cipher(const std::string& plaintext) {
    // Normalize the input: remove non-alphanumeric and lowercase
    normalized.reserve(plaintext.size());
    for (char ch : plaintext) {
        if (std::isalnum(ch)) {
            normalized += std::tolower(ch);
        }
    }

    int n = normalized.size();
    if (n == 0) {
        c = 0;
        r = 0;
        return;
    }

    double sqrt_n = std::sqrt(n);
    c = (sqrt_n == std::floor(sqrt_n)) ? static_cast<int>(sqrt_n) : static_cast<int>(std::floor(sqrt_n)) + 1;
    r = static_cast<int>(std::ceil(static_cast<double>(n) / c));
}

std::string cipher::normalized_cipher_text() const {
    if (normalized.empty()) {
        return "";
    }

    int n = normalized.size();
    int pad_length = r * c - n;
    std::string padded = normalized + std::string(pad_length, ' ');

    std::string encoded;

    for (int col = 0; col < c; ++col) {
        for (int row = 0; row < r; ++row) {
            int index = row * c + col;
            encoded += padded[index];
        }
        if (col < c - 1) {
            encoded += ' ';
        }
    }

    return encoded;
}

}  // namespace crypto_square
