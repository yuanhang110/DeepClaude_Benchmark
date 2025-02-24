#if !defined(CRYPTO_SQUARE_H)
#define CRYPTO_SQUARE_H

#include <string>

namespace crypto_square {

class cipher {
public:
    explicit cipher(const std::string& input);
    std::string normalized_cipher_text() const;

private:
    std::string normalized;
    int columns;
    int rows;

    void normalize(const std::string& input);
    void calculate_dimensions();
    std::string build_padded() const;
    std::string read_columns(const std::string& padded) const;
};

}  // namespace crypto_square

#endif // CRYPTO_SQUARE_H
