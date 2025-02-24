def can_chain(dominoes):
    if not dominoes:
        return []
    
    n = len(dominoes)
    if n == 1:
        a, b = dominoes[0]
        return dominoes if a == b else None

    def backtrack(chain, used, current_end):
        if len(chain) == n:
            return chain[0][0] == current_end
        for i in range(n):
            if not used[i]:
                domino = dominoes[i]
                for a, b in [domino, (domino[1], domino[0])]:
                    if a == current_end:
                        used[i] = True
                        chain.append((a, b))
                        if backtrack(chain, used, b):
                            return True
                        chain.pop()
                        used[i] = False
        return False

    for start_idx in range(n):
        for first in [dominoes[start_idx], (dominoes[start_idx][1], dominoes[start_idx][0])]:
            used = [False] * n
            used[start_idx] = True
            chain = [first]
            if backtrack(chain, used, first[1]):
                return chain
    return None
