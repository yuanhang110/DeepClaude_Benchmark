use anyhow::{Context, Error};

/// While using `&[&str]` to handle flags is convenient for exercise purposes,
/// and resembles the output of [`std::env::args`], in real-world projects it is
/// both more convenient and more idiomatic to contain runtime configuration in
/// a dedicated struct. Therefore, we suggest that you do so in this exercise.
///
/// [`std::env::args`]: https://doc.rust-lang.org/std/env/fn.args.html
#[derive(Debug)]
pub struct Flags {
    line_number: bool,
    list_files: bool,
    case_insensitive: bool,
    invert_match: bool,
    entire_line: bool,
}

impl Flags {
    pub fn new(flags: &[&str]) -> Self {
        let mut line_number = false;
        let mut list_files = false;
        let mut case_insensitive = false;
        let mut invert_match = false;
        let mut entire_line = false;

        for &flag in flags {
            match flag {
                "-n" => line_number = true,
                "-l" => list_files = true,
                "-i" => case_insensitive = true,
                "-v" => invert_match = true,
                "-x" => entire_line = true,
                _ => {} // Ignore unrecognized flags
            }
        }

        Self {
            line_number,
            list_files,
            case_insensitive,
            invert_match,
            entire_line,
        }
    }
}

pub fn grep(pattern: &str, flags: &Flags, files: &[&str]) -> Result<Vec<String>, Error> {
    let mut results = Vec::new();
    let pattern_check = if flags.case_insensitive {
        pattern.to_lowercase()
    } else {
        pattern.to_string()
    };

    for &file in files {
        let content = std::fs::read_to_string(file)
            .with_context(|| format!("Could not read file {}", file))?;
        let lines = content.lines().collect::<Vec<_>>();
        let mut file_matched = false;

        for (i, line) in lines.iter().enumerate() {
            let line_number = i + 1;
            let line_check = if flags.case_insensitive {
                line.to_lowercase()
            } else {
                line.to_string()
            };

            let mut matches = if flags.entire_line {
                line_check == pattern_check
            } else {
                line_check.contains(&pattern_check)
            };

            if flags.invert_match {
                matches = !matches;
            }

            if matches {
                if flags.list_files {
                    file_matched = true;
                    break;
                }

                let mut output = String::new();
                if files.len() > 1 {
                    output.push_str(file);
                    output.push(':');
                }

                if flags.line_number {
                    output.push_str(&format!("{}:", line_number));
                }

                output.push_str(line);
                results.push(output);
            }
        }

        if flags.list_files && file_matched {
            results.push(file.to_string());
        }
    }

    Ok(results)
}
