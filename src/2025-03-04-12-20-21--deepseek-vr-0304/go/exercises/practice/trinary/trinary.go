package trinary

import (
    "errors"
    "math"
)

func ParseTrinary(s string) (int64, error) {
    var result int64

    if len(s) == 0 {
        return 0, errors.New("invalid trinary")
    }

    for _, c := range s {
        var digit int64

        switch c {
        case '0':
            digit = 0
        case '1':
            digit = 1
        case '2':
            digit = 2
        default:
            return 0, errors.New("invalid trinary")
        }

        // Check for overflow before multiplication and addition
        max := (math.MaxInt64 - digit) / 3
        if result > max {
            return 0, errors.New("overflow")
        }

        result = result * 3 + digit
    }

    return result, nil
}
