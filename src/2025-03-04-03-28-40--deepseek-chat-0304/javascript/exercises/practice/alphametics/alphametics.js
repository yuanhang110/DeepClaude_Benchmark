function solveEquation(puzzle, letters, mapping) {
    const [left, right] = puzzle.split('=').map(s => s.trim());
    const leftWords = left.split('+').map(s => s.trim());
    
    let leftSum = 0;
    for (const word of leftWords) {
        let num = 0;
        for (const letter of word) {
            num = num * 10 + mapping[letter];
        }
        leftSum += num;
    }
    
    let rightSum = 0;
    for (const letter of right) {
        rightSum = rightSum * 10 + mapping[letter];
    }
    
    return leftSum === rightSum;
}

function assignDigits(letters, leadingLetters, digitsUsed, index, mapping, puzzle) {
    if (index === letters.length) {
        return solveEquation(puzzle, letters, mapping) ? {...mapping} : null;
    }
    
    const letter = letters[index];
    for (let digit = 0; digit <= 9; digit++) {
        if (digitsUsed[digit]) continue;
        if (digit === 0 && leadingLetters.has(letter)) continue;
        
        digitsUsed[digit] = true;
        mapping[letter] = digit;
        
        const result = assignDigits(letters, leadingLetters, digitsUsed, index + 1, mapping, puzzle);
        if (result) return result;
        
        digitsUsed[digit] = false;
    }
    
    return null;
}

export const solve = (puzzle) => {
    const letters = [...new Set(puzzle.replace(/[^A-Z]/g, ''))];
    const [left, right] = puzzle.split('=').map(s => s.trim());
    const leftWords = left.split('+').map(s => s.trim());
    
    const leadingLetters = new Set([
        ...leftWords.map(word => word[0]),
        right[0]
    ]);
    
    const digitsUsed = Array(10).fill(false);
    const mapping = {};
    
    return assignDigits(letters, leadingLetters, digitsUsed, 0, mapping, puzzle);
};
