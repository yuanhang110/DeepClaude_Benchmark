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

// Get command line arguments, skipping 'node' and script name
const args = ARGS.slice(2);

// Parse flags and get pattern
let pattern = '';
let files = [];
const flags = new Set();

for (const arg of args) {
  if (arg.startsWith('-')) {
    // Parse flags like -n, -l, etc
    const flagChars = arg.slice(1).split('');
    for (const flag of flagChars) {
      if (!VALID_OPTIONS.includes(flag)) {
        console.error(`Invalid flag: ${flag}`);
        process.exit(1);
      }
      flags.add(flag);
    }
  } else if (!pattern) {
    pattern = arg;
  } else {
    files.push(arg);
  }
}

// Helper function to check if a line matches the pattern
function matchesPattern(line, pattern) {
  if (flags.has('x')) {
    // Exact match of entire line
    return flags.has('i') 
      ? line.toLowerCase() === pattern.toLowerCase()
      : line === pattern;
  }
  // Partial match within line
  return flags.has('i')
    ? line.toLowerCase().includes(pattern.toLowerCase()) 
    : line.includes(pattern);
}

// Process each file
const results = [];
for (const file of files) {
  try {
    const lines = readLines(file);
    
    if (flags.has('l')) {
      // Just check if file has any matches
      const hasMatch = lines.some(line => 
        flags.has('v') ? !matchesPattern(line, pattern) : matchesPattern(line, pattern)
      );
      if (hasMatch) {
        results.push(file);
      }
      continue;
    }

    lines.forEach((line, index) => {
      const matches = flags.has('v') ? !matchesPattern(line, pattern) : matchesPattern(line, pattern);
      
      if (matches) {
        let output = '';
        if (files.length > 1) {
          output += `${file}:`;
        }
        if (flags.has('n')) {
          output += `${index + 1}:`;
        }
        output += line;
        results.push(output);
      }
    });
  } catch (err) {
    if (!flags.has('s')) {
      console.error(`Error reading file ${file}: ${err.message}`);
      process.exit(1);
    }
  }
}

// Output results
if (results.length > 0) {
  console.log(results.join('\n'));
}
