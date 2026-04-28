package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Edition.EditionFactory;
import TOPSECRET.persistence.repositoryjpa.datamodel.EditionDM;
import org.springframework.stereotype.Component;


/**
 * Converts between Edition domain objects and EditionDM data models.
 *
 * <p>
 * This component is responsible for mapping data between the Domain Layer
 * and the Persistence Layer, ensuring that domain objects remain independent
 * from database representations.
 * </p>
 *
 * <p>
 * Uses {@link EditionFactory} to recreate domain objects while preserving
 * their identity and validation rules.
 * </p>
 */
@Component
public class EditionAssembler {

    private final EditionFactory editionFactory;

    public EditionAssembler(EditionFactory editionFactory){
        this.editionFactory = editionFactory;
    }

    /**
     * Converts an Edition into EditionDM.
     *
     * @param edition the domain object
     * @return the corresponding data model
     */
    public EditionDM toDM(Edition edition) {

        if(edition == null){
            throw new IllegalArgumentException("Edition cannot be null");
        }

        return new EditionDM(
                edition.id(),
                edition.getEditionNumber(),
                edition.getCode(),
                edition.getPhysicalAttributes(),
                edition.getPublicationTypeID(),
                edition.getPublisherID(),
                edition.getMediaFormat(),
                edition.getEditionLanguage(),
                edition.getEditionDate(),
                edition.getTypeOfBinding()
        );

    }

    /**
     * Converts EditionDM into Edition.
     *
     * @param editionDM the data model
     * @return the domain object
     */
    public Edition toDomain(EditionDM editionDM) {

        Edition edition = editionFactory.create(
                editionDM.getEditionID(),
                editionDM.getEditionNumber(),
                editionDM.getCode(),
                editionDM.getPhysicalAttributes(),
                editionDM.getPublicationTypeId(),
                editionDM.getPublisherId(),
                editionDM.getMediaFormat(),
                editionDM.getEditionLanguage(),
                editionDM.getEditionDate(),
                editionDM.getTypeOfBinding()
                );

        return edition;

    }

}
