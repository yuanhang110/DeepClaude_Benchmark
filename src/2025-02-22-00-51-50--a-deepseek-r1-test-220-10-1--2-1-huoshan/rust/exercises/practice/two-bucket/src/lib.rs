use std::collections::{VecDeque, HashSet};

#[derive(PartialEq, Eq, Debug)]
pub enum Bucket {
    One,
    Two,
}

/// A struct to hold your results in.
#[derive(PartialEq, Eq, Debug)]
pub struct BucketStats {
    /// The total number of "moves" it should take to reach the desired number of liters, including
    /// the first fill.
    pub moves: u8,
    /// Which bucket should end up with the desired number of liters? (Either "one" or "two")
    pub goal_bucket: Bucket,
    /// How many liters are left in the other bucket?
    pub other_bucket: u8,
}

/// Solve the bucket problem
pub fn solve(
    capacity_1: u8,
    capacity_2: u8,
    goal: u8,
    start_bucket: &Bucket,
) -> Option<BucketStats> {
    if goal == 0 {
        return None;
    }

    let mut queue = VecDeque::new();
    let (initial_a, initial_b) = match start_bucket {
        Bucket::One => (capacity_1, 0),
        Bucket::Two => (0, capacity_2),
    };

    // Check if initial state already matches the goal
    if initial_a == goal {
        return Some(BucketStats {
            moves: 1,
            goal_bucket: Bucket::One,
            other_bucket: initial_b,
        });
    } else if initial_b == goal {
        return Some(BucketStats {
            moves: 1,
            goal_bucket: Bucket::Two,
            other_bucket: initial_a,
        });
    }

    let mut visited = HashSet::new();
    visited.insert((initial_a, initial_b));
    queue.push_back((initial_a, initial_b, 1));

    while let Some((a, b, moves)) = queue.pop_front() {
        // Generate all possible next states
        let next_states = [
            // Fill bucket 1
            (capacity_1, b, moves + 1),
            // Fill bucket 2
            (a, capacity_2, moves + 1),
            // Empty bucket 1
            (0, b, moves + 1),
            // Empty bucket 2
            (a, 0, moves + 1),
            // Pour from 1 to 2
            {
                let amount = a.min(capacity_2 - b);
                (a - amount, b + amount, moves + 1)
            },
            // Pour from 2 to 1
            {
                let amount = b.min(capacity_1 - a);
                (a + amount, b - amount, moves + 1)
            },
        ];

        for (new_a, new_b, new_moves) in next_states {
            // Check invalid state rule
            let invalid = match start_bucket {
                Bucket::One => new_a == 0 && new_b == capacity_2,
                Bucket::Two => new_b == 0 && new_a == capacity_1,
            };
            if invalid {
                continue;
            }

            // Check if we've reached the goal
            if new_a == goal {
                return Some(BucketStats {
                    moves: new_moves,
                    goal_bucket: Bucket::One,
                    other_bucket: new_b,
                });
            } else if new_b == goal {
                return Some(BucketStats {
                    moves: new_moves,
                    goal_bucket: Bucket::Two,
                    other_bucket: new_a,
                });
            }

            // Add to queue if not visited
            if visited.insert((new_a, new_b)) {
                queue.push_back((new_a, new_b, new_moves));
            }
        }
    }

    None
}
