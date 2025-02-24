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
    // Check if the goal is already met in the initial state
    let (initial_a, initial_b) = match start_bucket {
        Bucket::One => (capacity_1, 0),
        Bucket::Two => (0, capacity_2),
    };

    if initial_a == goal {
        return Some(BucketStats {
            moves: 1,
            goal_bucket: Bucket::One,
            other_bucket: initial_b,
        });
    }
    if initial_b == goal {
        return Some(BucketStats {
            moves: 1,
            goal_bucket: Bucket::Two,
            other_bucket: initial_a,
        });
    }

    // Initialize BFS
    let mut queue = VecDeque::new();
    let mut visited = HashSet::new();
    queue.push_back((initial_a, initial_b, 1));
    visited.insert((initial_a, initial_b));

    while let Some((a, b, moves)) = queue.pop_front() {
        // Generate all possible next states
        let next_moves = moves + 1;

        // Action 1: Fill bucket 1
        let states = [
            // Fill bucket 1
            (capacity_1, b),
            // Fill bucket 2
            (a, capacity_2),
            // Empty bucket 1
            (0, b),
            // Empty bucket 2
            (a, 0),
            // Pour from 1 to 2
            {
                let amount = a.min(capacity_2 - b);
                (a - amount, b + amount)
            },
            // Pour from 2 to 1
            {
                let amount = b.min(capacity_1 - a);
                (a + amount, b - amount)
            },
        ];

        for (new_a, new_b) in states {
            // Check invalid state rule
            let invalid = match start_bucket {
                Bucket::One => new_a == 0 && new_b == capacity_2,
                Bucket::Two => new_b == 0 && new_a == capacity_1,
            };
            if invalid {
                continue;
            }

            // Check goal
            if new_a == goal {
                return Some(BucketStats {
                    moves: next_moves,
                    goal_bucket: Bucket::One,
                    other_bucket: new_b,
                });
            }
            if new_b == goal {
                return Some(BucketStats {
                    moves: next_moves,
                    goal_bucket: Bucket::Two,
                    other_bucket: new_a,
                });
            }

            // Add to queue if not visited
            if visited.insert((new_a, new_b)) {
                queue.push_back((new_a, new_b, next_moves));
            }
        }
    }

    // No solution found
    None
}
