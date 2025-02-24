import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public class Ledger {
    public LedgerEntry createLedgerEntry(String d, String desc, int c) {
        LedgerEntry le = new LedgerEntry();
        le.setChange(c);
        le.setDescription(desc);
        le.setLocalDate(LocalDate.parse(d));
        return le;
    }

    public String format(String cur, String loc, LedgerEntry[] entries) {
        validateInputs(cur, loc);
        FormatConfig config = getFormatConfig(cur, loc);
        
        StringBuilder sb = new StringBuilder(config.header());

        if (entries.length > 0) {
            List<LedgerEntry> sortedEntries = getSortedEntries(entries);

            for (LedgerEntry e : sortedEntries) {
                String date = e.getLocalDate().format(DateTimeFormatter.ofPattern(config.datePattern()));

                String desc = e.getDescription();
                if (desc.length() > 25) {
                    desc = desc.substring(0, 22);
                    desc = desc + "...";
                }

                // Convert cents to currency units
                double change = e.getChange() / 100.0;
                String converted = String.format("%.02f", Math.abs(change));
                boolean isNegative = change < 0;

                String[] parts = converted.split("\\.");
                String amount = "";
                int count = 1;
                for (int ind = parts[0].length() - 1; ind >= 0; ind--) {
                    if (((count % 3) == 0) && ind > 0) {
                        amount = config.thousandSeparator() + parts[0].charAt(ind) + amount;
                    } else {
                        amount = parts[0].charAt(ind) + amount;
                    }
                    count++;
                }

                if (loc.equals("nl-NL")) {
                    amount = config.currencySymbol() + " " + amount + config.decimalSeparator() + parts[1];
                } else {
                    amount = config.currencySymbol() + amount + config.decimalSeparator() + parts[1];
                }
                
                if (isNegative && loc.equals("en-US")) {
                    amount = "(" + amount + ")";
                } else if (isNegative && loc.equals("nl-NL")) {
                    amount = "-" + amount + " ";
                } else if (loc.equals("nl-NL")) {
                    amount = " " + amount + " ";
                } else {
                    amount = amount + " ";
                }
                
                sb.append('\n');
                sb.append(String.format("%s | %-25s | %13s",
                        date,
                        desc,
                        amount));
            }

        }

        return sb.toString();
    }

    private record FormatConfig(
        String header,
        String datePattern,
        String currencySymbol,
        String decimalSeparator,
        String thousandSeparator
    ) {}
    
    private void validateInputs(String currency, String locale) {
        if (!currency.equals("USD") && !currency.equals("EUR")) {
            throw new IllegalArgumentException("Invalid currency");
        }
        if (!locale.equals("en-US") && !locale.equals("nl-NL")) {
            throw new IllegalArgumentException("Invalid locale");
        }
    }
    
    private FormatConfig getFormatConfig(String currency, String locale) {
        if (currency.equals("USD")) {
            return locale.equals("en-US") ?
                new FormatConfig(
                    "Date       | Description               | Change       ",
                    "MM/dd/yyyy", "$", ".", ","
                ) :
                new FormatConfig(
                    "Datum      | Omschrijving              | Verandering  ",
                    "dd/MM/yyyy", "$", ",", "."
                );
        }
        return locale.equals("en-US") ?
            new FormatConfig(
                "Date       | Description               | Change       ",
                "MM/dd/yyyy", "€", ".", ","
            ) :
            new FormatConfig(
                "Datum      | Omschrijving              | Verandering  ",
                "dd/MM/yyyy", "€", ",", "."
            );
    }
    
    private List<LedgerEntry> getSortedEntries(LedgerEntry[] entries) {
        List<LedgerEntry> negatives = new ArrayList<>();
        List<LedgerEntry> positives = new ArrayList<>();
        
        for (LedgerEntry entry : entries) {
            (entry.getChange() < 0 ? negatives : positives).add(entry);
        }
        
        negatives.sort(Comparator.comparing(LedgerEntry::getLocalDate));
        positives.sort(Comparator.comparing(LedgerEntry::getLocalDate));
        
        List<LedgerEntry> combined = new ArrayList<>(negatives);
        combined.addAll(positives);
        return combined;
    }

    public static class LedgerEntry {
        LocalDate localDate;
        String description;
        int change;

        public LocalDate getLocalDate() {
            return localDate;
        }

        public void setLocalDate(LocalDate localDate) {
            this.localDate = localDate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public double getChange() {
            return change;
        }

        public void setChange(double change) {
            this.change = change;
        }
    }

}
