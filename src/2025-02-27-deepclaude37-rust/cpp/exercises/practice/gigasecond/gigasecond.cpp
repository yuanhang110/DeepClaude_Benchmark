#include "gigasecond.h"

namespace gigasecond {
    boost::posix_time::ptime advance(const boost::posix_time::ptime& start) {
        // A gigasecond is 10^9 seconds
        return start + boost::posix_time::seconds(1000000000);
    }
}  // namespace gigasecond
