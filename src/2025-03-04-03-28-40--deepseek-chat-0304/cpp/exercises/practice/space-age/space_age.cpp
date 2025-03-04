#include "space_age.h"

namespace space_age {

space_age::space_age(long seconds) : seconds_(seconds) {}

long space_age::seconds() const {
    return seconds_;
}

double space_age::calculate_age(double orbital_period) const {
    return seconds_ / (EARTH_SECONDS_PER_YEAR * orbital_period);
}

double space_age::on_mercury() const {
    return calculate_age(MERCURY_ORBITAL_PERIOD);
}

double space_age::on_venus() const {
    return calculate_age(VENUS_ORBITAL_PERIOD);
}

double space_age::on_earth() const {
    return calculate_age(EARTH_ORBITAL_PERIOD);
}

double space_age::on_mars() const {
    return calculate_age(MARS_ORBITAL_PERIOD);
}

double space_age::on_jupiter() const {
    return calculate_age(JUPITER_ORBITAL_PERIOD);
}

double space_age::on_saturn() const {
    return calculate_age(SATURN_ORBITAL_PERIOD);
}

double space_age::on_uranus() const {
    return calculate_age(URANUS_ORBITAL_PERIOD);
}

double space_age::on_neptune() const {
    return calculate_age(NEPTUNE_ORBITAL_PERIOD);
}

}  // namespace space_age
