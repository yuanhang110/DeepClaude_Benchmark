//
// This is only a SKELETON file for the 'Wordy' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const answer = (question) => {
  // Check if it's a valid math question
  const match = question.match(/^What is (.*)\?$/i);
  if (!match) {
    throw new Error('Unknown operation');
  }

  let expression = match[1].trim();
  
  if (expression === '') {
    throw new Error('Syntax error');
  }
  
  // Replace word operations with symbols
  expression = expression.replace(/multiplied by/g, '*')
                         .replace(/divided by/g, '/')
                         .replace(/plus/g, '+')
                         .replace(/minus/g, '-');
  
  // Split into tokens
  const tokens = expression.split(' ').filter(token => token !== '');
  
  // Validate the expression structure
  if (tokens.length === 0) {
    throw new Error('Syntax error');
  }
  
  const validOperators = new Set(['+', '-', '*', '/']);
  
  // Validate each token
  for (let i = 0; i < tokens.length; i++) {
    const token = tokens[i];
    if (i % 2 === 0) {
      // Positions 0, 2, 4, etc. should be numbers
      if (!/^-?\d+$/.test(token)) {
        throw new Error('Syntax error');
      }
    } else {
      // Positions 1, 3, 5, etc. should be operators
      if (/^-?\d+$/.test(token)) {
        // Number in operator position is a syntax error
        throw new Error('Syntax error');
      }
      if (!validOperators.has(token)) {
        throw new Error('Unknown operation');
      }
    }
  }
  
  if (tokens.length % 2 === 0) {
    throw new Error('Syntax error');
  }
  
  // Evaluate the expression from left to right
  let result = parseInt(tokens[0], 10);
  
  for (let i = 1; i < tokens.length; i += 2) {
    const operator = tokens[i];
    const number = parseInt(tokens[i + 1], 10);
    
    switch (operator) {
      case '+':
        result += number;
        break;
      case '-':
        result -= number;
        break;
      case '*':
        result *= number;
        break;
      case '/':
        if (number === 0) {
          throw new Error('Division by zero');
        }
        result /= number;
        if (!Number.isInteger(result)) {
          throw new Error('Syntax error');
        }
        break;
    }
  }
  
  return result;
};
