#if !defined(GIGASECOND_H)
#define GIGASECOND_H

#include <boost/date_time/posix_time/posix_time.hpp>

namespace gigasecond {

using time_point = boost::posix_time::ptime;

time_point advance(time_point start);

}  // namespace gigasecond

#endif // GIGASECOND_H
