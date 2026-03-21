package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.AuctionFactory;
import TOPSECRET.dto.AuthorDTO_US026;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * In-memory implementation of {@link IAuctionRepository}.
 * <p>
 * This repository stores {@link Auction} instances in a static list and
 * provides operations for creating, retrieving, and filtering auctions.
 * </p>
 * <p>
 * Auctions are created through an {@link AuctionFactory} and kept only
 * during the application's runtime.
 * </p>
 */
public class AuctionRepositoryMem implements IAuctionRepository{

    private static List<Auction> _auctionsList;
    private final AuctionFactory _factoryAuction;


    /**
     * Constructs an in-memory auction repository with the given auction factory.
     * <p>
     * Initializes the repository with an empty auction list.
     * </p>
     *
     * @param auctionFactory the factory used to create {@link Auction} instances
     */
    public AuctionRepositoryMem(AuctionFactory auctionFactory) {
        _factoryAuction = auctionFactory;
        _auctionsList = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Auction> getAllAuctions() {
        return _auctionsList;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Auction createAndAdd(Listing listing, LocalDateTime initialDate, LocalDateTime finalDate, Price buyoutPrice) throws InstanceAlreadyExistsException {

        Auction auction = _factoryAuction.create(listing, initialDate, finalDate, buyoutPrice);

        if (isExistent(auction)) {
            throw new InstanceAlreadyExistsException("this auction already exists");
        }

        _auctionsList.add(auction);

        return auction;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Auction> getListOfItemsOnAuctionGivenGenre(Genre genre) {

        if (genre == null) {
            throw new IllegalArgumentException("genre cannot be null");
        }

        List<Auction> result = new ArrayList<>();
        // will iterate over all auctions in the repository
        for (Auction auction : _auctionsList) {
           // for each auction, will iterate over all items that are in Listing
            for(PublicationSample publicationSample : auction.getListing().getItems()) {
                //this condition means that we are getting throw all the information necessary to get the final list of genres
                if(publicationSample.getBibliographicalData()
                        .getGenres().contains(genre)) {
                    result.add(auction);
                   // break when finds one, prevents the duplication
                    break;
                }
            }
        }

        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Auction> getListOfItemsOnAuctionGivenAuthor(AuthorDTO_US026 author) {

        if (author == null || author.name().isBlank())
            throw new IllegalArgumentException("Author cannot be null or blank");

        List<Auction> authorAuctions = _auctionsList.stream().filter(auction -> auction.getListing().getItems()
                .stream()
                .anyMatch( item ->
                        item.getBibliographicalData()
                                .getAuthors().stream()
                                .anyMatch(a -> a.getProfile().getName().equals(author.name()))
                )
        ).toList();

        return authorAuctions;
    }

    private boolean isExistent(Auction auction) {
        return _auctionsList.contains(auction);
    }




    // US024 -------------------------------------------------------------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    @Override
    public List<PublicationSample> getListOfItemsOnAuctionByGivenPublication(String publicationDTO) {

        if(publicationDTO == null){
            throw new NullPointerException("Publication can not be null");
        }

        if(publicationDTO.isEmpty()){
            throw new IllegalArgumentException("Publication can not be empty");
        }

        List<PublicationSample> listOfItemsOnAuctionByPublication = new ArrayList<>();

        for (Auction auc : _auctionsList) {
            Listing listing = auc.getListing();

            if (listing != null && listing.getItems() != null) {

                for (PublicationSample pbSample : listing.getItems()) {

                    if (pbSample.getBibliographicalData().getTitle() != null && pbSample.getBibliographicalData().getTitle().equals(publicationDTO)) {
                        listOfItemsOnAuctionByPublication.add(pbSample);
                    }
                }
            }
        }

        return listOfItemsOnAuctionByPublication;
    }

}
