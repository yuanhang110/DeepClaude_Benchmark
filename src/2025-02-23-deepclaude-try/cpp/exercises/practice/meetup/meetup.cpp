#include "meetup.h"
#include <ctime>
#include <stdexcept>
#include <string>
#include <boost/date_time.hpp>

namespace meetup {

namespace {
    int weekday_to_num(const std::string& weekday) {
        if (weekday == "Sunday") return 0;
        if (weekday == "Monday") return 1;
        if (weekday == "Tuesday") return 2;
        if (weekday == "Wednesday") return 3;
        if (weekday == "Thursday") return 4;
        if (weekday == "Friday") return 5;
        if (weekday == "Saturday") return 6;
        throw std::invalid_argument("Invalid weekday: " + weekday);
    }

    int get_last_day_of_month(int year, int month) {
        std::tm tm = {};
        tm.tm_year = year - 1900;
        tm.tm_mon = month; // Next month (0-based)
        tm.tm_mday = 0;   // Last day of current month
        tm.tm_isdst = -1;
        mktime(&tm);
        return tm.tm_mday;
    }
} // namespace

// Helper function implementation remains the same
int meetup(int year, int month, const std::string& week, const std::string& weekday) {
    const int target_weekday = weekday_to_num(weekday);
    
    // Handle teenth (13-19)
    if (week == "teenth") {
        for (int day = 13; day <= 19; ++day) {
            std::tm tm = {};
            tm.tm_year = year - 1900;
            tm.tm_mon = month - 1;
            tm.tm_mday = day;
            tm.tm_isdst = -1;
            mktime(&tm);
            if (tm.tm_wday == target_weekday) {
                return day;
            }
        }
    }

    // Handle first, second, third, fourth
    if (week == "first" || week == "second" || week == "third" || week == "fourth") {
        // Find first occurrence of weekday in month
        int base_day = 1;
        while (base_day <= 7) {
            std::tm tm = {};
            tm.tm_year = year - 1900;
            tm.tm_mon = month - 1;
            tm.tm_mday = base_day;
            tm.tm_isdst = -1;
            mktime(&tm);
            if (tm.tm_wday == target_weekday) {
                break;
            }
            ++base_day;
        }

        const int offset = (week == "first") ? 0 :
                         (week == "second") ? 7 :
                         (week == "third") ? 14 : 21;
        return base_day + offset;
    }

    // Handle last
    if (week == "last") {
        const int last_day = get_last_day_of_month(year, month - 1);
        for (int day = last_day; day > 0; --day) {
            std::tm tm = {};
            tm.tm_year = year - 1900;
            tm.tm_mon = month - 1;
            tm.tm_mday = day;
            tm.tm_isdst = -1;
            mktime(&tm);
            if (tm.tm_wday == target_weekday) {
                return day;
            }
        }
    }

    throw std::invalid_argument("Invalid week specification: " + week);
}

// scheduler class implementation
scheduler::scheduler(int year, int month) : year_(year), month_(month) {}

// Teenth methods
int scheduler::monteenth() const { return meetup(year_, month_, "teenth", "Monday"); }
int scheduler::tuesteenth() const { return meetup(year_, month_, "teenth", "Tuesday"); }
int scheduler::wednesteenth() const { return meetup(year_, month_, "teenth", "Wednesday"); }
int scheduler::thursteenth() const { return meetup(year_, month_, "teenth", "Thursday"); }
int scheduler::friteenth() const { return meetup(year_, month_, "teenth", "Friday"); }
int scheduler::saturteenth() const { return meetup(year_, month_, "teenth", "Saturday"); }
int scheduler::sunteenth() const { return meetup(year_, month_, "teenth", "Sunday"); }

// First week methods
int scheduler::first_monday() const { return meetup(year_, month_, "first", "Monday"); }
int scheduler::first_tuesday() const { return meetup(year_, month_, "first", "Tuesday"); }
int scheduler::first_wednesday() const { return meetup(year_, month_, "first", "Wednesday"); }
int scheduler::first_thursday() const { return meetup(year_, month_, "first", "Thursday"); }
int scheduler::first_friday() const { return meetup(year_, month_, "first", "Friday"); }
int scheduler::first_saturday() const { return meetup(year_, month_, "first", "Saturday"); }
int scheduler::first_sunday() const { return meetup(year_, month_, "first", "Sunday"); }

// Second week methods
int scheduler::second_monday() const { return meetup(year_, month_, "second", "Monday"); }
int scheduler::second_tuesday() const { return meetup(year_, month_, "second", "Tuesday"); }
int scheduler::second_wednesday() const { return meetup(year_, month_, "second", "Wednesday"); }
int scheduler::second_thursday() const { return meetup(year_, month_, "second", "Thursday"); }
int scheduler::second_friday() const { return meetup(year_, month_, "second", "Friday"); }
int scheduler::second_saturday() const { return meetup(year_, month_, "second", "Saturday"); }
int scheduler::second_sunday() const { return meetup(year_, month_, "second", "Sunday"); }

// Third week methods
int scheduler::third_monday() const { return meetup(year_, month_, "third", "Monday"); }
int scheduler::third_tuesday() const { return meetup(year_, month_, "third", "Tuesday"); }
int scheduler::third_wednesday() const { return meetup(year_, month_, "third", "Wednesday"); }
int scheduler::third_thursday() const { return meetup(year_, month_, "third", "Thursday"); }
int scheduler::third_friday() const { return meetup(year_, month_, "third", "Friday"); }
int scheduler::third_saturday() const { return meetup(year_, month_, "third", "Saturday"); }
int scheduler::third_sunday() const { return meetup(year_, month_, "third", "Sunday"); }

// Fourth week methods
int scheduler::fourth_monday() const { return meetup(year_, month_, "fourth", "Monday"); }
int scheduler::fourth_tuesday() const { return meetup(year_, month_, "fourth", "Tuesday"); }
int scheduler::fourth_wednesday() const { return meetup(year_, month_, "fourth", "Wednesday"); }
int scheduler::fourth_thursday() const { return meetup(year_, month_, "fourth", "Thursday"); }
int scheduler::fourth_friday() const { return meetup(year_, month_, "fourth", "Friday"); }
int scheduler::fourth_saturday() const { return meetup(year_, month_, "fourth", "Saturday"); }
int scheduler::fourth_sunday() const { return meetup(year_, month_, "fourth", "Sunday"); }

// Last week methods
int scheduler::last_monday() const { return meetup(year_, month_, "last", "Monday"); }
int scheduler::last_tuesday() const { return meetup(year_, month_, "last", "Tuesday"); }
int scheduler::last_wednesday() const { return meetup(year_, month_, "last", "Wednesday"); }
int scheduler::last_thursday() const { return meetup(year_, month_, "last", "Thursday"); }
int scheduler::last_friday() const { return meetup(year_, month_, "last", "Friday"); }
int scheduler::last_saturday() const { return meetup(year_, month_, "last", "Saturday"); }
int scheduler::last_sunday() const { return meetup(year_, month_, "last", "Sunday"); }

}  // namespace meetup
