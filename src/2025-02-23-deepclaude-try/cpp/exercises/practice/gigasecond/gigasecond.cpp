#include "gigasecond.h"

namespace gigasecond {

time_point advance(time_point start) {
    return start + boost::posix_time::seconds(1000000000);
}

}  // namespace gigasecond
