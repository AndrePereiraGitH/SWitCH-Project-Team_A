package TOPSECRET.controller;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Edition.IEditionRepository;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.Listing.ListingFactory;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.Picture;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.domain.ValueObjects.PublicationSampleID;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.PublicationSampleDTO_US015;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for US015 – "To put for sale a publication in my library in a direct sale".
 * <p>
 * This controller coordinates the interaction between the UI layer and the domain layer
 * for the direct sale listing workflow. It retrieves the publication samples available
 * in the user's library that can be listed for sale and delegates the creation of
 * direct sale listings to the {@link ListingFactory}.
 */
public class US015ToPutPublicationInDirectSaleController {

    private final ILibraryRepository _libraryRepo;
    private final IPublicationSampleRepository  _pubSampleRepo;
    private final IBibliographicalDataRepository _bdRepo;
    private final IEditionRepository _editionRepo;
    private final IAuthorRepository _authorRepo;
    private final ListingFactory _listingFactory;

    // =================================================================================================================
    // ========= CONSTRUCTOR ===========================================================================================
    // =================================================================================================================

    /**
     * Creates a controller for US015 with the required domain dependencies.
     * <p>
     * All repositories and the {@link ListingFactory} are injected to allow the controller
     * to retrieve the user's library, access publication samples, bibliographical data,
     * editions, authors, and to create new direct sale listings.
     * </p>
     *
     * @param libraryRepo   the repository used to retrieve the user's library; must not be {@code null}
     * @param pubSampleRepo the repository used to retrieve publication samples; must not be {@code null}
     * @param bdRepo        the repository used to retrieve bibliographical data; must not be {@code null}
     * @param editionRepo   the repository used to retrieve editions; must not be {@code null}
     * @param authorRepo    the repository used to retrieve authors; must not be {@code null}
     * @param listingFactory the factory responsible for creating new listings; must not be {@code null}
     *
     * @throws IllegalArgumentException if any of the provided dependencies are {@code null}
     */
    public US015ToPutPublicationInDirectSaleController(ILibraryRepository libraryRepo,
                                                       IPublicationSampleRepository pubSampleRepo,
                                                       IBibliographicalDataRepository bdRepo,
                                                       IEditionRepository editionRepo,
                                                       IAuthorRepository authorRepo,
                                                       ListingFactory listingFactory) {

        if (libraryRepo == null) throw new IllegalArgumentException("LibraryRepository cannot be null");
        if(pubSampleRepo == null) throw new IllegalArgumentException("PublicationSampleRepository cannot be null");
        if(bdRepo == null) throw new IllegalArgumentException("BibliographicalDataRepository cannot be null");
        if(editionRepo == null) throw new IllegalArgumentException("EditionRepository cannot be null");
        if(authorRepo == null) throw new IllegalArgumentException("AuthorRepository cannot be null");
        if (listingFactory == null) throw new IllegalArgumentException("ListingFactory cannot be null");

        this._libraryRepo = libraryRepo;
        this._pubSampleRepo = pubSampleRepo;
        this._bdRepo = bdRepo;
        this._editionRepo = editionRepo;
        this._authorRepo = authorRepo;
        this._listingFactory = listingFactory;
    }


    // =================================================================================================================
    // ========= BUSINESS METHODS ======================================================================================
    // =================================================================================================================

    /**
     * Retrieves all library publication samples that are eligible to be put for sale
     * in a direct sale listing and converts them into DTOs for UI presentation.
     *
     * @return a list of {@link PublicationSampleDTO_US015} representing the available publications
     */

    public List<PublicationSampleDTO_US015> findLibraryPublicationsAvailableForSale(UserId userId) throws InstanceNotFoundException {

        // finds user library
        Library library = _libraryRepo.getLibraryOfUser(userId);
        if (library == null) {
            throw new InstanceNotFoundException("Library not found for user");
        }

        // finds publication samples in that library
        List<PublicationSample> libraryPubSamples = _pubSampleRepo.findByLibraryId(library.id());

        // filters only publications available for sale
        List<PublicationSample> availablePubSamples = new ArrayList<>();
        for (PublicationSample pubSample : libraryPubSamples) {
            if (pubSample.isAvailableForSale()) {
                availablePubSamples.add(pubSample);
            }
        }

        // converts to DTO
        List<PublicationSampleDTO_US015> listPubSamplesDTO = new ArrayList<>();
        for (PublicationSample pubSample : availablePubSamples) {
            BibliographicalData bd =
                    _bdRepo.findById(pubSample.getBibliographicalDataID())
                            .orElseThrow(() -> new InstanceNotFoundException("Bibliographical data not found"));

            Edition edition =
                    _editionRepo.findById(pubSample.getEditionID())
                            .orElseThrow(() -> new InstanceNotFoundException("Edition not found"));

            List<Author> authors = bd.getAuthors().stream()
                    .map(authorId -> {
                        try {
                            return _authorRepo.findAuthorById(authorId)
                                    .orElseThrow(() -> new InstanceNotFoundException("Author not found"));
                        } catch (InstanceNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();

            listPubSamplesDTO.add(PublicationSampleDTO_US015.toDTO(bd, authors, edition));
        }
        return listPubSamplesDTO;
    }

    /**
     * Creates a new {@link Listing} for a direct sale (US015).
     * <p>
     * This method validates all input parameters and delegates the creation of the
     * listing to the {@link ListingFactory}. The sale type is fixed as
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
     */
    public Listing putPublicationForSale(String listingTitle, List<PublicationSampleID> pubSampleIds,
                                         Price price, String shortDescription, UserId sellerId, List<Picture> pictures) {

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

        return _listingFactory.create(listingTitle, pubSampleIds, SaleType.DIRECT_SALE, price, shortDescription, sellerId, pictures);
    }
}