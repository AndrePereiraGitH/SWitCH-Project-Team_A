package TOPSECRET.services;

import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.Listing.ListingFactory;
import TOPSECRET.domain.ValueObjects.Picture;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Application service responsible for {@link Listing} use cases.
 *
 * <p>Orchestrates interactions between the domain layer ({@link ListingFactory})
 * and the persistence layer ({@link IListingRepository}). All public methods
 * validate their inputs before delegating to domain or repository operations.</p>
 */
@Service
public class ListingService {

    private final IListingRepository listingRepository;
    private final ListingFactory listingFactory;

    /**
     * Constructs a {@code ListingService} with the required collaborators.
     *
     * @param listingRepository the repository used to persist listings; must not be {@code null}
     * @param listingFactory    the factory used to create listing instances; must not be {@code null}
     */
    public ListingService(IListingRepository listingRepository, ListingFactory listingFactory) {
        this.listingRepository = Objects.requireNonNull(listingRepository, "listingRepository must not be null");
        this.listingFactory = Objects.requireNonNull(listingFactory, "listingFactory must not be null");
    }


    /**
     * Creates a new {@link Listing} from the supplied parameters and persists it.
     *
     * @param listingTitle     the display title of the listing; must not be {@code null}
     * @param itemsId          the publication sample identifiers associated with this listing; must not be {@code null}
     * @param saleType         the sale type (direct sale or auction); must not be {@code null}
     * @param price            the asking price; must not be {@code null}
     * @param shortDescription a brief description of the listing; must not be {@code null}
     * @param sellerId         the identifier of the seller; must not be {@code null}
     * @param pictures         the pictures attached to the listing; must not be {@code null}
     * @return the persisted {@link Listing}
     */
    public Listing createAndSave(String listingTitle,
                                 List<PublicationSampleId> itemsId,
                                 SaleType saleType,
                                 Price price,
                                 String shortDescription,
                                 UserId sellerId,
                                 List<Picture> pictures) {
        Objects.requireNonNull(listingTitle,"listingTitle must not be null");
        Objects.requireNonNull(itemsId,"itemsId must not be null");
        Objects.requireNonNull(saleType,"saleType must not be null");
        Objects.requireNonNull(price,"price must not be null");
        Objects.requireNonNull(shortDescription,"shortDescription must not be null");
        Objects.requireNonNull(sellerId,"sellerId must not be null");
        Objects.requireNonNull(pictures,"pictures must not be null");

        Listing createdListing = listingFactory.create(listingTitle, itemsId, saleType, price, shortDescription, sellerId, pictures);

        return listingRepository.save(createdListing);
    }
}