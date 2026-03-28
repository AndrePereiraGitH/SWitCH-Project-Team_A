package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.DomainID;

import java.util.Objects;

/**
 * Value object that represents the unique identifier of an author.
 * <p>
 * This class implements {@link DomainID} and encapsulates the author ID
 * as a non-null and non-blank {@link String}.
 */
public class AuthorId implements DomainID {

    private String _authorId;

    /**
     * Creates a new {@code AuthorId} instance.
     *
     * @param authorId the author identifier as a non-null and non-blank string
     * @throws IllegalArgumentException if {@code authorId} is null or blank
     */
    public AuthorId(String authorId) {

        if (authorId == null || authorId.isBlank()) {
            throw new IllegalArgumentException("AuthorId must be a non empty String");
        }
        this._authorId = authorId;
    }

    /**
     * Compares this author ID with another object for equality.
     * <p>
     * Two {@code AuthorId} objects are considered equal if they are of the same
     * class and contain the same identifier value.
     *
     * @param o the object to compare with
     * @return {@code true} if the given object is equal to this author ID;
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AuthorId authorId = (AuthorId) o;
        return Objects.equals(_authorId, authorId._authorId);
    }


}
