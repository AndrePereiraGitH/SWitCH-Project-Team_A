package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Auction.IAuctionRepository;
import TOPSECRET.domain.ValueObjects.AuctionId;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.persistence.repositoryjpa.assembler.AuctionAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.AuctionDM;
import TOPSECRET.persistence.repositoryjpa.jpa.IAuctionRepositoryJPA;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * JPA implementation of the {@link IAuctionRepository} interface.
 * <p>
 * This repository acts as an adapter between the domain layer and the
 * persistence layer, delegating database operations to
 * {@link IAuctionRepositoryJPA} and converting between domain objects
 * ({@link Auction}) and persistence models ({@link AuctionDM}) using
 * {@link AuctionAssembler}.
 * </p>
 * <p>
 * It encapsulates all mapping logic required to persist and retrieve
 * auctions while keeping the domain layer independent from JPA.
 * </p>
 */
@Repository
public class AuctionRepositoryJPAImpl implements IAuctionRepository {

    private final IAuctionRepositoryJPA auctionRepoJPA;
    private final AuctionAssembler auctionAssembler;

    /**
     * Constructs a new {@code AuctionRepositoryJPAImpl}.
     *
     * @param auctionRepoJPA the Spring Data JPA repository used for database operations; must not be {@code null}
     * @param auctionAssembler the assembler responsible for converting between {@link Auction} and {@link AuctionDM};
     *                        must not be {@code null}
     *
     * @throws IllegalArgumentException if any dependency is {@code null}
     */
    public AuctionRepositoryJPAImpl(IAuctionRepositoryJPA auctionRepoJPA,
                                    AuctionAssembler auctionAssembler) {

        if (auctionRepoJPA == null) {
            throw new IllegalArgumentException("iAuctionRepoJPA cannot be null");
        }
        if (auctionAssembler == null) {
            throw new IllegalArgumentException("auctionAssembler cannot be null");
        }

        this.auctionRepoJPA = auctionRepoJPA;
        this.auctionAssembler = auctionAssembler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Auction save(Auction auction) {

        Objects.requireNonNull(auction, "auction cannot be null");

        if(auctionRepoJPA.existsById(auction.id())){
            throw new IllegalArgumentException("auction already exists");
        }

        AuctionDM auctionDM = auctionAssembler.toDM(auction);

        AuctionDM savedAuctionDM = auctionRepoJPA.save(auctionDM);

        return auctionAssembler.toDomain(savedAuctionDM);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Auction> findById(AuctionId auctionId) {

        Objects.requireNonNull(auctionId, "auctionId cannot be null");

        Optional<AuctionDM> opAuctionDM = auctionRepoJPA.findById(auctionId);

        if(opAuctionDM.isPresent()) {
            Auction auctionDomain = auctionAssembler.toDomain(opAuctionDM.get());
            return Optional.of(auctionDomain);
        }
        else
            return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Auction> findAll() {
        List<AuctionDM> auctionsDM = auctionRepoJPA.findAll();

        List<Auction> auctionsDomain = auctionsDM.stream()
                .map(auctionAssembler::toDomain)
                .collect(Collectors.toList());

        return auctionsDomain;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Auction> findByListingId(List<ListingId> listingIds) {

        if (listingIds == null) {
            throw new IllegalArgumentException("ListingIds can't be null.");
        }

        List<AuctionDM> auctionsDM = auctionRepoJPA.findByListingIdIn(listingIds);

        List<Auction> auctionsDomain = auctionsDM.stream()
                .map(auctionAssembler::toDomain)
                .collect(Collectors.toList());

        return auctionsDomain;
    }
}
