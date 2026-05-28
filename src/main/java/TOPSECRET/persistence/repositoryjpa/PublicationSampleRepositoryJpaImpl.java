package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.persistence.repositoryjpa.assembler.PublicationSampleAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.PublicationSampleDM;
import TOPSECRET.persistence.repositoryjpa.jpa.IPublicationSampleRepositoryJpa;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class PublicationSampleRepositoryJpaImpl implements IPublicationSampleRepository {

    private final IPublicationSampleRepositoryJpa publicationSampleRepositoryJpa;
    private final PublicationSampleAssembler publicationSampleAssembler;

    public PublicationSampleRepositoryJpaImpl(IPublicationSampleRepositoryJpa publicationSampleRepositoryJpa,
                                              PublicationSampleAssembler publicationSampleAssembler) {
        this.publicationSampleRepositoryJpa = publicationSampleRepositoryJpa;
        this.publicationSampleAssembler = publicationSampleAssembler;
    }

    // =========================================================================
    // Persistence
    // =========================================================================

    /** {@inheritDoc} */
    @Override
    @Transactional
    public PublicationSample save(PublicationSample publicationSample) {
        Objects.requireNonNull(publicationSample, "PublicationSample cannot be null");

        PublicationSampleDM dm = publicationSampleAssembler.toDM(publicationSample);
        PublicationSampleDM saved = publicationSampleRepositoryJpa.save(dm);
        return publicationSampleAssembler.toDomain(saved);
    }


    // =========================================================================
    // Lookup — by identity
    // =========================================================================

    /** {@inheritDoc} */
    @Override
    public boolean existsById(PublicationSampleId publicationSampleId) {
        Objects.requireNonNull(publicationSampleId, "PublicationSampleId cannot be null");
        boolean isExistent = publicationSampleRepositoryJpa.existsById(publicationSampleId);
        return isExistent;
    }

    /** {@inheritDoc} */
    @Override
    public List<PublicationSample> findAll() {
        List<PublicationSample> allPublications =
                publicationSampleRepositoryJpa.findAll()
                .stream()
                .map(publicationSampleAssembler::toDomain)
                .toList();

        return allPublications;
    }

    /** {@inheritDoc} */
    @Override
    public Optional<PublicationSample> findById(PublicationSampleId publicationSampleId) {

        Objects.requireNonNull(publicationSampleId, "PublicationSampleId cannot be null");

        Optional<PublicationSample> publicationSample =
                publicationSampleRepositoryJpa.findById(publicationSampleId)
                .map(publicationSampleAssembler::toDomain);

        return publicationSample;
    }

    // =========================================================================
    // Lookup — by associated identifiers
    // =========================================================================

    /** {@inheritDoc} */
    @Override
    public List<PublicationSampleId> findIdsByBibliographicalDataId(BibliographicalDataId bdId) {

        Objects.requireNonNull(bdId, "BibliographicalDataId cannot be null");

        List<PublicationSampleId> allPublicationsWithSpecificBD =
                publicationSampleRepositoryJpa.findAllByBdId(bdId)
                .stream()
                .map(PublicationSampleDM::getPubSampleId).toList();

        return allPublicationsWithSpecificBD;
    }

    /** {@inheritDoc} */
    @Override
    public List<PublicationSampleId> findIdsByBibliographicalDataIdIn(List<BibliographicalDataId> bdIds) {

        Objects.requireNonNull(bdIds, "BibliographicalDataIds cannot be null");

        List<PublicationSampleId> allPublicationsWithSpecificBDs =
                publicationSampleRepositoryJpa.findAllByBdIdIn(bdIds)
                .stream()
                .map(PublicationSampleDM::getPubSampleId)
                .toList();

        return allPublicationsWithSpecificBDs;
    }

    /** {@inheritDoc} */
    @Override
    public List<PublicationSampleId> findIdsByEditionIdIn(List<EditionId> listOfEditionIds) {

        Objects.requireNonNull(listOfEditionIds, "EditionIds cannot be null");

        List<PublicationSampleId> allPublicationsWithSpecificEditions =
                publicationSampleRepositoryJpa.findAllByEditionIdIn(listOfEditionIds)
                .stream()
                .map(PublicationSampleDM::getPubSampleId)
                .toList();

        return allPublicationsWithSpecificEditions;
    }

    /** {@inheritDoc} */
    @Override
    public List<PublicationSample> findByLibraryId(LibraryId libraryId) {

        Objects.requireNonNull(libraryId, "LibraryId cannot be null");

        List<PublicationSample> allPublicationsInLibrary =
                publicationSampleRepositoryJpa.findByLibraryId(libraryId)
                .stream()
                .map(publicationSampleAssembler::toDomain)
                .toList();

        return allPublicationsInLibrary;
    }

    /** {@inheritDoc} */
    @Override
    public List<PublicationSample> findByCollectionOfPublicationsId(CollectionOfPublicationsId collectionId) {

        Objects.requireNonNull(collectionId, "CollectionId cannot be null");

        List<PublicationSample> allPublicationsInCollection =
                publicationSampleRepositoryJpa.findAllByCollectionOfPublicationsId(collectionId)
                .stream()
                .map(publicationSampleAssembler::toDomain)
                .toList();

        return allPublicationsInCollection;
    }

    /** {@inheritDoc} */
    @Override
    public List<PublicationSample> findByUserIdAndLibraryIdIsNotNull(UserId userId) {

        Objects.requireNonNull(userId, "UserId cannot be null");

        List<PublicationSample> allPublicationsInUserLibrary =
                publicationSampleRepositoryJpa.findAllByOwnerId(userId)
                .stream()
                .map(publicationSampleAssembler::toDomain)
                .filter(PublicationSample::isInUserLibrary)
                .toList();

        return allPublicationsInUserLibrary;
    }

    /** {@inheritDoc} */
    @Override
    public List<PublicationSample> findByUserIdAndLibraryIdIsNull(UserId userId) {

        Objects.requireNonNull(userId, "UserId cannot be null");

        List<PublicationSample> allPublicationsOwnedByUserAndNotInLibrary =
                publicationSampleRepositoryJpa.findAllByOwnerId(userId)
                .stream()
                .map(publicationSampleAssembler::toDomain)
                .filter(p -> !p.isInUserLibrary())
                .toList();

        return allPublicationsOwnedByUserAndNotInLibrary;
    }

    // =========================================================================
    // Domain Operations
    // =========================================================================

    /** {@inheritDoc} */
    @Override
    public List<PublicationSample> findByLibraryIdAndPublicationStatusNotIn(LibraryId libraryId, List<PublicationStatus> excludedStatuses) {

        Objects.requireNonNull(libraryId, "LibraryId cannot be null");
        Objects.requireNonNull(excludedStatuses, "ExcludedStatuses cannot be null");


        List<PublicationSample> allPublicationsInLibraryAvailableForSale =
                publicationSampleRepositoryJpa
                .findAllByLibraryIdAndPublicationStatusNotIn(libraryId, excludedStatuses)
                .stream()
                .map(publicationSampleAssembler::toDomain)
                .toList();

        return allPublicationsInLibraryAvailableForSale;
    }
}