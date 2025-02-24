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

type EntriesByDateDesc []Entry

func getHeader(locale string) (string, error) {
	switch locale {
	case "nl-NL":
		return fmt.Sprintf("%-10s | %-25s | Verandering\n", "Datum", "Omschrijving"), nil
	case "en-US":
		return fmt.Sprintf("%-10s | %-25s | Change\n", "Date", "Description"), nil
	default:
		return "", errors.New("unsupported locale")
	}
}

func (e EntriesByDateDesc) Len() int { return len(e) }
func (e EntriesByDateDesc) Less(i, j int) bool {
	if e[i].Date != e[j].Date {
		return e[i].Date > e[j].Date
	}
	if e[i].Description != e[j].Description {
		return e[i].Description < e[j].Description
	}
	return e[i].Change < e[j].Change
}
func (e EntriesByDateDesc) Swap(i, j int) { e[i], e[j] = e[j], e[i] }

func parseDate(date string, locale string) (string, error) {
	if len(date) != 10 || date[4] != '-' || date[7] != '-' {
		return "", errors.New("invalid date format")
	}
	
	if locale == "nl-NL" {
		return date[8:10] + "-" + date[5:7] + "-" + date[:4], nil
	}
	return date[5:7] + "/" + date[8:10] + "/" + date[:4], nil
}

func truncateDescription(desc string) string {
	if len(desc) > 25 {
		return desc[:22] + "..."
	}
	return desc + strings.Repeat(" ", 25-len(desc))
}

func formatCurrency(cents int, currency string, locale string) (string, error) {
	// Implementation will be extracted next
	return "", nil // Temporary implementation to satisfy compiler
}

func FormatLedger(currency string, locale string, entries []Entry) (string, error) {
	var entriesCopy []Entry
	for _, e := range entries {
		entriesCopy = append(entriesCopy, e)
	}
	if len(entries) == 0 {
		if _, err := FormatLedger(currency, "en-US", []Entry{{Date: "2014-01-01", Description: "", Change: 0}}); err != nil {
			return "", err
		}
		return "", nil
	}
	// Sort entries by date, then description, then change
	impl := EntriesByDateDesc(entriesCopy)
	sort.Sort(impl)

	// Create header based on locale
	s, err := getHeader(locale)
	if err != nil {
		return "", err
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
