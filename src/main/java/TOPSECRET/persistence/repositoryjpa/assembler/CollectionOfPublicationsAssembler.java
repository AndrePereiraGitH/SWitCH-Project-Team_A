package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;
import TOPSECRET.persistence.repositoryjpa.datamodel.CollectionOfPublicationsDM;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Assembler responsible for converting {@link CollectionOfPublications}
 * domain objects into {@link CollectionOfPublicationsDM} data models
 * and vice versa.
 *
 * <p>This class acts as a bridge between the domain layer and the
 * persistence layer, ensuring that domain entities are properly mapped
 * to JPA-compatible data models and reconstructed back into domain
 * objects when retrieved from the database.</p>
 *
 * <p>The conversion from data model to domain object is delegated to
 * {@link CollectionOfPublicationsFactory}, preserving domain creation
 * rules and avoiding direct instantiation outside the domain layer.</p>
 */
@Service
public class CollectionOfPublicationsAssembler {

    private final CollectionOfPublicationsFactory _factory;

    public CollectionOfPublicationsAssembler(CollectionOfPublicationsFactory factory) {

        _factory = factory;
    }

    /**
     * Converts a {@link CollectionOfPublications} domain object into
     * its corresponding {@link CollectionOfPublicationsDM} data model.
     *
     * <p>This method extracts the relevant attributes from the domain
     * object and creates a persistence representation suitable for
     * JPA storage.</p>
     *
     * @param collection the domain object to convert
     * @return the corresponding persistence data model
     */
    public CollectionOfPublicationsDM toDM(CollectionOfPublications collection) {

        if (collection == null) {
            throw new IllegalArgumentException("Collection of Publications cannot be null");
        }

        CollectionOfPublicationsDM collectionDM =
                new CollectionOfPublicationsDM(
                        collection.id(), collection.getCollectionName(),
                        collection.getCollectionGenre(), collection.getOwnerId(),
                        collection.getIsPrivate(),
                        collection.getPublicUntil()
                );

        return collectionDM;
    }

    /**
     * Converts a {@link CollectionOfPublicationsDM} data model into
     * its corresponding {@link CollectionOfPublications} domain object.
     *
     * <p>This method delegates object creation to the
     * {@link CollectionOfPublicationsFactory} to ensure domain
     * invariants and business rules remain enforced.</p>
     *
     * @param collectionDM the persistence data model to convert
     * @return the reconstructed domain object
     */
    public CollectionOfPublications toDomain(CollectionOfPublicationsDM collectionDM) {

        if (collectionDM == null) {
            throw new NullPointerException("CollectionOfPublicationsDM cannot be null");
        }

        CollectionOfPublications collection = _factory.create(
                collectionDM.getId(), collectionDM.getCollectionName(),
                collectionDM.getGenreId(), collectionDM.getUserId(),
                collectionDM.isPrivate(), collectionDM.getPublicUntil());

        return collection;
    }

    /**
     * Converts a list of {@link CollectionOfPublicationsDM} objects
     * into a list of {@link CollectionOfPublications} domain entities.
     *
     * <p>
     * Each data model instance is individually converted through
     * {@link #toDomain(CollectionOfPublicationsDM)} in order to ensure
     * consistency between persistence and domain layers.
     * </p>
     *
     * @param dmList list of collection data models to convert
     * @return list of corresponding domain entities
     *
     * @throws NullPointerException if dmList is null
     */
    public List<CollectionOfPublications> toDomainList(
            List<CollectionOfPublicationsDM> dmList) {

        Objects.requireNonNull(dmList,
                "CollectionOfPublicationsDM list must not be null");

        List<CollectionOfPublications> collections = new ArrayList<>();

        for (CollectionOfPublicationsDM dm : dmList) {
            collections.add(toDomain(dm));
        }

        return collections;
    }
}
