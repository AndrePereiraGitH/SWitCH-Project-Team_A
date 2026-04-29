package TOPSECRET.dto;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.ValueObjects.ListingId;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Mapper responsible for converting {@link Auction} domain objects
 * into {@link US026AuctionDTO} data transfer objects.
 */
public class US026AuctionMapper {

    private final IListingRepository listingRepo;

    /**
     * Creates a mapper with access to the listing repository.
     *
     * @param listingRepo the repository used to retrieve listings by their id
     */
    public US026AuctionMapper(IListingRepository listingRepo) {
        this.listingRepo = listingRepo;
    }

    /**
     * Converts a domain {@link Auction} into a {@link US026AuctionDTO}.
     * <p>
     * The corresponding listing is retrieved from the repository in order
     * to include the listing title in the DTO.
     *
     * @param auction the auction to be converted
     * @return a DTO containing the auction data
     * @throws InstanceNotFoundException if the listing associated with the auction is not found
     */
    public US026AuctionDTO fromDomain(Auction auction) throws InstanceNotFoundException {

        ListingId listingId = auction.getListingId();
        Optional<Listing> listing = listingRepo.findById(listingId);
        if (listing.isEmpty()) {
            throw new InstanceNotFoundException("Listing not found");
        }
        return new US026AuctionDTO(
                auction.id().getId(),
                listing.get().getListingTitle(),
                auction.getAuctionStatus().name()
        );
    }

    /**
     * Converts a list of domain {@link Auction} objects into
     * a list of {@link US026AuctionDTO}.
     *
     * @param auctionsList the list of auctions to be converted
     * @return a list of DTOs containing the auctions data
     * @throws InstanceNotFoundException if at least one associated listing is not found
     */
    public List<US026AuctionDTO> fromDomainList(List<Auction> auctionsList) throws InstanceNotFoundException {

        List<US026AuctionDTO> auctionDtoList = new ArrayList<>();

        for (Auction auction : auctionsList) {
            auctionDtoList.add(fromDomain(auction));
        }

        return auctionDtoList;
    }

}
