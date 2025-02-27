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

const VALID_OPTIONS = ['n', 'l', 'i', 'v', 'x'];
const ARGS = process.argv;

function parseArguments(args) {
  let i = 0;
  const flags = [];
  while (i < args.length && args[i].startsWith('-')) {
    const flagStr = args[i].slice(1);
    for (const c of flagStr) {
      if (!VALID_OPTIONS.includes(c)) {
        console.error(`Invalid flag: -${c}`);
        process.exit(1);
      }
      flags.push(c);
    }
    i++;
  }

  if (i >= args.length) {
    console.error('Missing search pattern');
    process.exit(1);
  }
  const pattern = args[i];
  const files = args.slice(i + 1);

  if (files.length === 0) {
    console.error('At least one file is required');
    process.exit(1);
  }

  return { flags, pattern, files };
}

function matchesPattern(line, pattern, flags) {
  let processedLine = line;
  let processedPattern = pattern;
  if (flags.includes('i')) {
    processedLine = processedLine.toLowerCase();
    processedPattern = processedPattern.toLowerCase();
  }
  return flags.includes('x') 
    ? processedLine === processedPattern
    : processedLine.includes(processedPattern);
}

try {
  const { flags, pattern, files } = parseArguments(ARGS.slice(2));
  const output = [];
  const uniqueFiles = new Set();

  for (const file of files) {
    const lines = readLines(file);
    let fileAdded = false;

    for (let lineNum = 0; lineNum < lines.length; lineNum++) {
      const line = lines[lineNum];
      const match = matchesPattern(line, pattern, flags);
      const shouldInclude = flags.includes('v') ? !match : match;

      if (shouldInclude) {
        if (flags.includes('l')) {
          if (!fileAdded) {
            uniqueFiles.add(file);
            fileAdded = true;
          }
          break; // Once added, no need to check other lines
        }

        const parts = [];
        if (files.length > 1) parts.push(file);
        if (flags.includes('n')) parts.push(lineNum + 1);
        parts.push(line);
        output.push(parts.join(':'));
      }
    }
  }

  if (flags.includes('l')) {
    console.log(Array.from(uniqueFiles).join('\n'));
  } else {
    console.log(output.join('\n'));
  }
} catch (error) {
  console.error(error.message);
  process.exit(1);
}
