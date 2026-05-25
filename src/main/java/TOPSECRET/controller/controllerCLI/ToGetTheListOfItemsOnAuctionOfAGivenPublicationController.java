package TOPSECRET.controller.controllerCLI;


import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.ValueObjects.BibliographicalDataId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.dto.US024AucListingDTO;
import TOPSECRET.dto.US024AucListingMapper;

import java.util.List;

/**
 * Controller responsible for handling the retrieval of auction listings
 * associated with a given publication.
 *
 * <p>This controller coordinates between the bibliographical data, publication sample,
 * and listing repositories to fetch and return auction data for a specified title.
 */
public class ToGetTheListOfItemsOnAuctionOfAGivenPublicationController {

    private IBibliographicalDataRepository _bdRepo;
    private IPublicationSampleRepository _pubRepo;
    private IListingRepository _listingRepo;


    /**
     * Constructs a new instance of this controller with the required repositories.
     *
     * @param _bdRepo      the bibliographical data repository; must not be null
     * @param _pubRepo     the publication sample repository; must not be null
     * @param _listingRepo the listing repository; must not be null
     * @throws NullPointerException if any of the provided repositories is null
     */
    public ToGetTheListOfItemsOnAuctionOfAGivenPublicationController(IBibliographicalDataRepository _bdRepo, IPublicationSampleRepository _pubRepo, IListingRepository _listingRepo) {

        if(_bdRepo == null){
            throw new NullPointerException("BibliographicalDataRepository can not be null");
        }

        if(_pubRepo == null){
            throw new NullPointerException("PublicationSampleRepository can not be null");
        }

        if(_listingRepo == null){
            throw new NullPointerException("ListingRepository can not be null");
        }

        this._bdRepo = _bdRepo;
        this._pubRepo = _pubRepo;
        this._listingRepo = _listingRepo;
    }

    /**
     * Retrieves all available publication titles from the bibliographical data repository.
     *
     * @return a {@link List} of publication titles; never null, may be empty
     */
    public List<String> getAllPublicationTitles() {

        List<String> listOfTitles = _bdRepo.getAllTitles();

        return listOfTitles;
    }

    /**
     * Retrieves all auction listings associated with the publication matching the given title.
     *
     * @param titleDTO the title of the publication to search for; must not be null or blank
     * @return a {@link List} of {@link US024AucListingDTO} representing the auction listings;
     *         never null, may be empty
     * @throws NullPointerException     if {@code titleDTO} is null
     * @throws IllegalArgumentException if {@code titleDTO} is blank or no publication is found
     *                                  matching the given title
     */
    public List<US024AucListingDTO> getAuctionListingsByTitle(String titleDTO){

        if(titleDTO == null){
            throw new NullPointerException("Title can not be null");
        }

        if(titleDTO.isBlank()){
            throw new IllegalArgumentException("Title can not be blank");
        }

        BibliographicalDataId bdId = _bdRepo.findIdByTitle(titleDTO)
                .orElseThrow(() ->
                        new IllegalArgumentException("Title is not valid or not found"));


        List<PublicationSampleId> listOfPubSampleId = _pubRepo.findIdsByBibliographicalDataId(bdId);

        List<Listing> listOfAuctionListingsOfGivenPublication = _listingRepo.findAllByAuctionAndPublicationSampleIdIn(listOfPubSampleId);

        List<US024AucListingDTO> listOfAuctionListingsDTO = US024AucListingMapper.toDTOs(listOfAuctionListingsOfGivenPublication);

        return listOfAuctionListingsDTO;
    }


}