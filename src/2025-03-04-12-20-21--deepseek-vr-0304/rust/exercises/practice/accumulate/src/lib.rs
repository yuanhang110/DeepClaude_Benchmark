/// What should the type of _function be?
pub fn map<T, U, F>(input: Vec<T>, function: F) -> Vec<U>
where
    F: Fn(T) -> U,
{
    let mut result = Vec::new();
    for x in input {
        result.push(function(x));
    }
    result
}
