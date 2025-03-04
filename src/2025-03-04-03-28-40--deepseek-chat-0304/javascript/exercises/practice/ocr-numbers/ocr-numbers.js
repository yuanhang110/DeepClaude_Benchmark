// Digit patterns for 0-9
const DIGITS = {
    ' _ | ||_|   ': '0',
    '     |  |   ': '1',
    ' _  _||_    ': '2',
    ' _  _| _|   ': '3',
    '   |_|  |   ': '4',
    ' _ |_  _|   ': '5',
    ' _ |_ |_|   ': '6',
    ' _   |  |   ': '7',
    ' _ |_||_|   ': '8',
    ' _ |_| _|   ': '9'
};

export const convert = (input) => {
    const lines = input.split('\n');
    
    // Validate input size
    if (lines.length % 4 !== 0) {
        throw new Error('Number of input lines is not a multiple of four');
    }
    for (let line of lines) {
        if (line.length % 3 !== 0) {
            throw new Error('Number of input columns is not a multiple of three');
        }
    }

    let result = [];
    for (let i = 0; i < lines.length; i += 4) {
        let lineResult = '';
        const numDigits = lines[i].length / 3;
        
        for (let digit = 0; digit < numDigits; digit++) {
            // Extract the 3x4 grid for this digit
            let pattern = '';
            for (let row = 0; row < 4; row++) {
                pattern += lines[i + row].substring(digit * 3, (digit + 1) * 3).padEnd(3, ' ');
            }
            // Match pattern or use ? if not found
            lineResult += DIGITS[pattern] || '?';
        }
        
        result.push(lineResult);
    }
    
    return result.join(',');
};
