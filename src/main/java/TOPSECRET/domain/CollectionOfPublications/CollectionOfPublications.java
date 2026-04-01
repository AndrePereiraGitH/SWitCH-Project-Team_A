package TOPSECRET.domain.CollectionOfPublications;

import TOPSECRET.ddd.AggregateRoot;

import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsID;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.UserId;

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
public class CollectionOfPublications implements AggregateRoot<CollectionOfPublicationsID> {

    // ============================== US017 ==============================

    // These fields make up the collection — all defined in US017
    private final CollectionOfPublicationsID _id;
    private String _collectionName;                            // the name the user picks for the collection
    private GenreID _collectionGenreId;                            // the genre chosen from the available list
    private final UserId _ownerId;                                       // the user who created and owns this collection
    private boolean _isPrivate;                                // US017 rule: always starts as true (private)

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
    protected CollectionOfPublications(String collectionName, GenreID collectionGenreId, UserId userId) {

        if (collectionName == null || collectionName.isBlank()) {
            throw new IllegalArgumentException("Collection name cannot be null or empty.");
        }

        if (collectionGenreId == null) {
            throw new IllegalArgumentException("Collection genre cannot be null.");
        }

        if (userId == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }

        this._id = new CollectionOfPublicationsID(UUID.randomUUID().toString());
        this._collectionName = collectionName;
        this._collectionGenreId = collectionGenreId;
        this._isPrivate = true;            // US017 rule: always private when created; US019 allows making it public
        this._ownerId = userId;
    }

    public String getCollectionName() {
        return _collectionName;
    }

    public boolean getIsPrivate() {
        return _isPrivate;
    }

    public GenreID getCollectionGenre() {
        return _collectionGenreId;
    }

    public UserId getOwnerId() {
        return _ownerId;
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

    public CollectionOfPublicationsID id() {
        return _id;
    }

    @Override
    public boolean sameAs(Object obj) {
        if (!(obj instanceof CollectionOfPublications)) {
            return false;
        }

        CollectionOfPublications newObj = (CollectionOfPublications) obj;

        return this._collectionName.equals(newObj._collectionName) &&
                this._collectionGenreId.equals(newObj._collectionGenreId) &&
                this._isPrivate == newObj._isPrivate;
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


    // ============================== US019 ==============================

    /**
     * Changes the privacy state of the collection — US019.
     *
     * <p>Pass {@code false} to make the collection public, {@code true} to make it private again.
     *
     * @param _isPrivate new privacy value
     */
    public void set_isPrivate(boolean _isPrivate) {
        this._isPrivate = _isPrivate;
    }

    // ============================== end US019 ==============================

}