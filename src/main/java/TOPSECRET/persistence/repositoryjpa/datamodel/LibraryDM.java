package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.domain.ValueObjects.UserId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * Persistence data model representing a Library entity.
 *
 * <p>This JPA entity maps the domain concept of a {@link TOPSECRET.domain.Library.Library}
 * to the underlying relational database. It stores the library's identity,
 * name, and associated user using embeddable value objects.</p>
 *
 * <p>The class is intended to be used exclusively within the persistence layer.
 * Conversion to and from the domain model is handled by the assembler.</p>
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Libraries")
public class LibraryDM {

    /**
     * Unique identifier of the library in the database.
     */
    @EmbeddedId
    @AttributeOverride(name = "_id", column = @Column(name = "library_id"))
    private LibraryId id;

    /**
     * Name of the library.
     */
    @Column(name = "library_name", nullable = false)
    private String libraryName;

    /**
     * Identifier of the user associated with this library.
     */
    @Embedded
    @AttributeOverride(name = "userId", column = @Column(name = "user_id"))
    private UserId userId;

    public LibraryDM(LibraryId id, String libraryName, UserId userId) {
        this.id = Objects.requireNonNull(id, "LibraryId cannot be null");
        this.libraryName = Objects.requireNonNull(libraryName, "Library name cannot be null");
        this.userId = Objects.requireNonNull(userId, "UserId cannot be null");
    }
}