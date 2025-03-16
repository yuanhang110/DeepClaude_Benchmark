def append(list1, list2):
    return list1 + list2


def concat(lists):
    return [item for sublist in lists for item in sublist]


def filter(function, list):
    return [x for x in list if function(x)]


def length(list):
    count = 0
    for _ in list:
        count += 1
    return count


def map(function, list):
    return [function(x) for x in list]


def foldl(function, list, initial):
    acc = initial
    for element in list:
        acc = function(acc, element)
    return acc


def foldr(function, list, initial):
    acc = initial
    for element in reversed(list):
        acc = function(acc, element)
    return acc


def reverse(list):
    return list[::-1]
