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
        let student_name = student.to_string();
        
        // Check if student exists in any grade
        for (existing_grade, students) in &self.roster {
            if students.contains(&student_name) {
                // Student already exists in some grade, do not add again
                return;
            }
        }
        
        // If not found in any grade, add to the specified grade
        self.roster
            .entry(grade)
            .or_default()
            .insert(student_name);
    }

    pub fn grades(&self) -> Vec<u32> {
        let mut grades: Vec<u32> = self.roster.keys().cloned().collect();
        grades.sort();
        grades
    }

    // If `grade` returned a reference, `School` would be forced to keep a `Vec<String>`
    // internally to lend out. By returning an owned vector of owned `String`s instead,
    // the internal structure can be completely arbitrary. The tradeoff is that some data
    // must be copied each time `grade` is called.
    pub fn grade(&self, grade: u32) -> Vec<String> {
        let mut students = match self.roster.get(&grade) {
            Some(names) => names.iter().cloned().collect(),
            None => Vec::new(),
        };
        students.sort();
        students
    }
}
