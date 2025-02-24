class LedgerEntry {
  constructor() {
    this.date = undefined;
    this.description = undefined;
    this.change = undefined;
  }
}

export function createEntry(date, description, change) {
  let entry = new LedgerEntry();
  entry.date = new Date(date);
  entry.description = description;
  entry.change = change;
  return entry;
}

export function formatEntries(currency, locale, entries) {
  let table = '';
  
  // Sort entries consistently
  entries.sort(
    (a, b) =>
      a.date - b.date ||
      a.change - b.change ||
      a.description.localeCompare(b.description),
  );

  // Create header based on locale
  table += createHeader(locale);
  
  // Helper functions
  function formatDate(date) {
    if (locale === 'en-US') {
      return `${(date.getMonth() + 1).toString().padStart(2, '0')}/${date.getDate().toString().padStart(2, '0')}/${date.getFullYear()}`;
    }
    return `${date.getDate().toString().padStart(2, '0')}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getFullYear()}`;
  }

  function createHeader(locale) {
    const headers = {
      'en-US': ['Date', 'Description', 'Change'],
      'nl-NL': ['Datum', 'Omschrijving', 'Verandering']
    };
    return headers[locale]
      .map((text, i) => text.padEnd([10,25,13][i], ' '))
      .join(' | ') + '\n';
  }

  entries.forEach((entry) => {
    // Write entry date to table
    table += `${formatDate(entry.date)} | `;

      // Write entry description to table
      const truncatedDescription =
        entry.description.length > 25
          ? `${entry.description.substring(0, 22)}...`
          : entry.description.padEnd(25, ' ');
      table += `${truncatedDescription} | `;

      // Write entry change to table
      const changeStr = formatCurrency(entry, currency, locale);
      table += changeStr.padStart(13, ' ');
      table += '\n';
    });
  function formatCurrency(entry, currency, locale) {
    const options = {
      style: 'currency',
      currency,
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
      currencyDisplay: locale === 'nl-NL' ? 'narrowSymbol' : 'symbol'
    };

    let value = entry.change / 100;
    let formatted = value.toLocaleString(locale, options);
    if (locale === 'en-US' && value < 0) {
      formatted = `(${Math.abs(value).toLocaleString(locale, options)})`;
    }
    return formatted.padStart(13, ' ');
  }

  function truncateDescription(description) {
    return description.length > 25 
      ? `${description.substring(0, 22)}...`
      : description.padEnd(25, ' ');
  }

  entries.forEach((entry) => {
    // Write entry date to table
    table += `${formatDate(entry.date)} | `;

    // Write entry description to table
    table += `${truncateDescription(entry.description)} | `;

    // Write entry change to table
    table += formatCurrency(entry, currency, locale);
    table += '\n';
  });
  
  return table.replace(/\n$/, '');
}
