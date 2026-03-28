package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.DomainID;
/**
 * Represents a unique identifier for a Library domain entity.
 *
 * <p>This value object encapsulates a non-null, non-blank string identifier
 * and enforces its validity at construction time. It implements {@link TOPSECRET.ddd.DomainID}
 * as part of the domain-driven design identity contract.</p>
 *
 * <p>Two {@code LibraryId} instances are considered equal if and only if
 * their underlying string identifiers are equal.</p>
 */
public class LibraryId implements DomainID {

    private final String _id;

    public LibraryId(String id) {
        if (id != null && !id.isBlank())
            this._id = id;
        else
            throw new IllegalArgumentException("ID must be a valid String.");
    }

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
     *         or if {@code object} is a {@code LibraryId} with an equal identifier;
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object object) {

        if (this == object)
            return true;

        if (object instanceof LibraryId) {
            LibraryId libraryId = (LibraryId) object;

            if (this._id.equals(libraryId._id))
                return true;
        }
        return false;
    }

    public String toString() {
        return _id;
    }
}
