package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.DomainID;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Represents a unique identifier for a Library domain entity.
 *
 * <p>This value object encapsulates a non-null, non-blank string identifier
 * and enforces its validity at construction time. It implements {@link DomainID}
 * as part of the domain-driven design identity contract.</p>
 *
 * <p>Two {@code LibraryId} instances are considered equal if and only if
 * their underlying string identifiers are equal.</p>
 *
 * <p>The protected no-arg constructor exists solely to satisfy JPA's reflection-based
 * instantiation requirement. It should never be called directly by application code.</p>
 */
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class LibraryId implements DomainID {

    @Column(name = "library_id")
    private String _id;


    /**
     * Constructs a new {@code LibraryId} with the given string identifier.
     *
     * @param id the string identifier; must not be {@code null} or blank
     * @throws IllegalArgumentException if {@code id} is {@code null} or blank
     */
    public LibraryId(String id) {
        if (id != null && !id.isBlank())
            this._id = id;
        else
            throw new IllegalArgumentException("LibraryId must be a valid String.");
    }

    /**
     * @return the string identifier
     */
    public String getId() {
        return _id;
    }

    /**
     * Indicates whether some other object is equal to this {@code LibraryId}.
     *
     * <p>Two {@code LibraryId} instances are equal if they share the same
     * underlying string identifier.</p>
     *
     * @param object the reference object with which to compare
     * @return {@code true} if this object is the same instance as {@code object},
     * or if {@code object} is a {@code LibraryId} with an equal identifier;
     * {@code false} otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object instanceof LibraryId libraryId)
            return this._id != null && this._id.equals(libraryId._id);
        return false;
    }

    /**
     * Returns a hash code for this {@code LibraryId}.
     *
     * <p>Returns {@code 0} if the underlying identifier is {@code null},
     * which may occur transiently during JPA reflection-based instantiation.</p>
     *
     * @return the hash code of the underlying string identifier, or {@code 0} if null
     */
    @Override
    public int hashCode() {
        return _id != null ? _id.hashCode() : 0;
    }

    /**
     * Returns the string representation of this {@code LibraryId}.
     *
     * @return the underlying string identifier
     */
    @Override
    public String toString() {
        return _id;
    }
}
