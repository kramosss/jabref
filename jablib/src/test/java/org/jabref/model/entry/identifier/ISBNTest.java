package org.jabref.model.entry.identifier;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ISBNTest {

    @ParameterizedTest
    @ValueSource(strings = {"0-123456-47-9", "0-9752298-0-X"})
    void isValidFormat10Correct(String isbn) {
        assertTrue(new ISBN(isbn).isValidFormat());
    }

    @Test
    void isValidFormat10Incorrect() {
        assertFalse(new ISBN("0-12B456-47-9").isValidFormat());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0-123456-47-9", "0-9752298-0-X", "0-9752298-0-x"})
    void isValidChecksum10Correct(String isbn) {
        assertTrue(new ISBN(isbn).isValidChecksum());
    }

    @Test
    void isValidChecksum10Incorrect() {
        assertFalse(new ISBN("0-123456-47-8").isValidChecksum());
    }

    @Test
    void isValidFormat13Correct() {
        assertTrue(new ISBN("978-1-56619-909-4").isValidFormat());
    }

    @Test
    void isValidFormat13Incorrect() {
        assertFalse(new ISBN("978-1-56619-9O9-4 ").isValidFormat());
    }

    @Test
    void isValidChecksum13Correct() {
        assertTrue(new ISBN("978-1-56619-909-4 ").isValidChecksum());
    }

    @Test
    void isValidChecksum13Incorrect() {
        assertFalse(new ISBN("978-1-56619-909-5").isValidChecksum());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0-123456-47-9", "0-9752298-0-X"})
    void isIsbn10Correct(String isbn) {
        assertTrue(new ISBN(isbn).isIsbn10());
    }

    @Test
    void isIsbn10Incorrect() {
        assertFalse(new ISBN("978-1-56619-909-4").isIsbn10());
    }

    @Test
    void isIsbn13Correct() {
        assertTrue(new ISBN("978-1-56619-909-4").isIsbn13());
    }

    @Test
    void isIsbn13Incorrect() {
        assertFalse(new ISBN("0-123456-47-9").isIsbn13());
    }

    @Test
    void toIsbn13ConvertsIsbn10() {
        assertEquals(Optional.of("9780123456472"), new ISBN("0-123456-47-9").toIsbn13().map(ISBN::asString));
    }

    @Test
    void toIsbn13ConvertsIsbn10WithCheckDigitX() {
        assertEquals(Optional.of("9780975229804"), new ISBN("0-9752298-0-X").toIsbn13().map(ISBN::asString));
    }

    @Test
    void toIsbn13ReturnsIsbn13Unchanged() {
        assertEquals(Optional.of("9781566199094"), new ISBN("978-1-56619-909-4").toIsbn13().map(ISBN::asString));
    }

    @Test
    void toIsbn13ReturnsEmptyForInvalidIsbn() {
        assertEquals(Optional.empty(), new ISBN("0-123456-47-8").toIsbn13());
    }
}
