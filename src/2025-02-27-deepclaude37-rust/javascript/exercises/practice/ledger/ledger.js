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

// Helper function to format date based on locale
function formatDate(date, locale) {
  if (locale === 'en-US') {
    return `${(date.getMonth() + 1)
      .toString()
      .padStart(2, '0')}/${date
      .getDate()
      .toString()
      .padStart(2, '0')}/${date.getFullYear()}`;
  } else if (locale === 'nl-NL') {
    return `${date.getDate().toString().padStart(2, '0')}-${(
      date.getMonth() + 1
    )
      .toString()
      .padStart(2, '0')}-${date.getFullYear()}`;
  }
}

// Helper function to truncate and pad description
function formatDescription(description) {
  return description.length > 25
    ? `${description.substring(0, 22)}...`
    : description.padEnd(25, ' ');
}

// Helper function to format currency
function formatCurrency(amount, currency, locale) {
  const amountInDecimal = amount / 100;
  
  // Define formatter options based on locale and currency
  const options = {
    style: 'currency',
    currency: currency,
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  };
  
  if (locale === 'nl-NL') {
    options.currencyDisplay = 'narrowSymbol';
    return `${amountInDecimal.toLocaleString(locale, options)} `;
  } else if (locale === 'en-US') {
    if (amount < 0) {
      return `(${Math.abs(amountInDecimal).toLocaleString(locale, options)})`;
    } else {
      return `${amountInDecimal.toLocaleString(locale, options)} `;
    }
  }
}

export function formatEntries(currency, locale, entries) {
  // Define locale-specific configurations
  const localeConfig = {
    'en-US': {
      headers: {
        date: 'Date',
        description: 'Description',
        change: 'Change'
      }
    },
    'nl-NL': {
      headers: {
        date: 'Datum',
        description: 'Omschrijving',
        change: 'Verandering'
      }
    }
  };

  // Build header row using locale config
  let table = 
    localeConfig[locale].headers.date.padEnd(10, ' ') +
    ' | ' +
    localeConfig[locale].headers.description.padEnd(25, ' ') +
    ' | ' +
    localeConfig[locale].headers.change.padEnd(13, ' ') +
    '\n';

  // Sort entries (moved outside locale conditions since sorting is identical)
  entries.sort(
    (a, b) =>
      a.date - b.date ||
      a.change - b.change ||
      a.description.localeCompare(b.description),
  );

  // Process each entry
  entries.forEach((entry) => {
    // Format date according to locale
    table += `${formatDate(entry.date, locale)} | `;
    
    // Format description
    table += `${formatDescription(entry.description)} | `;
    
    // Format currency amount
    const changeStr = formatCurrency(entry.change, currency, locale);
    table += changeStr.padStart(13, ' ');
    table += '\n';
  });

  return table.replace(/\n$/, '');
}
