/// Maps a function over all elements in the input vector.
/// 
/// Takes a vector of type T and a function that transforms T into U,
/// and returns a vector of type U.
pub fn map<T, U, F>(input: Vec<T>, function: F) -> Vec<U> 
where
    F: Fn(T) -> U,
{
    let mut result = Vec::with_capacity(input.len());
    for item in input {
        result.push(function(item));
    }
    result
}
