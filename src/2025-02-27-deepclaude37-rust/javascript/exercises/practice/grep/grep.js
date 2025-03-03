#!/usr/bin/env node

// The above line is a shebang. On Unix-like operating systems, or environments,
// this will allow the script to be run by node, and thus turn this JavaScript
// file into an executable. In other words, to execute this file, you may run
// the following from your terminal:
//
// ./grep.js args
//
// If you don't have a Unix-like operating system or environment, for example
// Windows without WSL, you can use the following inside a window terminal,
// such as cmd.exe:
//
// node grep.js args
//
// Read more about shebangs here: https://en.wikipedia.org/wiki/Shebang_(Unix)

const fs = require('fs');
const path = require('path');

/**
 * Reads the given file and returns lines.
 *
 * This function works regardless of POSIX (LF) or windows (CRLF) encoding.
 *
 * @param {string} file path to file
 * @returns {string[]} the lines
 */
function readLines(file) {
  const data = fs.readFileSync(path.resolve(file), { encoding: 'utf-8' });
  return data.split(/\r?\n/);
}

const VALID_OPTIONS = [
  'n', // add line numbers
  'l', // print file names where pattern is found
  'i', // ignore case
  'v', // reverse files results
  'x', // match entire line
];

const args = process.argv.slice(2);

// Check if we have enough arguments
if (args.length === 0) {
  console.error('Usage: ./grep.js searchString [-flags] files...');
  process.exit(1);
}

// First argument is the search string
const searchString = args[0];
const flags = [];
const files = [];

// Parse flags and files
let currentIndex = 1;
while (currentIndex < args.length && args[currentIndex].startsWith('-')) {
  const flagString = args[currentIndex].slice(1);
  for (const flag of flagString) {
    if (!VALID_OPTIONS.includes(flag)) {
      console.error(`grep: invalid option -- ${flag}`);
      process.exit(1);
    }
    flags.push(flag);
  }
  currentIndex++;
}

// Remaining arguments are files
files.push(...args.slice(currentIndex));

if (files.length === 0) {
  console.error('grep: missing file operand');
  process.exit(1);
}

// Process files and collect matching lines
const output = [];
const multipleFiles = files.length > 1;

for (const file of files) {
  let lines;
  try {
    // Look for files in both current directory and 'grep' subdirectory
    try {
      lines = readLines(file);
    } catch (firstError) {
      try {
        // Try with 'grep/' prefix if direct file access fails
        lines = readLines(`grep/${file}`);
      } catch (secondError) {
        // If both attempts fail, throw the original error
        throw firstError;
      }
    }
  } catch (e) {
    console.error(`grep: ${file}: ${e.message}`);
    process.exit(1);
  }

  const matchedLines = [];
  
  // Process each line in the file
  for (let i = 0; i < lines.length; i++) {
    const line = lines[i];
    const lineNumber = i + 1;
    
    // Determine if the line matches based on the flags
    let lineToCheck = line;
    let patternToCheck = searchString;
    
    if (flags.includes('i')) {
      lineToCheck = lineToCheck.toLowerCase();
      patternToCheck = patternToCheck.toLowerCase();
    }
    
    let isMatch;
    if (flags.includes('x')) {
      isMatch = lineToCheck === patternToCheck;
    } else {
      isMatch = lineToCheck.includes(patternToCheck);
    }
    
    // Handle invert flag
    if (flags.includes('v')) {
      isMatch = !isMatch;
    }
    
    if (isMatch) {
      matchedLines.push({ lineNumber, line });
    }
  }
  
  // Handle file name only flag
  if (flags.includes('l')) {
    if (matchedLines.length > 0) {
      output.push(file);
    }
  } else {
    // Format and add each matched line to the output
    for (const { lineNumber, line } of matchedLines) {
      let formattedLine = '';
      
      if (multipleFiles) {
        formattedLine += `${file}:`;
      }
      
      if (flags.includes('n')) {
        formattedLine += `${lineNumber}:`;
      }
      
      formattedLine += line;
      output.push(formattedLine);
    }
  }
}

// Print the results
if (output.length > 0) {
  console.log(output.join('\n'));
}
