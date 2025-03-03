package palindrome

import (
	"errors"
	"math"
	"strconv"
)

// Product represents a palindrome product with its factors
type Product struct {
	Value         int
	Factorizations [][2]int
}

func Products(fmin, fmax int) (Product, Product, error) {
	if fmin > fmax {
		return Product{}, Product{}, errors.New("fmin > fmax")
	}

	products := make(map[int][][2]int)

	for i := fmin; i <= fmax; i++ {
		for j := i; j <= fmax; j++ {
			p := i * j
			if isPalindrome(p) {
				products[p] = append(products[p], [2]int{i, j})
			}
		}
	}

	if len(products) == 0 {
		return Product{}, Product{}, errors.New("no palindromes found")
	}

	minVal := math.MaxInt
	maxVal := math.MinInt
	for p := range products {
		if p < minVal {
			minVal = p
		}
		if p > maxVal {
			maxVal = p
		}
	}

	minProduct := Product{Value: minVal, Factorizations: products[minVal]}
	maxProduct := Product{Value: maxVal, Factorizations: products[maxVal]}

	return minProduct, maxProduct, nil
}

// isPalindrome checks if a number reads the same backward as forward
func isPalindrome(n int) bool {
	s := strconv.Itoa(n)
	for i := 0; i < len(s)/2; i++ {
		if s[i] != s[len(s)-1-i] {
			return false
		}
	}
	return true
}
