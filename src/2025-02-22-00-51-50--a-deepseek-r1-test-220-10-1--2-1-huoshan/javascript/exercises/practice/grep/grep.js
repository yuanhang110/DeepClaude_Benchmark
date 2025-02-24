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
  const data = fs.readFileSync(file, { encoding: 'utf-8' });
  return data.split(/\r?\n/);
}

const VALID_OPTIONS = [
  'n', // add line numbers
  'l', // print file names where pattern is found
  'i', // ignore case
  'v', // reverse files results
  'x', // match entire line
];

// Parse command line arguments
const args = process.argv.slice(2);
const flags = args.filter(arg => arg.startsWith('-')).join('').replace(/-/g, '');
const pattern = args.find(arg => !arg.startsWith('-') && !args[args.indexOf(arg)+1]?.includes('.'));
const files = args.filter(arg => arg !== pattern && !arg.startsWith('-'));

// Validate input
if (!pattern || files.length === 0) {
  console.error('Usage: ./grep.js [flags] pattern files...');
  process.exit(1);
}

// Check for invalid flags
const invalidFlags = flags.split('').filter(f => !VALID_OPTIONS.includes(f));
if (invalidFlags.length > 0) {
  console.error(`Invalid flag(s) -${invalidFlags.join('')}`);
  process.exit(1);
}

// Prepare matching parameters
const opts = {
  lineNumbers: flags.includes('n'),
  fileNamesOnly: flags.includes('l'),
  ignoreCase: flags.includes('i'),
  invertMatch: flags.includes('v'),
  entireLine: flags.includes('x')
};

// Prepare pattern matching
const testPattern = opts.ignoreCase ? pattern.toLowerCase() : pattern;
const matchFn = (line) => {
  const testLine = opts.ignoreCase ? line.toLowerCase() : line;
  let found = false;
  
  if (opts.entireLine) {
    found = testLine === testPattern;
  } else {
    found = testLine.includes(testPattern);
  }
  
  return opts.invertMatch ? !found : found;
};

// Process files and collect results
const results = [];
const fileMatches = new Set();

for (const file of files) {
  try {
    const lines = readLines(file);
    let hasMatch = false;

    lines.forEach((line, index) => {
      if (matchFn(line)) {
        hasMatch = true;
        
        if (opts.fileNamesOnly) return;
        
        let output = '';
        if (files.length > 1) output += `${file}:`;
        if (opts.lineNumbers) output += `${index + 1}:`;
        output += line;
        
        results.push(output);
      }
    });

    if (hasMatch && opts.fileNamesOnly) {
      results.push(file);
    }
  } catch (err) {
    console.error(`Error reading file ${file}: ${err.message}`);
  }
}

// Output final results
if (results.length > 0) {
  console.log(results.join('\n'));
}
