package TOPSECRET.domain.BibliographicalData;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.Genre;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

/**
 * Repository interface for managing BibliographicalData entities.
 * Provides methods to create, store, and retrieve bibliographical records.
 */

public interface IBibliographicalDataRepository {


    BibliographicalData createAndAdd(String title, List<Author> authors, Locale originalLanguage, LocalDate publicationDate,
                                     List<Genre> genres, String chunksOfText) throws InstanceAlreadyExistsException;


    List<BibliographicalData> getAllPublicationsBD();

}
