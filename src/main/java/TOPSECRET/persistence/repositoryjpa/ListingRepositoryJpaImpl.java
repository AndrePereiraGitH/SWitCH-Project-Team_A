package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.persistence.repositoryjpa.assembler.ListingAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.ListingDM;
import TOPSECRET.persistence.repositoryjpa.jpa.IListingRepositoryJpa;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * JPA-backed implementation of {@link IListingRepository}.
 *
 * <p>Delegates all persistence operations to {@link IListingRepositoryJpa} and uses
 * {@link ListingAssembler} to convert between the domain model ({@link Listing}) and
 * the persistence data model ({@link ListingDM}).</p>
 *
 */
@Repository
public class ListingRepositoryJpaImpl implements IListingRepository {

    private final IListingRepositoryJpa listingRepositoryJpa;
    private final ListingAssembler assembler;

    public ListingRepositoryJpaImpl(IListingRepositoryJpa listingRepositoryJpa, ListingAssembler assembler) {
        this.listingRepositoryJpa = listingRepositoryJpa;
        this.assembler = assembler;
    }


    // =========================================================================
    // Persistence
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Listing save(Listing listing) {
        Objects.requireNonNull(listing);
        ListingDM saved = listingRepositoryJpa.save(assembler.toDM(listing));
        return assembler.toDomain(saved);
    }


    // =========================================================================
    // Lookup — by identity
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Listing> findById(ListingId id) {
        Objects.requireNonNull(id);
        return listingRepositoryJpa.findById(id)
                .map(assembler::toDomain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Listing> findAll() {
        return listingRepositoryJpa.findAll()
                .stream()
                .map(assembler::toDomain)
                .toList();
    }


    // =========================================================================
    // Lookup — by sale type
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Listing> findAllByDirectSale() {
        return findAll()
                .stream()
                .filter(Listing::isListingLinkedToADirectSale)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Listing> findAllByAuction() {
        return findAll()
                .stream()
                .filter(Listing::isListingLinkedToAnAuction)
                .toList();
    }


    // =========================================================================
    // Lookup — by associated identifiers
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ListingId> findIdsByPublicationSampleIdIn(List<PublicationSampleId> publicationSampleIds) {

        Objects.requireNonNull(publicationSampleIds, "List of PublicationSampleIds cannot be null");

        List<ListingId> listOfPublicationSampleIds = listingRepositoryJpa.findAllByItemsId(publicationSampleIds)
                .stream()
                .map(ListingDM::getListingId)
                .toList();
        return listOfPublicationSampleIds;
    }


    // =========================================================================
    // Domain Operations
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Listing> findAllByAuctionAndPublicationSampleIdIn(List<PublicationSampleId> publicationSampleIds) {
        Objects.requireNonNull(publicationSampleIds);
        return listingRepositoryJpa.findAllByItemsId(publicationSampleIds)
                .stream()
                .map(assembler::toDomain)
                .filter(Listing::isListingLinkedToAnAuction)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Listing> findAllByDirectSaleAndPublicationSampleIdIn(List<PublicationSampleId> publicationSampleIds) {
        Objects.requireNonNull(publicationSampleIds);
        return listingRepositoryJpa.findAllByItemsId(publicationSampleIds)
                .stream()
                .map(assembler::toDomain)
                .filter(Listing::isListingLinkedToADirectSale)
                .toList();
    }
}