package TOPSECRET.domain.CollectionOfPublications;

import TOPSECRET.ddd.AggregateRoot;

import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;


/**
 * Represents a collection of publications created by a user — US017.
 *
 * <p>US017 defines what a collection is and what data it must have:
 * a name chosen by the user, a genre, and an owner (User).
 * Every collection starts as private — the user can make it public later (US019).
 *
 * <p>This class also belongs to US018 (adding publications) and US019 (making it public),
 * but the core structure — name, genre, owner, and the private flag — comes from US017.
 */
public class CollectionOfPublications implements AggregateRoot<CollectionOfPublicationsId> {

    private final CollectionOfPublicationsId _id;
    private String _collectionName;
    private GenreId _collectionGenreId;
    private final UserId _ownerId;
    private boolean _isPrivate;
    private LocalDateTime publicUntil;

    /**
     * Creates a new collection of publications — US017.
     *
     * <p>All three parameters are required. The collection starts with no publications
     * and is always private. These are both US017 rules.
     *
     * @param collectionName    name chosen by the userId for the collection
     * @param collectionGenreId genre chosen by the userId
     * @param userId            the userId who owns the collection
     * @throws IllegalArgumentException if name is null or blank, or if genre or userId is null
     */
    protected CollectionOfPublications(String collectionName, GenreId collectionGenreId, UserId userId) {

        if (collectionName == null || collectionName.isBlank()) {
            throw new IllegalArgumentException("Collection name cannot be null or empty.");
        }

        if (collectionGenreId == null) {
            throw new IllegalArgumentException("Collection genre cannot be null.");
        }

        if (userId == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }

        this._id = new CollectionOfPublicationsId(UUID.randomUUID().toString());
        this._collectionName = collectionName;
        this._collectionGenreId = collectionGenreId;
        this._isPrivate = true;            // US017 rule: always private when created; US019 allows making it public
        this._ownerId = userId;
    }

    /**
     * Recreates an existing {@link CollectionOfPublications} with a predefined identity.
     *
     * <p>This constructor is intended for persistence reconstruction, allowing an
     * existing collection to be restored from stored data while preserving its
     * original {@link CollectionOfPublicationsId}. It is typically used by factories
     * and repository implementations when rebuilding domain objects from the database.
     *
     * <p>All domain invariants are validated here to ensure the reconstructed object
     * remains valid and consistent.</p>
     *
     * <p>The collection is initialized as private by default.</p>
     *
     * @param collectionOfPublicationsId the existing unique identifier of the collection;
     *                                   must not be null
     * @param collectionName             the name of the collection; must not be null or blank
     * @param collectionGenreId          the genre associated with the collection;
     *                                   must not be null
     * @param userId                     the owner of the collection; must not be null
     * @throws IllegalArgumentException if the collection ID is null
     * @throws IllegalArgumentException if the collection name is null or blank
     * @throws IllegalArgumentException if the genre is null
     * @throws IllegalArgumentException if the user is null
     */
    protected CollectionOfPublications(
            CollectionOfPublicationsId collectionOfPublicationsId, String collectionName,
            GenreId collectionGenreId, UserId userId, boolean isPrivate, LocalDateTime publicUntil) {

        if (collectionOfPublicationsId == null) {
            throw new IllegalArgumentException("CollectionOfPublicationsId cannot be null.");
        }

        if (collectionName == null || collectionName.isBlank()) {
            throw new IllegalArgumentException("Collection name cannot be null or empty.");
        }

        if (collectionGenreId == null) {
            throw new IllegalArgumentException("Collection genre cannot be null.");
        }

        if (userId == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }

        this._id = collectionOfPublicationsId;
        this._collectionName = collectionName;
        this._collectionGenreId = collectionGenreId;
        this._isPrivate = isPrivate;
        this._ownerId = userId;
        this.publicUntil = publicUntil;
    }

    /**
     * Makes this CollectionOfPublications public until the given date and time.
     *
     * <p>If the collection is already public until the same date and time, no change
     * is made and {@code false} is returned.</p>
     *
     * @param publicUntil the date and time until which the collection will remain public;
     *                    must not be null and must be in the future
     * @return {@code true} if the collection was changed;
     *         {@code false} if it was already public until the same date and time
     * @throws IllegalArgumentException if {@code publicUntil} is null or is not in the future
     */
    public boolean makePublicUntil(LocalDateTime publicUntil) {

        if (publicUntil == null) {
            throw new IllegalArgumentException("Public until date cannot be null.");
        }

        if (!publicUntil.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Public until date must be in the future.");
        }

        if (!_isPrivate && publicUntil.equals(this.publicUntil)) {
            return false;
        }

        this._isPrivate = false;
        this.publicUntil = publicUntil;

        return true;
    }

    /**
     * Makes this CollectionOfPublications public.
     *
     * @return {@code true} if the object was changed from private to public;
     *         {@code false} if it was already public.
     */
    public boolean makePublic() {

        if (!_isPrivate && publicUntil == null) {
            return false;
        }

        this._isPrivate = false;
        this.publicUntil = null;
        return true;
    }

    /**
     * Makes this CollectionOfPublications private.
     *
     * @return {@code true} if the object was changed from public to private;
     *         {@code false} if it was already private.
     */
    public boolean makePrivate() {

        if (_isPrivate) {
            return false;
        }

        this._isPrivate = true;
        this.publicUntil = null;
        return true;
    }

    public boolean getIsPrivate() {
        return _isPrivate;
    }

    public String getCollectionName() {
        return _collectionName;
    }

    public GenreId getCollectionGenre() {
        return _collectionGenreId;
    }

    public UserId getOwnerId() {
        return _ownerId;
    }

    public CollectionOfPublicationsId id() {
        return _id;
    }

    public LocalDateTime getPublicUntil() {
        return publicUntil;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (obj instanceof CollectionOfPublications) {
            CollectionOfPublications newObj = (CollectionOfPublications) obj;

            if (this._id.equals(newObj._id))
                return true;
        }
        return false;
    }

    @Override
    public boolean sameAs(Object obj) {
        if (!(obj instanceof CollectionOfPublications)) {
            return false;
        }

        CollectionOfPublications newObj = (CollectionOfPublications) obj;

        return this._collectionName.equals(newObj._collectionName) &&
                this._collectionGenreId.equals(newObj._collectionGenreId) &&
                this._ownerId.equals(newObj._ownerId) &&
                this._isPrivate == newObj._isPrivate &&
                Objects.equals(this.publicUntil, newObj.publicUntil);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                _collectionName.toLowerCase(), // must match equalsIgnoreCase — lowercase makes them consistent
                _collectionGenreId,
                _ownerId,
                _isPrivate
        );
    }
}