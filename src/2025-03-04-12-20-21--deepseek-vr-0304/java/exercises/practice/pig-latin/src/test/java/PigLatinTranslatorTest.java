import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PigLatinTranslatorTest {

    private PigLatinTranslator pigLatinTranslator;

    @BeforeEach
    public void setup() {
        pigLatinTranslator = new PigLatinTranslator();
    }

    @Test
    public void testWordBeginningWithA() {
        assertThat(pigLatinTranslator.translate("apple")).isEqualTo("appleay");
    }

        @Test
    public void testWordBeginningWithE() {
        assertThat(pigLatinTranslator.translate("ear")).isEqualTo("earay");
    }

        @Test
    public void testWordBeginningWithI() {
        assertThat(pigLatinTranslator.translate("igloo")).isEqualTo("iglooay");
    }

        @Test
    public void testWordBeginningWithO() {
        assertThat(pigLatinTranslator.translate("object")).isEqualTo("objectay");
    }

        @Test
    public void testWordBeginningWithU() {
        assertThat(pigLatinTranslator.translate("under")).isEqualTo("underay");
    }

        @Test
    public void testWordBeginningWithVowelAndFollowedByQu() {
        assertThat(pigLatinTranslator.translate("equal")).isEqualTo("equalay");
    }

        @Test
    public void testWordBeginningWithP() {
        assertThat(pigLatinTranslator.translate("pig")).isEqualTo("igpay");
    }

        @Test
    public void testWordBeginningWithK() {
        assertThat(pigLatinTranslator.translate("koala")).isEqualTo("oalakay");
    }

        @Test
    public void testWordBeginningWithX() {
        assertThat(pigLatinTranslator.translate("xenon")).isEqualTo("enonxay");
    }

        @Test
    public void testWordBeginningWithQWithoutAFollowingU() {
        assertThat(pigLatinTranslator.translate("qat")).isEqualTo("atqay");
    }

        @Test
    public void testChTreatedLikeAConsonantAtTheBeginningOfAWord() {
        assertThat(pigLatinTranslator.translate("chair")).isEqualTo("airchay");
    }

        @Test
    public void testQuTreatedLikeAConsonantAtTheBeginningOfAWord() {
        assertThat(pigLatinTranslator.translate("queen")).isEqualTo("eenquay");
    }

        @Test
    public void testQuAndAPrecedingConsonantTreatedLikeAConsonantAtTheBeginningOfAWord() {
        assertThat(pigLatinTranslator.translate("square")).isEqualTo("aresquay");
    }

        @Test
    public void testThTreatedLikeAConsonantAtTheBeginningOfAWord() {
        assertThat(pigLatinTranslator.translate("therapy")).isEqualTo("erapythay");
    }

        @Test
    public void testThrTreatedLikeAConsonantAtTheBeginningOfAWord() {
        assertThat(pigLatinTranslator.translate("thrush")).isEqualTo("ushthray");
    }

        @Test
    public void testSchTreatedLikeAConsonantAtTheBeginningOfAWord() {
        assertThat(pigLatinTranslator.translate("school")).isEqualTo("oolschay");
    }

        @Test
    public void testYtTreatedLikeAVowelAtTheBeginningOfAWord() {
        assertThat(pigLatinTranslator.translate("yttria")).isEqualTo("yttriaay");
    }

        @Test
    public void testXrTreatedLikeAVowelAtTheBeginningOfAWord() {
        assertThat(pigLatinTranslator.translate("xray")).isEqualTo("xrayay");
    }

        @Test
    public void testYTreatedLikeAConsonantAtTheBeginningOfAWord() {
        assertThat(pigLatinTranslator.translate("yellow")).isEqualTo("ellowyay");
    }

        @Test
    public void testYTreatedLikeAVowelAtTheEndOfAConsonantCluster() {
        assertThat(pigLatinTranslator.translate("rhythm")).isEqualTo("ythmrhay");
    }

        @Test
    public void testYAsSecondLetterInTwoLetterWord() {
        assertThat(pigLatinTranslator.translate("my")).isEqualTo("ymay");
    }

        @Test
    public void testAWholePhrase() {
        assertThat(pigLatinTranslator.translate("quick fast run")).isEqualTo("ickquay astfay unray");
    }
}
