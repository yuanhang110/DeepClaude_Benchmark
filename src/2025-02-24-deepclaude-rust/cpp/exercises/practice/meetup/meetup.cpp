#include "meetup.h"
#include <boost/gregorian/gregorian.hpp>

namespace meetup {

using namespace boost::gregorian;

scheduler::scheduler(months_of_year month, int year) 
    : month_(month), year_(year) {}

int scheduler::find_day(const std::string& week, const std::string& weekday) const {
    date start_date(year_, month_, 1);
    date end_date = start_date.end_of_month();
    
    std::vector<int> matching_days;
    
    for (day_iterator it(start_date); it <= end_date; ++it) {
        if (it->day_of_week() == greg_weekday(weekday).as_number()) {
            matching_days.push_back(it->day());
        }
    }
    
    if (week == "teenth") {
        for (int d : matching_days) {
            if (d >= 13 && d <= 19) return d;
        }
    }
    else if (week == "first") return matching_days[0];
    else if (week == "second") return matching_days[1];
    else if (week == "third") return matching_days[2];
    else if (week == "fourth") return matching_days[3];
    else if (week == "last") return matching_days.back();
    
    return -1;
}

int scheduler::monteenth() const { return find_day("teenth", "Monday"); }
int scheduler::tuesteenth() const { return find_day("teenth", "Tuesday"); }
int scheduler::wednesteenth() const { return find_day("teenth", "Wednesday"); }
int scheduler::thursteenth() const { return find_day("teenth", "Thursday"); }
int scheduler::friteenth() const { return find_day("teenth", "Friday"); }
int scheduler::saturteenth() const { return find_day("teenth", "Saturday"); }
int scheduler::sunteenth() const { return find_day("teenth", "Sunday"); }

int scheduler::first_monday() const { return find_day("first", "Monday"); }
int scheduler::first_tuesday() const { return find_day("first", "Tuesday"); }
int scheduler::first_wednesday() const { return find_day("first", "Wednesday"); }
int scheduler::first_thursday() const { return find_day("first", "Thursday"); }
int scheduler::first_friday() const { return find_day("first", "Friday"); }
int scheduler::first_saturday() const { return find_day("first", "Saturday"); }
int scheduler::first_sunday() const { return find_day("first", "Sunday"); }

int scheduler::second_monday() const { return find_day("second", "Monday"); }
int scheduler::second_tuesday() const { return find_day("second", "Tuesday"); }
int scheduler::second_wednesday() const { return find_day("second", "Wednesday"); }
int scheduler::second_thursday() const { return find_day("second", "Thursday"); }
int scheduler::second_friday() const { return find_day("second", "Friday"); }
int scheduler::second_saturday() const { return find_day("second", "Saturday"); }
int scheduler::second_sunday() const { return find_day("second", "Sunday"); }

int scheduler::third_monday() const { return find_day("third", "Monday"); }
int scheduler::third_tuesday() const { return find_day("third", "Tuesday"); }
int scheduler::third_wednesday() const { return find_day("third", "Wednesday"); }
int scheduler::third_thursday() const { return find_day("third", "Thursday"); }
int scheduler::third_friday() const { return find_day("third", "Friday"); }
int scheduler::third_saturday() const { return find_day("third", "Saturday"); }
int scheduler::third_sunday() const { return find_day("third", "Sunday"); }

int scheduler::fourth_monday() const { return find_day("fourth", "Monday"); }
int scheduler::fourth_tuesday() const { return find_day("fourth", "Tuesday"); }
int scheduler::fourth_wednesday() const { return find_day("fourth", "Wednesday"); }
int scheduler::fourth_thursday() const { return find_day("fourth", "Thursday"); }
int scheduler::fourth_friday() const { return find_day("fourth", "Friday"); }
int scheduler::fourth_saturday() const { return find_day("fourth", "Saturday"); }
int scheduler::fourth_sunday() const { return find_day("fourth", "Sunday"); }

int scheduler::last_monday() const { return find_day("last", "Monday"); }
int scheduler::last_tuesday() const { return find_day("last", "Tuesday"); }
int scheduler::last_wednesday() const { return find_day("last", "Wednesday"); }
int scheduler::last_thursday() const { return find_day("last", "Thursday"); }
int scheduler::last_friday() const { return find_day("last", "Friday"); }
int scheduler::last_saturday() const { return find_day("last", "Saturday"); }
int scheduler::last_sunday() const { return find_day("last", "Sunday"); }

}  // namespace meetup
