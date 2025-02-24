import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

class ProteinTranslator {
    private static final Map<String, String> CODON_MAP = Map.ofEntries(
        Map.entry("AUG", "Methionine"),
        Map.entry("UUU", "Phenylalanine"),
        Map.entry("UUC", "Phenylalanine"),
        Map.entry("UUA", "Leucine"),
        Map.entry("UUG", "Leucine"),
        Map.entry("UCU", "Serine"),
        Map.entry("UCC", "Serine"),
        Map.entry("UCA", "Serine"),
        Map.entry("UCG", "Serine"),
        Map.entry("UAU", "Tyrosine"),
        Map.entry("UAC", "Tyrosine"),
        Map.entry("UGU", "Cysteine"),
        Map.entry("UGC", "Cysteine"),
        Map.entry("UGG", "Tryptophan"),
        Map.entry("UAA", "STOP"),
        Map.entry("UAG", "STOP"),
        Map.entry("UGA", "STOP")
    );

    List<String> translate(String rnaSequence) {
        List<String> proteins = new ArrayList<>();
        
        for (int i = 0; i < rnaSequence.length(); i += 3) {
            if (i + 3 > rnaSequence.length()) {
                break;
            }
            String codon = rnaSequence.substring(i, i + 3);
            String aminoAcid = CODON_MAP.get(codon);
            
            if (aminoAcid == null) {
                continue;
            }
            
            if (aminoAcid.equals("STOP")) {
                break;
            }
            
            proteins.add(aminoAcid);
        }
        
        return proteins;
    }
}
