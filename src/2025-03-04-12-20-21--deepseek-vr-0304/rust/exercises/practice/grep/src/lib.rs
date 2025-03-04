use anyhow::Error;

/// While using `&[&str]` to handle flags is convenient for exercise purposes,
/// and resembles the output of [`std::env::args`], in real-world projects it is
/// both more convenient and more idiomatic to contain runtime configuration in
/// a dedicated struct. Therefore, we suggest that you do so in this exercise.
///
/// [`std::env::args`]: https://doc.rust-lang.org/std/env/fn.args.html
#[derive(Debug)]
pub struct Flags {
    pub line_number: bool,
    pub file_name: bool,
    pub case_insensitive: bool,
    pub invert: bool,
    pub entire_line: bool,
}

impl Flags {
    pub fn new(flags: &[&str]) -> Self {
        let mut f = Flags {
            line_number: false,
            file_name: false,
            case_insensitive: false,
            invert: false,
            entire_line: false,
        };

        for flag in flags {
            match *flag {
                "-n" => f.line_number = true,
                "-l" => f.file_name = true,
                "-i" => f.case_insensitive = true,
                "-v" => f.invert = true,
                "-x" => f.entire_line = true,
                _ => (),
            }
        }

        f
    }
}

pub fn grep(pattern: &str, flags: &Flags, files: &[&str]) -> Result<Vec<String>, Error> {
    todo!(
        "Search the files '{files:?}' for '{pattern}' pattern and save the matches in a vector. Your search logic should be aware of the given flags '{flags:?}'"
    );
}
