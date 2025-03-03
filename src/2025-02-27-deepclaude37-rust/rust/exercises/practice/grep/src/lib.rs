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
    exact_line: bool,
}

impl Flags {
    pub fn new(flags: &[&str]) -> Self {
        let mut result = Flags {
            line_numbers: false,
            list_files: false,
            case_insensitive: false,
            invert: false,
            exact_line: false,
        };
        
        for &flag in flags {
            match flag {
                "-n" => result.line_numbers = true,
                "-l" => result.list_files = true,
                "-i" => result.case_insensitive = true,
                "-v" => result.invert = true,
                "-x" => result.exact_line = true,
                _ => {} // Ignore unrecognized flags
            }
        }
        
        result
    }
}

pub fn grep(pattern: &str, flags: &Flags, files: &[&str]) -> Result<Vec<String>, Error> {
    use std::fs::read_to_string;

    let mut results = Vec::new();
    let pattern_lower = if flags.case_insensitive {
        pattern.to_lowercase()
    } else {
        pattern.to_string()
    };

    for &file in files {
        let content = read_to_string(file)?;
        let mut file_matched = false;

        for (line_num, line) in content.lines().enumerate() {
            let line_matched = {
                let processed_line = if flags.case_insensitive {
                    line.to_lowercase()
                } else {
                    line.to_string()
                };

                let matches = if flags.exact_line {
                    processed_line == pattern_lower
                } else {
                    processed_line.contains(&pattern_lower)
                };

                matches ^ flags.invert
            };

            if line_matched {
                if flags.list_files {
                    file_matched = true;
                    break; // No need to check other lines
                }

                let mut output = String::new();

                // Add filename if multiple files
                if files.len() > 1 {
                    output.push_str(file);
                    output.push(':');
                }

                // Add line number if flag is set
                if flags.line_numbers {
                    output.push_str(&(line_num + 1).to_string());
                    output.push(':');
                }

                // Add the line content
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
