package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.PublicationType.PublicationTypeFactory;
import TOPSECRET.persistence.repositoryjpa.datamodel.PublicationTypeDM;
import org.springframework.stereotype.Service;

/**
 * Assembler responsible for converting between
 * {@link PublicationType} (domain) and {@link PublicationTypeDM} (data model).
 *
 * <p>
 * This class isolates persistence concerns from the domain layer,
 * ensuring that transformations are handled in a single place.
 * </p>
 *
 * <p>
 * It is used internally by repository implementations to map
 * domain entities to persistence models and vice versa.
 * </p>
 *
 * <p>
 * The assembler does not contain business logic and should not
 * be used directly by application services.
 * </p>
 */
@Service
public class PublicationTypeAssembler {

    private final PublicationTypeFactory factory;

    /**
     * Constructs the assembler with the required factory.
     * @param factory the factory used to rehydrate domain objects
     */
    public PublicationTypeAssembler(PublicationTypeFactory factory) {

        if (factory == null) {
            throw new IllegalArgumentException("Factory cannot be null");
        }

        this.factory = factory;
    }

    /**
     * Converts a Data Model into a Domain object.
     *
     * @param dm the data model instance
     * @return a reconstructed PublicationType domain object
     * @throws IllegalArgumentException if dm is null
     */
    public PublicationType toDomain(PublicationTypeDM dm) {

        if (dm == null) {
            throw new IllegalArgumentException("PublicationTypeDM cannot be null");
        }

        return factory.create(
                dm.getId(),
                dm.getMediaFormat()
        );
    }

    /**
     * Converts a Domain object into a Data Model.
     *
     * @param domain the domain object
     * @return a data model ready for persistence
     * @throws IllegalArgumentException if domain is null
     */
    public PublicationTypeDM toDataModel(PublicationType domain) {

        if (domain == null) {
            throw new IllegalArgumentException("PublicationType cannot be null");
        }

        return new PublicationTypeDM(
                domain.id(),
                domain.getMediaFormat()
        );
    }
}