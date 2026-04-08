package TOPSECRET.controller;

import TOPSECRET.domain.Auction.IAuctionRepository;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;

import javax.management.InstanceAlreadyExistsException;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.US016_PublicationSampleDTO;
import TOPSECRET.dto.US016_PublicationSampleMapper;

import javax.management.InstanceNotFoundException;
import java.time.LocalDateTime;
import java.util.*;


/**
 * Controller responsible for handling the use case of placing one or more
 * publications from a user's library into an auction listing.
 * <p>
 * It provides operations to retrieve the publication samples that belong to a
 * user's library and to create a new auction listing for the selected publications.
 */
public class US016ToPutPublicationInAuctionController {

    private final ILibraryRepository libraryRepo;
    private final IListingRepository listingRepository;
    private final IAuctionRepository auctionRepository;
    private final IPublicationSampleRepository publicationSampleRepository;
    private final IBibliographicalDataRepository bdRepository;
    private final US016_PublicationSampleMapper mapper;

    /**
     * Creates a new controller with the repositories and mapper required for this use case.
     *
     * @param libraryRepo repository used to retrieve the user's library
     * @param listingRepository repository used to create and store listings
     * @param auctionRepository repository used to create and store auctions
     * @param publicationSampleRepository repository used to retrieve publication samples
     * @param bdRepository repository used to retrieve bibliographical data
     * @param mapper mapper used to convert publication samples into DTOs
     */
    public US016ToPutPublicationInAuctionController(ILibraryRepository libraryRepo, IListingRepository listingRepository, IAuctionRepository auctionRepository, IPublicationSampleRepository publicationSampleRepository, IBibliographicalDataRepository bdRepository, US016_PublicationSampleMapper mapper) {

        this.libraryRepo = libraryRepo;
        this.listingRepository = listingRepository;
        this.auctionRepository = auctionRepository;
        this.publicationSampleRepository = publicationSampleRepository;
        this.bdRepository = bdRepository;
        this.mapper = mapper;
    }


    /**
     * Retrieves all publication samples from the library associated with the given user
     * and converts them into DTOs for presentation.
     *
     * @param userId the identifier of the user whose library publications are to be retrieved
     * @return a list of DTOs representing the publication samples in the user's library
     * @throws IllegalArgumentException if {@code userId} is {@code null}
     * @throws InstanceNotFoundException if the user has no library or if the bibliographical
     * data for any publication sample cannot be found
     */
    public List<US016_PublicationSampleDTO> getAllPublicationsInUserLibrary(UserId userId) throws InstanceNotFoundException {

        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null!");
        }

        Library library = libraryRepo.getLibraryByUserId(userId);
        if (library == null) {
            throw new InstanceNotFoundException("There is no library for the userID: " + userId);
        }

        List<PublicationSample> samplesList = publicationSampleRepository.getPublicationsByLibraryID(library.id());

        return mapper.fromDomainList(samplesList);
    }

    /**
     * Creates a new auction listing for the selected publication samples and then creates
     * the corresponding auction associated with that listing.
     *
     * @param listingTitle the title of the listing
     * @param selectedPublicationsID the identifiers of the publication samples to include in the listing
     * @param initialPrice the starting price of the auction
     * @param shortDescription a short description of the listing
     * @param sellerId the identifier of the seller creating the listing
     * @param pictures the pictures associated with the listing
     * @param initialDate the date and time when the auction starts
     * @param finalDate the date and time when the auction ends
     * @param buyOutPrice the optional buyout price for the auction
     * @return {@code true} if the listing and the auction are successfully created
     * @throws InstanceAlreadyExistsException if the listing or the auction already exists
     */
    public boolean createAuctionListing(String listingTitle, List<PublicationSampleId> selectedPublicationsID, Price initialPrice, String shortDescription, UserId sellerId, List<Picture> pictures, LocalDateTime initialDate, LocalDateTime finalDate, Price buyOutPrice) throws InstanceAlreadyExistsException {

        Listing auctionListing = listingRepository.createAndAdd(listingTitle, selectedPublicationsID, SaleType.AUCTION, initialPrice, shortDescription, sellerId, pictures);

        auctionRepository.createAndAdd(auctionListing.id(), initialDate, finalDate, initialPrice, buyOutPrice);

        return true;
    }

}
