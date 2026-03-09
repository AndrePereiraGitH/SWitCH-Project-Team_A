package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.dto.AuctionDTO_US022;
import TOPSECRET.repository.AuctionRepository;
import TOPSECRET.repository.GenreRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class US022ToGetListOfItemsOnAuctionOfGivenGenreControllerTest {

    @Test
    void getListOfItemsOnAuctionGivenGenre_returnsDtosWhenAuctionsExist() {

        // Arrange
        AuctionRepository auctionRepoMock = mock(AuctionRepository.class);
        GenreRepository genreRepoMock = mock(GenreRepository.class);

        US022ToGetListOfItemsOnAuctionOfGivenGenreController controller =
                new US022ToGetListOfItemsOnAuctionOfGivenGenreController(
                        auctionRepoMock,
                        genreRepoMock
                );

        Genre genre = new Genre("Fiction");

        BibliographicalData bd = mock(BibliographicalData.class);
        when(bd.getTitle()).thenReturn("Test Title");
        when(bd.getGenres()).thenReturn(List.of(genre));

        PublicationSample publicationSample = mock(PublicationSample.class);
        when(publicationSample.getBibliographicalData()).thenReturn(bd);

        Listing listing = mock(Listing.class);
        when(listing.getItems()).thenReturn(List.of(publicationSample));

        Auction auction = mock(Auction.class);
        when(auction.getListing()).thenReturn(listing);

        when(auctionRepoMock.getListOfItemsOnAuctionGivenGenre(genre))
                .thenReturn(List.of(auction));

        // Act
        List<AuctionDTO_US022> result =
                controller.getListOfItemsOnAuctionGivenGenre(genre);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test Title", result.get(0).title);
    }

}