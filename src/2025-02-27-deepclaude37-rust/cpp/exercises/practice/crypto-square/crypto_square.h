#if !defined(CRYPTO_SQUARE_H)
#define CRYPTO_SQUARE_H

#include <string>

namespace crypto_square {

class cipher {
public:
    cipher(const std::string& plaintext);
    std::string normalized_cipher_text() const;

private:
    std::string normalized_;
    size_t columns_;
    size_t rows_;
};

}  // namespace crypto_square

#endif // CRYPTO_SQUARE_H
