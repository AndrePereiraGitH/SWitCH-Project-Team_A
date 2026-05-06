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

import java.util.List;
import java.util.Objects;

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

        if (collectionRepositoryJpa.existsById(collectionOfPublications.id())) {
            throw new IllegalArgumentException("CollectionOfPublications already exists");
        }

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
    public List<CollectionOfPublications> getUserCollections(UserId ownerId) {
        return List.of();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean makeCollectionPublic(CollectionOfPublicationsId id) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CollectionOfPublications> findPublicCollectionsByGenreId(GenreId genreId) {
        return List.of();
    }
}
