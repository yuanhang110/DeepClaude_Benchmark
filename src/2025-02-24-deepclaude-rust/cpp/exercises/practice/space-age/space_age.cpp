#include "space_age.h"

namespace space_age {

space_age::space_age(long long seconds) : seconds_(seconds) {}

long long space_age::seconds() const { 
    return seconds_; 
}

double space_age::on_earth() const {
    return seconds_ / 31557600.0;
}

double space_age::on_mercury() const {
    return seconds_ / (0.2408467 * 31557600.0);
}

double space_age::on_venus() const {
    return seconds_ / (0.61519726 * 31557600.0);
}

double space_age::on_mars() const {
    return seconds_ / (1.8808158 * 31557600.0);
}

double space_age::on_jupiter() const {
    return seconds_ / (11.862615 * 31557600.0);
}

double space_age::on_saturn() const {
    return seconds_ / (29.447498 * 31557600.0);
}

double space_age::on_uranus() const {
    return seconds_ / (84.016846 * 31557600.0);
}

double space_age::on_neptune() const {
    return seconds_ / (164.79132 * 31557600.0);
}

}  // namespace space_age
