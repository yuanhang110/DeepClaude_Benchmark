package markdown

import (
	"fmt"
	"strings"
)

// processEmphasis replaces bold and italic markers with HTML tags
func processEmphasis(markdown string) string {
	markdown = strings.Replace(markdown, "__", "<strong>", 1)
	markdown = strings.Replace(markdown, "__", "</strong>", 1)
	markdown = strings.Replace(markdown, "_", "<em>", 1)
	markdown = strings.Replace(markdown, "_", "</em>", 1)
	return markdown
}

// processHeader handles header markup (#) and returns HTML
func processHeader(markdown string, pos int) (string, int, int) {
	headerLevel := 0
	startPos := pos
	
	// Count consecutive # characters
	for pos < len(markdown) && markdown[pos] == '#' {
		headerLevel++
		pos++
	}
	
	// Invalid header (7 or more #) becomes paragraph
	if headerLevel >= 7 {
		return fmt.Sprintf("<p>%s ", strings.Repeat("#", headerLevel)), pos + 1, headerLevel
	}
	
	// Skip one space after #s if present
	if pos < len(markdown) && markdown[pos] == ' ' {
		pos++
	}
	
	return fmt.Sprintf("<h%d>", headerLevel), pos, headerLevel
}

// processList handles list item markup (*) and returns HTML
func processList(markdown string, pos int, list int, listOpened bool) (string, int, int, bool) {
	html := ""
	
	// Only process lists if markdown contains newlines
	if !strings.Contains(markdown, "\n") {
		return string(markdown[pos]), pos + 1, list, listOpened
	}
	
	// Start new list if needed
	if list == 0 {
		html = "<ul>"
	}
	list++
	
	// Handle list item
	if !listOpened {
		html += "<li>"
		listOpened = true
	} else {
		html += "* "
	}
	
	return html, pos + 2, list, listOpened
}

// processNewline handles newline character and returns HTML
func processNewline(markdown string, pos int, header int, list int, listOpened bool) (string, bool, int) {
	html := ""
	
	// Check if list needs to be closed
	if listOpened && strings.LastIndex(markdown, "\n") == pos && 
	   strings.LastIndex(markdown, "\n") > strings.LastIndex(markdown, "*") {
		html += "</li></ul><p>"
		listOpened = false
		list = 0
	} else if list > 0 && listOpened {
		html += "</li>"
		listOpened = false
	}
	
	// Close header if open
	if header > 0 {
		html += fmt.Sprintf("</h%d>", header)
	}
	
	return html, listOpened, list
}

// finalizeHTML adds closing tags based on document state
func finalizeHTML(html string, header int, list int) string {
	switch {
	case header == 7:
		return html + "</p>"
	case header > 0:
		return html + fmt.Sprintf("</h%d>", header)
	}
	
	if list > 0 {
		return html + "</li></ul>"
	}
	
	if strings.Contains(html, "<p>") {
		return html + "</p>"
	}
	
	return "<p>" + html + "</p>"
}

// Render translates markdown to HTML
func Render(markdown string) string {
	// Initialize state
	var html strings.Builder
	pos := 0
	header := 0
	list := 0
	listOpened := false
	
	// Process each character
	for pos < len(markdown) {
		if pos >= len(markdown) {
			break
		}
		
		char := markdown[pos]
		
		switch char {
		case '#':
			if pos == 0 || (pos > 0 && markdown[pos-1] == '\n') {
				// Process headers only at start of line
				headerHTML, newPos, newHeader := processHeader(markdown, pos)
				html.WriteString(headerHTML)
				pos = newPos
				header = newHeader
				continue
			}
			
		case '*':
			// Process lists (only if header is not active and at start of line)
			if header == 0 && (pos == 0 || (pos > 0 && markdown[pos-1] == '\n')) {
				if pos+1 < len(markdown) && markdown[pos+1] == ' ' {
					listHTML, newPos, newList, newListOpened := processList(markdown, pos, list, listOpened)
					html.WriteString(listHTML)
					pos = newPos
					list = newList
					listOpened = newListOpened
					continue
				}
			}
			
		case '\n':
			// Process newlines
			newlineHTML, newListOpened, newList := processNewline(markdown, pos, header, list, listOpened)
			html.WriteString(newlineHTML)
			listOpened = newListOpened
			list = newList
			header = 0
			pos++
			continue
		}
		
		// Process emphasis if we have enough characters ahead
		if pos+1 < len(markdown) {
			if char == '_' && pos+1 < len(markdown) {
				if markdown[pos+1] == '_' {
					// Handle bold
					html.WriteString("<strong>")
					pos += 2
					continue
				} else {
					// Handle italic
					html.WriteString("<em>")
					pos++
					continue
				}
			}
		}
		
		// Append regular character
		html.WriteByte(char)
		pos++
	}
	
	return finalizeHTML(html.String(), header, list)
}
