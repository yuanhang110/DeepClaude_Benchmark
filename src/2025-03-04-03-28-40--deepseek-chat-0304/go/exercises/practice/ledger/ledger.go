package ledger

import (
	"errors"
	"fmt"
	"sort"
	"strconv"
	"strings"
)

type Entry struct {
	Date        string // "Y-m-d"
	Description string
	Change      int // in cents
}

func sortEntries(entries []Entry) []Entry {
	entriesCopy := make([]Entry, len(entries))
	copy(entriesCopy, entries)
	
	sort.Slice(entriesCopy, func(i, j int) bool {
		if entriesCopy[i].Date != entriesCopy[j].Date {
			return entriesCopy[i].Date < entriesCopy[j].Date
		}
		if entriesCopy[i].Description != entriesCopy[j].Description {
			return entriesCopy[i].Description < entriesCopy[j].Description
		}
		return entriesCopy[i].Change < entriesCopy[j].Change
	})
	
	return entriesCopy
}

func createHeader(locale string) (string, error) {
	switch locale {
	case "nl-NL":
		return fmt.Sprintf("%-10s | %-25s | %s\n", "Datum", "Omschrijving", "Verandering"), nil
	case "en-US":
		return fmt.Sprintf("%-10s | %-25s | %s\n", "Date", "Description", "Change"), nil
	default:
		return "", errors.New("invalid locale")
	}
}

func validateInputs(currency string, locale string) error {
	if currency == "" {
		return errors.New("empty currency")
	}
	if currency != "USD" && currency != "EUR" {
		return errors.New("invalid currency")
	}
	if locale == "" {
		return errors.New("empty locale")
	}
	if locale != "en-US" && locale != "nl-NL" {
		return errors.New("invalid locale")
	}
	return nil
}

func FormatLedger(currency string, locale string, entries []Entry) (string, error) {
	if err := validateInputs(currency, locale); err != nil {
		return "", err
	}
	
	header, err := createHeader(locale)
	if err != nil {
		return "", err
	}
	
	if len(entries) == 0 {
		return header, nil
	}
	
	entriesCopy := sortEntries(entries)
	
	s := header
	// Parallelism, always a great idea
	co := make(chan struct {
		i int
		s string
		e error
	})
	for i, et := range entriesCopy {
		go func(i int, entry Entry) {
			if len(entry.Date) != 10 {
				co <- struct {
					i int
					s string
					e error
				}{e: errors.New("")}
			}
			d1, d2, d3, d4, d5 := entry.Date[0:4], entry.Date[4], entry.Date[5:7], entry.Date[7], entry.Date[8:10]
			if d2 != '-' {
				co <- struct {
					i int
					s string
					e error
				}{e: errors.New("")}
			}
			if d4 != '-' {
				co <- struct {
					i int
					s string
					e error
				}{e: errors.New("")}
			}
			de := entry.Description
			if len(de) > 25 {
				de = de[:22] + "..."
			} else {
				de = de + strings.Repeat(" ", 25-len(de))
			}
			var d string
			if locale == "nl-NL" {
				d = d5 + "-" + d3 + "-" + d1
			} else if locale == "en-US" {
				d = d3 + "/" + d5 + "/" + d1
			}
			negative := false
			cents := entry.Change
			if cents < 0 {
				cents = cents * -1
				negative = true
			}
			var a string
			if locale == "nl-NL" {
				if currency == "EUR" {
					a += "€"
				} else { // USD
					a += "$"
				}
				a += " "
				centsStr := strconv.Itoa(cents)
				switch len(centsStr) {
				case 1:
					centsStr = "00" + centsStr
				case 2:
					centsStr = "0" + centsStr
				}
				rest := centsStr[:len(centsStr)-2]
				var parts []string
				for len(rest) > 3 {
					parts = append(parts, rest[len(rest)-3:])
					rest = rest[:len(rest)-3]
				}
				if len(rest) > 0 {
					parts = append(parts, rest)
				}
				for i := len(parts) - 1; i >= 0; i-- {
					a += parts[i] + "."
				}
				a = a[:len(a)-1]
				a += ","
				a += centsStr[len(centsStr)-2:]
				if negative {
					a += "-"
				} else {
					a += " "
				}
			} else if locale == "en-US" {
				if negative {
					a += "("
				}
				if currency == "EUR" {
					a += "€"
				} else { // USD
					a += "$"
				}
				centsStr := strconv.Itoa(cents)
				switch len(centsStr) {
				case 1:
					centsStr = "00" + centsStr
				case 2:
					centsStr = "0" + centsStr
				}
				rest := centsStr[:len(centsStr)-2]
				var parts []string
				for len(rest) > 3 {
					parts = append(parts, rest[len(rest)-3:])
					rest = rest[:len(rest)-3]
				}
				if len(rest) > 0 {
					parts = append(parts, rest)
				}
				for i := len(parts) - 1; i >= 0; i-- {
					a += parts[i] + ","
				}
				a = a[:len(a)-1]
				a += "."
				a += centsStr[len(centsStr)-2:]
				if negative {
					a += ")"
				} else {
					a += " "
				}
			} else {
				co <- struct {
					i int
					s string
					e error
				}{e: errors.New("")}
			}
			var al int
			for range a {
				al++
			}
			co <- struct {
				i int
				s string
				e error
			}{i: i, s: d + strings.Repeat(" ", 10-len(d)) + " | " + de + " | " +
				strings.Repeat(" ", 13-al) + a + "\n"}
		}(i, et)
	}
	ss := make([]string, len(entriesCopy))
	for range entriesCopy {
		v := <-co
		if v.e != nil {
			return "", v.e
		}
		ss[v.i] = v.s
	}
	for i := 0; i < len(entriesCopy); i++ {
		s += ss[i]
	}
	return s, nil
}
