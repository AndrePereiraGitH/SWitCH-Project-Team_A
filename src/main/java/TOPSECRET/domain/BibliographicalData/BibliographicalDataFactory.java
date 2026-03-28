package TOPSECRET.domain.BibliographicalData;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Genre;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

/**
 * Factory responsible for creating {@link BibliographicalData} instances.
 * <p>
 * This class centralizes the creation logic of {@code BibliographicalData}
 * objects, ensuring that all instances are created in a consistent and
 * controlled manner.
 * <p>
 * The factory delegates validation of mandatory fields to the
 * {@code BibliographicalData} constructor.
 */
public class BibliographicalDataFactory {


    public BibliographicalData create(String title, List<Author> authors, Locale originalLanguage, LocalDate publicationDate,
                                      List<Genre> genres, String chunksOfText){

        return new BibliographicalData(title, authors, originalLanguage, publicationDate, genres, chunksOfText);
    }

}

