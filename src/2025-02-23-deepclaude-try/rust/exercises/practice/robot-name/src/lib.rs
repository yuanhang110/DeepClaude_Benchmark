use std::collections::HashSet;
use std::sync::Mutex;
use lazy_static::lazy_static;
use rand::Rng;

lazy_static! {
    static ref USED_NAMES: Mutex<HashSet<String>> = Mutex::new(HashSet::new());
}

pub struct Robot {
    name: String,
}

impl Robot {
    pub fn new() -> Self {
        let mut robot = Robot {
            name: String::new(),
        };
        robot.reset_name();
        robot
    }

    pub fn name(&self) -> &str {
        &self.name
    }

    pub fn reset_name(&mut self) {
        let mut used_names = USED_NAMES.lock().unwrap();
        
        // Remove old name if it exists
        if !self.name.is_empty() {
            used_names.remove(&self.name);
        }

        // Generate new unique name
        loop {
            let mut rng = rand::thread_rng();
            let name = format!(
                "{}{}{}{}{}",
                (b'A' + rng.gen_range(0..26)) as char,
                (b'A' + rng.gen_range(0..26)) as char,
                rng.gen_range(0..10),
                rng.gen_range(0..10),
                rng.gen_range(0..10)
            );

            if !used_names.contains(&name) {
                used_names.insert(name.clone());
                self.name = name;
                break;
            }
        }
    }
}
