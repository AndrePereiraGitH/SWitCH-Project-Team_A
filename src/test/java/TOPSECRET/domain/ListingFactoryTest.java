package TOPSECRET.domain;

import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.Picture;
import TOPSECRET.domain.ValueObjects.Price;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ListingFactoryTest {


    @Test
    void create_returnsNewConstructedListingInstance() {
        String listingTitle = "listing title";
        PublicationSample publicationDouble = mock(PublicationSample.class);
        List<PublicationSample> items = List.of(publicationDouble);
        SaleType saleType = SaleType.AUCTION;

        Price listingPrice = mock(Price.class);
        when(listingPrice.getPrice()).thenReturn(new BigDecimal("35.0"));

        String shortDescription = "this is a description";
        Picture pic = mock(Picture.class);
        List<Picture> pictures = List.of(pic);

        try (MockedConstruction<Listing> mocked =
                mockConstruction(Listing.class, (mock, context) -> {

        })) {

            ListingFactory listingFactory = new ListingFactory();

            // act
            Listing listing = listingFactory.create(listingTitle, items, saleType, listingPrice, shortDescription, pictures);

            // assert
            assertNotNull(listing);
            assertEquals(1, mocked.constructed().size());

            Listing constructedListing = mocked.constructed().get(0);
            assertSame(listing, constructedListing);

        }

    }
}