#if !defined(CRYPTO_SQUARE_H)
#define CRYPTO_SQUARE_H

#include <string>
#include <vector>
#include <algorithm>
#include <cmath>

namespace crypto_square {

class cipher {
public:
    explicit cipher(const std::string& text);
    std::string normalize_plain_text() const;
    std::vector<std::string> plain_text_segments() const;
    std::string cipher_text() const;
    std::string normalized_cipher_text() const;

private:
    std::string normalized_text;
    size_t columns;
    size_t rows;
    
    void calculate_rectangle_dimensions();
};

}  // namespace crypto_square

#endif // CRYPTO_SQUARE_H
