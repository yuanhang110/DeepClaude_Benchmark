use std::collections::{HashMap, HashSet};

pub struct School {
    students: HashMap<u32, HashSet<String>>,
}

impl School {
    pub fn new() -> School {
        School {
            students: HashMap::new(),
        }
    }

    pub fn add(&mut self, grade: u32, student: &str) {
        let student = student.to_string();
        
        // Check if student exists in any grade
        for students in self.students.values() {
            if students.contains(&student) {
                return;
            }
        }
        
        self.students
            .entry(grade)
            .or_insert_with(HashSet::new)
            .insert(student);
    }

    pub fn grades(&self) -> Vec<u32> {
        let mut grades: Vec<u32> = self.students.keys().cloned().collect();
        grades.sort_unstable();
        grades
    }

    // If `grade` returned a reference, `School` would be forced to keep a `Vec<String>`
    // internally to lend out. By returning an owned vector of owned `String`s instead,
    // the internal structure can be completely arbitrary. The tradeoff is that some data
    // must be copied each time `grade` is called.
    pub fn grade(&self, grade: u32) -> Vec<String> {
        let mut students = self.students
            .get(&grade)
            .cloned()
            .unwrap_or_default()
            .into_iter()
            .collect::<Vec<_>>();
        students.sort();
        students
    }
}
