package markdown

import (
	"fmt"
	"strings"
)

// Parser holds the state for markdown parsing
type Parser struct {
	headerLevel  int    // Current header level (0-6)
	listLevel    int    // Current list nesting level
	listOpened   bool   // Whether we're inside a list item
	html         string // Accumulated HTML output
	headerExists bool   // Whether we've seen a header on this line
}

// Render translates markdown to HTML
func Render(markdown string) string {
	p := &Parser{}
	// Handle inline formatting
	markdown = strings.Replace(markdown, "__", "<strong>", 1)
	markdown = strings.Replace(markdown, "__", "</strong>", 1)
	markdown = strings.Replace(markdown, "_", "<em>", 1)
	markdown = strings.Replace(markdown, "_", "</em>", 1)

	pos := 0
	for pos < len(markdown) {
		char := markdown[pos]
		
		// Check if we're at the start of a new line (or beginning of input)
		atLineStart := pos == 0 || (pos > 0 && markdown[pos-1] == '\n')
		
		// Handle headers only at line start
		if atLineStart && char == '#' {
			pos = p.processHeader(markdown, pos)
			continue
		}
		
		// Handle lists only at line start and not in header
		if atLineStart && char == '*' && p.headerLevel == 0 {
			pos = p.processList(markdown, pos)
			continue
		}
		
		// Handle newlines
		if char == '\n' {
			pos = p.processNewline(markdown, pos)
			continue
		}
		
		p.html += string(char)
		pos++
	}
	
	return p.finalizeHTML()
}

// processHeader handles markdown header syntax
func (p *Parser) processHeader(markdown string, pos int) int {
	originalPos := pos
	count := 0
	
	// Count consecutive '#' characters
	for pos < len(markdown) && markdown[pos] == '#' {
		count++
		pos++
	}
	
	// Header must have at least one space after #
	if pos >= len(markdown) || markdown[pos] != ' ' {
		// Not a valid header - treat as normal text
		p.html += markdown[originalPos:pos]
		return pos
	}
	
	// Skip the space after header markers
	pos++
	
	// Special case: 7 or more '#' characters
	if count >= 7 {
		p.html += fmt.Sprintf("<p>%s ", strings.Repeat("#", count))
	} else if p.headerExists {
		p.html += "# "
		p.headerLevel--
	} else {
		p.headerLevel = count
		p.html += fmt.Sprintf("<h%d>", count)
	}
	
	p.headerExists = true
	return pos + 1
}

// processList handles markdown list syntax
func (p *Parser) processList(markdown string, pos int) int {
	if p.listLevel == 0 {
		p.html += "<ul>"
		p.listLevel = 1
	}
	
	// Start new list item if not already in one
	if !p.listOpened {
		p.html += "<li>"
		p.listOpened = true
	} else {
		// Close previous list item and open new one
		p.html += "</li><li>"
	}
	
	return pos + 2
}

// processNewline handles line endings and closing tags
func (p *Parser) processNewline(markdown string, pos int) int {
	// Close any open list item
	if p.listOpened {
		p.html += "</li>"
		p.listOpened = false
	}
	
	// Close header if needed
	if p.headerLevel > 0 {
		p.html += fmt.Sprintf("</h%d>", p.headerLevel)
		p.headerLevel = 0
	}
	
	if p.headerLevel > 0 {
		p.html += fmt.Sprintf("</h%d>", p.headerLevel)
		p.headerLevel = 0
	}
	
	return pos + 1
}

// finalizeHTML adds any necessary closing tags
func (p *Parser) finalizeHTML() string {
	// Close any remaining open tags
	if p.headerLevel > 0 {
		p.html += fmt.Sprintf("</h%d>", p.headerLevel)
	}
	
	if p.listLevel > 0 {
		if p.listOpened {
			p.html += "</li>"
		}
		p.html += "</ul>"
	}
	
	// Only wrap in <p> if no block elements present
	if !strings.HasPrefix(p.html, "<h") && 
	   !strings.HasPrefix(p.html, "<ul") && 
	   !strings.HasPrefix(p.html, "<li") &&
	   !strings.Contains(p.html, "<p>") {
		return "<p>" + p.html + "</p>"
	}
	
	return p.html
}
