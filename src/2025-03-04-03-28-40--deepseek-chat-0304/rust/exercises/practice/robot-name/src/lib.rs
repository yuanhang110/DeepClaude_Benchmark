use std::collections::HashSet;
use std::sync::Mutex;
use std::sync::OnceLock;

static USED_NAMES: OnceLock<Mutex<HashSet<String>>> = OnceLock::new();

pub struct Robot {
    name: String,
}

impl Robot {
    pub fn new() -> Self {
        let name = Self::generate_unique_name();
        Self { name }
    }

    pub fn name(&self) -> &str {
        &self.name
    }

    pub fn reset_name(&mut self) {
        let mut used_names = USED_NAMES.lock().unwrap();
        used_names.remove(&self.name);
        self.name = Self::generate_unique_name();
    }

    fn generate_unique_name() -> String {
        let mut used_names = USED_NAMES
            .get_or_init(|| Mutex::new(HashSet::new()))
            .lock()
            .unwrap();
        
        loop {
            let letters: String = (0..2)
                .map(|_| {
                    let n = fastrand::u8(0..26);
                    (b'A' + n) as char
                })
                .collect();
            let digits: String = (0..3)
                .map(|_| {
                    let n = fastrand::u8(0..10);
                    (b'0' + n) as char
                })
                .collect();
            let name = format!("{}{}", letters, digits);
            
            if used_names.insert(name.clone()) {
                return name;
            }
        }
    }
}
