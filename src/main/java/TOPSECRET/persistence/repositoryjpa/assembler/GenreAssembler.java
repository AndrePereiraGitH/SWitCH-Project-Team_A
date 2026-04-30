package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.GenreFactory;
import TOPSECRET.persistence.repositoryjpa.datamodel.GenreDM;
import org.springframework.stereotype.Service;
import java.util.Objects;

/**
 * Assembler responsible for converting between {@link Genre} domain objects
 * and {@link GenreDM} persistence entities.
 *
 * <p>Acts as a translation layer between the domain and persistence layers,
 * ensuring neither layer is coupled to the other's representation.</p>
 */
@Service
public class GenreAssembler {

    private final GenreFactory factory;

    /**
     * Constructs a {@code GenreAssembler} with the required {@link GenreFactory}.
     *
     * @param factory the factory used to reconstruct {@link Genre} domain objects
     *                from persisted data; must not be {@code null}
     */
    public GenreAssembler(GenreFactory factory) {
        this.factory = factory;
    }

    /**
     * Converts a {@link Genre} domain object into a {@link GenreDM} persistence entity.
     *
     * @param genre the domain object to convert; must not be {@code null}
     * @return a {@link GenreDM} representing the given genre
     * @throws NullPointerException if {@code genre} is {@code null}
     */
    public GenreDM toDM(Genre genre) {
        Objects.requireNonNull(genre, "Genre cannot be null");
        return new GenreDM(
                genre.id(),
                genre.getGenreType()
        );
    }

    /**
     * Converts a {@link GenreDM} persistence entity into a {@link Genre} domain object.
     *
     * <p>Delegates instantiation to {@link GenreFactory #create(GenreId, String)} to ensure
     * domain construction rules are respected during reconstitution.</p>
     *
     * @param dm the persistence entity to convert; must not be {@code null}
     * @return a {@link Genre} domain object reconstructed from the given entity
     * @throws NullPointerException if {@code dm} is {@code null}
     */
    public Genre toDomain(GenreDM dm) {
        Objects.requireNonNull(dm, "GenreDM cannot be null");
        return factory.create(dm.getId(), dm.getGenreType());
    }
}
