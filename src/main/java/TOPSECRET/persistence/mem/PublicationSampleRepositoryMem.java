package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.PublicationSample.PublicationSampleFactory;
import TOPSECRET.domain.ValueObjects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * In-memory implementation of {@link IPublicationSampleRepository}.
 *
 * <p>This class stores {@link PublicationSample} objects in an internal
 * collection and uses a {@link PublicationSampleFactory} to create
 * new instances.</p>
 *
 * <p>It provides basic operations to manage publication samples such as creation,
 * addition, retrieval, and existence verification, without any persistence mechanism.</p>
 */
public class PublicationSampleRepositoryMem implements IPublicationSampleRepository {

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    private List<PublicationSample> allPublicationsInSystem;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructs a new {@code PublicationSampleRepositoryMem}
     * Initializes an empty internal list of publication samples.
     */
    public PublicationSampleRepositoryMem() {
        allPublicationsInSystem = new ArrayList<>();
    }

    // -------------------------------------------------------------------------
    // Persistence
    // -------------------------------------------------------------------------

    /** {@inheritDoc} */
    @Override
    public PublicationSample save(PublicationSample publicationSample) {
        if (existsById(publicationSample.id())) {
            throw new IllegalArgumentException("PublicationSample already exists in the repository");
        }
        allPublicationsInSystem.add(publicationSample);
        return publicationSample;
    }

    /** {@inheritDoc} */
    @Override
    public boolean existsById(PublicationSampleId id) {
        if (id == null) {
            throw new IllegalArgumentException("PublicationSample cannot be null");
        }

        for (PublicationSample pbSample : allPublicationsInSystem) {
            if (pbSample.id().equals(id)) {
                return true;
            }
        }
        return false;
    }


    // -------------------------------------------------------------------------
    // Identity Lookup
    // -------------------------------------------------------------------------

    /** {@inheritDoc} */
    @Override
    public Optional<PublicationSample> findById(PublicationSampleId id) {
        if (id == null) {
            throw new NullPointerException("PublicationSampleID cannot be null");
        }

        for (PublicationSample pbSample : allPublicationsInSystem) {
            if (pbSample.id().equals(id)) {
                return Optional.of(pbSample);
            }
        }
        return Optional.empty();
    }

    /** {@inheritDoc} */
    @Override
    public List<PublicationSample> findAll() {
        return List.copyOf(allPublicationsInSystem);
    }

    // -------------------------------------------------------------------------
    // Lookup - by associated identifiers
    // -------------------------------------------------------------------------

    /** {@inheritDoc} */
    @Override
    public List<PublicationSampleId> findIdsByBibliographicalDataId(BibliographicalDataId bdId) {
        if (bdId == null) {
            throw new NullPointerException("PublicationSampleID cannot be null");
        }

        List<PublicationSampleId> listOfPubSampleId = new ArrayList<>();

        for (PublicationSample pbSample : allPublicationsInSystem) {
            if (pbSample.getBibliographicalDataID().equals(bdId)) {
                listOfPubSampleId.add(pbSample.id());
            }
        }
        return listOfPubSampleId;
    }

    /** {@inheritDoc} */
    @Override
    public List<PublicationSampleId> findIdsByEditionIdIn(List<EditionId> listOfEditionIds) {
        if (listOfEditionIds == null) {
            throw new NullPointerException("listOfEditionIds cannot be null");
        }

        List<PublicationSampleId> listOfPubSampleIds = new ArrayList<>();

        for (EditionId editionId : listOfEditionIds) {
            for (PublicationSample pbSample : allPublicationsInSystem) {
                if (pbSample.getEditionID().equals(editionId)) {
                    listOfPubSampleIds.add(pbSample.id());
                }
            }
        }
        return listOfPubSampleIds;
    }

    /** {@inheritDoc} */
    @Override
    public List<PublicationSampleId> findIdsByBibliographicalDataIdIn(List<BibliographicalDataId> bdIds) {
        if (bdIds == null) {
            throw new IllegalArgumentException("BibliographicalDataID List cannot be null");
        }

        List<PublicationSampleId> publicationSampleIds = new ArrayList<>();

        for (PublicationSample pbSample : allPublicationsInSystem) {
            BibliographicalDataId bdId = pbSample.getBibliographicalDataID();

            for (BibliographicalDataId bdId2 : bdIds) {
                if (bdId.equals(bdId2)) {
                    publicationSampleIds.add(pbSample.id());
                    break;
                }
            }
        }

        return publicationSampleIds;
    }

    /** {@inheritDoc} */
    @Override
    public List<PublicationSample> findByUserIdAndLibraryIdIsNotNull(UserId userId) {
        if (userId == null) {
            throw new NullPointerException("UserId must not be null");
        }

        List<PublicationSample> listOfPublications = new ArrayList<>();

        for (PublicationSample pubSample : allPublicationsInSystem) {
            if (pubSample.isOwnedByUser(userId) && pubSample.isInUserLibrary()) {
                listOfPublications.add(pubSample);
            }
        }

        return listOfPublications;
    }

    /** {@inheritDoc} */
    @Override
    public List<PublicationSample> findByUserIdAndLibraryIdIsNull(UserId userId) {
        if (userId == null) {
            throw new NullPointerException("UserId must not be null");
        }

        List<PublicationSample> listOfPublicationsNotInLibrary = new ArrayList<>();

        for (PublicationSample pubSample : allPublicationsInSystem) {
            if (pubSample.isOwnedByUser(userId) && !pubSample.isInUserLibrary()) {
                listOfPublicationsNotInLibrary.add(pubSample);
            }
        }

        return listOfPublicationsNotInLibrary;
    }

    /** {@inheritDoc} */
    @Override
    public List<PublicationSample> findByLibraryId(LibraryId libraryId) {
        return allPublicationsInSystem.stream()
                .filter(p -> Objects.equals(p.getLibraryID(), libraryId))
                .toList();
    }

    /** {@inheritDoc} */
    @Override
    public List<PublicationSample> findByCollectionOfPublicationsId(CollectionOfPublicationsId collectionId) {

        List<PublicationSample> listOfPublications = allPublicationsInSystem.stream()
                .filter(p -> Objects.equals(p.getCollecOfPubID(), collectionId))
                .toList();

        return listOfPublications;
    }

    // -------------------------------------------------------------------------
    // Domain Operations
    // -------------------------------------------------------------------------

    /** {@inheritDoc} */
    @Override
    public List<PublicationSample> findByLibraryIdAndPublicationStatusNotIn(LibraryId libraryId, List<PublicationStatus> excludedStatuses) {

        if (libraryId == null || excludedStatuses == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        List<PublicationSample> listOfPublications =
                allPublicationsInSystem.stream()
                .filter(p -> Objects.equals(p.getLibraryID(), libraryId))
                .filter(p -> !excludedStatuses.contains(p.getPublicationStatus()))
                .toList();

        return listOfPublications;
    }


}