package TOPSECRET.services;

import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.Listing.ListingFactory;
import TOPSECRET.domain.ValueObjects.Picture;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link ListingService}.
 *
 * <p>Verifies the service's behaviour in isolation by mocking both the
 * repository ({@link IListingRepository}) and the factory ({@link ListingFactory}).
 * Tests are grouped by method under test.</p>
 *
 * <p>Each test follows the Arrange / Act / Assert structure and exercises
 * a single logical scenario.</p>
 */
@ExtendWith(MockitoExtension.class)
class ListingServiceTest {

    @Mock
    private IListingRepository listingRepository;

    @Mock
    private ListingFactory listingFactory;

    @InjectMocks
    private ListingService service;


    // =========================================================================
    // Constructor
    // =========================================================================

    @Test
    void constructor_shouldThrowNullPointerException_whenRepositoryIsNull() {

        //Act + Assert
        assertThrows(NullPointerException.class,
                () -> new ListingService(null, listingFactory));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenFactoryIsNull() {

        //Act + Assert
        assertThrows(NullPointerException.class,
                () -> new ListingService(listingRepository, null));
    }


    // =========================================================================
    // createAndSave
    // =========================================================================

    @Test
    void createAndSave_shouldReturnSavedListing() {
        // Arrange
        String title = "title";
        List<PublicationSampleId> itemsId = List.of(mock(PublicationSampleId.class));
        SaleType saleType  = mock(SaleType.class);
        Price price = mock(Price.class);
        String description = "description";
        UserId sellerId = mock(UserId.class);
        List<Picture> pictures = List.of(mock(Picture.class));

        Listing createdListing = mock(Listing.class);
        Listing savedListing = mock(Listing.class);

        when(listingFactory.create(title, itemsId, saleType, price, description, sellerId, pictures))
                .thenReturn(createdListing);
        when(listingRepository.save(createdListing))
                .thenReturn(savedListing);

        // Act
        Listing result = service.createAndSave(title, itemsId, saleType, price, description, sellerId, pictures);

        // Assert
        assertSame(savedListing, result);
    }

}