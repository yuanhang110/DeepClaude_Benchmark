package markdown

import (
	"fmt"
	"strings"
)

type parser struct {
	markdown     string
	position    int
	html        *strings.Builder
	headerLevel int
	listDepth   int
	isListItemOpen bool
}

// Render translates markdown to HTML
func Render(markdown string) string {
	p := &parser{
		markdown: replaceEmphasis(markdown),
		html:     &strings.Builder{},
	}
	
	for p.position < len(p.markdown) {
		char := p.markdown[p.position]
		
		switch {
		case char == '#':
			p.processHeader()
		case char == '*' && p.isListStart():
			p.processListStart()
		case char == '\n':
			p.processNewLine()
		default:
			p.html.WriteByte(char)
			p.position++
		}
	}
	
	return p.finalizeHTML()
}

// Replace markdown emphasis tags with HTML tags (preserves count logic)
func replaceEmphasis(input string) string {
	input = strings.Replace(input, "__", "<strong>", 1)
	input = strings.Replace(input, "__", "</strong>", 1)
	input = strings.Replace(input, "_", "<em>", 1)
	return strings.Replace(input, "_", "</em>", 1)
}

func (p *parser) processHeader() {
	// Only process headers at start of line or after newline
	if p.position > 0 && p.markdown[p.position-1] != '\n' {
		p.html.WriteByte(p.markdown[p.position])
		p.position++
		return
	}

	start := p.position
	for p.position < len(p.markdown) && p.markdown[p.position] == '#' {
		p.position++
	}
	p.headerLevel = p.position - start
	
	if p.headerLevel == 7 {
		p.html.WriteString(fmt.Sprintf("<p>%s ", strings.Repeat("#", p.headerLevel)))
		p.position++ // skip space after #
		return
	}
	
	if p.headerLevel > 6 {
		p.html.WriteString("# ")
		p.headerLevel = 0
		return
	}
	
	fmt.Fprintf(p.html, "<h%d>", p.headerLevel)
	p.position++ // skip space after #
}

func (p *parser) isListStart() bool {
	// List must start with "* " at beginning of line or after newline
	return p.headerLevel == 0 && 
		(p.position == 0 || p.markdown[p.position-1] == '\n') &&
		p.position+1 < len(p.markdown) && 
		p.markdown[p.position+1] == ' '
}

func (p *parser) processListStart() {
	if p.listDepth == 0 {
		p.html.WriteString("<ul>")
	}
	
	p.listDepth++
	p.html.WriteString("<li>")
	p.isListItemOpen = true
	p.position += 2 // skip "* " after list marker
}

func (p *parser) processNewLine() {
	// Close any open list items
	if p.isListItemOpen {
		p.html.WriteString("</li>")
		p.isListItemOpen = false
	}
	
	// Close list if next line doesn't start with *
	if p.listDepth > 0 {
		nextPos := p.position + 1
		if nextPos >= len(p.markdown) || (markdown[nextPos] != '*' && !(nextPos+1 < len(markdown) && markdown[nextPos+1] == ' ')) {
			p.html.WriteString("</ul>")
			p.listDepth = 0
		}
	}
	
	if p.headerLevel > 0 {
		fmt.Fprintf(p.html, "</h%d>", p.headerLevel)
		p.headerLevel = 0
	}
	
	p.position++ // move past newline
}

func (p *parser) finalizeHTML() string {
	html := p.html.String()
	
	// Close any remaining open tags
	if p.headerLevel > 0 && p.headerLevel <= 6 {
		html += fmt.Sprintf("</h%d>", p.headerLevel)
	}
	if p.listDepth > 0 {
		html += "</ul>"
	}
	
	// Wrap in paragraph if no block elements present
	if !strings.Contains(html, "<h") && !strings.Contains(html, "<ul>") && !strings.HasPrefix(html, "<p>") {
		return "<p>" + html + "</p>"
	}
	return html
}
