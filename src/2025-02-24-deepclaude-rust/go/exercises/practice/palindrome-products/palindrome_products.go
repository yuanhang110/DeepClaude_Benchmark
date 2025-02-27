package palindrome

import (
	"errors"
	"strconv"
)

type Product struct {
	Value          int
	Factorizations [][2]int
}

func isPalindrome(n int) bool {
	s := strconv.Itoa(n)
	for i := 0; i < len(s)/2; i++ {
		if s[i] != s[len(s)-1-i] {
			return false
		}
	}
	return true
}

func Products(fmin, fmax int) (Product, Product, error) {
	if fmin > fmax {
		return Product{}, Product{}, errors.New("fmin > fmax")
	}

	products := make(map[int][][2]int)

	for a := fmin; a <= fmax; a++ {
		for b := a; b <= fmax; b++ {
			p := a * b
			if isPalindrome(p) {
				products[p] = append(products[p], [2]int{a, b})
			}
		}
	}

	if len(products) == 0 {
		return Product{}, Product{}, errors.New("no palindromes")
	}

	var minP, maxP int
	first := true
	for p := range products {
		if first {
			minP = p
			maxP = p
			first = false
			continue
		}
		if p < minP {
			minP = p
		}
		if p > maxP {
			maxP = p
		}
	}

	smallest := Product{Value: minP, Factorizations: products[minP]}
	largest := Product{Value: maxP, Factorizations: products[maxP]}

	return smallest, largest, nil
}
