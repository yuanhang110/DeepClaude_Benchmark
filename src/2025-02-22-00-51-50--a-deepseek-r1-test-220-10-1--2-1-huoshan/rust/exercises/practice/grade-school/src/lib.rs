use std::collections::{HashMap, HashSet};

pub struct School {
    roster: HashMap<u32, HashSet<String>>,
}

impl School {
    pub fn new() -> School {
        School {
            roster: HashMap::new(),
        }
    }

    pub fn add(&mut self, grade: u32, student: &str) {
        // Remove student from all grades first
        for students in self.roster.values_mut() {
            students.remove(student);
        }
        // Add to specified grade
        self.roster
            .entry(grade)
            .or_insert_with(HashSet::new)
            .insert(student.to_string());
    }

    pub fn grades(&self) -> Vec<u32> {
        let mut grades: Vec<u32> = self.roster.keys().cloned().collect();
        grades.sort_unstable();
        grades
    }

    // If `grade` returned a reference, `School` would be forced to keep a `Vec<String>`
    // internally to lend out. By returning an owned vector of owned `String`s instead,
    // the internal structure can be completely arbitrary. The tradeoff is that some data
    // must be copied each time `grade` is called.
    pub fn grade(&self, grade: u32) -> Vec<String> {
        self.roster
            .get(&grade)
            .map(|students| {
                let mut students: Vec<String> = students.iter().cloned().collect();
                students.sort();
                students
            })
            .unwrap_or_default()
    }
}
