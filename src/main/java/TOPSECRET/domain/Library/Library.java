package TOPSECRET.domain.Library;

import TOPSECRET.ddd.AggregateRoot;

import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.domain.ValueObjects.UserId;


import java.util.Objects;
import java.util.UUID;

/**
 * Represents a user's library within the system.
 *
 * <p>A {@code Library} is an aggregate root uniquely identified by a {@link LibraryId},
 * owned by a specific user ({@link UserId}). It defines a logical grouping context
 * but does not directly hold or manage other domain entities.</p>
 *
 * <p>Publications (e.g., {@link PublicationSample}) belong to separate aggregates.
 * The association between a publication and a library is established via identity
 * reference — the {@code LibraryId} is stored in the publication aggregate.
 * This preserves aggregate independence and consistency boundaries.</p>
 *
 * <p>Retrieval or management of publications associated with a library must be
 * performed through repository queries using the {@code LibraryId}, rather than
 * through in-memory navigation from this aggregate.</p>
 *
 * <p>Business rules:</p>
 * <ul>
 *   <li>A library must have a non-null, non-blank name.</li>
 *   <li>A library must always be associated with a valid user.</li>
 *   <li>Library identity is defined solely by its {@link LibraryId}.</li>
 * </ul>
 */
public class Library implements AggregateRoot<LibraryId> {

    private final String _libraryName;
    private final UserId _userId;
    private final LibraryId _id;

    /**
     * Constructs a new {@code Library} with an auto-generated {@link LibraryId}.
     *
     * <p>This constructor should be used when creating a brand-new library
     * within the domain. A unique identifier is generated internally using
     * a random UUID.</p>
     *
     * @param libraryName the name of the library; must not be {@code null} or blank
     * @param userId      the identifier of the user who owns this library; must not be {@code null}
     * @throws IllegalArgumentException if {@code libraryName} is {@code null} or blank
     * @throws IllegalArgumentException if {@code userId} is {@code null}
     */
    protected Library(String libraryName, UserId userId) {
        if (libraryName == null || libraryName.isBlank()) {
            throw new IllegalArgumentException("Library Name cannot be null or blank");
        }
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be Null");
        }

        _libraryName = libraryName;
        _userId = userId;
        _id = new LibraryId(UUID.randomUUID().toString());
    }

    /**
     * Constructs a {@code Library} with a provided {@link LibraryId}.
     *
     * <p>This constructor is typically used when reconstructing a library
     * from persistence (e.g., database), where the identifier is already known.</p>
     *
     * @param libraryId   the unique identifier of the library; must not be {@code null}
     * @param libraryName the name of the library; must not be {@code null} or blank
     * @param userId      the identifier of the user who owns this library; must not be {@code null}
     * @throws IllegalArgumentException if {@code libraryId} is {@code null}
     * @throws IllegalArgumentException if {@code libraryName} is {@code null} or blank
     * @throws IllegalArgumentException if {@code userId} is {@code null}
     */

    protected Library(LibraryId libraryId, String libraryName, UserId userId) {
        if (libraryId == null) {
            throw new IllegalArgumentException("LibraryId cannot be null");
        }
        if (libraryName == null || libraryName.isBlank()) {
            throw new IllegalArgumentException("Library Name cannot be null or blank");
        }
        if (userId == null) {
            throw new IllegalArgumentException("User cannot be Null");
        }

        _libraryName = libraryName;
        _userId = userId;
        _id = libraryId;
    }

    /**
     * Returns the unique identifier of this {@code Library}.
     *
     * @return the {@link LibraryId} identifying this library; never {@code null}
     */
    public LibraryId id() {
        return _id;
    }

    /**
     * Returns the name of this {@code Library}.
     *
     * @return the library name; never {@code null} or blank
     */
    public String getLibraryName() {
        return _libraryName;
    }


    /**
     * Retrieves the unique identifier of the user.
     *
     * @return the username associated with this user
     */
    public UserId getUserId() {
        return _userId;
    }

    /**
     * Indicates whether some other object is equal to this {@code Library}.
     *
     * <p>Two libraries are considered equal if and only if they share the same
     * {@link LibraryId}, regardless of name or owner. This follows the DDD principle
     * of entity equality, where identity is determined solely by the entity's ID.</p>
     *
     * @param o the reference object with which to compare
     * @return {@code true} if {@code o} is a {@code Library} with the same identifier;
     * {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Library library = (Library) o;
        return Objects.equals(_id, library._id);
    }

    /**
     * Returns the hash code value for this {@code Library}.
     *
     * <p>The hash code is computed solely based on the {@link LibraryId},
     * ensuring consistency with the {@link #equals(Object)} implementation.
     * Since equality is defined exclusively by identity, only the identifier
     * is considered when generating the hash code.</p>
     *
     * <p>This implementation guarantees that two {@code Library} instances
     * considered equal (i.e., having the same {@code LibraryId}) will
     * produce the same hash code, fulfilling the general contract of
     * {@link Object#hashCode()}.</p>
     *
     * @return the hash code derived from the library's identifier
     */
    @Override
    public int hashCode() {
        return Objects.hash(_id);
    }

    /**
     * Determines whether this {@code Library} has the same identity as another object.
     *
     * <p>Two libraries share the same identity if and only if their {@link LibraryId}
     * values are equal. This method is part of the {@link AggregateRoot} contract
     * and is semantically equivalent to {@link #equals(Object)} for this aggregate.</p>
     *
     * @param object the object to compare identity with
     * @return {@code true} if {@code object} is a {@code Library} with an equal {@link LibraryId};
     * {@code false} otherwise
     */
    @Override
    public boolean sameAs(Object object) {
        if (object instanceof Library other) {
            return this._libraryName.equals(other._libraryName);
        }
        return false;
    }
}
