def can_chain(dominoes):
    if not dominoes:
        return []

    n = len(dominoes)
    dominoes = [tuple(d) for d in dominoes]

    def backtrack(used, current_chain, current_end, first_start):
        if all(used):
            return current_chain if current_end == first_start else None
        for i in range(n):
            if not used[i]:
                a, b = dominoes[i]
                # Try original orientation
                if a == current_end:
                    used[i] = True
                    result = backtrack(used, current_chain + [(a, b)], b, first_start)
                    if result is not None:
                        return result
                    used[i] = False
                # Try reversed orientation
                if b == current_end:
                    used[i] = True
                    result = backtrack(used, current_chain + [(b, a)], a, first_start)
                    if result is not None:
                        return result
                    used[i] = False
        return None

    for i in range(n):
        a, b = dominoes[i]
        # Try non-reversed
        used = [False] * n
        used[i] = True
        chain = [(a, b)]
        result = backtrack(used, chain, b, a)
        if result:
            return result
        # Try reversed
        used = [False] * n
        used[i] = True
        chain = [(b, a)]
        result = backtrack(used, chain, a, b)
        if result:
            return result
    return None
