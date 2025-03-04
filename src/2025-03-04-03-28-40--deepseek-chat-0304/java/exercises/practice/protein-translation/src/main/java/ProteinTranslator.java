import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class ProteinTranslator {
    
    private static final Map<String, String> CODON_PROTEIN_MAP = new HashMap<>();
    
    static {
        CODON_PROTEIN_MAP.put("AUG", "Methionine");
        CODON_PROTEIN_MAP.put("UUU", "Phenylalanine");
        CODON_PROTEIN_MAP.put("UUC", "Phenylalanine");
        CODON_PROTEIN_MAP.put("UUA", "Leucine");
        CODON_PROTEIN_MAP.put("UUG", "Leucine");
        CODON_PROTEIN_MAP.put("UCU", "Serine");
        CODON_PROTEIN_MAP.put("UCC", "Serine");
        CODON_PROTEIN_MAP.put("UCA", "Serine");
        CODON_PROTEIN_MAP.put("UCG", "Serine");
        CODON_PROTEIN_MAP.put("UAU", "Tyrosine");
        CODON_PROTEIN_MAP.put("UAC", "Tyrosine");
        CODON_PROTEIN_MAP.put("UGU", "Cysteine");
        CODON_PROTEIN_MAP.put("UGC", "Cysteine");
        CODON_PROTEIN_MAP.put("UGG", "Tryptophan");
        CODON_PROTEIN_MAP.put("UAA", "STOP");
        CODON_PROTEIN_MAP.put("UAG", "STOP");
        CODON_PROTEIN_MAP.put("UGA", "STOP");
    }

    List<String> translate(String rnaSequence) {
        if (rnaSequence == null) {
            throw new IllegalArgumentException("RNA sequence cannot be null");
        }
        
        List<String> proteins = new ArrayList<>();
        
        // Split RNA into codons (3-letter sequences)
        for (int i = 0; i < rnaSequence.length(); i += 3) {
            // Ensure we don't go out of bounds
            int endIndex = Math.min(i + 3, rnaSequence.length());
            String codon = rnaSequence.substring(i, endIndex);
            
            // Validate codon length
            if (codon.length() != 3) {
                break; // Incomplete codon at end of sequence
            }
            
            // Get corresponding protein or STOP
            String protein = CODON_PROTEIN_MAP.get(codon);
            
            // Stop translation if we hit a STOP codon or invalid codon
            if (protein == null) {
                throw new IllegalArgumentException("Invalid codon: " + codon);
            }
            if ("STOP".equals(protein)) {
                break;
            }
            
            proteins.add(protein);
        }
        
        return proteins;
    }
}
