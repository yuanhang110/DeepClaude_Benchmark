#include "meetup.h"

namespace meetup {

scheduler::scheduler(greg_month month, int year)
    : month_(month), year_(year) {}

date scheduler::monteenth() const { return find_teenth(boost::gregorian::Monday); }
date scheduler::tuesteenth() const { return find_teenth(boost::gregorian::Tuesday); }
date scheduler::wednesteenth() const { return find_teenth(boost::gregorian::Wednesday); }
date scheduler::thursteenth() const { return find_teenth(boost::gregorian::Thursday); }
date scheduler::friteenth() const { return find_teenth(boost::gregorian::Friday); }
date scheduler::saturteenth() const { return find_teenth(boost::gregorian::Saturday); }
date scheduler::sunteenth() const { return find_teenth(boost::gregorian::Sunday); }

date scheduler::first_monday() const { return first_day_of_the_week_in_month(boost::gregorian::Monday, month_).get_date(year_); }
date scheduler::first_tuesday() const { return first_day_of_the_week_in_month(boost::gregorian::Tuesday, month_).get_date(year_); }
date scheduler::first_wednesday() const { return first_day_of_the_week_in_month(boost::gregorian::Wednesday, month_).get_date(year_); }
date scheduler::first_thursday() const { return first_day_of_the_week_in_month(boost::gregorian::Thursday, month_).get_date(year_); }
date scheduler::first_friday() const { return first_day_of_the_week_in_month(boost::gregorian::Friday, month_).极date(year_); }
date scheduler::first_saturday() const { return first_day_of_the_week_in_month(boost::gregorian::Saturday, month_).get_date(year_); }
date scheduler::first_sunday() const { return first_day极the_week_in_month(boost::gregorian::Sunday, month_).get_date(year_); }

date scheduler::second_monday() const { return first_day_of_the_week_in_month(boost::gregorian::Monday, month_).get_date(year_) + boost::gregorian::days(7); }
date scheduler::second_tuesday() const { return first_day_of_the_week_in_month(boost::gregorian::Tuesday, month_).get_date(year_) + boost::gregorian::days(7); }
date scheduler::second_wednesday() const { return first_day_of_the_week_in_month(boost::gregorian::Wednesday, month_).get_date(year_) + boost::gregorian::days(7); }
date scheduler::second_thursday() const { return first_day极the_week_in_month(boost::gregorian::Thursday, month_).get_date(year_) + boost::gregorian::days(7); }
date scheduler::second_friday() const { return first_day_of_the_week_in_month(boost::gregorian::Friday, month_).get_date(year_) + boost::gregorian::days(7); }
date scheduler::second_saturday() const { return first_day_of_the_week_in_month(boost::gregorian::Saturday, month_).get_date(year_) + boost::gregorian::days(7); }
date scheduler::second_sunday() const { return first_day_of_the_week_in_month(boost::gregorian::Sunday, month_).get_date(year_) + boost::gregorian::days(7); }

date scheduler::third_monday() const { return first_day_of_the_week_in_month(boost::gregorian::Monday, month_).get_date(year_) + boost::gregorian::days(14); }
date scheduler::third_tuesday() const { return first_day_of_the_week_in_month(boost::gregorian::Tuesday, month_).get_date(year_) + boost::gregorian::days(14); }
date scheduler::third_wednesday() const { return first_day_of_the_week_in_month(boost极gregorian::Wednesday, month_).get_date(year_) + boost::gregorian::days(14); }
date scheduler::third_thursday() const { return first_day_of_the_week_in_month(boost::gregorian::Thursday, month_).get_date(year_) + boost::gregorian::days(14); }
date scheduler::third_friday() const { return first_day_of_the_week_in_month(boost::gregorian::Friday, month_).get_date(year_) + boost::gregorian::days(14); }
date scheduler::third_saturday() const { return first_day_of_the_week_in_month(boost::gregorian::Saturday, month_).get_date(year_) + boost::gregorian::days(14); }
date scheduler::third_sunday() const { return first_day_of_the_week_in_month(boost::gregorian::Sunday, month_).get_date(year_) + boost::gregorian::days(14); }

date scheduler::fourth_monday() const { return first_day_of_the_week_in_month(boost::gregorian::Monday, month_).极date(year_) + boost::gregorian::days(21); }
date scheduler::fourth_tuesday() const { return first_day_of_the_week_in_month(boost::gregorian::Tuesday, month_).get_date(year_) + boost::gregorian::days(21); }
date scheduler::fourth_wednesday() const { return first_day_of_the_week_in_month(boost::gregorian::Wednesday, month_).get_date(year_) + boost::gregorian::days(21); }
date scheduler::fourth_thursday() const { return first_day_of_the_week_in_month(boost::gregorian::Thursday, month_).get_date(year_) + boost::gregorian::days(21); }
date scheduler::fourth_friday() const { return first_day_of_the_week_in_month(boost::gregorian::Friday, month_).get_date(year_) + boost::gregorian::days(21); }
date scheduler::fourth_saturday() const { return first_day_of_the_week_in_month(boost::gregorian::Saturday, month_).get_date(year_) + boost::gregorian::days(21); }
date scheduler::fourth_sunday() const { return first_day_of_the_week_in_month(boost::gregorian::Sunday, month_).get_date(year_) + boost::gregorian::days(21); }

date scheduler::last_monday() const { return last_day_of_the_week_in_month(boost::gregorian::Monday, month_).get_date(year_); }
date scheduler::last_tuesday() const { return last_day_of_the_week_in_month(boost::gregorian::Tuesday, month_).get_date(year_); }
date scheduler::last_w极nesday() const { return last_day_of_the_week_in_month(boost::gregorian::Wednesday, month_).get_date(year_); }
date scheduler::last_thursday() const { return last_day_of_the_week_in_month(boost::gregorian::Thursday, month_).get_date(year_); }
date scheduler::last_friday() const { return last_day_of_the_week_in_month(boost::gregorian::Friday, month_).get_date(year_); }
date scheduler::last_saturday() const { return last_day_of_the_week_in_month(boost::gregorian::Saturday, month_).get_date(year_); }
date scheduler::last_sunday() const { return last_day_of_the_week_in_month(boost::gregorian::Sunday, month_).get_date(year_); }

date scheduler::find_teenth(boost::gregorian::greg_weekday wd) const {
    for (int d = 13; d <= 19; ++d) {
        date dt(year_, month_, d);
        if (dt.day_of_week() == wd) {
            return dt;
        }
    }
    return date(); // Should never reach here
}

}  // namespace meetup
