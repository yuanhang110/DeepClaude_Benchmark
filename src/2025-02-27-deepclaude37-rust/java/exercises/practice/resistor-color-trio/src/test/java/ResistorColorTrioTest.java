import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import static org.assertj.core.api.Assertions.assertThat;

public class ResistorColorTrioTest {
    private ResistorColorTrio resistorColorTrio;

    @BeforeEach
    public void setup() {
        resistorColorTrio = new ResistorColorTrio();
    }

    @Test
    public void testOrangeAndOrangeAndBlack() {
        assertThat(
            resistorColorTrio.label(new String[]{"orange", "orange", "black"})
        ).isEqualTo("33 ohms");
    }
    
        @Test
    public void testBlueAndGreyAndBrown() {
        assertThat(
            resistorColorTrio.label(new String[]{"blue", "grey", "brown"})
        ).isEqualTo("680 ohms");
    }
    
        @Test
    public void testRedAndBlackAndRed() {
        assertThat(
            resistorColorTrio.label(new String[]{"red", "black", "red"})
        ).isEqualTo("2 kiloohms");
    }
    
        @Test
    public void testGreenAndBrownAndOrange() {
        assertThat(
            resistorColorTrio.label(new String[]{"green", "brown", "orange"})
        ).isEqualTo("51 kiloohms");
    }
    
        @Test
    public void testYellowAndVioletAndYellow() {
        assertThat(
            resistorColorTrio.label(new String[]{"yellow", "violet", "yellow"})
        ).isEqualTo("470 kiloohms");
    }
    
        @Test
    public void testBlueAndVioletAndBlue() {
        assertThat(
            resistorColorTrio.label(new String[]{"blue", "violet", "blue"})
        ).isEqualTo("67 megaohms");
    }
    
        @Test
    public void testBlackAndBlackAndBlack() {
        assertThat(
            resistorColorTrio.label(new String[]{"black", "black", "black"})
        ).isEqualTo("0 ohms");
    }

        @Test
    public void testWhiteAndWhiteAndWhite() {
        assertThat(
            resistorColorTrio.label(new String[]{"white", "white", "white"})
        ).isEqualTo("99 gigaohms");
    }
    
        @Test
    public void testFirstTwoColorsMakeAnInvalidOctalNumber() {
        assertThat(
            resistorColorTrio.label(new String[]{"black", "grey", "black"})
        ).isEqualTo("8 ohms");
    }

        @Test
    public void testIgnoreExtraColors() {
        assertThat(
            resistorColorTrio.label(new String[]{"blue", "green", "yellow", "orange"})
        ).isEqualTo("650 kiloohms");
    }
}
