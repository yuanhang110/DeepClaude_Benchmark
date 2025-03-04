#if !defined(PHONE_NUMBER_H)
#define PHONE_NUMBER_H

#include <string>
#include <stdexcept>

namespace phone_number {

class PhoneNumber {
public:
    explicit PhoneNumber(const std::string& s);
    std::string number() const;
    std::string area_code() const;
    
private:
    std::string num_;
};

}  // namespace phone_number

#endif // PHONE_NUMBER_H
