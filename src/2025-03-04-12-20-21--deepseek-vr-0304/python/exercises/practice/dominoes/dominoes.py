from collections import defaultdict, deque

def can_chain(dominoes):
    if not dominoes:
        return []
    
    # Check if all degrees are even
    degrees = defaultdict(int)
    for a, b in dominoes:
        degrees[a] += 1
        degrees[b] += 1

    for node in degrees:
        if degrees[node] % 2 != 0:
            return None
    
    # Check connectedness
    # Build adjacency list for node connectivity check
    adj_nodes = defaultdict(list)
    nodes = set()
    for a, b in dominoes:
        adj_nodes[a].append(b)
        adj_nodes[b].append(a)
        nodes.add(a)
        nodes.add(b)

    if not nodes:
        return []  # All dominoes are [0,0] but even degrees

    start_node = next(iter(nodes))
    visited = set()
    queue = deque([start_node])
    visited.add(start_node)

    while queue:
        node = queue.popleft()
        for neighbor in adj_nodes[node]:
            if neighbor not in visited:
                visited.add(neighbor)
                queue.append(neighbor)

    if len(visited) != len(nodes):
        return None
    
    # Build adjacency list with domino indices for both directions
    adj = defaultdict(list)
    domino_list = list(dominoes)
    for i, (a, b) in enumerate(domino_list):
        adj[a].append((b, i))
        adj[b].append((a, i))
    
    # Hierholzer's algorithm to find Eulerian circuit
    path = []
    stack = []
    current_node = start_node

    while True:
        if adj[current_node]:
            # Select next edge
            next_node, domino_idx = adj[current_node].pop()
            # Remove all instances of this domino from both nodes' adjacency lists
            adj[current_node] = [(n, i) for n, i in adj[current_node] if i != domino_idx]
            adj[next_node] = [(n, i) for n, i in adj[next_node] if i != domino_idx]
            stack.append((current_node, next_node, domino_idx))
            current_node = next_node
        else:
            if not stack:
                break
            # Backtrack
            prev_node, current_node, domino_idx = stack.pop()
            a, b = domino_list[domino_idx]
            if prev_node == a:
                path.append((a, b))
            else:
                path.append((b, a))

    # Reverse path to get correct order
    return path[::-1]
