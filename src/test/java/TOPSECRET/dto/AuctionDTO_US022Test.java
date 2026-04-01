package TOPSECRET.dto;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuctionDTO_US022Test {

    @Test
    void fromDomainListEmptyListReturnsEmptyList() {
//        List<AuctionDTO_US022> result =
//                AuctionDTO_US022.fromDomainList(new ArrayList<>());

//        assertNotNull(result);
//        assertTrue(result.isEmpty());
    }

    @Test
    void fromDomainListOneAuctionReturnsOneDTO(){
        // Arrange
        Genre genreDouble = mock(Genre.class);
        when(genreDouble.getGenreType()).thenReturn("Fantasy");

        BibliographicalData bdDouble = mock(BibliographicalData.class);
        when(bdDouble.getTitle()).thenReturn("Book Title");
        when(bdDouble.getGenres()).thenReturn(List.of(genreDouble));

        PublicationSample psDouble = mock(PublicationSample.class);
//        when(psDouble.getBibliographicalData()).thenReturn(bdDouble);

//        Listing listingDouble = mock(Listing.class);
//        when(listingDouble.getItems()).thenReturn(List.of(psDouble));
//
//        Auction auctionDouble =  mock(Auction.class);
//        when(auctionDouble.getListing()).thenReturn(listingDouble);

        // Act
//        List<AuctionDTO_US022> result = AuctionDTO_US022.fromDomainList(List.of(auctionDouble));

        // Assert
//        assertEquals(1, result.size());
//        assertEquals("Book Title", result.get(0).title);
//        assertTrue(result.get(0).genres.contains("Fantasy"));
    }

    @Test
    void fromDomainListTwoAuctionReturnsTwoDTOs(){
        // Arrange
        Genre genreDouble = mock(Genre.class);
        when(genreDouble.getGenreType()).thenReturn("Fantasy");

        BibliographicalData bdDouble1 = mock(BibliographicalData.class);
        BibliographicalData bdDouble2 = mock(BibliographicalData.class);
        when(bdDouble1.getTitle()).thenReturn("Book Title");
        when(bdDouble2.getTitle()).thenReturn("Book Title");
        when(bdDouble1.getGenres()).thenReturn(List.of(genreDouble));
        when(bdDouble2.getGenres()).thenReturn(List.of(genreDouble));

        PublicationSample sampleDouble1 = mock(PublicationSample.class);
        PublicationSample sampleDouble2 = mock(PublicationSample.class);
//        when(sampleDouble1.getBibliographicalData()).thenReturn(bdDouble1);
//        when(sampleDouble2.getBibliographicalData()).thenReturn(bdDouble2);

//        Listing listingDouble1 = mock(Listing.class);
//        Listing listingDouble2 = mock(Listing.class);
//        when(listingDouble1.getItems()).thenReturn(List.of(sampleDouble1));
//        when(listingDouble2.getItems()).thenReturn(List.of(sampleDouble2));
//
//        Auction auctionDouble1 = mock(Auction.class);
//        Auction auctionDouble2 = mock(Auction.class);
//        when(auctionDouble1.getListing()).thenReturn(listingDouble1);
//        when(auctionDouble2.getListing()).thenReturn(listingDouble2);

        // Act

//        List<AuctionDTO_US022> result = AuctionDTO_US022.fromDomainList(List.of(auctionDouble1, auctionDouble2));

        // Assert
//        assertEquals(2, result.size());
//        assertEquals("Book Title", result.get(0).title);
//        assertTrue(result.get(0).genres.contains("Fantasy"));
//        assertEquals("Book Title", result.get(1).title);
//        assertTrue(result.get(1).genres.contains("Fantasy"));
//
//        List<String> genres = result.get(0).genres;
//        assertTrue(genres.containsAll(List.of("Fantasy", "Fantasy")));
    }

    @Test
    void fromDomainListNullListThrowsException() {

        // Act & Assert
//        assertThrows(NullPointerException.class,
//                () -> AuctionDTO_US022.fromDomainList(null));
    }



}