package TOPSECRET.services;

import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.PublicationType.PublicationTypeFactory;
import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;

import org.springframework.stereotype.Service;

/**
 * Application service responsible for PublicationType use cases.
 *
 * <p>
 * Handles creation and validation logic before delegating persistence
 * to the repository layer.
 * </p>
 */
@Service
public class PublicationTypeService {

    private final IPublicationTypeRepository repository;
    private final PublicationTypeFactory factory;

    /**
     * Constructs the service with required dependencies.
     *
     * @param repository the repository used for persistence
     * @param factory the factory used to create domain objects
     */
    public PublicationTypeService(
            IPublicationTypeRepository repository,
            PublicationTypeFactory factory
    ) {

        if (repository == null) {
            throw new IllegalArgumentException("Repository cannot be null");
        }

        if (factory == null) {
            throw new IllegalArgumentException("Factory cannot be null");
        }

        this.repository = repository;
        this.factory = factory;
    }

    /**
     * Creates and persists a new PublicationType.
     *
     * <p>
     * Ensures that the media format is valid and not duplicated.
     * </p>
     *
     * @param mediaFormat the publication format
     * @return the created and saved {@link PublicationType}
     * @throws IllegalArgumentException if mediaFormat is invalid or already exists
     */
    public PublicationType create(String mediaFormat) {

        if (mediaFormat == null || mediaFormat.isBlank()) {
            throw new IllegalArgumentException("Media format cannot be null or empty");
        }

        repository.findByMediaFormat(mediaFormat)
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("PublicationType already exists");
                });

        PublicationType entity = factory.create(mediaFormat);

        return repository.save(entity);
    }
}