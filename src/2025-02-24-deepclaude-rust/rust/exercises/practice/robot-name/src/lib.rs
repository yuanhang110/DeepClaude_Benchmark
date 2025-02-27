use std::sync::{OnceLock, Mutex};
use std::collections::HashSet;
use std::time::{SystemTime, UNIX_EPOCH};

struct LCG {
    state: u64,
}

impl LCG {
    fn new(seed: u64) -> Self {
        Self { state: seed }
    }

    fn next(&mut self) -> u64 {
        self.state = self.state.wrapping_mul(6364136223846793005).wrapping_add(1);
        self.state
    }
}

pub struct Robot {
    name: String,
}

impl Robot {
    pub fn new() -> Self {
        let used_names = USED_NAMES.get_or_init(|| Mutex::new(HashSet::new()));
        let mut rng = LCG::new(
            SystemTime::now()
                .duration_since(UNIX_EPOCH)
                .unwrap()
                .as_nanos() as u64,
        );
        loop {
            let c1 = (rng.next() % 26) as u8 + b'A';
            let c2 = (rng.next() % 26) as u8 + b'A';
            let d1 = (rng.next() % 10) as u8 + b'0';
            let d2 = (rng.next() % 10) as u8 + b'0';
            let d3 = (rng.next() % 10) as u8 + b'0';
            let name = format!(
                "{}{}{}{}{}",
                c1 as char, c2 as char, d1 as char, d2 as char, d3 as char
            );
            let mut guard = used_names.lock().unwrap();
            if !guard.contains(&name) {
                guard.insert(name.clone());
                return Self { name };
            }
        }
    }

    pub fn name(&self) -> &str {
        &self.name
    }

    pub fn reset_name(&mut self) {
        let used_names = USED_NAMES.get_or_init(|| Mutex::new(HashSet::new()));
        let old_name = std::mem::replace(&mut self.name, String::new());
        used_names.lock().unwrap().remove(&old_name);

        let mut rng = LCG::new(
            SystemTime::now()
                .duration_since(UNIX_EPOCH)
                .unwrap()
                .as_nanos() as u64,
        );
        loop {
            let c1 = (rng.next() % 26) as u8 + b'A';
            let c2 = (rng.next() % 26) as u8 + b'A';
            let d1 = (rng.next() % 10) as u8 + b'0';
            let d2 = (rng.next() % 10) as u8 + b'0';
            let d3 = (rng.next() % 10) as u8 + b'0';
            let new_name = format!(
                "{}{}{}{}{}",
                c1 as char, c2 as char, d1 as char, d2 as char, d3 as char
            );
            let mut guard = used_names.lock().unwrap();
            if !guard.contains(&new_name) {
                guard.insert(new_name.clone());
                self.name = new_name;
                return;
            }
        }
    }
}

static USED_NAMES: OnceLock<Mutex<HashSet<String>>> = OnceLock::new();
