package markdown

import (
	"fmt"
	"regexp"
	"strings"
)

// Render translates markdown to HTML
func Render(markdown string) string {
	markdown = replaceBoldAndItalic(markdown)
	markdown = replaceHeaders(markdown)
	markdown = replaceList(markdown)
	markdown = wrapParagraphs(markdown)
	return markdown
}

// replaceBoldAndItalic replaces __ with <strong> and _ with <em>
func replaceBoldAndItalic(markdown string) string {
	// Replace __ with <strong> tags, alternating between opening and closing
	strongToggle := false
	markdown = regexp.MustCompile(`__`).ReplaceAllStringFunc(markdown, func(_ string) string {
		strongToggle = !strongToggle
		if strongToggle {
			return "<strong>"
		}
		return "</strong>"
	})

	// Replace _ with <em> tags, alternating between opening and closing
	emToggle := false
	markdown = regexp.MustCompile(`_`).ReplaceAllStringFunc(markdown, func(_ string) string {
		emToggle = !emToggle
		if emToggle {
			return "<em>"
		}
		return "</em>"
	})

	return markdown
}

// replaceHeaders handles Markdown headers (#, ##, etc.)
func replaceHeaders(markdown string) string {
	return regexp.MustCompile(`(?m)^(#{1,6}) (.*)`).ReplaceAllStringFunc(markdown, func(match string) string {
		parts := regexp.MustCompile(`^(#{1,6}) (.*)`).FindStringSubmatch(match)
		if len(parts) == 3 {
			level := len(parts[1])
			if level == 7 {
				return fmt.Sprintf("<p>%s %s", parts[1], parts[2])
			}
			return fmt.Sprintf("<h%d>%s</h%d>", level, parts[2], level)
		}
		return match
	})
}

// replaceList handles Markdown lists (*)
func replaceList(markdown string) string {
	// Replace each list item
	markdown = regexp.MustCompile(`(?m)^\* (.*)`).ReplaceAllString(markdown, "<li>$1</li>")
	// Wrap consecutive list items in <ul> tags
	markdown = regexp.MustCompile(`(?s)(<li>[^<]*</li>(?:<li>[^<]*</li>)*)`).ReplaceAllString(markdown, "<ul>$1</ul>")
	// Remove newlines between list items
	markdown = strings.ReplaceAll(markdown, "</li>\n<li>", "</li><li>")
	return markdown
}

// wrapParagraphs wraps any remaining text in <p> tags
func wrapParagraphs(markdown string) string {
	// Split by lines and process each one
	lines := strings.Split(markdown, "\n")
	var result []string
	var currentParagraph []string

	for _, line := range lines {
		// If the line is already wrapped in a tag, add it directly
		if strings.HasPrefix(line, "<h") || strings.HasPrefix(line, "<ul") || 
		   strings.HasPrefix(line, "<li") || strings.HasPrefix(line, "<p") {
			if len(currentParagraph) > 0 {
				result = append(result, "<p>"+strings.Join(currentParagraph, " ")+"</p>")
				currentParagraph = []string{}
			}
			result = append(result, line)
		} else {
			currentParagraph = append(currentParagraph, strings.TrimSpace(line))
		}
	}

	// Add any remaining paragraph
	if len(currentParagraph) > 0 {
		result = append(result, "<p>"+strings.Join(currentParagraph, " ")+"</p>")
	}

	return strings.Join(result, "")
}
