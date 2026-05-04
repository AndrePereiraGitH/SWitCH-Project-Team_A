package TOPSECRET.persistence.repositoryjpa.jpa;

import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.persistence.repositoryjpa.datamodel.GenreDM;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository interface for {@link GenreDM} persistence entities.
 *
 * <p>Extends {@link JpaRepository} to inherit standard CRUD and pagination
 * operations keyed by {@link GenreId}. Custom query methods are declared
 * here and resolved automatically by Spring Data at runtime.</p>
 *
 * <p>This interface belongs to the persistence layer only and should never
 * be used directly by the domain or service layers.</p>
 */
public interface IGenreRepositoryJpa extends JpaRepository<GenreDM, GenreId> {

    /**
     * Finds a genre by its type name.
     *
     * <p>Spring derives the query automatically from the method name,
     * matching against the {@code genre_type} column.</p>
     *
     * @param genreType the genre name to search for
     * @return the matching {@link GenreDM}, or {@code null} if not found
     */
    Optional<GenreDM> findByGenreType(String genreType);
}