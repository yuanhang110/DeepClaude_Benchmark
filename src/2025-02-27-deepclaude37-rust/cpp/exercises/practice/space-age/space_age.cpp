#include "space_age.h"

namespace space_age {

namespace {
constexpr double earth_seconds_per_year = 31557600.0;
}

space_age::space_age(long long seconds) : seconds_(seconds) {}

long long space_age::seconds() const {
    return seconds_;
}

double space_age::on_mercury() const {
    return seconds_ / earth_seconds_per_year / 0.2408467;
}

double space_age::on_venus() const {
    return seconds_ / earth_seconds_per_year / 0.61519726;
}

double space_age::on_earth() const {
    return seconds_ / earth_seconds_per_year;
}

double space_age::on_mars() const {
    return seconds_ / earth_seconds_per_year / 1.8808158;
}

double space_age::on_jupiter() const {
    return seconds_ / earth_seconds_per_year / 11.862615;
}

double space_age::on_saturn() const {
    return seconds_ / earth_seconds_per_year / 29.447498;
}

double space_age::on_uranus() const {
    return seconds_ / earth_seconds_per_year / 84.016846;
}

double space_age::on_neptune() const {
    return seconds_ / earth_seconds_per_year / 164.79132;
}

}  // namespace space_age
