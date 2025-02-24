pub fn map<T, U, F>(input: Vec<T>, mut function: F) -> Vec<U>
where
    F: FnMut(T) -> U,
{
    let mut result = Vec::with_capacity(input.len());
    for x in input {
        result.push(function(x));
    }
    result
}
