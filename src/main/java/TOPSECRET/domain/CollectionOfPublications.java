package TOPSECRET.domain;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.PublicationSample.PublicationSample;

import TOPSECRET.domain.User.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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
public class CollectionOfPublications {

    // ============================== US017 ==============================

    // These five fields make up the collection — all defined in US017
    private String _collectionName;                            // the name the user picks for the collection
    private Genre _collectionGenre;                            // the genre chosen from the available list
    private List<PublicationSample> _collectionOfPublications; // the publications inside — starts empty (US018 adds them)
    private User _owner;                                       // the user who created and owns this collection
    private boolean _isPrivate;                                // US017 rule: always starts as true (private)

    /**
     * Creates a new collection of publications — US017.
     *
     * <p>All three parameters are required. The collection starts with no publications
     * and is always private. These are both US017 rules.
     *
     * @param collectionName  name chosen by the user for the collection
     * @param collectionGenre genre chosen by the user
     * @param user            the user who owns the collection
     * @throws IllegalArgumentException if name is null or blank, or if genre or user is null
     */
    protected CollectionOfPublications(String collectionName, Genre collectionGenre, User user) {

        // name cannot be null or blank — a collection must always have a name
        if (collectionName == null || collectionName.isBlank()) {
            throw new IllegalArgumentException("Collection name cannot be null or empty.");
        }

        // genre cannot be null — US017 requires the user to pick a genre
        if (collectionGenre == null) {
            throw new IllegalArgumentException("Collection genre cannot be null.");
        }

        // owner cannot be null — every collection belongs to a user
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }

        this._collectionName = collectionName;
        this._collectionGenre = collectionGenre;
        this._isPrivate = true;            // US017 rule: always private when created; US019 allows making it public
        this._owner = user;
        this._collectionOfPublications = new ArrayList<>(); // starts empty — publications are added in US018
    }

    /** Returns the name of the collection. */
    public String getCollectionName() {
        return _collectionName;
    }

    /** Returns true if the collection is private, false if it is public. */
    public boolean getIsPrivate() { return _isPrivate; }

    /** Returns the genre of the collection. */
    public Genre getCollectionGenre() { return _collectionGenre; }

    /** Returns the user who owns the collection. */
    public User getOwner() { return _owner; }

    /**
     * Returns the list of publications in this collection — US017 / US018.
     *
     * <p>A copy is returned instead of the real internal list.
     * This means callers can read the list but cannot change what is inside the collection.
     * This protects the collection's data from being changed from outside the class.
     *
     * @return a copy of the publications list
     */
    public List<PublicationSample> getPublications() {
        return new ArrayList<>(_collectionOfPublications); // defensive copy — the real list stays safe
    }

    /**
     * Two collections are equal if they have the same name (ignoring case), genre, owner,
     * privacy flag, and publications.
     *
     * <p>This is needed so Java can compare collections by their data, not by memory address.
     * Without this, two collections with the exact same content would look different to Java.
     * The duplicate check in the repository (US017) depends on this working correctly.
     */
    @Override
    public boolean equals(Object obj) {

        // same object in memory — definitely equal
        if (this == obj) return true;

        // instanceof returns false for null and for different types — safe check
        if (!(obj instanceof CollectionOfPublications)) return false;

        CollectionOfPublications other = (CollectionOfPublications) obj;

        return _collectionName.equalsIgnoreCase(other._collectionName) // names typed by users may differ in case
                && _collectionGenre.equals(other._collectionGenre)
                && Objects.equals(_owner, other._owner)
                && _isPrivate == other._isPrivate
                && _collectionOfPublications.equals(other._collectionOfPublications);
    }

    /**
     * hashCode must match equals — if two collections are equal, they must have the same hashCode.
     *
     * <p>We use {@code toLowerCase()} on the name to stay consistent with {@code equalsIgnoreCase} above.
     * If the hash was based on the original case, two equal collections could get different hash values,
     * which would break things like HashSets and HashMaps.
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                _collectionName.toLowerCase(), // must match equalsIgnoreCase — lowercase makes them consistent
                _collectionGenre,
                _owner,
                _isPrivate,
                _collectionOfPublications
        );
    }

    // ============================== end US017 ==============================


    // ============================== US018 ==============================

    /**
     * Adds a publication to the collection — US018.
     *
     * <p>Null publications are ignored. Duplicate publications are also ignored —
     * the same publication can only appear once in a collection.
     *
     * @param publication the publication to add
     * @return true if added, false if null or already in the collection
     */
    public boolean addPublication(PublicationSample publication) {

        if (publication == null) return false;

        if (_collectionOfPublications.contains(publication)) {
            return false; // already in the list — no duplicates allowed
        }

        _collectionOfPublications.add(publication);
        return true;
    }

    /**
     * Checks if a publication is already in the collection — US018.
     *
     * @param publication the publication to look for
     * @return true if it exists, false otherwise
     */
    public boolean containsPublication(PublicationSample publication) {
        return _collectionOfPublications.contains(publication);
    }

    // ============================== end US018 ==============================


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