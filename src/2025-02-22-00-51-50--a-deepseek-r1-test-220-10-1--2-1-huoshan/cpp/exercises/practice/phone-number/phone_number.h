#if !defined(PHONE_NUMBER_H)
#define PHONE_NUMBER_H

#include <stdexcept>
#include <string>

namespace phone_number {

class phone_number_error : public std::domain_error {
    using std::domain_error::domain_error;
};

class phone_number {
    std::string number_;
    std::string area_code_;
    std::string exchange_;
    std::string subscriber_;
    
    void validate(const std::string& number);
    
public:
    explicit phone_number(const std::string& raw);
    
    std::string number() const noexcept { return number_; }
    std::string area_code() const noexcept { return area_code_; }
    std::string exchange() const noexcept { return exchange_; }
    std::string subscriber() const noexcept { return subscriber_; }
    
    operator std::string() const { return number_; }
};

}  // namespace phone_number

#endif // PHONE_NUMBER_H
