#if !defined(PHONE_NUMBER_H)
#define PHONE_NUMBER_H

#include <string>

namespace phone_number {

class phone_number {
public:
    explicit phone_number(const std::string& input);
    std::string number() const;
    std::string area_code() const;
    operator std::string() const;

private:
    std::string digits;
    void clean_number(const std::string& input);
    void validate_number() const;
};

}  // namespace phone_number

#endif // PHONE_NUMBER_H
