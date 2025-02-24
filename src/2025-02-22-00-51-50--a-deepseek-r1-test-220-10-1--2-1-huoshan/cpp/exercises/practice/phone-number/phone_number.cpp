#include "phone_number.h"
#include <algorithm>
#include <cctype>

using namespace std;

namespace phone_number {

phone_number::phone_number(const string& raw) {
    string cleaned;
    
    // Remove all non-digit characters
    copy_if(raw.begin(), raw.end(), back_inserter(cleaned), 
        [](char c) { return isdigit(c); });

    // Validate number length and country code
    if (cleaned.length() == 11) {
        if (cleaned[0] != '1') {
            throw phone_number_error("Invalid number length");
        }
        cleaned = cleaned.substr(1);
    }
    
    if (cleaned.length() != 10) {
        throw phone_number_error("Invalid number length");
    }
    
    validate(cleaned);
    
    number_ = cleaned;
    area_code_ = cleaned.substr(0, 3);
    exchange_ = cleaned.substr(3, 3);
    subscriber_ = cleaned.substr(6);
}

void phone_number::validate(const string& number) {
    // Validate area code (NXX)
    if (number[0] < '2' || number[0] > '9') {
        throw phone_number_error("Area code must start with 2-9");
    }
    
    // Validate exchange code (NXX)
    if (number[3] < '2' || number[3] > '9') {
        throw phone_number_error("Exchange code must start with 2-9");
    }
}

}  // namespace phone_number
