package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.ValueObjects.Picture;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.dto.PublicationSampleDTO_US015;
import java.util.List;

/**
 * Controller for US015 – "To put for sale a publication in my library in a direct sale".
 * <p>
 * This controller coordinates the interaction between the UI layer and the domain layer.
 * It retrieves the list of publication samples available on the user's library for direct sale and delegates
 * the creation of a direct sale listing to the domain services.
 */

public class US015ToPutPublicationInDirectSaleController {

    private final Library _library;
    private final ListingFactory _listingFactory;

    // ========= Constructor ===========================================================================================
    /**
     * Creates a controller with the required domain dependencies.
     *
     * @param library        the user's library containing publication samples
     * @param listingFactory the factory responsible for creating listings
     */

    public US015ToPutPublicationInDirectSaleController(Library library, ListingFactory listingFactory) {

        if (library == null) throw new IllegalArgumentException("Library cannot be null");
        if (listingFactory == null) throw new IllegalArgumentException("ListingFactory cannot be null");

        this._library = library;
        this._listingFactory = listingFactory;
    }


    // ================ Getter =========================================================================================

    /**
     * Returns the library associated with this controller.
     * <p>
     * The library contains the user's publication samples and is used to
     * retrieve the publications eligible for direct sale.
     * </p>
     *
     * @return the {@link Library} instance managed by this controller
     */

    public Library getLibrary() {
        return _library;
    }

    /**
     * Returns the listing factory used by this controller.
     * <p>
     * The factory is responsible for creating new listings, such as direct sale
     * listings, based on the user's selected publication samples and listing details.
     * </p>
     *
     * @return the {@link ListingFactory} instance used to create listings
     */

    public ListingFactory getListingFactory() {
        return _listingFactory;
    }

    // =================================================================================================================


    /**
     * Retrieves all library publication samples that are eligible to be put for sale
     * in a direct sale listing and converts them into DTOs for UI presentation.
     *
     * @return a list of {@link PublicationSampleDTO_US015} representing the available publications
     */

    public List<PublicationSampleDTO_US015> findLibraryPublicationsAvailableForSale() {
        List<PublicationSample> available = _library.findLibraryPublicationsAvailableForSale();
        return available.stream()
                .map(PublicationSampleDTO_US015::toDTO)
                .toList();
    }

    /**
     * Creates a direct sale listing for the selected publication sample.
     *
     * @param listingTitle the title chosen by the user for the listing
     * @param pubSamples the list of publication samples chosen by the user
     * @param price  the price defined by the user
     * @param shortDescription  the short description chosen by the user for the listing
     * @param pictures  pictures chosen by the user for the listing
     * @return the created {@link Listing}
     * @throws IllegalArgumentException if the listingTitle, the pubSamples, price, shortDescription or pictures are invalid
     */

    public Listing putPublicationForSale(String listingTitle, List<PublicationSample> pubSamples, SaleType saleType,
                                         Price price, String shortDescription, List<Picture> pictures) {

        if (listingTitle == null || listingTitle.isBlank())
            throw new IllegalArgumentException("Listing title cannot be empty.");

        if (pubSamples == null)
            throw new IllegalArgumentException("Publication samples cannot be null.");

        if (saleType == null)
            throw new IllegalArgumentException("Sale type samples cannot be null.");

        if (price == null)
            throw new IllegalArgumentException("Price cannot be null.");

        if (shortDescription == null || shortDescription.isBlank())
            throw new IllegalArgumentException("Short description cannot be empty.");

        if (pictures == null)
            throw new IllegalArgumentException("Pictures cannot be null.");

        return _listingFactory.create(listingTitle, pubSamples, SaleType.DIRECT_SALE, price, shortDescription, pictures);
    }

    // The Listing class already sets the seller as the publication's owner.
}