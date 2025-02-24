def append(list1, list2):
    return list1 + list2


def concat(lists):
    result = []
    for lst in lists:
        result += lst
    return result


def filter(function, list):
    return [item for item in list if function(item)]


def length(list):
    count = 0
    for _ in list:
        count += 1
    return count


def map(function, list):
    return [function(item) for item in list]


def foldl(function, list, initial):
    accumulator = initial
    for item in list:
        accumulator = function(accumulator, item)
    return accumulator


def foldr(function, list, initial):
    accumulator = initial
    for item in reverse(list):
        accumulator = function(accumulator, item)
    return accumulator


def reverse(list):
    reversed_list = []
    for i in range(length(list)-1, -1, -1):
        reversed_list.append(list[i])
    return reversed_list
