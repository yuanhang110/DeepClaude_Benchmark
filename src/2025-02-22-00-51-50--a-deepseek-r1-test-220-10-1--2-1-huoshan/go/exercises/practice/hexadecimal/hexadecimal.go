package hexadecimal

import (
    "fmt"
    "math"
)

const (
    errEmpty     = "empty string"
    errInvalid   = "invalid syntax"
    errOverflow  = "overflow"
)

func ParseHex(in string) (int64, error) {
    var result int64
    var err string
    
    if len(in) == 0 {
        err = errEmpty
    } else {
        for _, c := range in {
            var val int64
            switch {
            case '0' <= c && c <= '9':
                val = int64(c - '0')
            case 'a' <= c && c <= 'f':
                val = int64(c - 'a' + 10)
            case 'A' <= c && c <= 'F':
                val = int64(c - 'A' + 10)
            default:
                err = errInvalid
                break
            }
            
            if err != "" {
                break
            }
            
            // Check for overflow before multiply-add
            if result > (math.MaxInt64 - val)/16 {
                err = errOverflow
                break
            }
            
            result = result*16 + val
        }
    }
    
    if err != "" {
        return 0, fmt.Errorf(err)
    }
    return result, nil
}
