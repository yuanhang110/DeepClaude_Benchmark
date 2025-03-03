use std::sync::{Mutex, OnceLock};
use std::collections::HashSet;
use std::time::{SystemTime, UNIX_EPOCH};

static USED_NAMES: OnceLock<Mutex<HashSet<String>>> = OnceLock::new();

pub struct Robot {
    name: String,
}

impl Robot {
    pub fn new() -> Self {
        Robot {
            name: generate_unique_name(),
        }
    }

    pub fn name(&self) -> &str {
        &self.name
    }

    pub fn reset_name(&mut self) {
        let new_name = generate_unique_name();
        let used_names = USED_NAMES.get_or_init(|| Mutex::new(HashSet::new()));
        let mut set = used_names.lock().unwrap();
        set.remove(&self.name);
        self.name = new_name;
    }
}

struct SimpleRng {
    seed: u32,
}

impl SimpleRng {
    fn new(seed: u32) -> Self {
        Self { seed }
    }

    fn next(&mut self) -> u32 {
        self.seed = self.seed.wrapping_mul(1103515245).wrapping_add(12345);
        self.seed
    }

    fn gen_range(&mut self, low: u32, high: u32) -> u32 {
        (self.next() % (high - low)) + low
    }
}

fn generate_unique_name() -> String {
    let used_names = USED_NAMES.get_or_init(|| Mutex::new(HashSet::new()));
    loop {
        let seed = SystemTime::now()
            .duration_since(UNIX_EPOCH)
            .unwrap()
            .subsec_nanos();
        let mut rng = SimpleRng::new(seed);
        let c1 = rng.gen_range(0, 26) as u8 + b'A';
        let c2 = rng.gen_range(0, 26) as u8 + b'A';
        let d1 = rng.gen_range(0, 10) as u8 + b'0';
        let d2 = rng.gen_range(0, 10) as u8 + b'0';
        let d3 = rng.gen_range(0, 10) as u8 + b'0';
        let name = format!("{}{}{}{}{}", c1 as char, c2 as char, d1 as char, d2 as char, d3 as char);
        let mut set = used_names.lock().unwrap();
        if !set.contains(&name) {
            set.insert(name.clone());
            return name;
        }
    }
}
