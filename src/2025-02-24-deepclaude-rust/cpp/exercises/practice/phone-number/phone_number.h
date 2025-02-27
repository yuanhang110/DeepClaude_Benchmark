#if !defined(PHONE_NUMBER_H)
#define PHONE_NUMBER_H

#include <string>
#include <stdexcept>
#include <regex>

namespace phone_number {

class phone_number {
public:
    explicit phone_number(const std::string& input);
    std::string number() const;
    std::string area_code() const;

private:
    std::string number_;
};

}  // namespace phone_number

#endif // PHONE_NUMBER_H
