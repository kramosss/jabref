package org.jabref.logic.formatter.bibtexfields;

import org.jabref.logic.formatter.Formatter;
import org.jabref.logic.l10n.Localization;
import org.jabref.model.entry.identifier.ISBN;

import org.jspecify.annotations.NonNull;

public class NormalizeIsbnFormatter extends Formatter {

    @Override
    public String getName() {
        return Localization.lang("Normalize ISBN");
    }

    @Override
    public String getKey() {
        return "normalize_isbn";
    }

    @Override
    public String format(@NonNull String value) {
        if (value.isBlank()) {
            return value;
        }

        return new ISBN(value).toIsbn13()
                              .map(ISBN::asString)
                              .orElse(value);
    }

    @Override
    public String getDescription() {
        return Localization.lang("Normalize ISBN by converting ISBN-10 to ISBN-13.");
    }

    @Override
    public String getExampleInput() {
        return "0-123456-47-9";
    }
}
