use std::collections::HashMap;
use std::collections::HashSet;

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
        // Check if student exists in any grade
        for (_, students) in &self.roster {
            if students.contains(student) {
                return;
            }
        }
        
        let students = self.roster.entry(grade).or_insert(HashSet::new());
        students.insert(student.to_string());
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
        match self.roster.get(&grade) {
            Some(students) => {
                let mut sorted_students: Vec<String> = students.iter().cloned().collect();
                sorted_students.sort();
                sorted_students
            }
            None => Vec::new(),
        }
    }
}
