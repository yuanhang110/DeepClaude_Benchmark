#include "meetup.h"
#include <boost/date_time/gregorian/gregorian.hpp>
#include <stdexcept>

namespace meetup {
    using namespace boost::gregorian;
    
    scheduler::scheduler(const month_type& month, int year) : month_(month), year_(year) {}

    // Teenth methods
    date scheduler::monteenth() const { return find_teenth(Monday); }
    date scheduler::tuesteenth() const { return find_teenth(Tuesday); }
    date scheduler::wednesteenth() const { return find_teenth(Wednesday); }
    date scheduler::thursteenth() const { return find_teenth(Thursday); }
    date scheduler::friteenth() const { return find_teenth(Friday); }
    date scheduler::saturteenth() const { return find_teenth(Saturday); }
    date scheduler::sunteenth() const { return find_teenth(Sunday); }

    // First week methods
    date scheduler::first_monday() const { return find_nth_weekday(1, Monday); }
    date scheduler::first_tuesday() const { return find_nth_weekday(1, Tuesday); }
    date scheduler::first_wednesday() const { return find_nth_weekday(1, Wednesday); }
    date scheduler::first_thursday() const { return find_nth_weekday(1, Thursday); }
    date scheduler::first_friday() const { return find_nth_weekday(1, Friday); }
    date scheduler::first_saturday() const { return find_nth_weekday(1, Saturday); }
    date scheduler::first_sunday() const { return find_nth_weekday(1, Sunday); }

    // Helper functions
    date scheduler::find_nth_weekday(int n, boost::date_time::weekdays weekday) const {
        nth_day_of_the_week_in_month nth(nth_day_of_the_week_in_month::week_num(n), weekday, month_);
        return nth.get_date(year_);
    }

    date scheduler::find_teenth(boost::date_time::weekdays weekday) const {
        for (int day = 13; day <= 19; ++day) {
            date d(year_, month_, day);
            if (d.day_of_week() == weekday) {
                return d;
            }
        }
        throw std::runtime_error("Teenth not found");
    }

    // Implement other methods similarly...
} // namespace meetup
