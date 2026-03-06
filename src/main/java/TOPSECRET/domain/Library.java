package TOPSECRET.domain;

import TOPSECRET.domain.Enum.PublicationStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a library that belongs to a user and contains a collection of publications.
 * <p>
 * A library is uniquely associated with a single user and manages a collection of
 * publication samples. The library name must not be null or blank, and the associated
 * user must not be null. Publications can be added to the library, and duplicate
 * publications are not allowed.
 * </p>
 *
 * @author TOPSECRET
 * @version 1.0
 */
public class Library {

    private final String _libraryName;
    private final User _user;
    private List<PublicationSample> _myPublications;

    /**
     * Constructs a Library with the specified name and user.
     * <p>
     * Initializes an empty library with the given name and user. The library name
     * must not be null or blank, and the user must not be null.
     * </p>
     *
     * @param libraryName the name of the library
     * @param user the user who owns this library
     * @throws IllegalArgumentException if libraryName is null or blank
     * @throws IllegalArgumentException if user is null
     */
    protected Library(String libraryName, User user) {
        if (libraryName == null || libraryName.isBlank()) {
            throw new IllegalArgumentException("Library Name cannot be null or blank");
        }
        if (user == null) {
            throw new IllegalArgumentException("User cannot be Null");
        }

        _libraryName = libraryName;
        _user = user;
        _myPublications = new ArrayList<>();
    }

    /**
     * Returns the name of this library.
     *
     * @return the library name
     */
    public String getLibraryName() {
        return _libraryName;
    }

    /**
     * Returns the user who owns this library.
     *
     * @return the owner user of this library
     */
    public User getUser() {
        return _user;
    }

    /**
     * Returns an unmodifiable list of all publications in this library.
     * <p>
     * The returned list is a defensive copy and cannot be modified. To add or
     * remove publications, use the {@link #addPublication(PublicationSample)} method.
     * </p>
     *
     * @return an unmodifiable list of publications in this library
     */
    public List<PublicationSample> getAllPublications() {
        return List.copyOf(_myPublications);
    }

    /**
     * Checks whether the specified publication exists in this library.
     *
     * @param publication the publication to check for
     * @return true if the publication is in this library, false otherwise
     */
    public boolean containsPublication(PublicationSample publication) {
        return _myPublications.contains(publication);
    }

    /**
     * Adds a publication to this library.
     * <p>
     * The publication must not be null and must not already exist in the library.
     * If the publication is successfully added, it is returned. If the publication
     * already exists, an exception is thrown.
     * </p>
     *
     * @param publicationSample the publication sample to add
     * @return the added publication sample
     * @throws NullPointerException if publicationSample is null
     * @throws IllegalArgumentException if the publication already exists in this library
     */
    public PublicationSample addPublication(PublicationSample publicationSample) {
        if (publicationSample == null) {
            throw new NullPointerException("PublicationSample can not be null");
        }

        if (_myPublications.contains(publicationSample)) {
            throw new IllegalArgumentException("This publication is already in the library");
        }

        _myPublications.add(publicationSample);
        return publicationSample;
    }

    /**
     * Returns an unmodifiable list of all publications in this library available for sale.
     * <p>
     * The returned list is a defensive copy and cannot be modified. To add or
     * remove publications, use the {@link #addPublication(PublicationSample)} method.
     * </p>
     *
     * @return an unmodifiable list of publications in this library available for sale.
     */
    public List<PublicationSample> findLibraryPublicationsAvailableForSale() {
        List<PublicationSample> availablePublicationsForSale = new ArrayList<>();
        for (PublicationSample pubSample : _myPublications) {
            if (pubSample.getPublicationStatus() != PublicationStatus.SOLD && pubSample.getPublicationStatus() != PublicationStatus.FOR_SALE) {
                availablePublicationsForSale.add(pubSample);
            }
        }
        return List.copyOf(availablePublicationsForSale);
    }
}