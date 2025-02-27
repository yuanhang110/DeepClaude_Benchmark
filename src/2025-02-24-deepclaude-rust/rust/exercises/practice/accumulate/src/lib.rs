/// What should the type of _function be?
pub fn map<F>(input: Vec<i32>, mut _function: F) -> Vec<i32>
where
    F: FnMut(i32) -> i32,
{
    let mut result = Vec::with_capacity(input.len());
    for num in input {
        result.push(_function(num));
    }
    result
}
