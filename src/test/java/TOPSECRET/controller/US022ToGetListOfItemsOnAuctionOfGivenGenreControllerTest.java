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

        Genre genreDouble = mock(Genre.class);
        when(genreDouble.getGenreType()).thenReturn("Fiction");

        BibliographicalData bdDouble = mock(BibliographicalData.class);
        when(bdDouble.getTitle()).thenReturn("Test Title");
        when(bdDouble.getGenres()).thenReturn(List.of(genreDouble));

        PublicationSample publicationSampleDouble = mock(PublicationSample.class);
        when(publicationSampleDouble.getBibliographicalData()).thenReturn(bdDouble);

        Listing listingDouble = mock(Listing.class);
        when(listingDouble.getItems()).thenReturn(List.of(publicationSampleDouble));

        Auction auctionDouble = mock(Auction.class);
        when(auctionDouble.getListing()).thenReturn(listingDouble);

        when(auctionRepoMock.getListOfItemsOnAuctionGivenGenre(genreDouble))
                .thenReturn(List.of(auctionDouble));

        // Act
        List<AuctionDTO_US022> result =
                controller.getListOfItemsOnAuctionGivenGenre(genreDouble);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test Title", result.get(0).title);
    }

    @Test
    void shouldReturnThrowsExceptionNull(){
        // Arrange
        AuctionRepository auctionRepo = mock(AuctionRepository.class);
        GenreRepository genreRepo = mock(GenreRepository.class);

        US022ToGetListOfItemsOnAuctionOfGivenGenreController controller =
                new US022ToGetListOfItemsOnAuctionOfGivenGenreController(
                        auctionRepo,
                        genreRepo
                );

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.getListOfItemsOnAuctionGivenGenre(null)
        );
    }

    @Test
    void shouldReturnEmptyList(){
        // Arrange
        AuctionRepository auctionRepoMock = mock(AuctionRepository.class);
        GenreRepository genreRepoMock = mock(GenreRepository.class);

        US022ToGetListOfItemsOnAuctionOfGivenGenreController controller =
                new US022ToGetListOfItemsOnAuctionOfGivenGenreController(
                        auctionRepoMock,
                        genreRepoMock
                );

        Genre genreDouble = mock(Genre.class);
        when(genreDouble.getGenreType()).thenReturn("fiction");

        when(auctionRepoMock.getListOfItemsOnAuctionGivenGenre(genreDouble))
                .thenReturn(List.of());

        // Act
        List<AuctionDTO_US022> result =
                controller.getListOfItemsOnAuctionGivenGenre(genreDouble);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnListOfItemsOnAuctionGivenGenre(){
        // Arrange
        AuctionRepository auctionRepoMock = mock(AuctionRepository.class);
        GenreRepository genreRepoMock = mock(GenreRepository.class);

        US022ToGetListOfItemsOnAuctionOfGivenGenreController controller =
                new US022ToGetListOfItemsOnAuctionOfGivenGenreController(
                        auctionRepoMock,
                        genreRepoMock
                );

        Genre genreDouble = mock(Genre.class);
        when(genreDouble.getGenreType()).thenReturn("fiction");

        BibliographicalData bd1Double = mock(BibliographicalData.class);
        when(bd1Double.getTitle()).thenReturn("Title 1");
        when(bd1Double.getGenres()).thenReturn(List.of(genreDouble));

        BibliographicalData bd2Double = mock(BibliographicalData.class);
        when(bd2Double.getTitle()).thenReturn("Title 2");
        when(bd2Double.getGenres()).thenReturn(List.of(genreDouble));

        PublicationSample sample1Double = mock(PublicationSample.class);
        when(sample1Double.getBibliographicalData()).thenReturn(bd1Double);

        PublicationSample sample2Double = mock(PublicationSample.class);
        when(sample2Double.getBibliographicalData()).thenReturn(bd2Double);

        Listing listing1Double = mock(Listing.class);
        when(listing1Double.getItems()).thenReturn(List.of(sample1Double));

        Listing listing2Double = mock(Listing.class);
        when(listing2Double.getItems()).thenReturn(List.of(sample2Double));

        Auction auction1Double = mock(Auction.class);
        when(auction1Double.getListing()).thenReturn(listing1Double);

        Auction auction2Double = mock(Auction.class);
        when(auction2Double.getListing()).thenReturn(listing2Double);

        when(auctionRepoMock.getListOfItemsOnAuctionGivenGenre(genreDouble))
                .thenReturn(List.of(auction1Double, auction2Double));

        // Act
        List<AuctionDTO_US022> result =
                controller.getListOfItemsOnAuctionGivenGenre(genreDouble);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void getListOfItemsOnAuctionGivenGenre_dtoContainsCorrectGenres() {

        AuctionRepository auctionRepoMock = mock(AuctionRepository.class);
        GenreRepository genreRepoMock = mock(GenreRepository.class);

        US022ToGetListOfItemsOnAuctionOfGivenGenreController controller =
                new US022ToGetListOfItemsOnAuctionOfGivenGenreController(
                        auctionRepoMock,
                        genreRepoMock
                );

        Genre genreDouble = mock(Genre.class);
        when(genreDouble.getGenreType()).thenReturn("fiction");

        BibliographicalData bdDouble = mock(BibliographicalData.class);
        when(bdDouble.getTitle()).thenReturn("Book");
        when(bdDouble.getGenres()).thenReturn(List.of(genreDouble));

        PublicationSample sampleDouble = mock(PublicationSample.class);
        when(sampleDouble.getBibliographicalData()).thenReturn(bdDouble);

        Listing listingDouble = mock(Listing.class);
        when(listingDouble.getItems()).thenReturn(List.of(sampleDouble));

        Auction auctionDouble = mock(Auction.class);
        when(auctionDouble.getListing()).thenReturn(listingDouble);

        when(auctionRepoMock.getListOfItemsOnAuctionGivenGenre(genreDouble))
                .thenReturn(List.of(auctionDouble));

        List<AuctionDTO_US022> result =
                controller.getListOfItemsOnAuctionGivenGenre(genreDouble);

        assertTrue(result.get(0).genres.contains("fiction"));
    }

}