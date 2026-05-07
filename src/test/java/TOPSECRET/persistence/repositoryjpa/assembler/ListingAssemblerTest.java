package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.Listing.ListingFactory;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.persistence.repositoryjpa.datamodel.ListingDM;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ListingAssembler}.
 *
 * <p>Covers:
 * <ul>
 *   <li>toDM - conversion from {@link Listing} domain object to {@link ListingDM}</li>
 *   <li>toDomain - conversion from {@link ListingDM} to {@link Listing} domain object</li>
 * </ul>
 *
 * <p>Uses Mockito doubles for {@link ListingFactory}, {@link Listing},
 * {@link ListingDM}, {@link Picture}, and all value objects to isolate
 * the assembler from domain and persistence implementations.
 */
@ExtendWith(MockitoExtension.class)
class ListingAssemblerTest {

    @Mock
    private ListingFactory factoryDouble;

    @InjectMocks
    private ListingAssembler assembler;

    private final String validTitle       = "PS5 Pro";
    private final String validDescription = "Valid Description";
    private final String validPriceString = "50";
    private final String validPictureUrl  = "https://example.com/pic.jpg";

    // =========================================================================
    // toDM
    // =========================================================================

    @Test
    void toDM_shouldReturnNonNullDataModel() {
        // Arrange
        ListingId listingIdDouble = mock(ListingId.class);
        UserId userIdDouble       = mock(UserId.class);
        PublicationSampleId pubIdDouble = mock(PublicationSampleId.class);
        Price priceDouble         = mock(Price.class);
        Picture pictureDouble     = mock(Picture.class);
        Listing listingDouble     = mock(Listing.class);

        when(listingDouble.id()).thenReturn(listingIdDouble);
        when(listingDouble.getShortDescription()).thenReturn(validDescription);
        when(listingDouble.getItemsId()).thenReturn(List.of(pubIdDouble));
        when(listingDouble.getSellerId()).thenReturn(userIdDouble);
        when(listingDouble.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        when(listingDouble.getListingTitle()).thenReturn(validTitle);
        when(listingDouble.getPictures()).thenReturn(List.of(pictureDouble));
        when(listingDouble.getPrice()).thenReturn(priceDouble);
        when(priceDouble.toString()).thenReturn(validPriceString);
        when(pictureDouble.getPicture()).thenReturn(validPictureUrl);

        // Act
        ListingDM result = assembler.toDM(listingDouble);

        // Assert
        assertNotNull(result);
    }

    @Test
    void toDM_shouldThrowException_WhenListingIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class, () -> assembler.toDM(null));
    }

    @Test
    void toDM_shouldConvertPicturesToStringList() {
        // Arrange
        ListingId listingIdDouble       = mock(ListingId.class);
        UserId userIdDouble             = mock(UserId.class);
        PublicationSampleId pubIdDouble = mock(PublicationSampleId.class);
        Price priceDouble               = mock(Price.class);
        Picture pictureDouble           = mock(Picture.class);
        Listing listingDouble           = mock(Listing.class);

        when(listingDouble.id()).thenReturn(listingIdDouble);
        when(listingDouble.getShortDescription()).thenReturn(validDescription);
        when(listingDouble.getItemsId()).thenReturn(List.of(pubIdDouble));
        when(listingDouble.getSellerId()).thenReturn(userIdDouble);
        when(listingDouble.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        when(listingDouble.getListingTitle()).thenReturn(validTitle);
        when(listingDouble.getPictures()).thenReturn(List.of(pictureDouble));
        when(listingDouble.getPrice()).thenReturn(priceDouble);
        when(priceDouble.toString()).thenReturn(validPriceString);
        when(pictureDouble.getPicture()).thenReturn(validPictureUrl);

        // Act
        ListingDM result = assembler.toDM(listingDouble);

        // Assert
        assertEquals(List.of(validPictureUrl), result.getPictures());
    }


    // =========================================================================
    // toDomain
    // =========================================================================

    @Test
    void toDomain_shouldReturnNonNullListing() {
        // Arrange
        ListingId listingIdDouble       = mock(ListingId.class);
        UserId userIdDouble             = mock(UserId.class);
        PublicationSampleId pubIdDouble = mock(PublicationSampleId.class);
        ListingDM dmDouble              = mock(ListingDM.class);
        Listing listingDouble           = mock(Listing.class);

        when(dmDouble.getListingId()).thenReturn(listingIdDouble);
        when(dmDouble.getListingTitle()).thenReturn(validTitle);
        when(dmDouble.getItemsId()).thenReturn(List.of(pubIdDouble));
        when(dmDouble.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        when(dmDouble.getPrice()).thenReturn(validPriceString);
        when(dmDouble.getShortDescription()).thenReturn(validDescription);
        when(dmDouble.getSellerId()).thenReturn(userIdDouble);
        when(dmDouble.getPictures()).thenReturn(List.of(validPictureUrl));
        when(factoryDouble.create(
                eq(listingIdDouble),
                eq(validTitle),
                eq(List.of(pubIdDouble)),
                eq(SaleType.DIRECT_SALE),
                any(Price.class),
                eq(validDescription),
                eq(userIdDouble),
                any()
        )).thenReturn(listingDouble);

        // Act
        Listing result = assembler.toDomain(dmDouble);

        // Assert
        assertNotNull(result);
    }

    @Test
    void toDomain_shouldThrowException_WhenListingDMIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class, () -> assembler.toDomain((ListingDM) null));
    }

    @Test
    void toDomain_shouldConvertPictureStringsToImageObjects() {
        // Arrange
        ListingId listingIdDouble       = mock(ListingId.class);
        UserId userIdDouble             = mock(UserId.class);
        PublicationSampleId pubIdDouble = mock(PublicationSampleId.class);
        ListingDM dmDouble              = mock(ListingDM.class);

        when(dmDouble.getListingId()).thenReturn(listingIdDouble);
        when(dmDouble.getListingTitle()).thenReturn(validTitle);
        when(dmDouble.getItemsId()).thenReturn(List.of(pubIdDouble));
        when(dmDouble.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        when(dmDouble.getPrice()).thenReturn(validPriceString);
        when(dmDouble.getShortDescription()).thenReturn(validDescription);
        when(dmDouble.getSellerId()).thenReturn(userIdDouble);
        when(dmDouble.getPictures()).thenReturn(List.of(validPictureUrl));
        when(factoryDouble.create(
                any(), any(), any(), any(), any(), any(), any(), any()
        )).thenReturn(mock(Listing.class));

        // Act
        assembler.toDomain(dmDouble);

        // Assert
        ArgumentCaptor<List<Picture>> pictureCaptor = ArgumentCaptor.forClass(List.class);
        verify(factoryDouble).create(
                any(), any(), any(), any(), any(), any(), any(), pictureCaptor.capture()
        );
        List<Picture> capturedPictures = pictureCaptor.getValue();
        assertEquals(1, capturedPictures.size());
        assertFalse(capturedPictures.isEmpty());
    }
}