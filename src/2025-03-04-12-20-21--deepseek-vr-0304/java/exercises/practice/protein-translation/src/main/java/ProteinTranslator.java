import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

class ProteinTranslator {
    private static final Map<String, String> codonMap = new HashMap<>();
    private static final Set<String> stopCodons = new HashSet<>();

    static {
        codonMap.put("AUG", "Methionine");

        codonMap.put("UUU", "Phenylalanine");
        codonMap.put("UUC", "Phenylalanine");

        codonMap.put("UUA", "Leucine");
        codonMap.put("UUG", "Leucine");

        codonMap.put("UCU", "Serine");
        codonMap.put("UCC", "Serine");
        codonMap.put("UCA", "Serine");
        codonMap.put("UCG", "Serine");

        codonMap.put("UAU", "Tyrosine");
        codonMap.put("UAC", "Tyrosine");

        codonMap.put("UGU", "Cysteine");
        codonMap.put("UGC", "Cysteine");

        codonMap.put("UGG", "Tryptophan");

        stopCodons.add("UAA");
        stopCodons.add("UAG");
        stopCodons.add("UGA");
    }

    List<String> translate(String rnaSequence) {
        List<String> protein = new ArrayList<>();
        for (int i = 0; i < rnaSequence.length(); i += 3) {
            if (i + 3 > rnaSequence.length()) {
                break;
            }
            String codon = rnaSequence.substring(i, i + 3);
            if (stopCodons.contains(codon)) {
                break;
            }
            String aminoAcid = codonMap.get(codon);
            if (aminoAcid != null) {
                protein.add(aminoAcid);
            }
        }
        return protein;
    }
}
