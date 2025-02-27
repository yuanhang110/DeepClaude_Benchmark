//
// This is only a SKELETON file for the 'BookStore' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const cost = (books) => {
    const counts = [0, 0, 0, 0, 0];
    for (const book of books) {
        counts[book - 1]++; // assuming books are 1-based
    }

    const groups = [0, 0, 0, 0, 0]; // groups of size 1-5
    let remaining = [...counts];
    while (remaining.some(x => x > 0)) {
        remaining.sort((a, b) => b - a);
        let k = 0;
        for (; k < 5; k++) {
            if (remaining[k] === 0) break;
        }
        if (k === 0) break;
        groups[k - 1]++;
        for (let i = 0; i < k; i++) {
            remaining[i]--;
        }
    }

    // Replace groups of 5 and 3 with two groups of 4
    const replacements = Math.min(groups[4], groups[2]);
    groups[4] -= replacements;
    groups[2] -= replacements;
    groups[3] += replacements * 2;

    // Calculate total cost in cents
    const total = groups[0] * 800 +
                 groups[1] * 1520 +
                 groups[2] * 2160 +
                 groups[3] * 2560 +
                 groups[4] * 3000;

    return total;
};
