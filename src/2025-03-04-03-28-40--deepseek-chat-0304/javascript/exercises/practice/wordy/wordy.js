export const answer = (question) => {
  // Validate basic question format
  if (!question.startsWith('What is ') || !question.endsWith('?')) {
    throw new Error('Syntax error');
  }

  // Remove wrapper text
  const expr = question.slice(8, -1).trim();

  // Handle empty expression
  if (expr.length === 0) {
    throw new Error('Syntax error');
  }

  // Handle simple number case
  if (/^-?\d+$/.test(expr)) {
    return parseInt(expr, 10);
  }

  // Tokenize the expression
  const tokens = expr.split(/(plus|minus|multiplied by|divided by)/i)
    .map(t => t.trim())
    .filter(t => t.length > 0);

  // Validate token sequence
  if (tokens.length < 3 || tokens.length % 2 === 0) {
    throw new Error('Syntax error');
  }

  // Check for two numbers in a row
  for (let i = 0; i < tokens.length - 1; i++) {
    if (/^-?\d+$/.test(tokens[i]) && /^-?\d+$/.test(tokens[i+1])) {
      throw new Error('Syntax error');
    }
  }

  // Initialize with first number
  let result = parseInt(tokens[0], 10);
  if (isNaN(result)) {
    throw new Error('Syntax error');
  }

  // Process operations left-to-right
  for (let i = 1; i < tokens.length; i += 2) {
    const op = tokens[i].toLowerCase();
    const num = parseInt(tokens[i+1], 10);
    if (isNaN(num)) {
      throw new Error('Syntax error');
    }

    switch(op) {
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
        result = Math.floor(result / num);
        break;
      default:
        throw new Error('Unknown operation');
    }
  }

  return result;
};
