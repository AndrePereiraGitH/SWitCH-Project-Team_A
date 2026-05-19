package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Edition.IEditionRepository;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.persistence.repositoryjpa.assembler.EditionAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.EditionDM;
import TOPSECRET.persistence.repositoryjpa.jpa.IEditionRepositoryJpa;
import jakarta.transaction.Transactional;
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

    private final IEditionRepositoryJpa editionRepositoryJpa;

    private final EditionAssembler editionAssembler;

    public EditionRepositoryJPAImpl(IEditionRepositoryJpa editionRepositoryJpa,
                                    EditionAssembler editionAssembler){
        this.editionRepositoryJpa = Objects.requireNonNull(editionRepositoryJpa);
        this.editionAssembler = Objects.requireNonNull(editionAssembler);
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(EditionId id) {

        Objects.requireNonNull(id, "Edition ID cannot be null");
        if(editionRepositoryJpa.existsById(id)){
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Edition> findById(EditionId editionID) {

        Optional<EditionDM> optionalEditionDM = editionRepositoryJpa.findById(editionID);

        if (optionalEditionDM.isPresent()) {
            Edition edition = editionAssembler.toDomain(optionalEditionDM.get());
            return Optional.of(edition);
        } else {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @Override
    public List<EditionId> findIdsByPublisherId(PublisherID publisherId) {

        Objects.requireNonNull(publisherId, "PublisherID cannot be null");

        List<EditionDM> editionsDM = editionRepositoryJpa.findAll();

        List<EditionId> editionIds = new ArrayList<>();

        for (EditionDM editionDM : editionsDM) {
            if (editionDM.getPublisherId().equals(publisherId)) {
                editionIds.add(editionDM.getEditionID());
            }
        }

        return editionIds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Edition> findByPublicationTypeId(PublicationTypeId publicationTypeId) {

        List<EditionDM> editionsDM = editionRepositoryJpa.findByPublicationTypeId(publicationTypeId);

        return editionsDM.stream()
                .map(editionAssembler::toDomain)
                .toList();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Edition> findByPublisherId(PublisherID publisherId) {

        List<EditionDM> editionsDM = editionRepositoryJpa.findByPublisherId(publisherId);

        return editionsDM.stream()
                .map(editionAssembler::toDomain)
                .toList();
    }
}
