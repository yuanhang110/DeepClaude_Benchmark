/// Maps each element of the input vector using the provided function
pub fn map<T, U>(input: Vec<T>, function: impl Fn(T) -> U) -> Vec<U> {
    let mut result = Vec::with_capacity(input.len());
    for item in input {
        result.push(function(item));
    }
    result
}
