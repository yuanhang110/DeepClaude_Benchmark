#if !defined(MEETUP_H)
#define MEETUP_H

#include <boost/date_time/gregorian/gregorian.hpp>
#include <string>

namespace meetup {
    class scheduler {
    public:
        scheduler(const boost::gregorian::month_type& month, int year);
        
        // Teenth methods
        boost::gregorian::date monteenth() const;
        boost::gregorian::date tuesteenth() const;
        boost::gregorian::date wednesteenth() const;
        boost::gregorian::date thursteenth() const;
        boost::gregorian::date friteenth() const;
        boost::gregorian::date saturteenth() const;
        boost::gregorian::date sunteenth() const;

        // First week methods
        boost::gregorian::date first_monday() const;
        boost::gregorian::date first_tuesday() const;
        boost::gregorian::date first_wednesday() const;
        boost::gregorian::date first_thursday() const;
        boost::gregorian::date first_friday() const;
        boost::gregorian::date first_saturday() const;
        boost::gregorian::date first_sunday() const;

        // Other weeks and last week methods...

    private:
        boost::gregorian::month_type month_;
        int year_;
        
        boost::gregorian::date find_nth_weekday(int n, boost::date_time::weekdays weekday) const;
        boost::gregorian::date find_teenth(boost::date_time::weekdays weekday) const;
        boost::gregorian::date find_last(boost::date_time::weekdays weekday) const;
    };
}  // namespace meetup

#endif // MEETUP_H
