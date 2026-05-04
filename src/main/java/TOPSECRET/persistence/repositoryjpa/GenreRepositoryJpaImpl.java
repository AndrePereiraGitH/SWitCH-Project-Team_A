package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.GenreFactory;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.persistence.repositoryjpa.assembler.GenreAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.GenreDM;
import TOPSECRET.persistence.repositoryjpa.jpa.IGenreRepositoryJpa;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * JPA-backed implementation of {@link IGenreRepository}.
 *
 * <p>Delegates all persistence operations to {@link IGenreRepositoryJpa} and uses
 * {@link GenreAssembler} to convert between the domain model ({@link Genre}) and
 * the persistence data model ({@link GenreDM}).</p>
 */
@Repository
public class GenreRepositoryJpaImpl implements IGenreRepository {

    private final IGenreRepositoryJpa genreRepositoryJpa;
    private final GenreAssembler assembler;

    public GenreRepositoryJpaImpl(IGenreRepositoryJpa genreRepositoryJpa, GenreAssembler assembler) {
        this.genreRepositoryJpa = Objects.requireNonNull(genreRepositoryJpa, "genreRepositoryJpa must not be null");
        this.assembler = Objects.requireNonNull(assembler, "assembler must not be null");
    }

    // =========================================================================
    // Persistence
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Genre save(Genre genre) {
        Objects.requireNonNull(genre, "Genre must not be null");
        GenreDM saved = genreRepositoryJpa.save(assembler.toDM(genre));
        return assembler.toDomain(saved);
    }

    /**
     * {@inheritDoc}
     */


    // =========================================================================
    // Lookup
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public Genre findByTitle(String title) {
        Objects.requireNonNull(title, "Title must not be null");
        return genreRepositoryJpa.findByGenreType(title)
                .map(assembler::toDomain)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Genre with title '" + title + "' not found"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Genre> getAllGenres() {
        return genreRepositoryJpa.findAll()
                .stream()
                .map(assembler::toDomain)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Genre> findGenreById(GenreId genreId) {
        Objects.requireNonNull(genreId, "GenreId must not be null");
        return genreRepositoryJpa.findById(genreId)
                .map(assembler::toDomain);
    }
}