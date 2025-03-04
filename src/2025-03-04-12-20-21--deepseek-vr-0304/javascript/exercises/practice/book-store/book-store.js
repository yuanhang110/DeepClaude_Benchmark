//
// This is only a SKELETON file for the 'BookStore' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const cost = (books) => {
    const counts = new Array(5).fill(0);
    for (const book of books) {
        counts[book - 1]++;
    }

    const groups = [];
    let tempCounts = [...counts];
    while (tempCounts.some(c => c > 0)) {
        tempCounts.sort((a, b) => b - a);
        let groupSize = 0;
        for (let i = 0; i < tempCounts.length; i++) {
            if (tempCounts[i] > 0) {
                groupSize++;
                tempCounts[i]--;
            }
        }
        groups.push(groupSize);
    }

    let total = 0;
    for (const size of groups) {
        switch(size) {
            case 1:
                total += 800;
                break;
            case 2:
                total += 2 * 800 * 0.95;
                break;
            case 3:
                total += 3 * 800 * 0.90;
                break;
            case 4:
                total += 4 * 800 * 0.80;
                break;
            case 5:
                total += 5 * 800 * 0.75;
                break;
            default:
                total += size * 800;
        }
    }

    const num5 = groups.filter(g => g ===5).length;
    const num3 = groups.filter(g => g ===3).length;
    const pairs = Math.min(num5, num3);
    total -= pairs * 40;

    return Math.round(total);
};
