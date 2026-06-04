package TOPSECRET.persistence.repositoryjpa;


import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.persistence.repositoryjpa.assembler.CollectionOfPublicationsAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.CollectionOfPublicationsDM;
import TOPSECRET.persistence.repositoryjpa.jpa.ICollectionOfPublicationsRepositoryJpa;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class CollectionOfPublicationsRepositoryJpaImpl implements ICollectionOfPublicationsRepository {


    private final ICollectionOfPublicationsRepositoryJpa collectionRepositoryJpa;

    private final CollectionOfPublicationsAssembler collectionOfPublicationsAssembler;

    public CollectionOfPublicationsRepositoryJpaImpl(
            ICollectionOfPublicationsRepositoryJpa collectionRepositoryJpa,
            CollectionOfPublicationsAssembler collectionOfPublicationsAssembler) {

        if (collectionRepositoryJpa == null) {
            throw new IllegalArgumentException("CollectionOfPublicationsRepositoryJpa cannot be null");
        }

        if (collectionOfPublicationsAssembler == null) {
            throw new IllegalArgumentException("CollectionOfPublicationsAssembler cannot be null");
        }

        this.collectionRepositoryJpa = collectionRepositoryJpa;
        this.collectionOfPublicationsAssembler = collectionOfPublicationsAssembler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CollectionOfPublications save(CollectionOfPublications collectionOfPublications) {

        Objects.requireNonNull(collectionOfPublications, "collectionOfPublications cannot be null");

        CollectionOfPublicationsDM collectionDM =
                collectionOfPublicationsAssembler.toDM(collectionOfPublications);

        CollectionOfPublicationsDM collectionDMSaved =
                collectionRepositoryJpa.save(collectionDM);

        CollectionOfPublications collectionSaved =
                collectionOfPublicationsAssembler.toDomain(collectionDMSaved);

        return collectionSaved;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(CollectionOfPublicationsId collectionOfPublicationsID) {

        Objects.requireNonNull(collectionOfPublicationsID,
                "collectionOfPublicationsID cannot be null");

        if (collectionRepositoryJpa.existsById(collectionOfPublicationsID)) {
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CollectionOfPublications findById(CollectionOfPublicationsId collectionId) throws InstanceNotFoundException {
        Objects.requireNonNull(collectionId, "CollectionOfPublications Id cannot be null");

        Optional<CollectionOfPublicationsDM> collectionDM = collectionRepositoryJpa.findById(collectionId);

        if (collectionDM.isEmpty()) {
            throw new InstanceNotFoundException("Collection does not exists");
        }
        return collectionOfPublicationsAssembler.toDomain(collectionDM.get());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CollectionOfPublications> findAllByOwnerId(UserId ownerId) {
        Objects.requireNonNull(ownerId, "ownerId cannot be null");
        List<CollectionOfPublicationsDM> ownerCollections = collectionRepositoryJpa.findAllByOwnerId(ownerId);

        List<CollectionOfPublications> listOfUserCollections = ownerCollections.stream()
                .map(collectionOfPublicationsAssembler::toDomain)
                .toList();

        return listOfUserCollections;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CollectionOfPublications> findPublicCollectionsByGenreId(GenreId genreId) {

        Objects.requireNonNull(genreId, "genreId cannot be null");

        List<CollectionOfPublicationsDM> genreCollections =
                collectionRepositoryJpa.findByGenreIdAndIsPrivateFalse(genreId);

        return genreCollections.stream()
                .map(collectionOfPublicationsAssembler::toDomain)
                .filter(CollectionOfPublications::isCurrentlyPublic)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CollectionOfPublications> findAllPublicCollections() {

        List<CollectionOfPublicationsDM> publicCollections =
                collectionRepositoryJpa.findByIsPrivateFalse();

        return publicCollections.stream()
                .map(collectionOfPublicationsAssembler::toDomain)
                .filter(CollectionOfPublications::isCurrentlyPublic)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CollectionOfPublications> findAll() {

        return collectionRepositoryJpa.findAll()
                .stream()
                .map(collectionOfPublicationsAssembler::toDomain)
                .toList();
    }
}