use anyhow::Error;

/// While using `&[&str]` to handle flags is convenient for exercise purposes,
/// and resembles the output of [`std::env::args`], in real-world projects it is
/// both more convenient and more idiomatic to contain runtime configuration in
/// a dedicated struct. Therefore, we suggest that you do so in this exercise.
///
/// [`std::env::args`]: https://doc.rust-lang.org/std/env/fn.args.html
#[derive(Debug)]
pub struct Flags {
    line_number: bool,
    files_with_matches: bool,
    case_insensitive: bool,
    invert: bool,
    entire_line: bool,
}

impl Flags {
    pub fn new(flags: &[&str]) -> Self {
        let mut line_number = false;
        let mut files_with_matches = false;
        let mut case_insensitive = false;
        let mut invert = false;
        let mut entire_line = false;

        for &flag in flags {
            match flag {
                "-n" => line_number = true,
                "-l" => files_with_matches = true,
                "-i" => case_insensitive = true,
                "-v" => invert = true,
                "-x" => entire_line = true,
                _ => {}, // ignore unrecognized flags
            }
        }

        Self {
            line_number,
            files_with_matches,
            case_insensitive,
            invert,
            entire_line,
        }
    }
}

pub fn grep(pattern: &str, flags: &Flags, files: &[&str]) -> Result<Vec<String>, Error> {
    let mut results = Vec::new();

    for &file in files {
        let content = std::fs::read_to_string(file)
            .map_err(|e| Error::msg(format!("Error reading file {}: {}", file, e)))?;

        let mut file_has_match = false;
        let mut matched_lines = Vec::new();

        for (line_num, line) in content.lines().enumerate() {
            let normalized_line = if flags.case_insensitive {
                line.to_lowercase()
            } else {
                line.to_string()
            };
            let normalized_pattern = if flags.case_insensitive {
                pattern.to_lowercase()
            } else {
                pattern.to_string()
            };

            let mut is_match = if flags.entire_line {
                normalized_line == normalized_pattern
            } else {
                normalized_line.contains(&normalized_pattern)
            };

            if flags.invert {
                is_match = !is_match;
            }

            if is_match {
                if flags.files_with_matches {
                    file_has_match = true;
                    break;
                }

                let mut output = String::new();

                // Prepend filename if multiple files
                if files.len() > 1 {
                    output.push_str(file);
                    output.push(':');
                }

                // Add line number if required
                if flags.line_number {
                    output.push_str(&(line_num + 1).to_string());
                    output.push(':');
                }

                output.push_str(line);
                matched_lines.push(output);
            }
        }

        if flags.files_with_matches {
            if file_has_match {
                results.push(file.to_string());
            }
        } else {
            results.extend(matched_lines);
        }
    }

    Ok(results)
}
