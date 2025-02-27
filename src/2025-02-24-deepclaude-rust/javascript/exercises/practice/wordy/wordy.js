//
// This is only a SKELETON file for the 'Wordy' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const answer = (question) => {
  if (!/^What is .+\?$/.test(question)) {
    throw new Error('Unknown operation');
  }

  const mathPart = question.slice(8, -1).trim();

  if (mathPart === '') {
    throw new Error('Syntax error');
  }

  const tokens = mathPart.match(/-?\d+|multiplied by|divided by|plus|minus/g);

  // Check if all input was consumed by valid tokens
  if (!tokens || tokens.length === 0 || tokens.join(' ') !== mathPart) {
    throw new Error('Unknown operation');
  }

  if (!/^-?\d+$/.test(tokens[0])) {
    throw new Error('Syntax error');
  }

  if (tokens.length % 2 === 0) {
    throw new Error('Syntax error');
  }

  for (let i = 1; i < tokens.length; i += 2) {
    const operator = tokens[i];
    const numberToken = tokens[i + 1];

    if (!['plus', 'minus', 'multiplied by', 'divided by'].includes(operator)) {
      throw new Error('Unknown operation');
    }

    if (!/^-?\d+$/.test(numberToken)) {
      throw new Error('Syntax error');
    }
  }

  let result = parseInt(tokens[0], 10);

  for (let i = 1; i < tokens.length; i += 2) {
    const operator = tokens[i];
    const num = parseInt(tokens[i + 1], 10);

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
        if (num === 0) {
          throw new Error('Division by zero');
        }
        if (result % num !== 0) {
          throw new Error('Non-integer division');
        }
        result /= num;
        break;
      default:
        throw new Error('Unknown operation');
    }
  }

  return result;
};
