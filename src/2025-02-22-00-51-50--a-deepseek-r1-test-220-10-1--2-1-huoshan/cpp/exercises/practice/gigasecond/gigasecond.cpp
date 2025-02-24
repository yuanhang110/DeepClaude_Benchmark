#include "gigasecond.h"

namespace gigasecond {

boost::posix_time::ptime advance(const boost::posix_time::ptime& start) {
    // Add one billion seconds to the input time point
    return start + boost::posix_time::seconds(1000000000);
}

}  // namespace gigasecond
