package TOPSECRET.controller;

import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.Picture;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.US015PublicationSampleDTO;
import TOPSECRET.dto.US015PublicationSampleMapper;
import TOPSECRET.persistence.mem.ListingRepositoryMem;
import TOPSECRET.services.ListingService;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.util.List;

/**
 * Controller for US015 – "To put for sale a publication in my library in a direct sale".
 * <p>
 * This controller coordinates the interaction between the UI layer and the domain layer
 * for the direct sale listing workflow. It retrieves the publication samples available
 * in the user's library that can be listed for sale and delegates the creation of
 * direct sale listings to the {@link IListingRepository}.
 */
public class ToPutPublicationInDirectSaleController {

    private final ListingService listingService;
    private final ILibraryRepository _libraryRepo;
    private final IPublicationSampleRepository  _pubSampleRepo;
    private final IListingRepository _listingRepo;
    private final US015PublicationSampleMapper _pubSampleMapper;

    // =================================================================================================================
    // ========= CONSTRUCTOR ===========================================================================================
    // =================================================================================================================

    /**
     * Creates a controller for US015 with the required domain dependencies.
     * <p>
     * All repositories are injected to allow the controller
     * to retrieve the user's library, its publication samples, and to create new direct sale listings.
     * </p>
     *
     * @param libraryRepo   the repository used to retrieve the user's library; must not be {@code null}
     * @param pubSampleRepo the repository used to retrieve publication samples; must not be {@code null}
     * @param listingRepo the repository used for creating new listings; must not be {@code null}
     * @param pubSampleMapper the mapper used for creating publication sample DTOs; must not be {@code null}
     *
     * @throws IllegalArgumentException if any of the provided dependencies are {@code null}
     */
    public ToPutPublicationInDirectSaleController(ILibraryRepository libraryRepo,
                                                       IPublicationSampleRepository pubSampleRepo,
                                                       IListingRepository listingRepo,
                                                       US015PublicationSampleMapper pubSampleMapper,
                                                       ListingService listingService) {

        if (libraryRepo == null) throw new IllegalArgumentException("LibraryRepository cannot be null");
        if (pubSampleRepo == null) throw new IllegalArgumentException("PublicationSampleRepository cannot be null");
        if (listingRepo == null) throw new IllegalArgumentException("ListingRepository cannot be null");
        if (pubSampleMapper == null) throw new IllegalArgumentException("US015PublicationSampleMapper cannot be null");
        if (listingService == null) throw new IllegalArgumentException("ListingService cannot be null");

        this._libraryRepo = libraryRepo;
        this._pubSampleRepo = pubSampleRepo;
        this._listingRepo = listingRepo;
        this._pubSampleMapper = pubSampleMapper;
        this.listingService = listingService;
    }


    // =================================================================================================================
    // ========= BUSINESS METHODS ======================================================================================
    // =================================================================================================================

    /**
     * Retrieves all library publication samples that are eligible to be put for sale
     * in a direct sale listing and converts them into DTOs for UI presentation.
     *
     * @return a list of {@link US015PublicationSampleDTO} representing the available publications
     */

    public List<US015PublicationSampleDTO> findLibraryPublicationsAvailableForSale(UserId userId) throws InstanceNotFoundException {

        // finds user library
        Library library = _libraryRepo.getLibraryByUserId(userId);

        // finds publication samples in that library available for sale
        List<PublicationSample> availablePubSamples = _pubSampleRepo.findAvailableForSaleByLibraryId(library.id());

        // converts to DTO
        List<US015PublicationSampleDTO> listOfPublicationSampleDTO = _pubSampleMapper.toDTOs(availablePubSamples);

        return listOfPublicationSampleDTO;
    }

    /**
     * Creates a new {@link Listing} for a direct sale.
     * <p>
     * This method validates all input parameters and delegates the creation of the
     * listing to the {@link ListingRepositoryMem}. The sale type is fixed as
     * {@link SaleType#DIRECT_SALE}, since this controller is specific to US015.
     * </p>
     *
     * @param listingTitle     the title of the listing; must not be {@code null} or blank
     * @param pubSampleIds     the identifiers of the publication samples to be listed; must not be {@code null}
     * @param price            the price associated with the listing; must not be {@code null}
     * @param shortDescription a short description of the listing; must not be {@code null} or blank
     * @param sellerId         the identifier of the seller creating the listing; must not be {@code null}
     * @param pictures         the pictures associated with the listing; must not be {@code null}
     *
     * @return the newly created {@link Listing}
     *
     * @throws IllegalArgumentException if any of the required parameters are {@code null} or invalid
     * @throws InstanceAlreadyExistsException if a listing already exists for any of the selected publications
     */
    public Listing putPublicationForSale(String listingTitle, List<PublicationSampleId> pubSampleIds,
                                         Price price, String shortDescription, UserId sellerId, List<Picture> pictures) throws InstanceAlreadyExistsException {

        if (listingTitle == null || listingTitle.isBlank())
            throw new IllegalArgumentException("Listing title cannot be null or blank.");

        if (pubSampleIds == null)
            throw new IllegalArgumentException("Publication samples IDs cannot be null.");

        if (price == null)
            throw new IllegalArgumentException("Price cannot be null.");

        if (shortDescription == null || shortDescription.isBlank())
            throw new IllegalArgumentException("Short description cannot be null or blank.");

        if (sellerId == null)
            throw new IllegalArgumentException("Seller ID cannot be empty.");

        if (pictures == null)
            throw new IllegalArgumentException("Pictures cannot be null.");

        return listingService.createAndSave(listingTitle, pubSampleIds, SaleType.DIRECT_SALE, price, shortDescription, sellerId, pictures);
    }
}