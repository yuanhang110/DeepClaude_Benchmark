use std::collections::HashSet;
use std::sync::{Mutex, Once};

static INIT: Once = Once::new();
static mut USED_NAMES: Option<Mutex<HashSet<String>>> = None;

fn get_used_names() -> &'static Mutex<HashSet<String>> {
    unsafe {
        INIT.call_once(|| {
            USED_NAMES = Some(Mutex::new(HashSet::new()));
        });
        USED_NAMES.as_ref().unwrap()
    }
}

pub struct Robot {
    name: String,
}

impl Robot {
    pub fn new() -> Self {
        let name = generate_unique_name();
        Self { name }
    }

    pub fn name(&self) -> &str {
        &self.name
    }

    pub fn reset_name(&mut self) {
        let new_name = generate_unique_name();
        get_used_names().lock().unwrap().remove(&self.name);
        self.name = new_name;
    }
}

fn generate_unique_name() -> String {
    use std::time::{SystemTime, UNIX_EPOCH};
    
    loop {
        let nanos = SystemTime::now()
            .duration_since(UNIX_EPOCH)
            .unwrap()
            .subsec_nanos();
        
        let letters = |n: u8| (b'A' + (nanos.wrapping_add(n as u32) % 26) as u8) as char;
        let numbers = format!("{:03}", nanos % 1000);
        
        let name = format!("{}{}{}", letters(0), letters(1), numbers);

        let mut names = get_used_names().lock().unwrap();
        if !names.contains(&name) {
            names.insert(name.clone());
            return name;
        }
    }
}
