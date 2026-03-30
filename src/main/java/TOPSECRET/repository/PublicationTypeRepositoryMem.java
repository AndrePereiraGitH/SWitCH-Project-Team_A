package TOPSECRET.repository;

import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
import TOPSECRET.domain.PublicationType.PublicationTypeFactory;
import TOPSECRET.domain.PublicationType.PublicationType;

import java.util.ArrayList;
import java.util.List;

/**
 * Contract for managing {@link PublicationType} objects.
 *
 * <p>
 * Defines operations for creating, storing, and retrieving publication types.
 * Ensures validation of input data and prevention of duplicates.
 * </p>
 */
public class PublicationTypeRepositoryMem implements IPublicationTypeRepository {

    private final List<PublicationType> _publicationTypes = new ArrayList<>();
    private final PublicationTypeFactory _factory;


    /**
     * Creates a new repository instance.
     *
     * @param factory factory used to create PublicationType instances
     */
    public PublicationTypeRepositoryMem(PublicationTypeFactory factory) {
        this._factory = factory;
    }

    /**
     * Creates a new publication type and stores it in the repository.
     *
     * @param mediaFormat the media format of the publication type;
     *                    must not be null or blank
     * @return the newly created {@link PublicationType} instance
     * @throws IllegalArgumentException if {@code mediaFormat} is null or blank,
     *                                  or if the publication type already exists
     */
    @Override
    public PublicationType create (String mediaFormat) {

        if (mediaFormat == null || mediaFormat.isBlank()) {
            throw new IllegalArgumentException("Media format can't be null or blank.");
        }

        PublicationType pt = _factory.create(mediaFormat);
        return save(pt);
    }

    /**
     * Saves a PublicationType in the repository.
     *
     * @param pubType publication type to save
     * @return saved PublicationType
     * @throws IllegalArgumentException if publication type is invalid or duplicated
     */
    private PublicationType save(PublicationType pubType) {
        if (!isValid(pubType)) {
            throw new IllegalArgumentException("Publication type already exists or is invalid.");
        }

        _publicationTypes.add(pubType);
        return pubType;
    }


    /**
     * Validates if a PublicationType can be added.
     *
     * @param pubType publication type to validate
     * @return true if valid and not duplicated, false otherwise
     */
    private boolean isValid(PublicationType pubType) {
        if (pubType == null) {
            return false;
        }

        String newFormat = pubType.getMediaFormat().trim();

        return _publicationTypes.stream()
                .noneMatch(p -> p.getMediaFormat().trim()
                        .equalsIgnoreCase(newFormat));
    }

    /**
     * Validates if a PublicationType can be added.
     *
     * @param pubType publication type to validate
     * @return true if valid and not duplicated, false otherwise
     */
    public PublicationType addToList(PublicationType pubType) {
        if (!isValid(pubType)) {
            throw new IllegalArgumentException("Publication type already exists or is null");
        }

        _publicationTypes.add(pubType);
        return pubType;
    }


    /**
     * Returns all stored PublicationTypes.
     *
     * @return unmodifiable list of publication types
     */
    public List<PublicationType> getAllPublicationTypes() {
        return List.copyOf(_publicationTypes);
    }


    /**
     * Finds a PublicationType by media format.
     *
     * @param mediaFormat publication format
     * @return matching PublicationType
     * @throws IllegalArgumentException if input is invalid or not found
     */
    public PublicationType findByAttributes(String mediaFormat) {

        if (mediaFormat == null || mediaFormat.isBlank()) {
            throw new IllegalArgumentException("MediaFormat can't be null.");
        }

        return _publicationTypes.stream()
                .filter(pt -> pt.getMediaFormat().trim()
                        .equalsIgnoreCase(mediaFormat.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("PublicationType not found."));
    }

}
