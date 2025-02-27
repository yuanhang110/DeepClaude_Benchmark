#if !defined(MEETUP_H)
#define MEETUP_H

#include <string>
#include <boost/gregorian/gregorian.hpp>

namespace meetup {

class scheduler {
public:
    scheduler(boost::gregorian::months_of_year month, int year);
    
    // teenth methods
    int monteenth() const;
    int tuesteenth() const;
    int wednesteenth() const;
    int thursteenth() const;
    int friteenth() const;
    int saturteenth() const;
    int sunteenth() const;
    
    // first week methods
    int first_monday() const;
    int first_tuesday() const;
    int first_wednesday() const;
    int first_thursday() const;
    int first_friday() const;
    int first_saturday() const;
    int first_sunday() const;
    
    // second week methods
    int second_monday() const;
    int second_tuesday() const;
    int second_wednesday() const;
    int second_thursday() const;
    int second_friday() const;
    int second_saturday() const;
    int second_sunday() const;
    
    // third week methods
    int third_monday() const;
    int third_tuesday() const;
    int third_wednesday() const;
    int third_thursday() const;
    int third_friday() const;
    int third_saturday() const;
    int third_sunday() const;
    
    // fourth week methods
    int fourth_monday() const;
    int fourth_tuesday() const;
    int fourth_wednesday() const;
    int fourth_thursday() const;
    int fourth_friday() const;
    int fourth_saturday() const;
    int fourth_sunday() const;
    
    // last week methods
    int last_monday() const;
    int last_tuesday() const;
    int last_wednesday() const;
    int last_thursday() const;
    int last_friday() const;
    int last_saturday() const;
    int last_sunday() const;

private:
    int find_day(const std::string& week, const std::string& weekday) const;
    boost::gregorian::months_of_year month_;
    int year_;
};

}  // namespace meetup

#endif // MEETUP_H
