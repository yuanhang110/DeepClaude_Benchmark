// the PhantomData instances in this file are just to stop compiler complaints
// about missing generics; feel free to remove them

/// A Matcher is a single rule of fizzbuzz: given a function on T, should
/// a word be substituted in? If yes, which word?
pub struct Matcher<T> {
    predicate: Box<dyn Fn(T) -> bool>,
    subs: String,
}

impl<T> Matcher<T> {
    pub fn new<F, S>(matcher: F, subs: S) -> Matcher<T>
    where
        F: Fn(T) -> bool + 'static,
        S: Into<String>,
    {
        Matcher {
            predicate: Box::new(matcher),
            subs: subs.into(),
        }
    }
}

/// A Fizzy is a set of matchers, which may be applied to an iterator.
///
/// Strictly speaking, it's usually more idiomatic to use `iter.map()` than to
/// consume an iterator with an `apply` method. Given a Fizzy instance, it's
/// pretty straightforward to construct a closure which applies it to all
/// elements of the iterator. However, we're using the `apply` pattern
/// here because it's a simpler interface for students to implement.
///
/// Also, it's a good excuse to try out using impl trait.
pub struct Fizzy<T> {
    matchers: Vec<Matcher<T>>,
}

impl<T> Fizzy<T> {
    pub fn new() -> Self {
        Fizzy { matchers: Vec::new() }
    }

    #[must_use]
    pub fn add_matcher(mut self, matcher: Matcher<T>) -> Self {
        self.matchers.push(matcher);
        self
    }

    pub fn apply<I>(self, iter: I) -> impl Iterator<Item = String>
    where
        T: std::fmt::Display,
        I: IntoIterator<Item = T>,
    {
        iter.into_iter().map(move |item| {
            let substitutions: String = self.matchers.iter()
                .filter(|m| (m.predicate)(item))
                .map(|m| m.subs.as_str())
                .collect();
            
            if substitutions.is_empty() {
                item.to_string()
            } else {
                substitutions
            }
        })
    }
}

/// convenience function: return a Fizzy which applies the standard fizz-buzz rules
pub fn fizz_buzz<T>() -> Fizzy<T>
where
    T: Copy + From<u8> + std::ops::Rem<Output = T> + PartialEq,
{
    Fizzy::new()
        .add_matcher(Matcher::new(|n: T| n % T::from(3) == T::from(0), "fizz"))
        .add_matcher(Matcher::new(|n: T| n % T::from(5) == T::from(0), "buzz"))
}
