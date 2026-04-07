package TOPSECRET.domain.Edition;

import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.Code;
import TOPSECRET.domain.ValueObjects.EditionID;
import TOPSECRET.domain.ValueObjects.PhysicalAttributes;
import TOPSECRET.domain.PublicationType.PublicationType;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Repository interface for managing {@link Edition} entities.
 * <p>
 * Defines the contract for creating and storing editions in the system.
 */

public interface IEditionRepository {
    /**
     * Creates a new {@link Edition} with the given data and adds it to the repository.
     *
     * @param editionNumber the edition number identifier
     * @param code the unique code associated with the edition
     * @param physicalAttributes the physical attributes of the edition
     * @param publicationType the type of publication
     * @param publisher the publisher responsible for the edition
     * @param editionLanguage the language of the edition
     * @param editionDate the publication date of the edition
     * @param typeOfBinding the type of binding used in the edition
     * @return a list of created and stored {@link Edition} objects
     */
    List<Edition> createAndAddEdition(String editionNumber, Code code, PhysicalAttributes physicalAttributes, PublicationType publicationType,
                                      Publisher publisher, Locale editionLanguage, LocalDate editionDate, TypeOfBinding typeOfBinding);

    /**
     * Retrieves an {@link Edition} instance by its identifier.
     * <p>
     * This method searches the in-memory list of stored editions and returns
     * the first element whose {@code id()} matches the provided {@link EditionID}.
     * If no matching edition is found, an empty {@link Optional} is returned.
     * </p>
     *
     * @param id the identifier of the edition to retrieve; must not be {@code null}
     * @return an {@link Optional} containing the matching edition if found,
     *         or an empty {@link Optional} otherwise
     *
     * @throws IllegalArgumentException if {@code id} is {@code null}
     */
    Optional<Edition> findById(EditionID id);
}
