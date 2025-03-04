class LedgerEntry {
  constructor(date, description, change) {
    this.date = new Date(date);
    this.description = description;
    this.change = change;
  }
}

const LOCALE_CONFIG = {
  'en-US': {
    headers: ['Date', 'Description', 'Change'],
    dateFormat: (date) => 
      `${(date.getMonth() + 1).toString().padStart(2, '0')}/` +
      `${date.getDate().toString().padStart(2, '0')}/` +
      `${date.getFullYear()}`,
    negativeFormat: (amount) => `(${amount})`
  },
  'nl-NL': {
    headers: ['Datum', 'Omschrijving', 'Verandering'],
    dateFormat: (date) =>
      `${date.getDate().toString().padStart(2, '0')}-` +
      `${(date.getMonth() + 1).toString().padStart(2, '0')}-` +
      `${date.getFullYear()}`,
    negativeFormat: (amount) => `-${amount}`
  }
};

const CURRENCY_CONFIG = {
  USD: { style: 'currency', currency: 'USD' },
  EUR: { style: 'currency', currency: 'EUR' }
};

export function createEntry(date, description, change) {
  return new LedgerEntry(date, description, change);
}

function formatCurrency(amount, currency, locale) {
  const options = {
    ...CURRENCY_CONFIG[currency],
    currencyDisplay: locale === 'nl-NL' ? 'narrowSymbol' : undefined,
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  };
  
  const formatted = (Math.abs(amount) / 100).toLocaleString(locale, options);
  return amount < 0 ? LOCALE_CONFIG[locale].negativeFormat(formatted) : formatted;
}

function formatEntry(entry, currency, locale) {
  const config = LOCALE_CONFIG[locale];
  const dateStr = config.dateFormat(entry.date);
  
  const description = entry.description.length > 25
    ? `${entry.description.substring(0, 22)}...`
    : entry.description.padEnd(25, ' ');
    
  let changeStr = formatCurrency(entry.change, currency, locale);
  // Adjust padding based on locale
  if (locale === 'en-US') {
    changeStr = changeStr.padStart(13, ' ');
  } else {
    changeStr = changeStr.padStart(12, ' ');
  }
  
  return `${dateStr} | ${description} | ${changeStr}`;
}

function createHeader(locale) {
  const [date, description, change] = LOCALE_CONFIG[locale].headers;
  return `${date.padEnd(10, ' ')} | ${description.padEnd(25, ' ')} | ${change.padEnd(13, ' ')}`;
}

export function formatEntries(currency, locale, entries) {
  // Sort entries by date, then change, then description
  entries.sort((a, b) =>
    a.date - b.date ||
    a.change - b.change ||
    a.description.localeCompare(b.description)
  );

  const header = createHeader(locale);
  const formattedEntries = entries.map(entry => 
    formatEntry(entry, currency, locale)
  ).join('\n');
  
  return `${header}\n${formattedEntries}`.trim();
}
