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

function parseArgs(args) {
  const flags = new Set();
  let pattern = '';
  const files = [];
  
  for (let i = 2; i < args.length; i++) {
    const arg = args[i];
    if (arg.startsWith('-')) {
      for (const flag of arg.slice(1)) {
        if (VALID_OPTIONS.includes(flag)) {
          flags.add(flag);
        } else {
          console.error(`Invalid option: -${flag}`);
          process.exit(1);
        }
      }
    } else if (!pattern) {
      pattern = arg;
    } else {
      files.push(arg);
    }
  }
  
  if (!pattern || files.length === 0) {
    console.error('Usage: grep [-nlivx] pattern file...');
    process.exit(1);
  }
  
  return { flags, pattern, files };
}

function matchLine(line, pattern, flags) {
  let text = line;
  let searchPattern = pattern;
  
  if (flags.has('i')) {
    text = text.toLowerCase();
    searchPattern = searchPattern.toLowerCase();
  }
  
  if (flags.has('x')) {
    return text === searchPattern;
  }
  
  return text.includes(searchPattern);
}

function formatLine(line, file, lineNumber, flags) {
  if (flags.has('l')) {
    return file;
  }
  
  let output = '';
  if (files.length > 1) {
    output += `${file}:`;
  }
  if (flags.has('n')) {
    output += `${lineNumber}:`;
  }
  output += line;
  return output;
}

function grep() {
  const { flags, pattern, files } = parseArgs(process.argv);
  const outputLines = new Set();
  
  for (const file of files) {
    try {
      const lines = readLines(file);
      let hasMatch = false;
      
      for (let i = 0; i < lines.length; i++) {
        const line = lines[i];
        const matches = matchLine(line, pattern, flags);
        
        if ((matches && !flags.has('v')) || (!matches && flags.has('v'))) {
          const formattedLine = formatLine(line, file, i + 1, flags);
          outputLines.add(formattedLine);
          hasMatch = true;
        }
      }
      
      if (flags.has('l') && hasMatch) {
        outputLines.add(file);
      }
    } catch (err) {
      console.error(`grep: ${file}: No such file or directory`);
      process.exit(1);
    }
  }
  
  for (const line of outputLines) {
    console.log(line);
  }
}

grep();
