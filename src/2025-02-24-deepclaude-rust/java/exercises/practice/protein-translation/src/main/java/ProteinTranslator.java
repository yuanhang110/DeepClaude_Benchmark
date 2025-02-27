import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

class ProteinTranslator {

    private static final HashMap<String, String> codonToProtein = new HashMap<>();

    static {
        codonToProtein.put("AUG", "Methionine");
        codonToProtein.put("UUU", "Phenylalanine");
        codonToProtein.put("UUC", "Phenylalanine");
        codonToProtein.put("UUA", "Leucine");
        codonToProtein.put("UUG", "Leucine");
        codonToProtein.put("UCU", "Serine");
        codonToProtein.put("UCC", "Serine");
        codonToProtein.put("UCA", "Serine");
        codonToProtein.put("UCG", "Serine");
        codonToProtein.put("UAU", "Tyrosine");
        codonToProtein.put("UAC", "Tyrosine");
        codonToProtein.put("UGU", "Cysteine");
        codonToProtein.put("UGC", "Cysteine");
        codonToProtein.put("UGG", "Tryptophan");
        codonToProtein.put("UAA", "STOP");
        codonToProtein.put("UAG", "STOP");
        codonToProtein.put("UGA", "STOP");
    }

    List<String> translate(String rnaSequence) {
        List<String> proteins = new ArrayList<>();
        for (int i = 0; i + 3 <= rnaSequence.length(); i += 3) {
            String codon = rnaSequence.substring(i, i + 3);
            String protein = codonToProtein.get(codon);
            if (protein == null) {
                continue;
            }
            if (protein.equals("STOP")) {
                break;
            }
            proteins.add(protein);
        }
        return proteins;
    }
}
