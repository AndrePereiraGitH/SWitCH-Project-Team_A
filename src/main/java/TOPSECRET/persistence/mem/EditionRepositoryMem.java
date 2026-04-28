package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Edition.EditionFactory;
import TOPSECRET.domain.Edition.IEditionRepository;
import TOPSECRET.domain.ValueObjects.*;

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

/**
 * Repository responsible for managing {@link Edition} entities.
 * <p>
 * Provides operations to create and store new editions while ensuring
 * that duplicate editions are not added.
 */
public class EditionRepositoryMem implements IEditionRepository {

    private final List<Edition> _allEditionsInSystem;

    public EditionRepositoryMem() {

        this._allEditionsInSystem = new ArrayList<>();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Edition save(Edition edition) {
        if (existsById(edition.id())) {
            throw new IllegalArgumentException("Edition with id " + edition.id() + " already exists");
        }
        _allEditionsInSystem.add(edition);
        return edition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(EditionID id) {
        for (Edition edition : _allEditionsInSystem) {
            if (edition.id().equals(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public Optional<Edition> findById(EditionID editionID){

        if(editionID == null){
            throw new NullPointerException("EditionId must not be null");
        }

        for(Edition edition: _allEditionsInSystem){
            if(edition.id().equals(editionID)){
                return Optional.of(edition);
            }
        }
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Edition> findAll() {
        return List.copyOf(_allEditionsInSystem);
    }

    /**
     * {@inheritDoc}
     */
    public List<EditionID> findEditionIdsByPublisherId(PublisherID publisherId) {

        if (publisherId == null) {
            throw new IllegalArgumentException("publisherId must not be null");
        }

        List<EditionID> editionIDs = new ArrayList<>();

        for (Edition edition : _allEditionsInSystem) {
            if (publisherId.equals(edition.getPublisherID())) {
                editionIDs.add(edition.id());
            }
        }

        return editionIDs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Edition> findByPublisherId(PublisherID publisherId) {
        if (publisherId == null) {
            throw new IllegalArgumentException("publisherId must not be null");
        }

        List<Edition> result = new ArrayList<>();
        for (Edition edition : _allEditionsInSystem) {
            if (publisherId.equals(edition.getPublisherID())) {
                result.add(edition);
            }
        }
        return List.copyOf(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Edition> findByPublicationTypeId(PublicationTypeId publicationTypeId) {
        if (publicationTypeId == null) {
            throw new IllegalArgumentException("publicationTypeId must not be null");
        }

        List<Edition> result = new ArrayList<>();
        for (Edition edition : _allEditionsInSystem) {
            if (publicationTypeId.equals(edition.getPublicationTypeID())) {
                result.add(edition);
            }
        }
        return List.copyOf(result);
    }
}

