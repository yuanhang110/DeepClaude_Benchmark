import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Ledger {
    private static final Map<String, LocaleFormat> LOCALE_FORMATS = new HashMap<>();
    
    static {
        LOCALE_FORMATS.put("en-US-USD", new LocaleFormat("MM/dd/yyyy", "$", ".", ",", "Date       | Description               | Change       "));
        LOCALE_FORMATS.put("nl-NL-USD", new LocaleFormat("dd/MM/yyyy", "$", ",", ".", "Datum      | Omschrijving              | Verandering  "));
        LOCALE_FORMATS.put("en-US-EUR", new LocaleFormat("MM/dd/yyyy", "€", ".", ",", "Date       | Description               | Change       "));
        LOCALE_FORMATS.put("nl-NL-EUR", new LocaleFormat("dd/MM/yyyy", "€", ",", ".", "Datum      | Omschrijving              | Verandering  "));
    }
    public LedgerEntry createLedgerEntry(String date, String description, int change) {
        if (date == null || description == null) {
            throw new IllegalArgumentException("Date and description cannot be null");
        }
        try {
            return new LedgerEntry(LocalDate.parse(date), description, change);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format", e);
        }
    }

    public String format(String currency, String locale, LedgerEntry[] entries) {
        if (currency == null || locale == null || entries == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        validateInputs(currency, locale);
        
        LocaleFormat localeFormat = LOCALE_FORMATS.get(locale + "-" + currency);
        StringBuilder result = new StringBuilder(localeFormat.getHeader());

        if (entries.length > 0) {
            List<LedgerEntry> sortedEntries = sortEntries(entries);
            
            for (LedgerEntry entry : sortedEntries) {
                result.append("\n")
                      .append(formatEntry(entry, localeFormat));
            }
        }

        return result.toString();
    }

    private static class LocaleFormat {
        private final String datePattern;
        private final String currencySymbol;
        private final String decimalSeparator;
        private final String thousandSeparator;
        private final String header;

        public LocaleFormat(String datePattern, String currencySymbol, 
                          String decimalSeparator, String thousandSeparator,
                          String header) {
            this.datePattern = datePattern;
            this.currencySymbol = currencySymbol;
            this.decimalSeparator = decimalSeparator;
            this.thousandSeparator = thousandSeparator;
            this.header = header;
        }

        public String getDatePattern() { return datePattern; }
        public String getCurrencySymbol() { return currencySymbol; }
        public String getDecimalSeparator() { return decimalSeparator; }
        public String getThousandSeparator() { return thousandSeparator; }
        public String getHeader() { return header; }
    }

    private void validateInputs(String currency, String locale) {
        if (!currency.equals("USD") && !currency.equals("EUR")) {
            throw new IllegalArgumentException("Invalid currency");
        }
        if (!locale.equals("en-US") && !locale.equals("nl-NL")) {
            throw new IllegalArgumentException("Invalid locale");
        }
    }

    private List<LedgerEntry> sortEntries(LedgerEntry[] entries) {
        List<LedgerEntry> neg = new ArrayList<>();
        List<LedgerEntry> pos = new ArrayList<>();
        
        for (LedgerEntry entry : entries) {
            if (entry.getChange() >= 0) {
                pos.add(entry);
            } else {
                neg.add(entry);
            }
        }

        neg.sort((o1, o2) -> o1.getLocalDate().compareTo(o2.getLocalDate()));
        pos.sort((o1, o2) -> o1.getLocalDate().compareTo(o2.getLocalDate()));

        List<LedgerEntry> all = new ArrayList<>();
        all.addAll(neg);
        all.addAll(pos);
        return all;
    }

    private String formatEntry(LedgerEntry entry, LocaleFormat localeFormat) {
        String date = entry.getLocalDate().format(
            DateTimeFormatter.ofPattern(localeFormat.getDatePattern()));
        
        String description = formatDescription(entry.getDescription());
        String amount = formatAmount(entry.getChange(), localeFormat);
        
        return String.format("%s | %-25s | %13s", date, description, amount);
    }

    private String formatDescription(String description) {
        if (description.length() > 25) {
            return description.substring(0, 22) + "...";
        }
        return description;
    }

    private String formatAmount(double change, LocaleFormat localeFormat) {
        String converted = String.format("%.02f", Math.abs(change / 100));
        String[] parts = converted.split("\\.");
        String amount = formatIntegerPart(parts[0], localeFormat.getThousandSeparator());
        
        String formattedAmount = localeFormat.getCurrencySymbol() + 
            (localeFormat.getDecimalSeparator().equals(",") ? " " : "") +
            amount + localeFormat.getDecimalSeparator() + parts[1];
        
        if (change < 0) {
            formattedAmount = formatNegativeAmount(formattedAmount, localeFormat);
        } else if (localeFormat.getDecimalSeparator().equals(",")) {
            formattedAmount = " " + formattedAmount + " ";
        } else {
            formattedAmount += " ";
        }
        
        return formattedAmount;
    }

    private String formatIntegerPart(String integerPart, String thousandSeparator) {
        StringBuilder amount = new StringBuilder();
        int count = 1;
        for (int ind = integerPart.length() - 1; ind >= 0; ind--) {
            if ((count % 3) == 0 && ind > 0) {
                amount.insert(0, thousandSeparator + integerPart.charAt(ind));
            } else {
                amount.insert(0, integerPart.charAt(ind));
            }
            count++;
        }
        return amount.toString();
    }

    private String formatNegativeAmount(String amount, LocaleFormat localeFormat) {
        if (localeFormat.getDecimalSeparator().equals(".")) {
            return "(" + amount + ")";
        } else {
            return localeFormat.getCurrencySymbol() + " -" + 
                   amount.replace(localeFormat.getCurrencySymbol(), "").trim() + " ";
        }
    }

    public static final class LedgerEntry {
        private final LocalDate localDate;
        private final String description;
        private final double change;

        public LedgerEntry(LocalDate localDate, String description, double change) {
            if (localDate == null || description == null) {
                throw new IllegalArgumentException("Fields cannot be null");
            }
            this.localDate = localDate;
            this.description = description;
            this.change = change;
        }

        public LocalDate getLocalDate() {
            return localDate;
        }

        public String getDescription() {
            return description;
        }

        public double getChange() {
            return change;
        }
    }

}
