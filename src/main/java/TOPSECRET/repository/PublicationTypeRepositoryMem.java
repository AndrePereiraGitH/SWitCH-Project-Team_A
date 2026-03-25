package TOPSECRET.repository;

import TOPSECRET.domain.PublicationTypeFactory;
import TOPSECRET.domain.PublicationType;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository class responsible for managing {@link PublicationType} instances.
 * <p>
 * This repository provides functionality to:
 * <ul>
 *     <li>Create new {@link PublicationType} objects using a factory</li>
 *     <li>Validate publication types before insertion</li>
 *     <li>Store unique publication types in memory</li>
 *     <li>Retrieve an immutable list of all stored publication types</li>
 * </ul>
 *
 * The repository prevents null and duplicate {@link PublicationType} objects
 * from being added to the internal collection.
 */

public class PublicationTypeRepositoryMem implements IPublicationTypeRepository {

    /**
     * Internal list that stores all created publication types.
     */
    private final List<PublicationType> _publicationTypes = new ArrayList<>();
    PublicationTypeFactory _factory;

    /**
     * Constructs an empty {@code PublicationTypeRepositoryMem}.
     */
    public PublicationTypeRepositoryMem(PublicationTypeFactory factory) {
        this._factory = factory;
    }

    /**
     * Creates a new {@link PublicationType} using {@link PublicationTypeFactory}.
     *
     * @param mediaFormat   the media format of the publication (e.g., book, magazine)
     * @return a newly created {@link PublicationType} instance
     */
    public PublicationType create(String mediaFormat)
    {
        PublicationType p1 = _factory.create(mediaFormat);
        return p1;
    }

    /**
     * Validates a {@link PublicationType} instance.
     * <p>
     * A publication type is considered valid if:
     * <ul>
     *     <li>It is not {@code null}</li>
     *     <li>It does not already exist in the repository</li>
     * </ul>
     *
     * @param pubType the publication type to validate
     * @return {@code true} if the publication type is valid and can be added;
     *         {@code false} otherwise
     */
    private boolean validatePublicationType(PublicationType pubType)
    {
        if (pubType == null) {
            return false;
        }

        String newFormat = pubType.getMediaFormat().trim();

        boolean exists = _publicationTypes.stream()
                .anyMatch(p -> p.getMediaFormat().trim()
                        .equalsIgnoreCase(newFormat));

        return !exists; // Returns true if the publication type does not already exist in the list and false if it does
    }


    /**
     * Adds a {@link PublicationType} to the repository if it passes validation.
     *
     * @param pubType the publication type to add
     * @return the added {@link PublicationType}
     * @throws IllegalArgumentException if the publication type is {@code null}
     *                                  or already exists in the repository
     */
    public PublicationType addToList(PublicationType pubType)
    {
        if (!validatePublicationType(pubType)) { // if validation returns false
            throw new IllegalArgumentException("Publication type already exists or is null");
        }

        _publicationTypes.add(pubType); // if it returns true, adds publication type to the list
        return pubType;
    }


    /**
     * Returns an immutable copy of all stored {@link PublicationType} instances.
     *
     * @return an unmodifiable list containing all publication types
     */
    public List<PublicationType> getAllPublicationTypes() { return List.copyOf(_publicationTypes); }

    // ------------------------ US004 starts ------------------------

    /**
     * US004
     * Finds an existing PublicationType by its attributes.
     *
     * @param mediaFormat   Media format of the publication.
     * @return Existing PublicationType domain object.
     * @throws IllegalArgumentException if attributes are invalid or not found.
     */
    public PublicationType findByAttributes(String mediaFormat) {

        // Validate input parameters
        if (mediaFormat == null) {
            throw new IllegalArgumentException("MediaFormat cannot be null.");
        }


        for (PublicationType pt : _publicationTypes) {
            if (pt.getMediaFormat().equals(mediaFormat)) {
                return pt;
            }
        }

        throw new IllegalArgumentException("PublicationType not found.");
    }

    // ------------------------ ends ------------------------

}
