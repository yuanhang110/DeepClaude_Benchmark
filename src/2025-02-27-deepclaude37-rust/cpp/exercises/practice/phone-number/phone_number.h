#if !defined(PHONE_NUMBER_H)
#define PHONE_NUMBER_H

#include <string>
#include <stdexcept> // for domain_error

namespace phone_number {

class phone_number {
public:
    explicit phone_number(const std::string& s);
    std::string number() const { return num; }
    std::string area_code() const { return num.substr(0, 3); }
    std::string exchange_code() const { return num.substr(3, 3); }
    std::string subscriber_number() const { return num.substr(6); }

private:
    std::string num;
};

}  // namespace phone_number

#endif // PHONE_NUMBER_H
