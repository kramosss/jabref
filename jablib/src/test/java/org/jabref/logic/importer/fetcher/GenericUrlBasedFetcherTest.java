package org.jabref.logic.importer.fetcher;

import java.util.List;

import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.field.StandardField;
import org.jabref.model.entry.types.StandardEntryType;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GenericUrlBasedFetcherTest {

    private final GenericUrlBasedFetcher fetcher = new GenericUrlBasedFetcher();

    @Test
    void getNameReturnsUrlFetcher() {
        assertEquals("URL Fetcher", fetcher.getName());
    }

    @Test
    void performSearchCreatesMiscEntryWithUrl() {
        String url = "https://example.com/some-paper";
        BibEntry expected = new BibEntry(StandardEntryType.Misc)
                .withField(StandardField.URL, url);

        assertEquals(List.of(expected), fetcher.performSearch(url));
    }

    @Test
    void performSearchReturnsEmptyListForBlankUrl() {
        assertEquals(List.of(), fetcher.performSearch("   "));
    }
}

