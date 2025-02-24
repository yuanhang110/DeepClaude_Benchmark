package ledger

import (
	"errors"
	"sort"
	"strconv"
	"strings"
)

// Supported locales
const (
	LocaleNL = "nl-NL"
	LocaleUS = "en-US"
)

// Supported currencies
const (
	CurrencyEUR = "EUR"
	CurrencyUSD = "USD"
)

// Entry represents a single ledger entry
type Entry struct {
	Date        string // "Y-m-d"
	Description string
	Change      int // in cents
}

// DateParts holds the components of a parsed date
type DateParts struct {
	year  string
	month string
	day   string
}

// parseDateParts validates and splits a date string into its components
func parseDateParts(date string) (DateParts, error) {
	if len(date) != 10 {
		return DateParts{}, errors.New("invalid date format")
	}

	year := date[0:4]
	month := date[5:7]
	day := date[8:10]

	if date[4] != '-' || date[7] != '-' {
		return DateParts{}, errors.New("invalid date separator")
	}

	return DateParts{year: year, month: month, day: day}, nil
}

// formatDate formats a date according to the locale
func formatDate(parts DateParts, locale string) string {
	switch locale {
	case LocaleNL:
		return parts.day + "-" + parts.month + "-" + parts.year
	case LocaleUS:
		return parts.month + "/" + parts.day + "/" + parts.year
	default:
		return ""
	}
}

func FormatLedger(currency string, locale string, entries []Entry) (string, error) {
	// Create a copy of entries to avoid modifying the input
	entriesCopy := make([]Entry, len(entries))
	copy(entriesCopy, entries)
	// Handle empty entries case
	if len(entries) == 0 {
		if _, err := FormatLedger(currency, LocaleUS, []Entry{{Date: "2014-01-01", Description: "", Change: 0}}); err != nil {
			return "", err
		}
	}

	// Sort entries by date, description, and change
	sort.Slice(entriesCopy, func(i, j int) bool {
		if entriesCopy[i].Date != entriesCopy[j].Date {
			return entriesCopy[i].Date < entriesCopy[j].Date
		}
		if entriesCopy[i].Description != entriesCopy[j].Description {
			return entriesCopy[i].Description < entriesCopy[j].Description
		}
		return entriesCopy[i].Change < entriesCopy[j].Change
	})

	var s string
	if locale == "nl-NL" {
		s = "Datum" +
			strings.Repeat(" ", 10-len("Datum")) +
			" | " +
			"Omschrijving" +
			strings.Repeat(" ", 25-len("Omschrijving")) +
			" | " + "Verandering" + "\n"
	} else if locale == "en-US" {
		s = "Date" +
			strings.Repeat(" ", 10-len("Date")) +
			" | " +
			"Description" +
			strings.Repeat(" ", 25-len("Description")) +
			" | " + "Change" + "\n"
	} else {
		return "", errors.New("")
	}
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
				} else if currency == "USD" {
					a += "$"
				} else {
					co <- struct {
						i int
						s string
						e error
					}{e: errors.New("")}
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
				} else if currency == "USD" {
					a += "$"
				} else {
					co <- struct {
						i int
						s string
						e error
					}{e: errors.New("")}
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
