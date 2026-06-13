package TOPSECRET.domain.Listing;

import TOPSECRET.domain.Enum.ListingStatus;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.ValueObjects.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Test suite for the {@link ListingFactory}, ensuring that new {@link Listing}
 * instances are correctly constructed through the factory method.
 *
 * <p>This suite verifies that the factory delegates object creation to the
 * {@link Listing} constructor, rather than performing alternative instantiation
 * logic.
 * <ul>
 *     <li>a new Listing instance is created exactly once;</li>
 *     <li>the returned instance is the same object produced by the constructor;</li>
 *     <li>all required parameters (title, items, sale type, price, description,
 *         seller ID, pictures) are passed correctly to the constructor.</li>
 * </ul>
 *
 * <p>By validating the factory's behaviour in isolation, this test class ensures
 * that the Listing creation process remains consistent, predictable, and aligned
 * with the domain model's construction rules.
 */
@Service
public class ListingFactory {


    /**
     * Verifies that {@link ListingFactory#create(String, List, SaleType, Price, String, UserId, List)}
     * constructs exactly one {@link Listing} instance and returns it.
     *
     * <p>Uses MockedConstruction to intercept the {@link Listing} constructor call,
     * confirming the factory delegates instantiation correctly without generating a
     * {@link ListingId} externally.
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
            List<Picture> pictures,
            LocalDate publicationDate,
            ListingStatus listingStatus) {

        return new Listing(listingId, listingTitle, itemsId, saleType, price, shortDescription, sellerId, pictures, publicationDate, listingStatus);
    }
}
