package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Edition.IEditionRepository;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.persistence.repositoryjpa.assembler.EditionAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.EditionDM;
import TOPSECRET.persistence.repositoryjpa.jpa.IEditionRepositoryJpa;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * JPA implementation of the {@link IEditionRepository} interface.
 *
 * <p>
 * This repository is responsible for persisting and retrieving {@link Edition}
 * aggregates using Spring Data JPA.
 * </p>
 *
 * <p>
 * It converts between Domain objects and Data Model objects using
 * {@link EditionAssembler}, ensuring that the domain layer remains
 * independent from persistence concerns.
 * </p>
 *
 * <p>
 * This class does not contain business logic. Its responsibility is limited
 * to data access and object mapping.
 * </p>
 */
@Repository
public class EditionRepositoryJPAImpl implements IEditionRepository {

    @Autowired
    private IEditionRepositoryJpa editionRepositoryJpa;

    @Autowired
    private EditionAssembler editionAssembler;

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
    @Transactional
    @Override
    public Edition save(Edition edition) {

        if(edition == null){
            throw new IllegalArgumentException("Edition cannot be null");
        }

        EditionDM editionDM = editionAssembler.toDM(edition);

        EditionDM editionDMPersisted = editionRepositoryJpa.save(editionDM);

        Edition editionSaved = editionAssembler.toDomain(editionDMPersisted);

        return editionSaved;
    }

    /**
     * Checks whether an Edition exists with the given identifier.
     *
     * @param id the unique identifier of the Edition; must not be null
     * @return {@code true} if an Edition with the given ID exists, {@code false} otherwise
     *
     * @throws NullPointerException if {@code id} is null
     */
    @Override
    public boolean existsById(EditionID id) {

        Objects.requireNonNull(id, "Edition ID cannot be null");
        if(editionRepositoryJpa.existsById(id)){
            return true;
        }
        return false;
    }

    /**
     * Retrieves an Edition by its identifier.
     *
     * @param editionID the identifier of the Edition
     * @return an Optional containing the Edition if found,
     *         or empty if no Edition exists with the given ID
     */
    @Override
    public Optional<Edition> findById(EditionID editionID) {

        Optional<EditionDM> optionalEditionDM = editionRepositoryJpa.findById(editionID);

        if (optionalEditionDM.isPresent()) {
            Edition edition = editionAssembler.toDomain(optionalEditionDM.get());
            return Optional.of(edition);
        } else {
            return Optional.empty();
        }
    }

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
    @Override
    public List<Edition> findAll() {

        List<Edition> allEditions;

        allEditions = editionRepositoryJpa.findAll().stream()
                .map(editionAssembler::toDomain)
                .toList();

        return allEditions;
    }

    /**
     * Retrieves all Edition identifiers associated with a given Publisher.
     *
     * <p>
     * This method searches all persisted editions and returns the IDs
     * of those that match the provided publisher identifier.
     * </p>
     *
     * @param publisherId the identifier of the Publisher
     * @return a list of EditionIDs belonging to the given Publisher
     */
    @Override
    public List<EditionID> findEditionIdsByPublisherId(PublisherID publisherId) {

        Objects.requireNonNull(publisherId, "PublisherID cannot be null");

        List<EditionDM> editionsDM = editionRepositoryJpa.findAll();

        List<EditionID> editionIds = new ArrayList<>();

        for (EditionDM editionDM : editionsDM) {
            if (editionDM.getPublisherId().equals(publisherId)) {
                editionIds.add(editionDM.getEditionID());
            }
        }

        return editionIds;
    }

    /**
     * Retrieves all Edition aggregates associated with the given publication type.
     *
     * <p>
     * This method queries the persistence layer for all {@link EditionDM} entities
     * matching the provided {@link PublicationTypeId}, and converts them into
     * domain {@link Edition} objects using the assembler.
     * </p>
     *
     * @param publicationTypeId the identifier of the publication type; must not be null
     * @return a list of {@link Edition} matching the given publication type;
     *         never {@code null}, but may be empty
     *
     * @throws IllegalArgumentException if {@code publicationTypeId} is null
     */
    @Override
    public List<Edition> findByPublicationTypeId(PublicationTypeId publicationTypeId) {

        List<EditionDM> editionsDM = editionRepositoryJpa.findByPublicationTypeId(publicationTypeId);

        return editionsDM.stream()
                .map(editionAssembler::toDomain)
                .toList();

    }

    /**
     * Retrieves all Edition aggregates associated with the given publisher.
     *
     * <p>
     * This method queries the persistence layer for all {@link EditionDM} entities
     * matching the provided {@link PublisherID}, and converts them into
     * domain {@link Edition} objects using the assembler.
     * </p>
     *
     * @param publisherId the identifier of the publisher; must not be null
     * @return a list of {@link Edition} belonging to the given publisher;
     *         never {@code null}, but may be empty
     *
     * @throws IllegalArgumentException if {@code publisherId} is null
     */
    @Override
    public List<Edition> findByPublisherId(PublisherID publisherId) {

        List<EditionDM> editionsDM = editionRepositoryJpa.findByPublisherId(publisherId);

        return editionsDM.stream()
                .map(editionAssembler::toDomain)
                .toList();
    }
}
