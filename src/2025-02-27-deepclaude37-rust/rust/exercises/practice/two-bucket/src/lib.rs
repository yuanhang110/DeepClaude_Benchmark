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
    // Return None if the goal is impossible
    if goal > capacity_1 && goal > capacity_2 {
        return None;
    }

    // Initialize the starting state after the first fill
    let (initial_a, initial_b) = match start_bucket {
        Bucket::One => (capacity_1, 0),
        Bucket::Two => (0, capacity_2),
    };

    // Check if the initial state already meets the goal
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

    // Set up BFS
    let mut queue = VecDeque::new();
    let mut visited = HashSet::new();

    queue.push_back((initial_a, initial_b, 1));
    visited.insert((initial_a, initial_b));

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

        for (next_a, next_b, next_moves) in next_states {
            // Check if the next state is forbidden (starting bucket empty, other full)
            let forbidden = match start_bucket {
                Bucket::One => next_a == 0 && next_b == capacity_2,
                Bucket::Two => next_b == 0 && next_a == capacity_1,
            };
            
            if !forbidden && !visited.contains(&(next_a, next_b)) {
                // Check if either bucket has reached the goal
                if next_a == goal {
                    return Some(BucketStats {
                        moves: next_moves,
                        goal_bucket: Bucket::One,
                        other_bucket: next_b,
                    });
                } else if next_b == goal {
                    return Some(BucketStats {
                        moves: next_moves,
                        goal_bucket: Bucket::Two,
                        other_bucket: next_a,
                    });
                }

                // Add to visited and queue
                visited.insert((next_a, next_b));
                queue.push_back((next_a, next_b, next_moves));
            }
        }
    }

    // If no solution found
    None
}
