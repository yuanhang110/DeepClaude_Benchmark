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
  // Sort entries once
  entries.sort(
    (a, b) =>
      a.date - b.date ||
      a.change - b.change ||
      a.description.localeCompare(b.description),
  );

  function getHeader(locale) {
    if (locale === 'en-US') {
      return 'Date'.padEnd(10, ' ') + ' | ' + 'Description'.padEnd(25, ' ') + ' | ' + 'Change'.padEnd(13, ' ');
    } else {
      return 'Datum'.padEnd(10, ' ') + ' | ' + 'Omschrijving'.padEnd(25, ' ') + ' | ' + 'Verandering'.padEnd(13, ' ');
    }
  }

  function formatDate(date, locale) {
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear();
    return locale === 'en-US' 
      ? `${month}/${day}/${year}` 
      : `${day}-${month}-${year}`;
  }

  function truncateDescription(description) {
    return description.length > 25 
      ? `${description.substring(0, 22)}...` 
      : description.padEnd(25, ' ');
  }

  function formatCurrency(amount, locale, currency) {
    const value = amount / 100;
    const options = {
      style: 'currency',
      currency: currency,
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    };

    if (locale === 'en-US') {
      if (amount < 0) {
        const absValue = Math.abs(value);
        const formatted = absValue.toLocaleString(locale, options);
        return `(${formatted})`.padStart(13, ' ');
      } else {
        return `${value.toLocaleString(locale, options)} `.padStart(13, ' ');
      }
    } else {
      options.currencyDisplay = 'narrowSymbol';
      const formatted = value.toLocaleString(locale, options) + ' ';
      return formatted.padStart(13, ' ');
    }
  }

  const rows = [];
  if (locale === 'en-US' || locale === 'nl-NL') {
    rows.push(getHeader(locale));
    entries.forEach(entry => {
      const dateStr = formatDate(entry.date, locale);
      const description = truncateDescription(entry.description);
      const changeStr = formatCurrency(entry.change, locale, currency);
      rows.push(`${dateStr} | ${description} | ${changeStr}`);
    });
  }
  return rows.join('\n');
}
