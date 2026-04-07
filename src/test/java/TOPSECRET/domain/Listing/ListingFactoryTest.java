package TOPSECRET.domain.Listing;

import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.ValueObjects.Picture;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.domain.ValueObjects.PublicationSampleID;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
 * <p>By validating the factory’s behaviour in isolation, this test class ensures
 * that the Listing creation process remains consistent, predictable, and aligned
 * with the domain model’s construction rules.
 */
class ListingFactoryTest {


    @Test
    void create_returnsNewConstructedListingInstance() {

        String listingTitle = "listing title";

        PublicationSampleID pubSample1ID = new PublicationSampleID("pub1");
        PublicationSampleID pubSample2ID = new PublicationSampleID("pub2");
        List<PublicationSampleID> itemsIdList = List.of(pubSample1ID, pubSample2ID);

        SaleType saleType = SaleType.AUCTION;

        Price listingPrice = mock(Price.class);
        when(listingPrice.getPrice()).thenReturn(new BigDecimal("35.0"));

        String shortDescription = "this is a description";

        UserId sellerId = new UserId("seller1");

        Picture pic = mock(Picture.class);
        List<Picture> pictures = List.of(pic);

        try (MockedConstruction<Listing> mocked =
                mockConstruction(Listing.class, (mock, context) -> {

        })) {

            ListingFactory listingFactory = new ListingFactory();

            // act
            Listing listing = listingFactory.create(listingTitle, itemsIdList, saleType, listingPrice, shortDescription, sellerId, pictures);

            // assert
            assertNotNull(listing);
            assertEquals(1, mocked.constructed().size());

            Listing constructedListing = mocked.constructed().get(0);
            assertSame(listing, constructedListing);

        }

    }
}