import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ProteinTranslatorTest {

    private ProteinTranslator proteinTranslator;

    @BeforeEach
    public void setUp() {
        proteinTranslator = new ProteinTranslator();
    }

    @Test
    public void testEmptyRnaSequenceResultInNoproteins() {
        assertThat(proteinTranslator.translate("")).isEmpty();
    }

        @Test
    public void testMethionineRnaSequence() {
        assertThat(proteinTranslator.translate("AUG")).containsExactly("Methionine");
    }

        @Test
    public void testPhenylalanineRnaSequence1() {
        assertThat(proteinTranslator.translate("UUU")).containsExactly("Phenylalanine");
    }

        @Test
    public void testPhenylalanineRnaSequence2() {
        assertThat(proteinTranslator.translate("UUC")).containsExactly("Phenylalanine");
    }

        @Test
    public void testLeucineRnaSequence1() {
        assertThat(proteinTranslator.translate("UUA")).containsExactly("Leucine");
    }

        @Test
    public void testLeucineRnaSequence2() {
        assertThat(proteinTranslator.translate("UUG")).containsExactly("Leucine");
    }

        @Test
    public void testSerineRnaSequence1() {
        assertThat(proteinTranslator.translate("UCU")).containsExactly("Serine");
    }

        @Test
    public void testSerineRnaSequence2() {
        assertThat(proteinTranslator.translate("UCC")).containsExactly("Serine");
    }

        @Test
    public void testSerineRnaSequence3() {
        assertThat(proteinTranslator.translate("UCA")).containsExactly("Serine");
    }

        @Test
    public void testSerineRnaSequence4() {
        assertThat(proteinTranslator.translate("UCG")).containsExactly("Serine");
    }

        @Test
    public void testTyrosineRnaSequence1() {
        assertThat(proteinTranslator.translate("UAU")).containsExactly("Tyrosine");
    }

        @Test
    public void testTyrosineRnaSequence2() {
        assertThat(proteinTranslator.translate("UAC")).containsExactly("Tyrosine");
    }

        @Test
    public void testCysteineRnaSequence1() {
        assertThat(proteinTranslator.translate("UGU")).containsExactly("Cysteine");
    }

        @Test
    public void testCysteineRnaSequence2() {
        assertThat(proteinTranslator.translate("UGC")).containsExactly("Cysteine");
    }

        @Test
    public void testTryptophanRnaSequence1() {
        assertThat(proteinTranslator.translate("UGG")).containsExactly("Tryptophan");
    }

        @Test
    public void testStopRnaSequence1() {
        assertThat(proteinTranslator.translate("UAA")).isEmpty();
    }

        @Test
    public void testStopRnaSequence2() {
        assertThat(proteinTranslator.translate("UAG")).isEmpty();
    }

        @Test
    public void testStopRnaSequence3() {
        assertThat(proteinTranslator.translate("UGA")).isEmpty();
    }

        @Test
    public void testSequenceOfTwoProteinCodonsTranslatesIntoProteins() {
        assertThat(proteinTranslator.translate("UUUUUU")).containsExactly("Phenylalanine", "Phenylalanine");
    }

        @Test
    public void testSequenceOfTwoDifferentProteinCodonsTranslatesIntoProteins() {
        assertThat(proteinTranslator.translate("UUAUUG")).containsExactly("Leucine", "Leucine");
    }

        @Test
    public void testTranslationOfRnaToProteinList() {
        assertThat(proteinTranslator.translate("AUGUUUUGG"))
                .containsExactly("Methionine", "Phenylalanine", "Tryptophan");
    }

        @Test
    public void testTranslationStopsIfStopCodonAtBeginning() {
        assertThat(proteinTranslator.translate("UAGUGG")).isEmpty();
    }

        @Test
    public void testTranslationStopsIfStopCodonAtEnd1() {
        assertThat(proteinTranslator.translate("UGGUAG")).containsExactly("Tryptophan");
    }

        @Test
    public void testTranslationStopsIfStopCodonAtEnd2() {
        assertThat(proteinTranslator.translate("AUGUUUUAA")).containsExactly("Methionine", "Phenylalanine");
    }

        @Test
    public void testTranslationStopsIfStopCodonInMiddle1() {
        assertThat(proteinTranslator.translate("UGGUAGUGG")).containsExactly("Tryptophan");
    }

        @Test
    public void testTranslationStopsIfStopCodonInMiddle2() {
        assertThat(proteinTranslator.translate("UGGUGUUAUUAAUGGUUU"))
                .containsExactly("Tryptophan", "Cysteine", "Tyrosine");
    }

        @Test
    public void testSequenceOfTwoNonStopCodonsDoNotTranslateToAStopCodon() {
        assertThat(proteinTranslator.translate("AUGAUG"))
                .containsExactly("Methionine", "Methionine");
    }

        @Test
    public void testNonExistingCodonCantTranslate() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> proteinTranslator.translate("AAA"))
                .withMessage("Invalid codon");
    }

        @Test
    public void testUnknownAminoAcidsNotPartOfACodonCantTranslate() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> proteinTranslator.translate("XYZ"))
                .withMessage("Invalid codon");
    }

        @Test
    public void testIncompleteRnaSequenceCantTranslate() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> proteinTranslator.translate("AUGU"))
                .withMessage("Invalid codon");
    }

        @Test
    public void testIncompleteRnaSequenceCanTranslateIfValidUntilAStopCodon() {
        assertThat(proteinTranslator.translate("UUCUUCUAAUGGU")).containsExactly("Phenylalanine", "Phenylalanine");
    }
}
