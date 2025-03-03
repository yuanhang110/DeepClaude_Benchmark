package markdown

import (
	"fmt"
	"strings"
)

// Parser holds the state during markdown parsing
type parser struct {
	markdown    string
	pos         int
	headerLevel int
	listCount   int
	listOpened  bool
	headerFound bool
	output      strings.Builder
}

// Render translates markdown to HTML
func Render(markdown string) string {
	// Apply text styling replacements first, just like the original
	markdown = applyTextStyling(markdown)
	
	p := &parser{
		markdown: markdown,
	}
	
	// Process the markdown character by character
	for p.pos < len(p.markdown) {
		c := p.markdown[p.pos]
		
		switch c {
		case '#':
			if p.isStartOfLine() {
				p.processHeader()
			} else {
				p.writeCharacter(c)
				p.pos++
			}
		case '*':
			if p.isStartOfLine() && p.headerLevel == 0 && strings.Contains(p.markdown, "\n") {
				p.processList()
			} else {
				p.writeCharacter(c)
				p.pos++
			}
		case '\n':
			p.processNewline()
		default:
			p.writeCharacter(c)
			p.pos++
		}
		
		// Check if we've reached the end
		if p.pos >= len(p.markdown) {
			break
		}
	}
	
	// Finalize the output with any closing tags
	return p.finalizeOutput()
}

// Apply the initial text styling replacements (bold, italic)
func applyTextStyling(markdown string) string {
	markdown = strings.Replace(markdown, "__", "<strong>", 1)
	markdown = strings.Replace(markdown, "__", "</strong>", 1)
	markdown = strings.Replace(markdown, "_", "<em>", 1)
	markdown = strings.Replace(markdown, "_", "</em>", 1)
	return markdown
}

// Process header markdown (lines starting with #)
func (p *parser) processHeader() {
	// Count the consecutive # characters
	startPos := p.pos
	for p.pos < len(p.markdown) && p.markdown[p.pos] == '#' {
		p.pos++
	}
	p.headerLevel = p.pos - startPos
	
	// Handle the various header scenarios
	if p.headerLevel == 7 {
		// Special case: 7 or more # is treated as a paragraph with literal #
		p.output.WriteString(fmt.Sprintf("<p>%s ", strings.Repeat("#", p.headerLevel)))
	} else if p.headerFound {
		// If we already found a header and see another #, it's literal content
		p.output.WriteString("# ")
		p.headerLevel--
	} else {
		// Regular header case
		p.output.WriteString(fmt.Sprintf("<h%d>", p.headerLevel))
	}
	
	p.headerFound = true
	p.pos++ // Skip the space after #
}

// Process list items (lines starting with *)
func (p *parser) processList() {
	// Start a new list if needed
	if p.listCount == 0 {
		p.output.WriteString("<ul>")
	}
	
	p.listCount++
	
	// Handle list item
	if !p.listOpened {
		p.output.WriteString("<li>")
		p.listOpened = true
	} else {
		// This is the case where we already have an open list item
		p.output.WriteString("* ")
	}
	
	p.pos += 2 // Skip "* " marker
}

// Process newline characters
func (p *parser) processNewline() {
	// Special case: if this is the last newline after a list
	if p.listOpened && 
	   strings.LastIndex(p.markdown, "\n") == p.pos && 
	   strings.LastIndex(p.markdown, "\n") > strings.LastIndex(p.markdown, "*") {
		p.output.WriteString("</li></ul><p>")
		p.listOpened = false
		p.listCount = 0
	} else if p.listCount > 0 && p.listOpened {
		// Close list item but keep the list open
		p.output.WriteString("</li>")
		p.listOpened = false
	}
	
	// Close header tags if necessary
	if p.headerLevel > 0 {
		p.output.WriteString(fmt.Sprintf("</h%d>", p.headerLevel))
		p.headerLevel = 0
	}
	
	p.pos++
}

// Write a character to the output
func (p *parser) writeCharacter(c byte) {
	p.output.WriteByte(c)
}

// Check if the current position is at the start of a line
func (p *parser) isStartOfLine() bool {
	return p.pos == 0 || (p.pos > 0 && p.markdown[p.pos-1] == '\n')
}

// Finalize the output with any necessary closing tags
func (p *parser) finalizeOutput() string {
	result := p.output.String()
	
	// Handle any unclosed tags
	switch {
	case p.headerLevel == 7:
		return result + "</p>"
	case p.headerLevel > 0:
		return result + fmt.Sprintf("</h%d>", p.headerLevel)
	}
	
	if p.listCount > 0 {
		return result + "</li></ul>"
	}
	
	// Ensure paragraphs are properly closed
	if strings.Contains(result, "<p>") {
		return result + "</p>"
	}
	
	// Wrap plain text in paragraph tags - but only if there are no block elements
	if !strings.Contains(result, "<h") && !strings.Contains(result, "<ul") {
		return "<p>" + result + "</p>"
	}
	return result
}
