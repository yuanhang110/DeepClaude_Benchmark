/// A munger which XORs a key with some data
#[derive(Clone)]
pub struct Xorcism<'a> {
    key_cycle: std::iter::Cycle<std::slice::Iter<'a, u8>>,
}

impl<'a> Xorcism<'a> {
    /// Create a new Xorcism munger from a key
    ///
    /// Should accept anything which has a cheap conversion to a byte slice.
    pub fn new<Key>(key: &'a Key) -> Xorcism<'a>
    where
        Key: AsRef<[u8]> + ?Sized,
    {
        Xorcism {
            key_cycle: key.as_ref().iter().cycle(),
        }
    }

    /// XOR each byte of the input buffer with a byte from the key.
    ///
    /// Note that this is stateful: repeated calls are likely to produce different results,
    /// even with identical inputs.
    pub fn munge_in_place(&mut self, data: &mut [u8]) {
        for byte in data {
            *byte ^= self.key_cycle.next().unwrap();
        }
    }

    /// XOR each byte of the data with a byte from the key.
    ///
    /// Note that this is stateful: repeated calls are likely to produce different results,
    /// even with identical inputs.
    ///
    /// Should accept anything which has a cheap conversion to a byte iterator.
    /// Shouldn't matter whether the byte iterator's values are owned or borrowed.
    pub fn munge<'b, Data>(&'b mut self, data: Data) -> impl Iterator<Item = u8> + 'b 
    where
        Data: IntoIterator,
        Data::Item: AsRef<u8>,
        <Data as IntoIterator>::IntoIter: 'b,
    {
        data.into_iter().map(move |d| {
            let key_byte = self.key_cycle.next().unwrap();
            *d.as_ref() ^ key_byte
        })
    }
}
