package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.ValueObjects.GenreID;
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
        this.genreRepositoryJpa = genreRepositoryJpa;
        this.assembler = assembler;
    }

    // =========================================================================
    // Persistence
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Genre createAndAdd(String genreType) {
        Objects.requireNonNull(genreType);
        if (genreRepositoryJpa.findByGenreType(genreType) != null) {
            throw new IllegalArgumentException(
                    "Genre with type '" + genreType + "' already exists");
        }
        Genre genre = new TOPSECRET.domain.Genre.GenreFactory().create(genreType);
        GenreDM saved = genreRepositoryJpa.save(assembler.toDM(genre));
        return assembler.toDomain(saved);
    }

    // =========================================================================
    // Lookup
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public Genre findByTitle(String title) {
        Objects.requireNonNull(title);
        GenreDM dm = genreRepositoryJpa.findByGenreType(title);
        if (dm == null) {
            throw new IllegalArgumentException(
                    "Genre with title '" + title + "' not found");
        }
        return assembler.toDomain(dm);
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
    public Optional<Genre> findGenreById(GenreID genreId) {
        Objects.requireNonNull(genreId);
        return genreRepositoryJpa.findById(genreId)
                .map(assembler::toDomain);
    }
}
