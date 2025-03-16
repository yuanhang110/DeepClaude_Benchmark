def append(list1, list2):
    result = list1.copy()
    for item in list2:
        result.append(item)
    return result


def concat(lists):
    result = []
    for list in lists:
        for item in list:
            result.append(item)
    return result


def filter(function, list):
    result = []
    for item in list:
        if function(item):
            result.append(item)
    return result


def length(list):
    count = 0
    for _ in list:
        count += 1
    return count


def map(function, list):
    result = []
    for item in list:
        result.append(function(item))
    return result


def foldl(function, list, initial):
    result = initial
    for item in list:
        result = function(result, item)
    return result


def foldr(function, list, initial):
    if length(list) == 0:
        return initial
    return function(list[0], foldr(function, list[1:], initial))


def reverse(list):
    result = []
    for i in range(length(list) - 1, -1, -1):
        result.append(list[i])
    return result
