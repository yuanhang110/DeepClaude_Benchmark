use anyhow::Error;

/// While using `&[&str]` to handle flags is convenient for exercise purposes,
/// and resembles the output of [`std::env::args`], in real-world projects it is
/// both more convenient and more idiomatic to contain runtime configuration in
/// a dedicated struct. Therefore, we suggest that you do so in this exercise.
///
/// [`std::env::args`]: https://doc.rust-lang.org/std/env/fn.args.html
#[derive(Debug)]
pub struct Flags {
    line_numbers: bool,
    list_files: bool,
    case_insensitive: bool,
    invert: bool,
    exact_match: bool,
}

impl Flags {
    pub fn new(flags: &[&str]) -> Self {
        let mut result = Flags {
            line_numbers: false,
            list_files: false,
            case_insensitive: false,
            invert: false,
            exact_match: false,
        };

        for flag in flags {
            match *flag {
                "-n" => result.line_numbers = true,
                "-l" => result.list_files = true,
                "-i" => result.case_insensitive = true,
                "-v" => result.invert = true,
                "-x" => result.exact_match = true,
                _ => (),
            }
        }

        result
    }
}

use std::fs;
use std::path::Path;

pub fn grep(pattern: &str, flags: &Flags, files: &[&str]) -> Result<Vec<String>, Error> {
    let mut results = Vec::new();
    let pattern = if flags.case_insensitive {
        pattern.to_lowercase()
    } else {
        pattern.to_string()
    };

    for file in files {
        let content = fs::read_to_string(file)?;
        let mut file_matches = Vec::new();

        for (line_num, line) in content.lines().enumerate() {
            let line_to_match = if flags.case_insensitive {
                line.to_lowercase()
            } else {
                line.to_string()
            };

            let matches = if flags.exact_match {
                line_to_match == pattern
            } else {
                line_to_match.contains(&pattern)
            };

            let matches = if flags.invert { !matches } else { matches };

            if matches {
                if flags.list_files {
                    file_matches.push(file.to_string());
                    break;
                }

                let mut result = String::new();
                if files.len() > 1 {
                    result.push_str(file);
                    result.push(':');
                }

                if flags.line_numbers {
                    result.push_str(&(line_num + 1).to_string());
                    result.push(':');
                }

                result.push_str(line);
                file_matches.push(result);
            }
        }

        results.extend(file_matches);
    }

    Ok(results)
}
