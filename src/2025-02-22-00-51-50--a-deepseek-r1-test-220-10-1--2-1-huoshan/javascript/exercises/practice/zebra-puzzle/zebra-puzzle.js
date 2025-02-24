//
// This is only a SKELETON file for the 'Zebra Puzzle' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class ZebraPuzzle {
  constructor() {
    this.solution = this.solve();
  }

  solve() {
    const permutations = (arr) => {
      if (arr.length <= 1) return [arr];
      const perms = [];
      for (let i = 0; i < arr.length; i++) {
        const rest = [...arr.slice(0, i), ...arr.slice(i + 1)];
        for (const p of permutations(rest)) {
          perms.push([arr[i], ...p]);
        }
      }
      return perms;
    };

    // Predefine possible values
    const colors = ['red', 'green', 'ivory', 'yellow', 'blue'];
    const nations = ['Norwegian', 'Englishman', 'Spaniard', 'Ukrainian', 'Japanese'];
    const pets = ['dog', 'snail', 'fox', 'horse', 'zebra'];
    const drinks = ['water', 'tea', 'milk', 'coffee', 'orange juice'];
    const hobbies = ['dancing', 'painting', 'reading', 'football', 'chess'];

    // Generate all valid permutations with constraints
    for (const color of permutations(colors)) {
      // Green is immediately to the right of ivory (clue 6)
      const ivoryIdx = color.indexOf('ivory');
      if (color[ivoryIdx + 1] !== 'green') continue;
      
      // Norwegian in first house (clue 10) and next to blue (clue 15)
      if (color[1] !== 'blue') continue;

      for (const nation of permutations(nations)) {
        // Norwegian must be first (clue 10)
        if (nation[0] !== 'Norwegian') continue;
        
        // Englishman in red house (clue 2)
        const englishIdx = nation.indexOf('Englishman');
        if (color[englishIdx] !== 'red') continue;

        for (const drink of permutations(drinks)) {
          // Milk in middle house (clue 9)
          if (drink[2] !== 'milk') continue;
          
          // Coffee in green house (clue 4)
          const greenIdx = color.indexOf('green');
          if (drink[greenIdx] !== 'coffee') continue;
          
          // Ukrainian drinks tea (clue 5)
          const ukrainianIdx = nation.indexOf('Ukrainian');
          if (drink[ukrainianIdx] !== 'tea') continue;

          for (const pet of permutations(pets)) {
            // Spaniard owns dog (clue 3)
            const spaniardIdx = nation.indexOf('Spaniard');
            if (pet[spaniardIdx] !== 'dog') continue;

            for (const hobby of permutations(hobbies)) {
              // Yellow house is painter (clue 8)
              const yellowIdx = color.indexOf('yellow');
              if (hobby[yellowIdx] !== 'painting') continue;
              
              // Snail owner likes dancing (clue 7)
              const snailIdx = pet.indexOf('snail');
              if (hobby[snailIdx] !== 'dancing') continue;
              
              // Football player drinks orange juice (clue 13)
              const footballIdx = hobby.indexOf('football');
              if (drink[footballIdx] !== 'orange juice') continue;
              
              // Japanese plays chess (clue 14)
              const japaneseIdx = nation.indexOf('Japanese');
              if (hobby[japaneseIdx] !== 'chess') continue;

              // Check adjacency constraints
              const isAdjacent = (i, j) => Math.abs(i - j) === 1;
              
              // Reading next to fox (clue 11)
              const readingIdx = hobby.indexOf('reading');
              if (readingIdx >= 0 && !isAdjacent(readingIdx, pet.indexOf('fox'))) continue;
              
              // Painter next to horse (clue 12)
              const painterIdx = hobby.indexOf('painting');
              if (painterIdx >= 0 && !isAdjacent(painterIdx, pet.indexOf('horse'))) continue;

              // Found valid solution
              return {
                nations,
                drinks,
                pets
              };
            }
          }
        }
      }
    }
    throw new Error('No solution found');
  }

  waterDrinker() {
    const waterIdx = this.solution.drinks.indexOf('water');
    return this.solution.nations[waterIdx];
  }

  zebraOwner() {
    const zebraIdx = this.solution.pets.indexOf('zebra');
    return this.solution.nations[zebraIdx];
  }
}
