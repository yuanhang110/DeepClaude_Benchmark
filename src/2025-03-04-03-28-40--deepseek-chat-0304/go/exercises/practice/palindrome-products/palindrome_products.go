package palindrome

import (
	"errors"
	"sort"
)

// Product represents a palindrome product with its factors
type Product struct {
	Value          int
	Factorizations [][2]int
}

// isPalindrome checks if a number is a palindrome
func isPalindrome(n int) bool {
	original := n
	reversed := 0
	for n > 0 {
		reversed = reversed*10 + n%10
		n /= 10
	}
	return original == reversed
}

// findFactors finds all factor pairs for a product within the range
func findFactors(product, fmin, fmax int) [][2]int {
	var factors [][2]int
	for i := fmin; i <= fmax; i++ {
		if product%i == 0 {
			j := product / i
			if j >= fmin && j <= fmax {
				// Avoid duplicate pairs by only storing when i <= j
				if i <= j {
					factors = append(factors, [2]int{i, j})
				}
			}
		}
	}
	return factors
}

func Products(fmin, fmax int) (Product, Product, error) {
	if fmin > fmax {
		return Product{}, Product{}, errors.New("fmin > fmax")
	}

	// Generate all products
	products := make(map[int]bool)
	for i := fmin; i <= fmax; i++ {
		for j := i; j <= fmax; j++ {
			products[i*j] = true
		}
	}

	// Find all palindromes
	var palindromes []int
	for p := range products {
		if isPalindrome(p) {
			palindromes = append(palindromes, p)
		}
	}

	if len(palindromes) == 0 {
		return Product{}, Product{}, errors.New("no palindromes found")
	}

	// Sort palindromes to find min and max
	sort.Ints(palindromes)
	minP := palindromes[0]
	maxP := palindromes[len(palindromes)-1]

	// Find factors for min and max palindromes
	minFactors := findFactors(minP, fmin, fmax)
	maxFactors := findFactors(maxP, fmin, fmax)

	return Product{Value: minP, Factorizations: minFactors},
		Product{Value: maxP, Factorizations: maxFactors},
		nil
}
