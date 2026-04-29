package TOPSECRET.services;

import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Edition.EditionFactory;
import TOPSECRET.domain.Edition.IEditionRepository;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.ValueObjects.Code;
import TOPSECRET.domain.ValueObjects.PhysicalAttributes;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.domain.ValueObjects.PublisherID;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;

/**
 * Application service responsible for managing {@link Edition} creation and persistence.
 *
 * <p>
 * This service coordinates the interaction between the domain layer and the
 * persistence layer. It delegates the creation of {@link Edition} aggregates
 * to the {@link EditionFactory} and ensures their persistence through the
 * {@link IEditionRepository}.
 * </p>
 *
 * <p>
 * This class does not contain business rules related to the internal consistency
 * of the {@link Edition} aggregate. Such rules are enforced within the domain layer.
 * Its responsibility is to orchestrate operations and validate input parameters.
 * </p>
 *
 * <p>
 * All input parameters are validated to be non-null before processing.
 * </p>
 */
@Service
public class EditionService {

    private final IEditionRepository editionRepository;

    private final EditionFactory factory;

    public EditionService(IEditionRepository editionRepository, EditionFactory factory) {
        this.editionRepository = editionRepository;
        this.factory = factory;
    }

    /**
     * Creates a new {@link Edition} and persists it in the repository.
     *
     * <p>
     * This method performs the following steps:
     * <ol>
     *     <li>Validates that all input parameters are non-null.</li>
     *     <li>Delegates the creation of the {@link Edition} to the {@link EditionFactory}.</li>
     *     <li>Persists the created {@link Edition} using the {@link IEditionRepository}.</li>
     *     <li>Returns the persisted {@link Edition}.</li>
     * </ol>
     * </p>
     *
     * @param editionNumber the edition number identifier
     * @param code the unique code associated with the edition
     * @param physicalAttributes the physical characteristics of the edition
     * @param publicationTypeId the type of publication
     * @param publisherID the publisher responsible for the edition
     * @param mediaFormat the format of the media (e.g., Book, Digital)
     * @param editionLanguage the language of the edition
     * @param editionDate the publication date
     * @param typeOfBinding the binding type of the edition
     *
     * @return the persisted {@link Edition}
     *
     * @throws NullPointerException if any of the parameters is {@code null}
     */
    public Edition createAndSave(String editionNumber,
                                 Code code,
                                 PhysicalAttributes physicalAttributes,
                                 PublicationTypeId publicationTypeId,
                                 PublisherID publisherID,
                                 String mediaFormat,
                                 Locale editionLanguage,
                                 LocalDate editionDate,
                                 TypeOfBinding typeOfBinding){

        Objects.requireNonNull(editionNumber, "Edition Number cannot be null");
        Objects.requireNonNull(code, "Code cannot be null");
        Objects.requireNonNull(physicalAttributes, "PhysicalAttributes cannot be null");
        Objects.requireNonNull(publicationTypeId, "PublicationTypeId cannot be null");
        Objects.requireNonNull(publisherID, "PublisherID cannot be null");
        Objects.requireNonNull(mediaFormat, "MediaFormat cannot be null");
        Objects.requireNonNull(editionLanguage, "EditionLanguage cannot be null");
        Objects.requireNonNull(editionDate, "EditionDate cannot be null");
        Objects.requireNonNull(typeOfBinding, "TypeOfBinding cannot be null");

        Edition editionCreated = factory.create(editionNumber,
                code,
                physicalAttributes,
                publicationTypeId,
                publisherID,
                mediaFormat,
                editionLanguage,
                editionDate,
                typeOfBinding);

        Edition editionSaved = editionRepository.save(editionCreated);

        return editionSaved;

    }
}
