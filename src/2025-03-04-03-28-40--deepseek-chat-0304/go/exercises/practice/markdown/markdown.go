package markdown

import (
	"fmt"
	"strings"
)

// Render translates markdown to HTML
func Render(markdown string) string {
	// First handle inline formatting (bold and italic)
	markdown = applyInlineFormatting(markdown)

	var html strings.Builder
	lines := strings.Split(markdown, "\n")
	inList := false

	for _, line := range lines {
		// Handle headers
		if strings.HasPrefix(line, "#") {
			headerLevel := countHeaderLevel(line)
			if headerLevel > 0 && headerLevel <= 6 {
				html.WriteString(fmt.Sprintf("<h%d>%s</h%d>", headerLevel, strings.TrimSpace(line[headerLevel:]), headerLevel))
				continue
			}
		}

		// Handle lists
		if strings.HasPrefix(line, "* ") {
			if !inList {
				html.WriteString("<ul>")
				inList = true
			}
			html.WriteString(fmt.Sprintf("<li>%s</li>", strings.TrimSpace(line[2:])))
			continue
		}

		// Handle non-list lines when in a list
		if inList && !strings.HasPrefix(line, "* ") {
			html.WriteString("</ul>")
			inList = false
		}

		// Handle regular paragraphs
		if line != "" && !strings.HasPrefix(line, "#") && !strings.HasPrefix(line, "* ") {
			html.WriteString(fmt.Sprintf("<p>%s</p>", line))
		}
	}

	// Close any open list
	if inList {
		html.WriteString("</ul>")
	}

	return html.String()
}

// applyInlineFormatting handles bold and italic markdown
func applyInlineFormatting(markdown string) string {
	markdown = strings.Replace(markdown, "__", "<strong>", 1)
	markdown = strings.Replace(markdown, "__", "</strong>", 1)
	markdown = strings.Replace(markdown, "_", "<em>", 1)
	markdown = strings.Replace(markdown, "_", "</em>", 1)
	return markdown
}

// countHeaderLevel returns the number of # characters at the start of a line
func countHeaderLevel(line string) int {
	count := 0
	for _, char := range line {
		if char == '#' {
			count++
		} else {
			break
		}
	}
	return count
}
