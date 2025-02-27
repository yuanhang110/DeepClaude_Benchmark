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
    // Check if goal is zero or exceeds both capacities
    if goal == 0 || goal > capacity_1.max(capacity_2) {
        return None;
    }

    // Determine initial state based on start_bucket
    let (initial_b1, initial_b2) = match start_bucket {
        Bucket::One => (capacity_1, 0),
        Bucket::Two => (0, capacity_2),
    };

    // Check if initial state is the solution
    if initial_b1 == goal {
        return Some(BucketStats {
            moves: 1,
            goal_bucket: Bucket::One,
            other_bucket: initial_b2,
        });
    }
    if initial_b2 == goal {
        return Some(BucketStats {
            moves: 1,
            goal_bucket: Bucket::Two,
            other_bucket: initial_b1,
        });
    }

    let mut queue = VecDeque::new();
    let mut visited = HashSet::new();
    queue.push_back((initial_b1, initial_b2, 1));
    visited.insert((initial_b1, initial_b2));

    while let Some((b1, b2, moves)) = queue.pop_front() {
        // Generate all possible next states
        let next_states = [
            // Fill bucket 1
            (capacity_1, b2, moves + 1),
            // Fill bucket 2
            (b1, capacity_2, moves + 1),
            // Empty bucket 1
            (0, b2, moves + 1),
            // Empty bucket 2
            (b1, 0, moves + 1),
            // Pour from 1 to 2
            {
                let amount = b1.min(capacity_2 - b2);
                (b1 - amount, b2 + amount, moves + 1)
            },
            // Pour from 2 to 1
            {
                let amount = b2.min(capacity_1 - b1);
                (b1 + amount, b2 - amount, moves + 1)
            },
        ];

        for (new_b1, new_b2, new_moves) in next_states {
            // Check for invalid state based on start bucket
            let invalid = match start_bucket {
                Bucket::One => new_b1 == 0 && new_b2 == capacity_2,
                Bucket::Two => new_b2 == 0 && new_b1 == capacity_1,
            };
            if invalid {
                continue;
            }

            if !visited.contains(&(new_b1, new_b2)) {
                // Check if goal is achieved
                if new_b1 == goal {
                    return Some(BucketStats {
                        moves: new_moves,
                        goal_bucket: Bucket::One,
                        other_bucket: new_b2,
                    });
                } else if new_b2 == goal {
                    return Some(BucketStats {
                        moves: new_moves,
                        goal_bucket: Bucket::Two,
                        other_bucket: new_b1,
                    });
                }

                visited.insert((new_b1, new_b2));
                queue.push_back((new_b1, new_b2, new_moves));
            }
        }
    }

    // No solution found
    None
}
