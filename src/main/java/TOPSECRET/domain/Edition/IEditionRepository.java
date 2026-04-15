package TOPSECRET.domain.Edition;

import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.ValueObjects.*;

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
     * @param publicationTypeId the type of publication
     * @param publisherID the publisher responsible for the edition
     * @param editionLanguage the language of the edition
     * @param editionDate the publication date of the edition
     * @param typeOfBinding the type of binding used in the edition
     * @return a list of created and stored {@link Edition} objects
     */
    List<Edition> createAndAddEdition(String editionNumber, Code code, PhysicalAttributes physicalAttributes, PublicationTypeId publicationTypeId,
                                      PublisherID publisherID, String mediaFormat, Locale editionLanguage, LocalDate editionDate, TypeOfBinding typeOfBinding);

    /**
     * Saves the given {@link Edition} into the repository.
     * <p>
     * An edition is only added to the repository if it doesn't already exist.
     * If it does exist, an exception is thrown.
     * </p>
     *
     * @param edition the edition to be stored; must not be null
     * @return the stored {@link Edition} instance
     *
     * @throws IllegalArgumentException if {@code edition} is null
     */
    Edition save(Edition edition);

    /**
     * Finds an edition by its unique identifier.
     *
     * @param editionID the unique identifier of the edition to search for; must not be {@code null}
     * @return an {@link Optional} containing the matching {@link Edition} if found,
     *         or an empty {@link Optional} if no edition with the given ID exists
     * @throws NullPointerException if {@code editionID} is {@code null}
     */
    Optional<Edition> findEditionById(EditionID editionID);

    /**
     * Retrieves all {@link EditionID} values associated with the given {@link PublisherID}.
     * <p>
     * This method performs a repository-level query that returns the identifiers of all editions
     * published by the specified publisher. If no editions exist for the given publisher, the method
     * returns an empty list rather than null.
     * </p>
     *
     * @param publisherId the identifier of the publisher whose edition IDs should be retrieved;
     *                    must not be null
     * @return an immutable list of {@link EditionID} values associated with the given publisher;
     *         never null, but may be empty
     *
     * @throws IllegalArgumentException if {@code publisherId} is null
     */
    List<EditionID> findEditionIdsByPublisherId(PublisherID publisherId);
}
