package palindrome

import (
	"errors"
	"strconv"
)

type Product struct {
	Value          int
	Factorizations [][2]int
}

func Products(fmin, fmax int) (Product, Product, error) {
	if fmin > fmax {
		return Product{}, Product{}, errors.New("fmin > fmax")
	}

	productsMap := make(map[int][][2]int)

	for i := fmin; i <= fmax; i++ {
		for j := i; j <= fmax; j++ {
			product := i * j
			if product < 0 {
				continue
			}
			if isPalindrome(product) {
				productsMap[product] = append(productsMap[product], [2]int{i, j})
			}
		}
	}

	if len(productsMap) == 0 {
		return Product{}, Product{}, errors.New("no palindromes found")
	}

	var smallest, largest int
	first := true
	for p := range productsMap {
		if first {
			smallest, largest = p, p
			first = false
		} else {
			if p < smallest {
				smallest = p
			}
			if p > largest {
				largest = p
			}
		}
	}

	minProduct := Product{Value: smallest, Factorizations: productsMap[smallest]}
	maxProduct := Product{Value: largest, Factorizations: productsMap[largest]}
	return minProduct, maxProduct, nil
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
