/// Apply a function to each element of the input vector
pub fn map<T, U, F>(input: Vec<T>, function: F) -> Vec<U> 
where
    F: Fn(T) -> U,
{
    let mut result = Vec::with_capacity(input.len());
    for num in input {
        result.push(function(num));
    }
    result
}
