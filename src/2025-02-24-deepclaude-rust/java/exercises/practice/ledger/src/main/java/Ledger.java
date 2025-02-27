import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Ledger {
    private static String getHeader(String loc) {
        return loc.equals("en-US") 
            ? "Date       | Description               | Change       "
            : "Datum      | Omschrijving              | Verandering  ";
    }

    private static String getCurrencySymbol(String cur) {
        return cur.equals("USD") ? "$" : "€";
    }

    private static String getDatePattern(String loc) {
        return loc.equals("en-US") ? "MM/dd/yyyy" : "dd/MM/yyyy";
    }

    private static String[] getSeparators(String loc) {
        return loc.equals("en-US") 
            ? new String[]{".", ","} // decimal, thousand
            : new String[]{",", "."};
    }

    public LedgerEntry createLedgerEntry(String d, String desc, int c) {
        LedgerEntry le = new LedgerEntry();
        le.setChange(c);
        le.setDescription(desc);
        le.setLocalDate(LocalDate.parse(d));
        return le;
    }

    public String format(String cur, String loc, LedgerEntry[] entries) {
        String s = null;
        String header = null;
        String curSymb = null;
        String datPat = null;
        String decSep = null;
        String thSep = null;
        if (!cur.equals("USD") && !cur.equals("EUR")) {
            throw new IllegalArgumentException("Invalid currency");
        } else if (!loc.equals("en-US") && !loc.equals("nl-NL")) {
            throw new IllegalArgumentException("Invalid locale");
        }
        
        curSymb = getCurrencySymbol(cur);
        datPat = getDatePattern(loc);
        String[] separators = getSeparators(loc);
        decSep = separators[0];
        thSep = separators[1];
        header = getHeader(loc);

        s = header;

        if (entries.length > 0) {
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

                String date = e.getLocalDate().format(DateTimeFormatter.ofPattern(datPat));

                String desc = e.getDescription();
                if (desc.length() > 25) {
                    desc = desc.substring(0, 22);
                    desc = desc + "...";
                }

                String converted = null;
                if (e.getChange() < 0) {
                    converted = String.format("%.02f", (e.getChange() / 100) * -1);
                } else {
                    converted = String.format("%.02f", e.getChange() / 100);
                }

                String[] parts = converted.split("\\.");
                String amount = "";
                int count = 1;
                for (int ind = parts[0].length() - 1; ind >= 0; ind--) {
                    if (((count % 3) == 0) && ind > 0) {
                        amount = thSep + parts[0].charAt(ind) + amount;
                    } else {
                        amount = parts[0].charAt(ind) + amount;
                    }
                    count++;
                }

                if (loc.equals("nl-NL")) {
                    amount = curSymb + " " + amount + decSep + parts[1];
                } else {
                    amount = curSymb + amount + decSep + parts[1];
                }
                

                if (e.getChange() < 0 && loc.equals("en-US")) {
                    amount = "(" + amount + ")";
                } else if (e.getChange() < 0 && loc.equals("nl-NL")) {
                    amount = curSymb + " -" + amount.replace(curSymb, "").trim() + " ";
                } else if (loc.equals("nl-NL")) {
                    amount = " " + amount + " ";
                } else {
                    amount = amount + " ";
                }
                
                s = s + "\n";
                s = s + String.format("%s | %-25s | %13s",
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
        double change;

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
