package org.jabref.logic.formatter.bibtexfields;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NormalizeIsbnFormatterTest {

    private NormalizeIsbnFormatter formatter;

    @BeforeEach
    void setUp() {
        formatter = new NormalizeIsbnFormatter();
    }

    @Test
    void formatConvertsIsbn10ToIsbn13() {
        assertEquals("9780123456472", formatter.format("0-123456-47-9"));
    }

    @Test
    void formatConvertsIsbn10WithCheckDigitX() {
        assertEquals("9780975229804", formatter.format("0-9752298-0-X"));
    }

    @Test
    void formatReturnsCanonicalIsbn13Unchanged() {
        assertEquals("9781566199094", formatter.format("978-1-56619-909-4"));
    }

    @Test
    void formatLeavesInvalidIsbnUntouched() {
        assertEquals("0-123456-47-8", formatter.format("0-123456-47-8"));
    }

    @Test
    void formatLeavesBlankValueUntouched() {
        assertEquals("   ", formatter.format("   "));
    }

    @Test
    void formatExampleInputProducesValidIsbn13() {
        assertEquals("9780123456472", formatter.format(formatter.getExampleInput()));
    }
}

