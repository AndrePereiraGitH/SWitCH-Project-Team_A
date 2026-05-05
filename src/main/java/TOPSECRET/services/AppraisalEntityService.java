package TOPSECRET.services;

import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.AppraisalEntity.AppraisalEntityFactory;
import TOPSECRET.domain.AppraisalEntity.IAppraisalEntityRepository;
import TOPSECRET.domain.ValueObjects.AppraisalEntityId;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Application service responsible for AppraisalEntity use cases.
 */
@Service
public class AppraisalEntityService {

    private final IAppraisalEntityRepository repository;
    private final AppraisalEntityFactory factory;

    /**
     * Constructs the service with required dependencies.
     *
     * @param repository the repository used for persistence operations
     * @param factory the factory used to create AppraisalEntity instances
     */
    public AppraisalEntityService(
            IAppraisalEntityRepository repository,
            AppraisalEntityFactory factory

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
     * Creates and persists a new AppraisalEntity.
     */
    public AppraisalEntity createAndSave(
            String name,
            Contact contact,
            List<PublicationTypeId> publicationTypes,
            List<GenreId> genres
    ) {
        AppraisalEntity entity = factory.create(
                name,
                contact,
                publicationTypes,
                genres
        );

        return repository.save(entity);
    }

    /**
     * Updates an existing AppraisalEntity.
     */
    public AppraisalEntity update(
            AppraisalEntityId id,
            String name,
            Contact contact,
            List<PublicationTypeId> publicationTypes,
            List<GenreId> genres

    ) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        AppraisalEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("AppraisalEntity not found with id: " + id));

        entity.update(name, contact, publicationTypes, genres);

        return repository.save(entity);
    }
}
