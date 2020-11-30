package com.example.vivlio;

import com.example.vivlio.Controllers.ValidateISBN;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidateISBNTest {

    ValidateISBN validator = new ValidateISBN();

    @Test
    public void verifyIsbn13() {
        String validIsbn = "9786656883617";
        String invalidLength = "19876";
        String invalidCheckDig = "2897741258639";

        assertTrue(validator.verify(validIsbn));
        assertFalse(validator.verify(invalidLength));
        assertFalse(validator.verify(invalidCheckDig));
    }

    @Test
    public void verifyIsbn10() {
        String validIsbn1 = "039480001X";
        String validIsbn2 = "1569319014";
        String invalidLength1 = "654555X";
        String invalidLength2 = "6546587";
        String invalidCheckDig1 = "039895561X";
        String invalidCheckDig2 = "5687802079";

        assertTrue(validator.verify(validIsbn1));
        assertTrue(validator.verify(validIsbn2));
        assertFalse(validator.verify(invalidLength1));
        assertFalse(validator.verify(invalidLength2));
        assertFalse(validator.verify(invalidCheckDig1));
        assertFalse(validator.verify(invalidCheckDig2));
    }
}
