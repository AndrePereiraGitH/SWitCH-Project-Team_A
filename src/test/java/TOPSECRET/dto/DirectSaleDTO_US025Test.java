package TOPSECRET.dto;

import TOPSECRET.domain.*;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.DirectSale.DirectSale;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.domain.Contact;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.domain.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link DirectSaleDTO_US025}.
 *
 * This test class validates that DTO mapping from domain objects (DirectSale, Listing, PublicationSample)
 * is done correctly. All domain dependencies are mocked to isolate DTO conversion logic.
 */
class DirectSaleDTO_US025Test {

    private User seller1;
    private User seller2;
    private Author author1;
    private Author author2;
    private PublicationSample sample1;
    private PublicationSample sample2;
    private Listing listing1;
    private Listing listing2;
    private DirectSale directSale;

    /**
     * Sets up mocks for users, authors, publication samples, listings, and a direct sale.
     * This setup ensures all dependencies return controlled values to test DTO conversion.
     */
    @BeforeEach
    void setup() {
        // --- USERS ---
        seller1 = mock(User.class);
        when(seller1.getUserName()).thenReturn("namee");
        when(seller1.getContact()).thenReturn(mock(Contact.class));
        when(seller1.getProfile()).thenReturn(mock(Profile.class));
        when(seller1.getUserRole()).thenReturn(UserRole.NORMAL_USER);

        seller2 = mock(User.class);
        when(seller2.getUserName()).thenReturn("nmeee");
        when(seller2.getContact()).thenReturn(mock(Contact.class));
        when(seller2.getProfile()).thenReturn(mock(Profile.class));
        when(seller2.getUserRole()).thenReturn(UserRole.NORMAL_USER);

        // --- AUTHORS ---
        // --- AUTHORS ---
        author1 = mock(Author.class);
        when(author1.getName()).thenReturn("author1");

        author2 = mock(Author.class);
        when(author2.getName()).thenReturn("author2");
        // --- GENRES ---
        Genre genre1 = mock(Genre.class);
        when(genre1.getGenreType()).thenReturn("genre");

        // --- BIBLIOGRAPHICAL DATA ---
        BibliographicalData bd1 = mock(BibliographicalData.class);
        when(bd1.getTitle()).thenReturn("Title One");
        when(bd1.getAuthors()).thenReturn(List.of(author2));
        when(bd1.getGenres()).thenReturn(List.of(genre1));

        BibliographicalData bd2 = mock(BibliographicalData.class);
        when(bd2.getTitle()).thenReturn("Title Two");
        when(bd2.getAuthors()).thenReturn(List.of(author2));
        when(bd2.getGenres()).thenReturn(List.of(genre1));

        // --- PUBLICATION SAMPLES ---
        sample1 = mock(PublicationSample.class);
        when(sample1.getBibliographicalData()).thenReturn(bd1);
        when(sample1.getOwner()).thenReturn(seller1);
        when(sample1.getPublicationStatus()).thenReturn(PublicationStatus.READ);

        sample2 = mock(PublicationSample.class);
        when(sample2.getBibliographicalData()).thenReturn(bd2);
        when(sample2.getOwner()).thenReturn(seller2);
        when(sample2.getPublicationStatus()).thenReturn(PublicationStatus.READ);

        // --- LISTINGS ---
        listing1 = mock(Listing.class);
        when(listing1.getItems()).thenReturn(List.of(sample1));
        when(listing1.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        when(listing1.getPrice()).thenReturn(new Price(new BigDecimal("10.00")));
        when(listing1.getListingTitle()).thenReturn("Listing One");
        when(listing1.getSeller()).thenReturn(seller1);

        listing2 = mock(Listing.class);
        when(listing2.getItems()).thenReturn(List.of(sample2));
        when(listing2.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        when(listing2.getPrice()).thenReturn(new Price(new BigDecimal("15.00")));
        when(listing2.getListingTitle()).thenReturn("Listing Two");
        when(listing2.getSeller()).thenReturn(seller2);

        // --- DIRECT SALE ---
        directSale = mock(DirectSale.class);
        when(directSale.getListings()).thenReturn(List.of(listing1, listing2));
    }

    /**
     * Validates that {@link DirectSaleDTO_US025#fromItemAndDirectSale(PublicationSample, DirectSale)}
     * correctly maps a single publication sample and aggregates direct sale data.
     */
    @Test
    void testFromItemAndDirectSale() {
        DirectSaleDTO_US025 dto = DirectSaleDTO_US025.fromItemAndDirectSale(sample1, directSale);

        assertEquals("Title One", dto.getTitle());
        assertEquals(List.of("author2"), dto.getAuthors());
        assertEquals(new BigDecimal("25.00"), dto.getPrice());
        assertEquals("Pack of 2 listings", dto.getListingTitle());
        assertEquals("nmeee", dto.getSeller());
    }

    /**
     * Validates that {@link DirectSaleDTO_US025#fromDirectSale(DirectSale)}
     * correctly maps all publication samples from all listings in the direct sale.
     */
    @Test
    void testFromDirectSale() {
        List<DirectSaleDTO_US025> dtos = DirectSaleDTO_US025.fromDirectSale(directSale);

        assertEquals(2, dtos.size());

        DirectSaleDTO_US025 dto1 = dtos.get(0);
        assertEquals("Title One", dto1.getTitle());
        assertEquals(List.of("author2"), dto1.getAuthors());
        assertEquals(new BigDecimal("25.00"), dto1.getPrice());

        DirectSaleDTO_US025 dto2 = dtos.get(1);
        assertEquals("Title Two", dto2.getTitle());
        assertEquals(List.of("author2"), dto2.getAuthors());
        assertEquals(new BigDecimal("25.00"), dto2.getPrice());
    }
}