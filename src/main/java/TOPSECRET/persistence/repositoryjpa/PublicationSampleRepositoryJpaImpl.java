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

    public PublicationSampleRepositoryJpaImpl(IPublicationSampleRepositoryJpa publicationSampleRepositoryJpa, PublicationSampleAssembler publicationSampleAssembler) {
        this.publicationSampleRepositoryJpa = publicationSampleRepositoryJpa;
        this.publicationSampleAssembler = publicationSampleAssembler;
    }

    // =========================================================================
    // Persistence
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public PublicationSample save(PublicationSample publicationSample) {

        Objects.requireNonNull(publicationSample, "PublicationSample cannot be null");

        if(publicationSampleRepositoryJpa.existsById(publicationSample.id())){
            throw new IllegalArgumentException("PublicationSample already exists in the repository");
        }
        PublicationSampleDM pbSampleDM = publicationSampleAssembler.toDM(publicationSample);

        PublicationSampleDM pbSampleDMSaved = publicationSampleRepositoryJpa.save(pbSampleDM);

        PublicationSample pbSampleSaved = publicationSampleAssembler.toDomain(pbSampleDMSaved);

        return pbSampleSaved;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(PublicationSampleId publicationSampleId) {

        Objects.requireNonNull(publicationSampleId, "PublicationSampleId cannot be null");

        if(publicationSampleRepositoryJpa.existsById(publicationSampleId)){
            return true;
        }
        return false;
    }

    // =========================================================================
    // Lookup — by identity
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PublicationSample> findAll() {

        List<PublicationSample> allPublications;

        allPublications = publicationSampleRepositoryJpa.findAll().stream()
                .map(publicationSampleAssembler::toDomain)
                .toList();

        return allPublications;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<PublicationSample> findById(PublicationSampleId publicationSampleId) {

        Objects.requireNonNull(publicationSampleId, "PublicationSampleId cannot be null");

        Optional<PublicationSample> pbSample = publicationSampleRepositoryJpa.findById(publicationSampleId)
                .map(publicationSampleAssembler::toDomain);

        return pbSample;
    }

    // =========================================================================
    // Lookup — by associated identifiers
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PublicationSampleId> findIdsByBibliographicalDataId(BibliographicalDataID bdId) {

        Objects.requireNonNull(bdId, "BibliographicalDataID cannot be null");

        List<PublicationSampleId> listOfPubSampleId = publicationSampleRepositoryJpa.findAllByBdId(bdId)
                .stream()
                .map(PublicationSampleDM::getPubSampleId)
                .toList();

        return listOfPubSampleId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PublicationSampleId> findIdsByBibliographicalDataIdIn(List<BibliographicalDataID> bdIds) {


        Objects.requireNonNull(bdIds, "BibliographicalData Ids cannot be null");

        return publicationSampleRepositoryJpa
                .findAllByBdIdIn(bdIds)
                .stream()
                .map(PublicationSampleDM::getPubSampleId)
                .toList();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PublicationSampleId> findIdsByEditionIdIn(List<EditionID> listOfEditionIds) {

        if (listOfEditionIds == null) {
            throw new IllegalArgumentException("listOfEditionIds cannot be null");
        }

        if (listOfEditionIds.isEmpty()) {
            return List.of();
        }

        List<PublicationSampleDM> publicationSamples =
                publicationSampleRepositoryJpa.findAllByEditionIdIn(listOfEditionIds);

        return publicationSamples.stream()
                .map(PublicationSampleDM::getPubSampleId)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PublicationSample> findByLibraryId(LibraryId libraryId) {

        Objects.requireNonNull(libraryId, "LibraryId cannot be null");

        List<PublicationSampleDM> samplesDMList = publicationSampleRepositoryJpa.findByLibraryId(libraryId);

        return samplesDMList.stream().map(publicationSampleAssembler::toDomain).toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PublicationSample> findByCollectionOfPublicationsId(CollectionOfPublicationsId collectionID) {

        return List.of();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PublicationSample> findByUserIdAndLibraryIdIsNotNull(UserId userId) {

        Objects.requireNonNull(userId,"UserId cannot be null");

        List<PublicationSample> listOfPublications = publicationSampleRepositoryJpa.findAllByOwnerId(userId)
                .stream().map(publicationSampleAssembler::toDomain)
                .filter(PublicationSample::isInUserLibrary).toList();

        return listOfPublications;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PublicationSample> findByUserIdAndLibraryIdIsNull(UserId userId) {
        return List.of();
    }


    // =========================================================================
    // Domain Operations
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addPublicationToLibrary(PublicationSample publicationSample, LibraryId libraryId) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PublicationSample> findAvailableForSaleByLibraryId(LibraryId libraryId) {

        Objects.requireNonNull(libraryId, "LibraryId cannot be null");

        List<PublicationStatus> excluded = List.of(PublicationStatus.SOLD, PublicationStatus.FOR_SALE);

        return publicationSampleRepositoryJpa
                .findAllByLibraryIdAndPublicationStatusNotIn(libraryId, excluded)
                .stream()
                .map(publicationSampleAssembler::toDomain)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean assignPublicationsToCollection(CollectionOfPublicationsId collectionId, List<PublicationSampleId> publicationSampleIds) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PublicationSample> findAvailableForCollection(UserId userId, CollectionOfPublicationsId collectionId) {
        return List.of();
    }
}