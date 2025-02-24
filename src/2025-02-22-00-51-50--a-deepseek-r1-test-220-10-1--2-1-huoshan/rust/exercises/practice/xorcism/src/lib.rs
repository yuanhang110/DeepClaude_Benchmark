use std::borrow::Borrow;

/// A munger which XORs a key with some data
#[derive(Clone)]
pub struct Xorcism<'a> {
    key: &'a [u8],
    pos: usize,
}

impl<'a> Xorcism<'a> {
    /// Create a new Xorcism munger from a key
    ///
    /// Should accept anything which has a cheap conversion to a byte slice.
    pub fn new<Key: AsRef<[u8]>>(key: &'a Key) -> Xorcism<'a> {
        Xorcism {
            key: key.as_ref(),
            pos: 0,
        }
    }

    /// XOR each byte of the input buffer with a byte from the key.
    ///
    /// Note that this is stateful: repeated calls are likely to produce different results,
    /// even with identical inputs.
    pub fn munge_in_place(&mut self, data: &mut [u8]) {
        if self.key.is_empty() {
            return;
        }
        for byte in data {
            *byte ^= self.key[self.pos % self.key.len()];
            self.pos += 1;
        }
    }

    /// XOR each byte of the data with a byte from the key.
    ///
    /// Note that this is stateful: repeated calls are likely to produce different results,
    /// even with identical inputs.
    ///
    /// Should accept anything which has a cheap conversion to a byte iterator.
    /// Shouldn't matter whether the byte iterator's values are owned or borrowed.
    pub fn munge<Data>(&mut self, data: Data) -> impl Iterator<Item = u8> + '_
    where
        Data: IntoIterator,
        Data::Item: Borrow<u8>,
    {
        let key = self.key;
        let pos = &mut self.pos;
        data.into_iter().map(move |b| {
            let byte = *b.borrow();
            if key.is_empty() {
                return byte;
            }
            let key_byte = key[*pos % key.len()];
            *pos += 1;
            byte ^ key_byte
        })
    }
}
