package org.jabref.model.entry.identifier;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jabref.model.entry.field.Field;
import org.jabref.model.entry.field.StandardField;

import org.jspecify.annotations.NonNull;

public class ISBN implements Identifier {

    private static final Pattern ISBN_PATTERN = Pattern.compile("^(\\d{9}[\\dxX]|\\d{13})$");

    private final String isbnString;

    public ISBN(@NonNull String isbnString) {
        this.isbnString = isbnString.trim().replace("-", "");
    }

    public static Optional<ISBN> parse(String input) {
        ISBN isbn = new ISBN(input);
        if (isbn.isValid()) {
            return Optional.of(isbn);
        } else {
            return Optional.empty();
        }
    }

    public boolean isValidFormat() {
        Matcher isbnMatcher = ISBN_PATTERN.matcher(isbnString);
        return isbnMatcher.matches();
    }

    public boolean isValidChecksum() {
        boolean valid;
        if (isbnString.length() == 10) {
            valid = isbn10check();
        } else {
            // length is either 10 or 13 based on regexp so will be 13 here
            valid = isbn13check();
        }
        return valid;
    }

    public boolean isIsbn10() {
        return isbn10check();
    }

    public boolean isIsbn13() {
        return isbn13check();
    }

    // Check that the control digit is correct, see e.g. https://en.wikipedia.org/wiki/International_Standard_Book_Number#Check_digits
    private boolean isbn10check() {
        if (isbnString.length() != 10) {
            return false;
        }

        int sum = 0;
        for (int pos = 0; pos <= 8; pos++) {
            sum += (isbnString.charAt(pos) - '0') * (10 - pos);
        }
        char control = isbnString.charAt(9);
        if ((control == 'x') || (control == 'X')) {
            control = '9' + 1;
        }
        sum += control - '0';
        return (sum % 11) == 0;
    }

    // Check that the control digit is correct, see e.g. https://en.wikipedia.org/wiki/International_Standard_Book_Number#Check_digits
    private boolean isbn13check() {
        if (isbnString.length() != 13) {
            return false;
        }

        int checkDigit = computeIsbn13CheckDigit(isbnString.substring(0, 12));
        return checkDigit == (isbnString.charAt(12) - '0');
    }

    /// Computes the ISBN-13 check digit for the given 12-digit prefix using the standard
    /// alternating 1/3 weighting.
    private static int computeIsbn13CheckDigit(String twelveDigits) {
        int sum = 0;
        for (int pos = 0; pos < 12; pos++) {
            sum += (twelveDigits.charAt(pos) - '0') * ((pos % 2) == 0 ? 1 : 3);
        }
        return (10 - (sum % 10)) % 10;
    }

    /// Returns the ISBN-13 representation of this ISBN.
    ///
    /// A valid ISBN-13 is returned unchanged. A valid ISBN-10 is converted by dropping its
    /// check digit, prefixing the remaining 9 digits with "978", and computing a fresh
    /// ISBN-13 check digit. An invalid ISBN yields an empty result.
    public Optional<ISBN> toIsbn13() {
        if (!isValid()) {
            return Optional.empty();
        }
        if (isIsbn13()) {
            return Optional.of(this);
        }

        String core = "978" + isbnString.substring(0, 9);
        int checkDigit = computeIsbn13CheckDigit(core);
        return Optional.of(new ISBN(core + checkDigit));
    }

    public boolean isValid() {
        return isValidFormat() && isValidChecksum();
    }

    @Override
    public Field getDefaultField() {
        return StandardField.ISBN;
    }

    @Override
    public String asString() {
        return isbnString;
    }

    @Override
    public Optional<URI> getExternalURI() {
        try {
            return Optional.of(new URI("https://www.worldcat.org/isbn/" + isbnString));
        } catch (URISyntaxException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        ISBN other = (ISBN) o;
        return isbnString.equalsIgnoreCase(other.isbnString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbnString.toLowerCase(Locale.ENGLISH));
    }
}
