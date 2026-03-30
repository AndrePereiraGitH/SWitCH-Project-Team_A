package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;

import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.repository.*;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.util.*;

/**
 * Controller responsible for handling the use case of placing one or more publications
 * from a user's library into an auction listing.
 * <p>
 * It retrieves publications from the user's library, converts them into DTOs for presentation,
 * and creates both the listing and the corresponding auction.
 */
public class US016ToPutPublicationInAuctionController {

    private final ILibraryRepository libraryRepo;
    private final IListingRepository listingRepository;
    private final IAuctionRepository auctionRepository;
    private final IPublicationSampleRepository IPublicationSampleRepository;

    public US016ToPutPublicationInAuctionController(ILibraryRepository libraryRepo, IListingRepository listingRepository, IAuctionRepository auctionRepository, IPublicationSampleRepository IPublicationSampleRepository) {

        this.libraryRepo = libraryRepo;
        this.listingRepository = listingRepository;
        this.auctionRepository = auctionRepository;
        this.IPublicationSampleRepository = IPublicationSampleRepository;
    }


    /**
     * Retrieves the library associated with a given user.
     *
     * @param userId the username of the user whose library should be retrieved
     * @return the library belonging to the specified user
     * @throws InstanceNotFoundException if no library is associated with the given username
     */
    private Library findLibraryByUsername(UserId userId) throws InstanceNotFoundException {
        Library userLibrary = libraryRepo.getLibraryOfUser(userId);
        return userLibrary;
    }

//    /**
//     * Returns all publications in the user's library as DTOs.
//     *
//     * @param username the username of the user
//     * @return a list of publication DTOs representing all publications in the user's library
//     * @throws IllegalArgumentException if {@code username} is {@code null}
//     * @throws InstanceNotFoundException if the user's library does not exist
//     * @throws EmptyStackException if the user's library contains no publications
//     */
//    public List<PublicationSampleDTO_US016> getAllPublicationsInUserLibrary(String username) throws InstanceNotFoundException {
//        if (username == null) {
//            throw new IllegalArgumentException("username cannot be null");
//        }
//
//        Library userLibrary = findLibraryByUsername(username);
//
//        if (userLibrary.getAllPublications().isEmpty()) {
//            throw new EmptyStackException();
//        }
//
//        List<PublicationSample> publications = userLibrary.getAllPublications();
//
//        List<PublicationSampleDTO_US016> publicationsDTO = publications.stream().map(PublicationSampleDTO_US016::toDTO).toList();
//
//        return publicationsDTO;
//    }

    /**
     * Creates a listing of type auction and associates an auction with it.
     *
     * @param listingTitle the title of the listing
     * @param publicationDTOs the publications to be included in the listing (as DTOs)
     * @param listingPrice the base/listing price
     * @param shortDescription a short description for the listing
     * @param pictures pictures associated with the listing
     * @param auctionStartDateTime the auction start date and time
     * @param auctionEndDateTime the auction end date and time
     * @param buyoutPrice the buyout price for the auction
     * @return {@code true} if the listing and auction are successfully created
     * @throws IllegalArgumentException if the publications list is null/empty, or if required auction fields are null
     * @throws InstanceAlreadyExistsException if the listing or auction already exists
     */
//    public boolean createAuctionListing(
//            // for listing
//            String listingTitle,
//            List<PublicationSampleDTO_US016> publicationDTOs,
//            Price listingPrice,
//            String shortDescription,
//            List<Picture> pictures,
//            // for auction
//            LocalDateTime auctionStartDateTime,
//            LocalDateTime auctionEndDateTime,
//            Price buyoutPrice
//    ) throws InstanceAlreadyExistsException {
//
//        if (publicationDTOs == null || publicationDTOs.isEmpty()) {
//            throw new IllegalArgumentException("You should send at least one publication to Listing");
//        }
//
//        if (auctionStartDateTime == null || auctionEndDateTime == null || buyoutPrice == null) {
//            throw new IllegalArgumentException("auctionStartDateTime, auctionEndDateTime and buyoutPrice cannot be null");
//        }
//
//        List<PublicationSample> items = IPublicationSampleRepository.getPublicationsByDTO(publicationDTOs);
//
//        Listing auctionListing = listingRepository.createAndAdd(listingTitle, items, SaleType.AUCTION, listingPrice, shortDescription, pictures);
//
//        auctionRepository.createAndAdd(auctionListing, auctionStartDateTime, auctionEndDateTime, buyoutPrice);
//
//        return true;
//    }


}
