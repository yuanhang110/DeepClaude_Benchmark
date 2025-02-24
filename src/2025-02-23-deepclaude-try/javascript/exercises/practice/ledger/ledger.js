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

// Helper functions
function formatDate(date, locale) {
  if (locale === 'en-US') {
    return `${(date.getMonth() + 1).toString().padStart(2, '0')}/${date.getDate().toString().padStart(2, '0')}/${date.getFullYear()}`;
  }
  return `${date.getDate().toString().padStart(2, '0')}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getFullYear()}`;
}

function truncateDescription(description) {
  return description.length > 25 ? `${description.substring(0, 22)}...` : description.padEnd(25, ' ');
}

function formatCurrency(amount, currency, locale) {
  const formatOptions = {
    style: 'currency',
    currency,
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
    ...(locale === 'nl-NL' && { currencyDisplay: 'narrowSymbol' })
  };

  const value = amount / 100;
  if (locale === 'en-US' && value < 0) {
    return `(${Math.abs(value).toLocaleString(locale, formatOptions)})`.padStart(13, ' ');
  }
  return `${value.toLocaleString(locale, formatOptions)} `.padStart(13, ' ');
}

function getHeader(locale) {
  if (locale === 'en-US') {
    return 'Date'.padEnd(10, ' ') + ' | ' + 'Description'.padEnd(25, ' ') + ' | ' + 'Change'.padEnd(13, ' ');
  }
  return 'Datum'.padEnd(10, ' ') + ' | ' + 'Omschrijving'.padEnd(25, ' ') + ' | ' + 'Verandering'.padEnd(13, ' ');
}

export function formatEntries(currency, locale, entries) {
  let table = '';
  
  // Generate Header Row
  table += getHeader(locale) + '\n';
  // Sort entries
  entries.sort((a, b) =>
    a.date - b.date ||
    a.change - b.change ||
    a.description.localeCompare(b.description)
  );

  // Format entries
  entries.forEach((entry) => {
    table += `${formatDate(entry.date, locale)} | `;
    table += `${truncateDescription(entry.description)} | `;
    table += `${formatCurrency(entry.change, currency, locale)}\n`;
  });
  return table.replace(/\n$/, '');
}
