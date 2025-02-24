//
// This is only a SKELETON file for the 'BookStore' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const cost = (books) => {
  if (books.length === 0) return 0;

  // Count book frequencies
  const counts = {};
  books.forEach(book => counts[book] = (counts[book] || 0) + 1);
  
  // Get sorted frequencies in descending order
  let freq = Object.values(counts).sort((a, b) => b - a);
  const groups = {1: 0, 2: 0, 3: 0, 4: 0, 5: 0};

  // Greedily create largest possible groups
  while (freq.length > 0 && freq[0] > 0) {
    // Determine current group size (number of different books)
    const size = freq.findIndex(c => c === 0) === -1 ? freq.length : freq.findIndex(c => c === 0);
    if (size === 0) break;
    
    groups[size]++;
    
    // Reduce counts for books in this group
    for (let i = 0; i < size; i++) {
      freq[i]--;
    }
    
    // Re-sort and remove zeros
    freq = freq.filter(c => c > 0).sort((a, b) => b - a);
  }

  // Optimize groups: replace 5+3 groups with 4+4 for better discount
  const optimizedGroups = Math.min(groups[5], groups[3]);
  groups[5] -= optimizedGroups;
  groups[3] -= optimizedGroups;
  groups[4] += optimizedGroups * 2;

  // Calculate total cost with discounts
  const discounts = {1: 1.0, 2: 0.95, 3: 0.9, 4: 0.8, 5: 0.75};
  let total = 0;
  for (const [size, count] of Object.entries(groups)) {
    total += count * size * 800 * discounts[size];
  }

  return Math.round(total);
};
