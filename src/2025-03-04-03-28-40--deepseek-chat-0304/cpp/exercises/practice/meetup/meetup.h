#if !defined(MEETUP_H)
#define MEETUP_H

#include <boost/date_time/gregorian/gregorian.hpp>

namespace meetup {

using boost::gregorian::date;
using boost::gregorian::greg_month;
using boost::gregorian::greg_year;
using boost::gregorian::greg_day;
using boost::gregorian::first_day_of_the_week_in_month;
using boost::gregorian::last_day_of_the_week_in_month;

class scheduler {
public:
    scheduler(greg_month month, int year);
    
    date monteenth() const;
    date tuesteenth() const;
    date wednesteenth() const;
    date thursteenth() const;
    date friteenth() const;
    date saturteenth() const;
    date sunteenth() const;
    
    date first_monday() const;
    date first_tuesday() const;
    date first_wednesday() const;
    date first_thursday() const;
    date first_friday() const;
    date first_saturday() const;
    date first_sunday() const;
    
    date second_monday() const;
    date second_tuesday() const;
    date second_wednesday() const;
    date second_thursday() const;
    date second_friday() const;
    date second_saturday() const;
    date second_sunday() const;
    
    date third_monday() const;
    date third_tuesday() const;
    date third_wednesday() const;
    date third_thursday() const;
    date third_friday() const;
    date third_saturday() const;
    date third_sunday() const;
    
    date fourth_monday() const;
    date fourth_tuesday() const;
    date fourth_wednesday() const;
    date fourth_thursday() const;
    date fourth_friday() const;
    date fourth_saturday() const;
    date fourth_sunday() const;
    
    date last_monday() const;
    date last_tuesday() const;
    date last_wednesday() const;
    date last_thursday() const;
    date last_friday() const;
    date last_saturday() const;
    date last_sunday() const;

private:
    date find_teenth(boost::gregorian::greg_weekday wd) const;
    
    greg_month month_;
    int year_;
};

}  // namespace meetup

#endif // MEETUP_H
