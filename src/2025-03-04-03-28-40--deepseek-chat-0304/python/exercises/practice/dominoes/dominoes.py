def can_chain(dominoes):
    if not dominoes:
        return []
    
    # Try all possible starting dominoes and rotations
    for i, start in enumerate(dominoes):
        # Try both orientations
        for rotated_start in [start, start[::-1]]:
            used = [False] * len(dominoes)
            used[i] = True
            chain = [rotated_start]
            if _build_chain(dominoes, used, chain):
                return chain
    return None

def _build_chain(dominoes, used, chain):
    if all(used):
        # Check if first and last match
        return chain[0][0] == chain[-1][1]
    
    last = chain[-1][1]
    for i, domino in enumerate(dominoes):
        if not used[i]:
            # Try both orientations
            if domino[0] == last:
                used[i] = True
                chain.append(domino)
                if _build_chain(dominoes, used, chain):
                    return True
                chain.pop()
                used[i] = False
            elif domino[1] == last:
                used[i] = True
                chain.append(domino[::-1])
                if _build_chain(dominoes, used, chain):
                    return True
                chain.pop()
                used[i] = False
    return False
