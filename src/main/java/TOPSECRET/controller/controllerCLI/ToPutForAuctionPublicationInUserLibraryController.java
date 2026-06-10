package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;

import javax.management.InstanceAlreadyExistsException;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.US016PublicationSampleDTO;
import TOPSECRET.dto.US016PublicationSampleMapper;
import TOPSECRET.services.AuctionService;
import TOPSECRET.services.ListingService;
import org.springframework.security.access.AccessDeniedException;

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

public class ToPutForAuctionPublicationInUserLibraryController {

    private final ListingService listingService;
    private final AuctionService auctionService;
    private final ILibraryRepository libraryRepo;
    private final IPublicationSampleRepository publicationSampleRepository;
    private final US016PublicationSampleMapper mapper;

    /**
     * Creates a new controller with the repositories and mapper required for this use case.
     *
     * @param libraryRepo repository used to retrieve the user's library
     * @param publicationSampleRepository repository used to retrieve publication samples
     * @param mapper mapper used to convert publication samples into DTOs
     */
    public ToPutForAuctionPublicationInUserLibraryController(ListingService service, AuctionService auctionService, ILibraryRepository libraryRepo , IPublicationSampleRepository publicationSampleRepository, US016PublicationSampleMapper mapper) {

        this.listingService = service;
        this.auctionService = auctionService;
        this.libraryRepo = libraryRepo;
        this.publicationSampleRepository = publicationSampleRepository;
        this.mapper = mapper;
    }


    /**
     * Retrieves all publication samples from the library associated with the given user
     * and converts them into DTOs for presentation.
     *
     * @param userId the identifier of the user whose library publications are to be retrieved
     * @return a list of DTOs representing the publication samples in the user's library
     * @throws IllegalArgumentException if {@code userId} is {@code null}
     * @throws InstanceNotFoundException if the user has no library
     */
    public List<US016PublicationSampleDTO> getAllPublicationsInUserLibrary(UserId userId)
            throws InstanceNotFoundException {

        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null!");
        }

        Library library = libraryRepo.findByUserId(userId)
                .orElseThrow(() ->
                        new InstanceNotFoundException("There is no library for userId: " + userId)
                );

        List<PublicationSample> samplesList =
                publicationSampleRepository.findByLibraryId(library.id());

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

    public boolean createAuctionListing(String listingTitle, List<PublicationSampleId> selectedPublicationsID,
                                        Price initialPrice, String shortDescription, UserId sellerId,
                                        List<Picture> pictures, LocalDateTime initialDate, LocalDateTime finalDate, Price buyOutPrice)
            throws InstanceAlreadyExistsException, InstanceNotFoundException, AccessDeniedException {

        if (listingTitle == null
                || selectedPublicationsID == null
                || initialPrice == null
                || shortDescription == null
                || sellerId == null
                || pictures == null
                || initialDate == null
                || finalDate == null
                || buyOutPrice == null
        ) {
            throw new IllegalArgumentException("All fields must be non-null.");
        }

        Listing persistedListing = listingService.createAndSave(listingTitle, selectedPublicationsID, SaleType.AUCTION, initialPrice, shortDescription, sellerId, pictures);

        Auction persistedAuction = auctionService.createAndSave(persistedListing.id(), initialDate, finalDate, initialPrice, buyOutPrice, sellerId);


        return true;
    }

}