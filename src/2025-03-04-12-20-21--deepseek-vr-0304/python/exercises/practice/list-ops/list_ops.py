def append(list1, list2):
    result = []
    for elem in list1:
        result.append(elem)
    for elem in list2:
        result.append(elem)
    return result


def concat(lists):
    result = []
    for sublist in lists:
        for elem in sublist:
            result.append(elem)
    return result


def filter(function, list):
    result = []
    for elem in list:
        if function(elem):
            result.append(elem)
    return result


def length(list):
    count = 0
    for _ in list:
        count += 1
    return count


def map(function, list):
    return [function(elem) for elem in list]


def foldl(function, list, initial):
    acc = initial
    for elem in list:
        acc = function(acc, elem)
    return acc


def foldr(function, list, initial):
    acc = initial
    reversed_list = []
    for i in range(len(list)-1, -1, -1):
        reversed_list.append(list[i])
    for elem in reversed_list:
        acc = function(acc, elem)
    return acc


def reverse(list):
    reversed_list = []
    for i in range(len(list)-1, -1, -1):
        reversed_list.append(list[i])
    return reversed_list
