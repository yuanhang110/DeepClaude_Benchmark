#if !defined(GIGASECOND_H)
#define GIGASECOND_H

#include <boost/date_time/posix_time/posix_time.hpp>

namespace gigasecond {

// Calculate the date/time one gigasecond (1,000,000,000 seconds) after the input time
boost::posix_time::ptime advance(const boost::posix_time::ptime& start);

}  // namespace gigasecond

#endif // GIGASECOND_H
