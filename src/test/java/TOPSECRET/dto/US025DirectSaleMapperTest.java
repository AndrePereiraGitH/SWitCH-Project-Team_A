package TOPSECRET.dto;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import java.util.Optional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link US025DirectSaleMapper}.
 *
 * <p>Covers:
 * <ul>
 *   <li>{@code toDTO()} - valid conversion with all fields correctly mapped</li>
 * </ul>
 */
class US025DirectSaleMapperTest {

    // =========================================================================
    // toDTO
    // =========================================================================

    /**
     * Ensures {@code toDTO()} returns a DTO with all fields correctly mapped
     * from the given listing and bibliographical data.
     */
    @Test
    void toDTOShouldReturnDTOWithCorrectFields() {

        // ARRANGE
        Listing listingDouble = mock(Listing.class);
        BibliographicalData bdDouble = mock(BibliographicalData.class);
        Price priceDouble = mock(Price.class);
        UserId sellerIdDouble = mock(UserId.class);
        IBibliographicalDataRepository bdRepositoryDouble = mock(IBibliographicalDataRepository.class);
        IPublicationSampleRepository pubRepositoryDouble = mock(IPublicationSampleRepository.class);
        IAuthorRepository authorRepositoryDouble = mock(IAuthorRepository.class);

        when(bdDouble.getTitle()).thenReturn("Clean Code");
        when(priceDouble.getPrice()).thenReturn(new BigDecimal("29.99"));
        when(listingDouble.getPrice()).thenReturn(priceDouble);
        when(listingDouble.getListingTitle()).thenReturn("Clean Code - Good Condition");
        when(sellerIdDouble.toString()).thenReturn("seller123");
        when(listingDouble.getSellerId()).thenReturn(sellerIdDouble);

        List<String> authorNames = List.of("Robert Martin");

        US025DirectSaleMapper sut = new US025DirectSaleMapper(bdRepositoryDouble,
                pubRepositoryDouble,
                authorRepositoryDouble);

        // ACT
        US025DirectSaleDTO result = sut.toDTO(listingDouble, bdDouble, authorNames);

        // ASSERT
        assertEquals("Clean Code", result.getPublicationTitle());
        assertEquals(authorNames, result.getAuthors());
        assertEquals(new BigDecimal("29.99"), result.getPrice());
        assertEquals("Clean Code - Good Condition", result.getListingTitle());
        assertEquals("seller123", result.getSeller());
    }

    /**
     * Ensures {@code toDTO()} returns a DTO with an empty authors list
     * when no author names are provided.
     */
    @Test
    void toDTOShouldReturnDTOWithEmptyAuthorsWhenNoAuthorNamesProvided() {

        // ARRANGE
        Listing listingDouble = mock(Listing.class);
        BibliographicalData bdDouble = mock(BibliographicalData.class);
        Price priceDouble = mock(Price.class);
        UserId sellerIdDouble = mock(UserId.class);
        IBibliographicalDataRepository bdRepositoryDouble = mock(IBibliographicalDataRepository.class);
        IPublicationSampleRepository pubRepositoryDouble = mock(IPublicationSampleRepository.class);
        IAuthorRepository authorRepositoryDouble = mock(IAuthorRepository.class);

        when(bdDouble.getTitle()).thenReturn("Clean Code");
        when(priceDouble.getPrice()).thenReturn(new BigDecimal("29.99"));
        when(listingDouble.getPrice()).thenReturn(priceDouble);
        when(listingDouble.getListingTitle()).thenReturn("Clean Code - Good Condition");
        when(sellerIdDouble.toString()).thenReturn("seller123");
        when(listingDouble.getSellerId()).thenReturn(sellerIdDouble);

        US025DirectSaleMapper sut = new US025DirectSaleMapper(bdRepositoryDouble,
                pubRepositoryDouble,
                authorRepositoryDouble);

        // ACT
        US025DirectSaleDTO result = sut.toDTO(listingDouble, bdDouble, List.of());

        // ASSERT
        assertTrue(result.getAuthors().isEmpty());
    }

    // =========================================================================
// toDTOList
// =========================================================================

    /**
     * Ensures {@code toDTOList()} returns a list of DTOs with all fields correctly mapped.
     */
    @Test
    void toDTOListShouldReturnDTOList() {

        // ARRANGE
        Listing listingDouble = mock(Listing.class);
        BibliographicalData bdDouble = mock(BibliographicalData.class);
        PublicationSample pubDouble = mock(PublicationSample.class);
        Price priceDouble = mock(Price.class);
        UserId sellerIdDouble = mock(UserId.class);
        Author authorDouble = mock(Author.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        PublicationSampleId pubIdDouble = mock(PublicationSampleId.class);
        BibliographicalDataID bdIdDouble = mock(BibliographicalDataID.class);

        IBibliographicalDataRepository bdRepositoryDouble = mock(IBibliographicalDataRepository.class);
        IPublicationSampleRepository pubRepositoryDouble = mock(IPublicationSampleRepository.class);
        IAuthorRepository authorRepositoryDouble = mock(IAuthorRepository.class);

        when(listingDouble.getItemsId()).thenReturn(List.of(pubIdDouble));
        when(pubDouble.getBibliographicalDataID()).thenReturn(bdIdDouble);
        when(bdDouble.getAuthors()).thenReturn(List.of(authorIdDouble));
        when(authorDouble.getName()).thenReturn("Robert Martin");
        when(bdDouble.getTitle()).thenReturn("Clean Code");
        when(priceDouble.getPrice()).thenReturn(new BigDecimal("29.99"));
        when(listingDouble.getPrice()).thenReturn(priceDouble);
        when(listingDouble.getListingTitle()).thenReturn("Clean Code - Good Condition");
        when(sellerIdDouble.toString()).thenReturn("seller123");
        when(listingDouble.getSellerId()).thenReturn(sellerIdDouble);

        when(pubRepositoryDouble.findById(pubIdDouble)).thenReturn(Optional.of(pubDouble));
        when(bdRepositoryDouble.findById(bdIdDouble)).thenReturn(Optional.of(bdDouble));
        when(authorRepositoryDouble.findById(authorIdDouble)).thenReturn(Optional.of(authorDouble));

        US025DirectSaleMapper sut = new US025DirectSaleMapper(bdRepositoryDouble,
                pubRepositoryDouble,
                authorRepositoryDouble);

        // ACT
        List<US025DirectSaleDTO> result = sut.toDTOList(List.of(listingDouble));


        // ASSERT
        assertEquals(1, result.size());
        assertEquals("Clean Code", result.get(0).getPublicationTitle());
        assertEquals(List.of("Robert Martin"), result.get(0).getAuthors());
    }

    /**
     * Ensures {@code toDTOList()} returns an empty list when no listings are provided.
     */
    @Test
    void toDTOListShouldReturnEmptyListWhenNoListingsProvided() {

        // ARRANGE
        IBibliographicalDataRepository bdRepositoryDouble = mock(IBibliographicalDataRepository.class);
        IPublicationSampleRepository pubRepositoryDouble = mock(IPublicationSampleRepository.class);
        IAuthorRepository authorRepositoryDouble = mock(IAuthorRepository.class);

        US025DirectSaleMapper sut = new US025DirectSaleMapper(bdRepositoryDouble,
                pubRepositoryDouble,
                authorRepositoryDouble);

        // ACT
        List<US025DirectSaleDTO> result = sut.toDTOList(List.of());

        // ASSERT
        assertTrue(result.isEmpty());
    }
}