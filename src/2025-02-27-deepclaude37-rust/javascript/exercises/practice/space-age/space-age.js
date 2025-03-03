//
// This is only a SKELETON file for the 'Space Age' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const age = (planet, seconds) => {
  const EARTH_YEAR_IN_SECONDS = 31557600;
  
  const orbitalPeriods = {
    mercury: 0.2408467,
    venus: 0.61519726,
    earth: 1.0,
    mars: 1.8808158,
    jupiter: 11.862615,
    saturn: 29.447498,
    uranus: 84.016846,
    neptune: 164.79132
  };
  
  const earthYears = seconds / EARTH_YEAR_IN_SECONDS;
  const planetYears = earthYears / orbitalPeriods[planet.toLowerCase()];
  
  // Round to 2 decimal places
  return Number(planetYears.toFixed(2));
};
