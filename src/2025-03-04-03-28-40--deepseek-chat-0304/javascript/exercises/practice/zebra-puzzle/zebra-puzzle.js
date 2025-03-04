export class ZebraPuzzle {
  constructor() {
    this.solution = this.solvePuzzle();
  }

  solvePuzzle() {
    // Predefined constraints
    const constraints = [
      // 2. The Englishman lives in the red house
      (houses) => houses.find(h => h.nationality === 'Englishman').color === 'red',
      
      // 3. The Spaniard owns the dog
      (houses) => houses.find(h => h.nationality === 'Spaniard').pet === 'dog',
      
      // 4. The person in the green house drinks coffee
      (houses) => houses.find(h => h.color === 'green').drink === 'coffee',
      
      // 5. The Ukrainian drinks tea
      (houses) => houses.find(h => h.nationality === 'Ukrainian').drink === 'tea',
      
      // 6. The green house is immediately to the right of the ivory house
      (houses) => {
        const green = houses.find(h => h.color === 'green');
        const ivory = houses.find(h => h.color === 'ivory');
        return green.position === ivory.position + 1;
      },
      
      // 7. The snail owner likes to go dancing
      (houses) => houses.find(h => h.pet === 'snail').hobby === 'dancing',
      
      // 8. The person in the yellow house is a painter
      (houses) => houses.find(h => h.color === 'yellow').hobby === 'painter',
      
      // 9. The person in the middle house drinks milk
      (houses) => houses[2].drink === 'milk',
      
      // 10. The Norwegian lives in the first house
      (houses) => houses[0].nationality === 'Norwegian',
      
      // 11. The person who enjoys reading lives next to the person with the fox
      (houses) => {
        const reading = houses.find(h => h.hobby === 'reading');
        const fox = houses.find(h => h.pet === 'fox');
        return Math.abs(reading.position - fox.position) === 1;
      },
      
      // 12. The painter's house is next to the house with the horse
      (houses) => {
        const painter = houses.find(h => h.hobby === 'painter');
        const horse = houses.find(h => h.pet === 'horse');
        return Math.abs(painter.position - horse.position) === 1;
      },
      
      // 13. The person who plays football drinks orange juice
      (houses) => houses.find(h => h.hobby === 'football').drink === 'orange juice',
      
      // 14. The Japanese person plays chess
      (houses) => houses.find(h => h.nationality === 'Japanese').hobby === 'chess',
      
      // 15. The Norwegian lives next to the blue house
      (houses) => {
        const norwegian = houses.find(h => h.nationality === 'Norwegian');
        const blue = houses.find(h => h.color === 'blue');
        return Math.abs(norwegian.position - blue.position) === 1;
      }
    ];

    // Generate all possible houses
    const houses = Array.from({length: 5}, (_, i) => ({position: i + 1}));
    
    // Solve using backtracking
    const solution = this.backtrack(houses, constraints);
    if (!solution) throw new Error('No solution found');
    return solution;
  }

  backtrack(houses, constraints, index = 0) {
    if (index === houses.length) {
      // Check all constraints
      return constraints.every(constraint => constraint(houses)) ? houses : null;
    }

    // Get possible values for each category
    const possibleValues = this.getPossibleValues(houses, index);
    
    for (const values of possibleValues) {
      // Assign values to current house
      Object.assign(houses[index], values);
      
      // Check constraints that can be evaluated at this point
      const valid = constraints.every(constraint => {
        try {
          return constraint(houses);
        } catch {
          return true; // Constraint can't be evaluated yet
        }
      });
      
      if (valid) {
        const result = this.backtrack(houses, constraints, index + 1);
        if (result) return result;
      }
      
      // Backtrack
      Object.keys(values).forEach(key => delete houses[index][key]);
    }
    
    return null;
  }

  getPossibleValues(houses, index) {
    // Get used values in each category
    const used = {
      color: new Set(houses.map(h => h.color).filter(Boolean)),
      nationality: new Set(houses.map(h => h.nationality).filter(Boolean)),
      drink: new Set(houses.map(h => h.drink).filter(Boolean)),
      pet: new Set(houses.map(h => h.pet).filter(Boolean)),
      hobby: new Set(houses.map(h => h.hobby).filter(Boolean))
    };

    // Get all possible values for each category
    const allValues = {
      color: ['red', 'green', 'ivory', 'yellow', 'blue'],
      nationality: ['Englishman', 'Spaniard', 'Ukrainian', 'Norwegian', 'Japanese'],
      drink: ['coffee', 'tea', 'milk', 'orange juice', 'water'],
      pet: ['dog', 'snail', 'fox', 'horse', 'zebra'],
      hobby: ['dancing', 'painter', 'reading', 'football', 'chess']
    };

    // Generate possible combinations
    const combinations = [];
    for (const color of allValues.color) {
      if (used.color.has(color)) continue;
      for (const nationality of allValues.nationality) {
        if (used.nationality.has(nationality)) continue;
        for (const drink of allValues.drink) {
          if (used.drink.has(drink)) continue;
          for (const pet of allValues.pet) {
            if (used.pet.has(pet)) continue;
            for (const hobby of allValues.hobby) {
              if (used.hobby.has(hobby)) continue;
              combinations.push({color, nationality, drink, pet, hobby});
            }
          }
        }
      }
    }
    
    return combinations;
  }

  waterDrinker() {
    return this.solution.find(h => h.drink === 'water').nationality;
  }

  zebraOwner() {
    return this.solution.find(h => h.pet === 'zebra').nationality;
  }
}
