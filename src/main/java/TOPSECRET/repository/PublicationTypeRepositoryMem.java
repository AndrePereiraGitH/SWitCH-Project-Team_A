package TOPSECRET.repository;

import TOPSECRET.domain.PublicationTypeFactory;
import TOPSECRET.domain.PublicationType;

import java.util.ArrayList;
import java.util.List;

/**
 * In-memory implementation of {@link IPublicationTypeRepository}.
 * <p>
 * This repository manages {@link PublicationType} instances using an internal list.
 * It relies on a {@link PublicationTypeFactory} to create new objects and ensures
 * that only valid and unique publication types are stored.
 *
 * <p><b>Main responsibilities:</b>
 * <ul>
 *     <li>Create {@link PublicationType} instances via factory</li>
 *     <li>Validate publication types before insertion</li>
 *     <li>Store unique publication types in memory</li>
 *     <li>Provide read-only access to stored data</li>
 * </ul>
 *
 * <p>This implementation does not allow {@code null} or duplicate entries
 * (based on media format, case-insensitive).
 */
public class PublicationTypeRepositoryMem implements IPublicationTypeRepository {

    /**
     * Internal list that stores all created publication types.
     */
    private final List<PublicationType> _publicationTypes = new ArrayList<>();

    /**
     * Factory used to instantiate {@link PublicationType} objects.
     */
    private final PublicationTypeFactory _factory;

    /**
     * Constructs a {@code PublicationTypeRepositoryMem} with the given factory.
     *
     * @param factory the factory used to create PublicationType instances
     */
    public PublicationTypeRepositoryMem(PublicationTypeFactory factory) {
        this._factory = factory;
    }

    /**
     * {@inheritDoc}
     */
   
    public PublicationType create(String mediaFormat) {
        return _factory.create(mediaFormat);
    }

    /**
     * Validates a {@link PublicationType} instance.
     * <p>
     * A publication type is valid if:
     * <ul>
     *     <li>It is not {@code null}</li>
     *     <li>No existing publication type has the same media format (case-insensitive)</li>
     * </ul>
     *
     * @param pubType the publication type to validate
     * @return {@code true} if valid; {@code false} otherwise
     */
    private boolean validatePublicationType(PublicationType pubType) {
        if (pubType == null) {
            return false;
        }

        String newFormat = pubType.getMediaFormat().trim();

        return _publicationTypes.stream()
                .noneMatch(p -> p.getMediaFormat().trim()
                        .equalsIgnoreCase(newFormat));
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if the publication type is {@code null}
     *                                  or already exists
     */
    
    public PublicationType addToList(PublicationType pubType) {
        if (!validatePublicationType(pubType)) {
            throw new IllegalArgumentException("Publication type already exists or is null");
        }

        _publicationTypes.add(pubType);
        return pubType;
    }

    /**
     * {@inheritDoc}
     */

    public List<PublicationType> getAllPublicationTypes() {
        return List.copyOf(_publicationTypes);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if the media format is {@code null}
     *                                  or no matching PublicationType is found
     */

    public PublicationType findByAttributes(String mediaFormat) {

        // Validate input parameters
        if (mediaFormat == null) {
            throw new IllegalArgumentException("MediaFormat cannot be null.");
        }


        for (PublicationType pt : _publicationTypes) {
            if (pt.getMediaFormat().equalsIgnoreCase(mediaFormat)) {
                return pt;
            }
        }

        throw new IllegalArgumentException("PublicationType not found.");
    }
}

}
