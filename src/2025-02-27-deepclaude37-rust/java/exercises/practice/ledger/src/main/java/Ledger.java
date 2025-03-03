import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Ledger {
    public LedgerEntry createLedgerEntry(String d, String desc, int c) {
        LedgerEntry le = new LedgerEntry();
        le.setChange(c);
        le.setDescription(desc);
        le.setLocalDate(LocalDate.parse(d));
        return le;
    }

    public String format(String cur, String loc, LedgerEntry[] entries) {
        // Validate input
        if (!cur.equals("USD") && !cur.equals("EUR")) {
            throw new IllegalArgumentException("Invalid currency");
        } else if (!loc.equals("en-US") && !loc.equals("nl-NL")) {
            throw new IllegalArgumentException("Invalid locale");
        }
        
        // Configure format settings based on currency and locale
        String curSymb = cur.equals("USD") ? "$" : "€";
        String datPat = loc.equals("en-US") ? "MM/dd/yyyy" : "dd/MM/yyyy";
        String decSep = loc.equals("en-US") ? "." : ",";
        String thSep = loc.equals("en-US") ? "," : ".";
        String header = loc.equals("en-US") ? 
            "Date       | Description               | Change       " :
            "Datum      | Omschrijving              | Verandering  ";
        
        String s = header;

        if (entries.length > 0) {
            // Sort entries: negative entries first, then positive, both sorted by date
            List<LedgerEntry> neg = new ArrayList<>();
            List<LedgerEntry> pos = new ArrayList<>();
            for (int i = 0; i < entries.length; i++) {
                LedgerEntry e = entries[i];
                if (e.getChange() >= 0) {
                    pos.add(e);
                } else {
                    neg.add(e);
                }
            }

            neg.sort((o1, o2) -> o1.getLocalDate().compareTo(o2.getLocalDate()));
            pos.sort((o1, o2) -> o1.getLocalDate().compareTo(o2.getLocalDate()));

            List<LedgerEntry> all = new ArrayList<>();
            all.addAll(neg);
            all.addAll(pos);

            for (int i = 0; i < all.size(); i++) {
                LedgerEntry e = all.get(i);

                // Format date according to locale
                String date = e.getLocalDate().format(DateTimeFormatter.ofPattern(datPat));

                // Format description (truncate if longer than 25 chars)
                String desc = e.getDescription();
                if (desc.length() > 25) {
                    desc = desc.substring(0, 22) + "...";
                }

                // Convert cents to dollars/euros with proper decimal formatting
                String converted = null;
                if (e.getChange() < 0) {
                    converted = String.format("%.02f", (e.getChange() / 100.0) * -1);
                } else {
                    converted = String.format("%.02f", e.getChange() / 100.0);
                }

                // Format with thousands separators
                String[] parts = converted.split("\\.");
                StringBuilder amountBuilder = new StringBuilder();
                int count = 1;
                for (int ind = parts[0].length() - 1; ind >= 0; ind--) {
                    if (((count % 3) == 0) && ind > 0) {
                        amountBuilder.insert(0, parts[0].charAt(ind)).insert(0, thSep);
                    } else {
                        amountBuilder.insert(0, parts[0].charAt(ind));
                    }
                    count++;
                }
                
                String amount;
                
                // Format according to locale and sign conventions
                if (loc.equals("nl-NL")) {
                    amount = curSymb + " " + amountBuilder + decSep + parts[1];
                    if (e.getChange() < 0) {
                        amount = curSymb + " -" + amount.replace(curSymb, "").trim() + " ";
                    } else {
                        amount = " " + amount + " ";
                    }
                } else { // en-US
                    amount = curSymb + amountBuilder + decSep + parts[1];
                    if (e.getChange() < 0) {
                        amount = "(" + amount + ")";
                    } else {
                        amount = amount + " ";
                    }
                }
                
                // Append formatted entry to the result
                s = s + "\n" + String.format("%s | %-25s | %13s",
                        date,
                        desc,
                        amount);
            }
        }

        return s;
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

        public int getChange() {
            return change;
        }

        public void setChange(int change) {
            this.change = change;
        }
    }

}
