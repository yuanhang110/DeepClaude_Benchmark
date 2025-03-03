#if !defined(MEETUP_H)
#define MEETUP_H

#include <string>
#include <boost/date_time/gregorian/gregorian.hpp>

namespace meetup {
    class scheduler {
    private:
        int year_;
        int month_;
        int calculate_day(const std::string& weekday, const std::string& week) const;
        
    public:
        scheduler(int month, int year) : year_(year), month_(month) {}
        scheduler(boost::gregorian::greg_month month, int year) : year_(year), month_(month.as_number()) {}
        
        // Teenth methods
        int monteenth() const { return calculate_day("Monday", "teenth"); }
        int tuesteenth() const { return calculate_day("Tuesday", "teenth"); }
        int wednesteenth() const { return calculate_day("Wednesday", "teenth"); }
        int thursteenth() const { return calculate_day("Thursday", "teenth"); }
        int friteenth() const { return calculate_day("Friday", "teenth"); }
        int saturteenth() const { return calculate_day("Saturday", "teenth"); }
        int sunteenth() const { return calculate_day("Sunday", "teenth"); }
        
        // First week methods
        int first_monday() const { return calculate_day("Monday", "first"); }
        int first_tuesday() const { return calculate_day("Tuesday", "first"); }
        int first_wednesday() const { return calculate_day("Wednesday", "first"); }
        int first_thursday() const { return calculate_day("Thursday", "first"); }
        int first_friday() const { return calculate_day("Friday", "first"); }
        int first_saturday() const { return calculate_day("Saturday", "first"); }
        int first_sunday() const { return calculate_day("Sunday", "first"); }
        
        // Second week methods
        int second_monday() const { return calculate_day("Monday", "second"); }
        int second_tuesday() const { return calculate_day("Tuesday", "second"); }
        int second_wednesday() const { return calculate_day("Wednesday", "second"); }
        int second_thursday() const { return calculate_day("Thursday", "second"); }
        int second_friday() const { return calculate_day("Friday", "second"); }
        int second_saturday() const { return calculate_day("Saturday", "second"); }
        int second_sunday() const { return calculate_day("Sunday", "second"); }
        
        // Third week methods
        int third_monday() const { return calculate_day("Monday", "third"); }
        int third_tuesday() const { return calculate_day("Tuesday", "third"); }
        int third_wednesday() const { return calculate_day("Wednesday", "third"); }
        int third_thursday() const { return calculate_day("Thursday", "third"); }
        int third_friday() const { return calculate_day("Friday", "third"); }
        int third_saturday() const { return calculate_day("Saturday", "third"); }
        int third_sunday() const { return calculate_day("Sunday", "third"); }
        
        // Fourth week methods
        int fourth_monday() const { return calculate_day("Monday", "fourth"); }
        int fourth_tuesday() const { return calculate_day("Tuesday", "fourth"); }
        int fourth_wednesday() const { return calculate_day("Wednesday", "fourth"); }
        int fourth_thursday() const { return calculate_day("Thursday", "fourth"); }
        int fourth_friday() const { return calculate_day("Friday", "fourth"); }
        int fourth_saturday() const { return calculate_day("Saturday", "fourth"); }
        int fourth_sunday() const { return calculate_day("Sunday", "fourth"); }
        
        // Last week methods
        int last_monday() const { return calculate_day("Monday", "last"); }
        int last_tuesday() const { return calculate_day("Tuesday", "last"); }
        int last_wednesday() const { return calculate_day("Wednesday", "last"); }
        int last_thursday() const { return calculate_day("Thursday", "last"); }
        int last_friday() const { return calculate_day("Friday", "last"); }
        int last_saturday() const { return calculate_day("Saturday", "last"); }
        int last_sunday() const { return calculate_day("Sunday", "last"); }
    };
}  // namespace meetup

#endif // MEETUP_H
