//
// This is only a SKELETON file for the 'Transpose' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const transpose = (input) => {
    if (input.length === 0) return [];
    
    const maxLen = Math.max(...input.map(row => row.length));
    const result = [];
    
    for (let i = 0; i < maxLen; i++) {
        let transposedRow = [];
        let pad = false;
        
        for (let j = input.length - 1; j >= 0; j--) {
            const row = input[j];
            if (row.length > i) {
                transposedRow.unshift(row[i]);
                pad = true;
            } else if (pad) {
                transposedRow.unshift(' ');
            }
        }
        
        result.push(transposedRow.join(''));
    }
    
    return result;
};
