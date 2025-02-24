export const answer = (question) => {
  // Normalize and validate question format
  const normalized = question.toLowerCase().replace(/[^a-z0-9\s-]/g, '').trim();
  if (!normalized.startsWith('what is ')) {
    throw new Error('Unknown operation');
  }

  const rest = normalized.slice('what is '.length).trim();
  if (!rest) {
    throw new Error('Syntax error');
  }

  const tokens = rest.match(/(-?\d+|plus|minus|multiplied by|divided by)/g) || [];
  if (tokens.join(' ') !== rest) {
    throw new Error('Unknown operation');
  }

  if (tokens.length % 2 === 0 || tokens.length === 0) {
    throw new Error('Syntax error');
  }

  // Validate token sequence
  if (isNaN(tokens[0])) {
    throw new Error('Syntax error');
  }

  for (let i = 1; i < tokens.length; i += 2) {
    if (!['plus', 'minus', 'multiplied by', 'divided by'].includes(tokens[i])) {
      throw new Error('Unknown operation');
    }
    if (i + 1 >= tokens.length || isNaN(tokens[i + 1])) {
      throw new Error('Syntax error');
    }
  }

  let result = parseInt(tokens[0]);
  
  for (let i = 2; i < tokens.length; i += 2) {
    const operation = tokens[i];
    const operand = parseInt(tokens[i+1]);
    
    switch(operation) {
      case 'plus':
        result += operand;
        break;
      case 'minus':
        result -= operand;
        break;
      case 'multiplied by':
        result *= operand;
        break;
      case 'divided by':
        if (operand === 0) throw new Error('Division by zero');
        result = Math.floor(result / operand);
        break;
    }
  }
  
  return result;
};
