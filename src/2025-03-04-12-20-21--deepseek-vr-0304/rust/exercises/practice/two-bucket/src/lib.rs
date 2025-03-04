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

fn gcd(a: u8, b: u8) -> u8 {
    let mut a = a;
    let mut b = b;
    while b != 0 {
        let temp = b;
        b = a % b;
        a = temp;
    }
    a
}

/// Solve the bucket problem
pub fn solve(
    capacity_1: u8,
    capacity_2: u8,
    goal: u8,
    start_bucket: &Bucket,
) -> Option<BucketStats> {
    // Early checks
    if goal == 0 {
        return None;
    }

    if goal > capacity_1.max(capacity_2) {
        return None;
    }

    let gcd_value = gcd(capacity_1, capacity_2);
    if goal % gcd_value != 0 {
        return None;
    }

    // Initial state based on starting bucket
    let initial_state = match start_bucket {
        Bucket::One => (capacity_1, 0),
        Bucket::Two => (0, capacity_2),
    };

    // Check if initial state is the goal
    if initial_state.0 == goal {
        return Some(BucketStats {
            moves: 1,
            goal_bucket: Bucket::One,
            other_bucket: initial_state.1,
        });
    } else if initial_state.1 == goal {
        return Some(BucketStats {
            moves: 1,
            goal_bucket: Bucket::Two,
            other_bucket: initial_state.0,
        });
    }

    // BFS setup
    let mut queue = VecDeque::new();
    queue.push_back((initial_state.0, initial_state.1, 1));

    let mut visited = HashSet::new();
    visited.insert((initial_state.0, initial_state.1));

    // Determine forbidden state based on start bucket
    let forbidden_state = match start_bucket {
        Bucket::One => (0, capacity_2),
        Bucket::Two => (capacity_1, 0),
    };

    while let Some((b1, b2, moves)) = queue.pop_front() {
        // Generate all possible next states
        let mut next_states = Vec::new();

        // Fill bucket1
        if b1 < capacity_1 {
            next_states.push((capacity_1, b2, moves + 1));
        }

        // Fill bucket2
        if b2 < capacity_2 {
            next_states.push((b1, capacity_2, moves + 1));
        }

        // Empty bucket1
        if b1 > 0 {
            next_states.push((0, b2, moves + 1));
        }

        // Empty bucket2
        if b2 > 0 {
            next_states.push((b1, 0, moves + 1));
        }

        // Pour from bucket1 to bucket2
        let pour_amount = b1.min(capacity_2 - b2);
        if pour_amount > 0 {
            next_states.push((b1 - pour_amount, b2 + pour_amount, moves + 1));
        }

        // Pour from bucket2 to bucket1
        let pour_amount = b2.min(capacity_1 - b1);
        if pour_amount > 0 {
            next_states.push((b1 + pour_amount, b2 - pour_amount, moves + 1));
        }

        for (new_b1, new_b2, new_moves) in next_states {
            // Check if this state is the forbidden state
            if (new_b1, new_b2) == forbidden_state {
                continue;
            }

            // Check if any of the new states reaches the goal
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

            // Check if the state is valid and not visited
            if !visited.contains(&(new_b1, new_b2)) {
                visited.insert((new_b1, new_b2));
                queue.push_back((new_b1, new_b2, new_moves));
            }
        }
    }

    None
}
