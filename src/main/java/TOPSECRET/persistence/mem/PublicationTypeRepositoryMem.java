package TOPSECRET.persistence.mem;

import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * In-memory implementation of {@link IPublicationTypeRepository}.
 *
 * <p>
 * Stores publication types in a local list.
 * This implementation is mainly used for testing purposes.
 * </p>
 */
public class PublicationTypeRepositoryMem implements IPublicationTypeRepository {

    private final List<PublicationType> publicationTypes = new ArrayList<>();

    /**
     * Saves a publication type in memory.
     *
     * @param pubType the publication type to persist; must not be null
     * @return the persisted {@link PublicationType}
     * @throws IllegalArgumentException if {@code pubType} is null
     */
    @Override
    public PublicationType save(PublicationType pubType) {

        if (pubType == null) {
            throw new IllegalArgumentException("PublicationType cannot be null");
        }

        publicationTypes.add(pubType);
        return pubType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PublicationType> findAll() {
        return List.copyOf(publicationTypes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<PublicationType> findById(PublicationTypeId id) {

        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        return publicationTypes.stream()
                .filter(pt -> pt.id().equals(id))
                .findFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<PublicationType> findByMediaFormat(String mediaFormat) {

        if (mediaFormat == null || mediaFormat.isBlank()) {
            throw new IllegalArgumentException("Media format cannot be null or empty");
        }

        return publicationTypes.stream()
                .filter(pt -> pt.getMediaFormat().equalsIgnoreCase(mediaFormat.trim()))
                .findFirst();
    }
}