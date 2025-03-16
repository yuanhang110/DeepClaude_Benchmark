def append(list1, list2):
    result = []
    for item in list1:
        result.append(item)
    for item in list2:
        result.append(item)
    return result


def concat(lists):
    result = []
    for sublist in lists:
        for item in sublist:
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
    accumulator = initial
    for item in list:
        accumulator = function(accumulator, item)
    return accumulator


def foldr(function, list, initial):
    accumulator = initial
    n = length(list)
    for i in range(n-1, -1, -1):
        accumulator = function(accumulator, list[i])
    return accumulator


def reverse(list):
    result = []
    n = length(list)
    for i in range(n-1, -1, -1):
        result.append(list[i])
    return result
