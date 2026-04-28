package TOPSECRET.domain.Edition;

import TOPSECRET.domain.ValueObjects.*;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Edition} entities.
 * <p>
 * Defines the contract for creating and storing editions in the system.
 */

public interface IEditionRepository {

    /**
     * Saves an Edition aggregate to the database.
     *
     * <p>
     * The Edition is first converted into a persistence data model,
     * then stored using JPA, and finally converted back into a
     * domain object.
     * </p>
     *
     * @param edition the Edition to be saved
     * @return the persisted Edition
     */
    Edition save(Edition edition);

    /**
     * Checks whether an Edition exists with the given identifier.
     *
     * @param id the unique identifier of the Edition; must not be null
     * @return {@code true} if an Edition with the given ID exists, {@code false} otherwise
     *
     * @throws NullPointerException if {@code id} is null
     */
    boolean existsById(EditionID id);

    /**
     * Finds an edition by its unique identifier.
     *
     * @param editionID the unique identifier of the edition to search for; must not be {@code null}
     * @return an {@link Optional} containing the matching {@link Edition} if found,
     *         or an empty {@link Optional} if no edition with the given ID exists
     * @throws NullPointerException if {@code editionID} is {@code null}
     */
    Optional<Edition> findById(EditionID editionID);

    /**
     * Retrieves all Edition aggregates stored in the repository.
     *
     * <p>
     * The returned list contains all persisted editions. If no editions exist,
     * an empty list is returned instead of {@code null}.
     * </p>
     *
     * @return an immutable list of all {@link Edition} instances;
     *         never {@code null}, but may be empty
     */
    List<Edition> findAll();

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

    /**
     * Finds all Edition aggregates associated with the given publisher.
     *
     * @param publisherId the publisher identifier; must not be null
     * @return a list of matching {@link Edition}; never {@code null}, may be empty
     *
     *
     */
    List<Edition> findByPublisherId(PublisherID publisherId);

    /**
     * Finds all Edition aggregates associated with the given publication type.
     *
     * @param publicationTypeId the publication type identifier; must not be null
     * @return a list of matching {@link Edition}; never {@code null}, may be empty
     *
     * @throws NullPointerException if {@code publicationTypeIdDouble} is null
     */
    List<Edition> findByPublicationTypeId(PublicationTypeId publicationTypeId);
}
