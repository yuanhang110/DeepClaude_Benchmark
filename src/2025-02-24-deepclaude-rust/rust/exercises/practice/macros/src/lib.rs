#[macro_export]
macro_rules! hashmap {
    () => {
        {
            let mut hm = ::std::collections::HashMap::new();
            hm
        }
    };
    ($( $key:expr => $value:expr ),+ $(,)?) => {
        {
            let mut hm = ::std::collections::HashMap::new();
            $( hm.insert($key, $value); )+
            hm
        }
    };
}
