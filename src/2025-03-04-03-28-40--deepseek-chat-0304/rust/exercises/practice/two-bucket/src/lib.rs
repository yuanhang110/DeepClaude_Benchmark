use std::collections::{HashSet, VecDeque};

#[derive(PartialEq, Eq, Debug, Clone, Copy, Hash)]
pub enum Bucket {
    One,
    Two,
}

#[derive(PartialEq, Eq, Debug)]
pub struct BucketStats {
    pub moves: u8,
    pub goal_bucket: Bucket,
    pub other_bucket: u8,
}

#[derive(Clone, Copy, Hash, PartialEq, Eq)]
struct State {
    b1: u8,
    b2: u8,
    moves: u8,
}

pub fn solve(
    capacity_1: u8,
    capacity_2: u8,
    goal: u8,
    start_bucket: &Bucket,
) -> Option<BucketStats> {
    let mut visited = HashSet::new();
    let mut queue = VecDeque::new();
    
    // Initial state based on starting bucket
    let initial_state = match start_bucket {
        Bucket::One => State { b1: capacity_1, b2: 0, moves: 1 },
        Bucket::Two => State { b1: 0, b2: capacity_2, moves: 1 },
    };
    
    // Check if goal is already in starting bucket
    if initial_state.b1 == goal {
        return Some(BucketStats {
            moves: 1,
            goal_bucket: Bucket::One,
            other_bucket: initial_state.b2,
        });
    }
    if initial_state.b2 == goal {
        return Some(BucketStats {
            moves: 1,
            goal_bucket: Bucket::Two,
            other_bucket: initial_state.b1,
        });
    }
    
    queue.push_back(initial_state);
    visited.insert((initial_state.b1, initial_state.b2));
    
    while let Some(state) = queue.pop_front() {
        // Generate all possible next states
        let states = vec![
            // Fill bucket 1
            State {
                b1: capacity_1,
                b2: state.b2,
                moves: state.moves + 1,
            },
            // Fill bucket 2
            State {
                b1: state.b1,
                b2: capacity_2,
                moves: state.moves + 1,
            },
            // Empty bucket 1
            State {
                b1: 0,
                b2: state.b2,
                moves: state.moves + 1,
            },
            // Empty bucket 2
            State {
                b1: state.b1,
                b2: 0,
                moves: state.moves + 1,
            },
            // Pour from 1 to 2
            State {
                b1: state.b1.saturating_sub(capacity_2 - state.b2),
                b2: (state.b2 + state.b1).min(capacity_2),
                moves: state.moves + 1,
            },
            // Pour from 2 to 1
            State {
                b1: (state.b1 + state.b2).min(capacity_1),
                b2: state.b2.saturating_sub(capacity_1 - state.b1),
                moves: state.moves + 1,
            },
        ];
        
        for next_state in states {
            // Check if we've reached the goal
            if next_state.b1 == goal {
                return Some(BucketStats {
                    moves: next_state.moves,
                    goal_bucket: Bucket::One,
                    other_bucket: next_state.b2,
                });
            }
            if next_state.b2 == goal {
                return Some(BucketStats {
                    moves: next_state.moves,
                    goal_bucket: Bucket::Two,
                    other_bucket: next_state.b1,
                });
            }
            
            // Check if this state is valid and not visited
            if !visited.contains(&(next_state.b1, next_state.b2)) {
                // Check the special rule about starting bucket
                let is_invalid = match start_bucket {
                    Bucket::One => next_state.b1 == 0 && next_state.b2 == capacity_2,
                    Bucket::Two => next_state.b2 == 0 && next_state.b1 == capacity_1,
                };
                
                if !is_invalid {
                    visited.insert((next_state.b1, next_state.b2));
                    queue.push_back(next_state);
                }
            }
        }
    }
    
    None
}
