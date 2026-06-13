package TOPSECRET.domain.Listing;

import TOPSECRET.domain.Enum.ListingStatus;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.ValueObjects.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Factory responsible for creating {@link Listing} instances.
 *
 * <p>Encapsulates the construction logic for {@link Listing}, ensuring that
 * new listings are created with a generated identifier and the current
 * publication date, while reconstructed listings preserve their persisted
 * identity and state.</p>
 */
@Service
public class ListingFactory {

    /**
     * Creates a new {@link Listing} with a generated identifier and the current publication date.
     *
     * @param listingTitle     the title of the listing; must be between 5 and 100 characters
     * @param itemsId          the list of publication sample IDs; must not be {@code null} or empty
     * @param saleType         the type of sale (e.g. direct sale, auction); must not be {@code null}
     * @param price            the price of the listing; must not be {@code null}
     * @param shortDescription a brief description of the listing; must be between 10 and 100 characters
     * @param sellerId         the ID of the seller; must not be {@code null}
     * @param pictures         the list of pictures associated with the listing; must not be {@code null}
     * @return a newly created {@link Listing}
     */
    public Listing create(
            String listingTitle,
            List<PublicationSampleId> itemsId,
            SaleType saleType,
            Price price,
            String shortDescription,
            UserId sellerId,
            List<Picture> pictures) {

        return new Listing(listingTitle, itemsId, saleType, price, shortDescription, sellerId, pictures);
    }

    /**
     * Reconstructs an existing {@link Listing} from persisted data using a known {@link ListingId}.
     *
     * <p>This method is intended for use by the persistence layer only, when rehydrating
     * a {@link Listing} from the database. Unlike
     * {@link #create(String, List, SaleType, Price, String, UserId, List)},
     * this method does not generate a new identifier or publication date — it uses the ones provided.
     *
     * @param listingId       the existing unique identifier of the listing; must not be {@code null}
     * @param listingTitle    the display title of the listing; must not be {@code null}
     * @param itemsId         the list of publication sample IDs grouped in this listing; must not be {@code null}
     * @param saleType        the type of sale; must not be {@code null}
     * @param price           the price of the listing; must not be {@code null}
     * @param shortDescription a brief description of the listing; must not be {@code null}
     * @param sellerId        the ID of the seller; must not be {@code null}
     * @param buyerId         the ID of the buyer, or {@code null} if the listing has not been sold
     * @param pictures        the list of pictures associated with the listing; must not be {@code null}
     * @param publicationDate the original publication date of the listing; must not be {@code null}
     * @param listingStatus   the status of the listing; must not be {@code null}
     * @return a reconstructed {@link Listing} instance representing the persisted state
     */
    public Listing create(
            ListingId listingId,
            String listingTitle,
            List<PublicationSampleId> itemsId,
            SaleType saleType,
            Price price,
            String shortDescription,
            UserId sellerId,
            UserId buyerId,
            List<Picture> pictures,
            LocalDate publicationDate,
            ListingStatus listingStatus) {

        return new Listing(listingId, listingTitle, itemsId, saleType, price, shortDescription, sellerId, buyerId, pictures, publicationDate, listingStatus);
    }
}