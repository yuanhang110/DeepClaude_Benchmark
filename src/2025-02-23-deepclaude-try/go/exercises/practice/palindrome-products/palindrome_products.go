package palindrome

import "errors"

type Product struct {
	Value          int
	Factorizations [][2]int
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

	var minProduct, maxProduct int
	first := true
	for p := range products {
		if first {
			minProduct = p
			maxProduct = p
			first = false
		} else {
			if p < minProduct {
				minProduct = p
			}
			if p > maxProduct {
				maxProduct = p
			}
		}
	}

	minFactors := products[minProduct]
	maxFactors := products[maxProduct]

	return Product{minProduct, minFactors}, Product{maxProduct, maxFactors}, nil
}

func isPalindrome(n int) bool {
	if n < 0 {
		return false
	}
	reversed := 0
	original := n
	for n > 0 {
		reversed = reversed*10 + n%10
		n /= 10
	}
	return original == reversed
}
