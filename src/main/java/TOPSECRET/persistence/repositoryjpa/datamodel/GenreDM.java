package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.ValueObjects.GenreId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * JPA data model representing a genre in the persistence layer.
 *
 * <p>Acts as the persistence twin of the {@link TOPSECRET.domain.Genre.Genre} domain aggregate,
 * keeping JPA annotations out of the domain layer.</p>
 *
 * <p>Mapped to the {@code genres} table in the database.</p>
 *
 * <p>The no-arg constructor is protected and reserved for JPA use only.
 * Use {@link #GenreDM(GenreId, String)} to create instances programmatically.</p>
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "genres")

public class GenreDM {

    /**
     * The embedded primary key, mapped to the {@code genre_id} column.
     * Uses the domain {@link GenreId} Value Object directly as the identifier.
     */
    @EmbeddedId
    @AttributeOverride(name = "genreID",column=@Column(name = "genre_id"))
    private GenreId id;

    @Column(name = "genre_type", nullable = false)
    private String genreType;

    public GenreDM(GenreId id, String genreType) {
        this.id = Objects.requireNonNull(id, "GenreId cannot be null");
        this.genreType = Objects.requireNonNull(genreType, "GenreType cannot be null");
    }
}
