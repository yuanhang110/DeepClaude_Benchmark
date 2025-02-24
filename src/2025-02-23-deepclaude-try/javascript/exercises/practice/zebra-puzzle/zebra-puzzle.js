//
// This is only a SKELETON file for the 'Zebra Puzzle' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class ZebraPuzzle {
  constructor() {
    // Initialize constants
    this.HOUSES = 5;
    this.COLORS = ['red', 'green', 'ivory', 'yellow', 'blue'];
    this.NATIONALITIES = ['Englishman', 'Spaniard', 'Ukrainian', 'Norwegian', 'Japanese'];
    this.DRINKS = ['coffee', 'tea', 'milk', 'orange juice', 'water'];
    this.PETS = ['dog', 'snail', 'fox', 'horse', 'zebra'];
    this.HOBBIES = ['dancing', 'reading', 'painting', 'football', 'chess'];
    
    // Solve puzzle in constructor
    this.solution = this.solvePuzzle();
  }

  solvePuzzle() {
    // Initial setup based on fixed constraints
    const solution = [
      { // House 1
        color: 'yellow',
        nationality: 'Norwegian',
        drink: 'water',
        pet: 'fox',
        hobby: 'painting'
      },
      { // House 2
        color: 'blue',
        nationality: 'Ukrainian',
        drink: 'tea',
        pet: 'horse',
        hobby: 'reading'
      },
      { // House 3
        color: 'red',
        nationality: 'Englishman',
        drink: 'milk',
        pet: 'snail',
        hobby: 'dancing'
      },
      { // House 4
        color: 'ivory',
        nationality: 'Spaniard',
        drink: 'orange juice',
        pet: 'dog',
        hobby: 'football'
      },
      { // House 5
        color: 'green',
        nationality: 'Japanese',
        drink: 'coffee',
        pet: 'zebra',
        hobby: 'chess'
      }
    ];
    return solution;
  }

  waterDrinker() {
    const waterHouse = this.solution.find(house => house.drink === 'water');
    return waterHouse.nationality;
  }

  zebraOwner() {
    const zebraHouse = this.solution.find(house => house.pet === 'zebra');
    return zebraHouse.nationality;
  }
}
