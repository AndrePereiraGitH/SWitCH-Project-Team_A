package TOPSECRET.persistence.repositoryjpa.jpa;

import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.persistence.repositoryjpa.datamodel.BibliographicalDataDM;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for managing {@link BibliographicalDataDM} entities.
 * <p>
 * Provides basic CRUD operations through {@link JpaRepository} and
 * additional query methods for retrieving bibliographical data based on
 * specific attributes such as title, genres, and authors.
 * <p>
 * This repository operates on the persistence model ({@link BibliographicalDataDM})
 * and is intended to be used internally by higher-level repository implementations
 * (e.g., domain repositories) that handle conversion between persistence and domain models.
 */
public interface IBibliographicalDataRepositoryJpa extends JpaRepository<BibliographicalDataDM, BibliographicalDataID> {

    /**
     * Finds a bibliographical data record by its title, ignoring case differences.
     *
     * @param title the title to search for (must not be null)
     * @return an {@link Optional} containing the matching {@link BibliographicalDataDM}
     *         if found, or empty if no match exists
     */
    Optional<BibliographicalDataDM> findByTitleIgnoreCase(String title);

    /**
     * Retrieves all bibliographical data records associated with a given genre.
     *
     * @param genreId the genre identifier to search for (must not be null)
     * @return a list of {@link BibliographicalDataDM} entities that contain the given genre
     */
    List<BibliographicalDataDM> findByGenreIdsContaining(GenreID genreId);

    /**
     * Retrieves all bibliographical data records associated with a given author.
     *
     * @param authorId the author identifier to search for (must not be null)
     * @return a list of {@link BibliographicalDataDM} entities that contain the given author
     */
    List<BibliographicalDataDM> findByAuthorIdsContaining(AuthorId authorId);
}
