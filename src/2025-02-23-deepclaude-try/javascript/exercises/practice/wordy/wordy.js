export const answer = (question) => {
  // Validate question format
  if (!question || typeof question !== 'string') {
    throw new Error('Invalid question format');
  }

  const match = question.match(/^What is (.*)\?$/i);
  if (!match) {
    throw new Error('Unknown operation');
  }

  // Extract the expression
  const expression = match[1].trim();
  if (!expression) {
    throw new Error('Syntax error');
  }

  // Split into tokens and filter empty strings
  const tokens = expression
    .split(/(plus|minus|multiplied by|divided by)/i)
    .map(token => token.trim())
    .filter(Boolean);

  // Validate first token is a number
  if (!/^-?\d+$/.test(tokens[0])) {
    throw new Error('Syntax error');
  }

  let result = parseInt(tokens[0]);

  // Process operations from left to right
  for (let i = 1; i < tokens.length; i += 2) {
    const operator = tokens[i].toLowerCase();
    const nextNum = tokens[i + 1];

    // Validate next token exists and is a number
    if (!nextNum || !/^-?\d+$/.test(nextNum)) {
      throw new Error('Syntax error');
    }

    const num = parseInt(nextNum);

    switch (operator) {
      case 'plus':
        result += num;
        break;
      case 'minus':
        result -= num;
        break;
      case 'multiplied by':
        result *= num;
        break;
      case 'divided by':
        result /= num;
        break;
      default:
        throw new Error('Unknown operation');
    }
  }

  return result;
};
